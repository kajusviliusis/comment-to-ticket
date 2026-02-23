package org.example.pulsedesk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pulsedesk.client.HuggingFaceClient;
import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.model.Ticket;
import org.example.pulsedesk.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final HuggingFaceClient huggingFaceClient;
    private final ObjectMapper objectMapper;

    public List<Ticket> getAllTickets()
    {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id)
    {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    private String buildPrompt(String text)
    {
        return """
                Analyze this user comment and return JSON:
                {
                "isTicket": true/false,
                "title": "...",
                "category": "bug/feature/billing/account/other",
                "priority": "low/medium/high",
                "shortSummary": "..."
                }
                
                Comment: %s
                """.formatted(text);
    }

    public void createTicketIfNeeded(Comment comment)
    {
        try{
            String prompt = buildPrompt(comment.getText());
            String raw = huggingFaceClient.analyze(prompt);
            if (raw == null || raw.isBlank()) {
                log.warn("Empty AI response for comment {}",comment.getId());
                return;
            }

            JsonNode node = objectMapper.readTree(raw);
            JsonNode choices = node.path("choices");
            if (!choices.isArray() || choices.isEmpty())
            {
                log.warn("Invalid AI structure for comment {}",comment.getId());
                return;
            }
            String generated = choices.get(0)
                    .path("message")
                    .path("content")
                    .asText();

            if (generated == null || generated.isBlank()) {
                log.warn("Empty AI content for comment {}",comment.getId());
                return;
            }

            JsonNode resultNode = objectMapper.readTree(generated);

            if(!resultNode.path("isTicket").asBoolean(false))
            {
                log.info("AI determined comment {} is not a ticket", comment.getId());
                return;
            }

            Ticket ticket = new Ticket();
            ticket.setTitle(resultNode.path("title").asText(""));
            ticket.setCategory(resultNode.path("category").asText(""));
            ticket.setPriority(resultNode.path("priority").asText(""));
            ticket.setShortSummary(resultNode.path("shortSummary").asText(""));
            ticket.setComment(comment);

            ticketRepository.save(ticket);

            log.info("Ticket created for comment {}",comment.getId());
        }
        catch(WebClientResponseException ex)
        {
            log.error("HF returned error {} with body {}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        }
        catch(WebClientRequestException ex)
        {
            log.error("HF connection error for comment {}", comment.getId(), ex);
        }
        catch (Exception ex)
        {
            log.error("AI ticket creation failed for comment {}", comment.getId(), ex);
        }
    }

}

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
                log.warn("Empty response from Hugging Face");
                return;
            }

            JsonNode node = objectMapper.readTree(raw);
            String generated = node
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            if (generated == null || generated.isBlank()) {
                log.warn("Content empty: {}", raw);
                return;
            }

            JsonNode resultNode = objectMapper.readTree(generated);

            boolean isTicket = resultNode.path("isTicket").asBoolean(false);
            if (!isTicket) return;

            Ticket ticket = new Ticket();
            ticket.setTitle(resultNode.path("title").asText(""));
            ticket.setCategory(resultNode.path("category").asText(""));
            ticket.setPriority(resultNode.path("priority").asText(""));
            ticket.setShortSummary(resultNode.path("shortSummary").asText(""));
            ticket.setComment(comment);

            ticketRepository.save(ticket);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Failed to create ticket", ex);
        }
    }

}

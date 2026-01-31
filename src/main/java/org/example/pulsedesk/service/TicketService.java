package org.example.pulsedesk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.pulsedesk.client.HuggingFaceClient;
import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.model.Ticket;
import org.example.pulsedesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
            String jsonResponse = huggingFaceClient.analyze(prompt);

            JsonNode node = objectMapper.readTree(jsonResponse);

            boolean isTicket = node.get("isTicket").asBoolean();
            if(!isTicket) return;

            Ticket ticket = new Ticket();
            ticket.setTitle(node.get("title").asText());
            ticket.setCategory(node.get("category").asText());
            ticket.setPriority(node.get("priority").asText());
            ticket.setShortSummary(node.get("shortSummary").asText());
            ticket.setComment(comment);

            ticketRepository.save(ticket);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Failed to create ticket", ex);
        }
    }

}

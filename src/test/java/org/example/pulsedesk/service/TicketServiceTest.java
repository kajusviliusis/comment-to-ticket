package org.example.pulsedesk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pulsedesk.client.HuggingFaceClient;
import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.model.Ticket;
import org.example.pulsedesk.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    TicketRepository ticketRepository;

    @Mock
    HuggingFaceClient huggingFaceClient;

    ObjectMapper objectMapper;
    TicketService ticketService;

    @BeforeEach
    void setup()
    {
        objectMapper = new ObjectMapper();
        ticketService = new TicketService(ticketRepository, huggingFaceClient, objectMapper);
    }

    @Test
    void createTicketIfNeeded_skips_when_model_returns_blank()
    {
        Comment c = new Comment();
        c.setText("Minor typo");

        when(huggingFaceClient.analyze(anyString())).thenReturn(" ");
        ticketService.createTicketIfNeeded(c);

        verifyNoInteractions(ticketRepository);
    }

    @Test
    void createTicketIfNeeded_skips_when_IsTicket_false()
    {
        Comment c = new Comment();
        c.setText("Good feedback");

        String llmOutput = """
            {"choices":[{"message":{"content":"{\\"isTicket\\":false}"}}]}
        """;

        when(huggingFaceClient.analyze(anyString())).thenReturn(llmOutput);

        ticketService.createTicketIfNeeded(c);

        verifyNoInteractions(ticketRepository);
    }

    @Test
    void createTicketIfNeeded_saves_ticket_when_IsTicket_true()
    {
        Comment c = new Comment();
        c.setText("App crashes on login");

        String generatedInner = """
            {"isTicket":true,"title":"Crash on login","category":"bug","priority":"high","shortSummary":"App crashes when logging in"}
        """;
        String llmOuter = "{\"choices\":[{\"message\":{\"content\":\""
                + generatedInner.trim().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                + "\"}}]}";

        when(huggingFaceClient.analyze(anyString())).thenReturn(llmOuter);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        ticketService.createTicketIfNeeded(c);

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(captor.capture());
        Ticket saved = captor.getValue();
        assertEquals("Crash on login", saved.getTitle());
        assertEquals("bug", saved.getCategory());
        assertEquals("high", saved.getPriority());
        assertEquals("App crashes when logging in", saved.getShortSummary());
        assertEquals(c, saved.getComment());
    }

    @Test
    void getTicketById_throws_when_not_found()
    {
        when(ticketRepository.findById(99L)).thenReturn(java.util.Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> ticketService.getTicketById(99L));
        assertTrue(ex.getMessage().contains("Ticket not found"));
    }

}

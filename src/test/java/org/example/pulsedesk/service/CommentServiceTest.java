package org.example.pulsedesk.service;

import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    CommentRepository commentRepository;

    @Mock
    TicketService ticketService;

    @InjectMocks
    CommentService commentService;

    @Test
    void saveComment_persists_and_triggers_ticket_check()
    {
        Comment input = new Comment();
        input.setText("App crashes on login");

        Comment saved = new Comment();
        saved.setId(1L);
        saved.setText("App crashes on login");

        when(commentRepository.save(any(Comment.class))).thenReturn(saved);

        Comment result = commentService.saveComment(input);

        assertEquals(1L, result.getId());
        assertEquals("App crashes on login", result.getText());

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        assertEquals("App crashes on login", captor.getValue().getText());

        verify(ticketService, times(1)).createTicketIfNeeded(saved);
    }

    @Test
    void getAllComments_returns_lists_from_repo()
    {
       Comment c1 = new Comment(); c1.setId(1L); c1.setText("a");
       Comment c2 = new Comment(); c2.setId(2L); c2.setText("b");
       when(commentRepository.findAll()).thenReturn(List.of(c1, c2));

       List<Comment> result = commentService.getAllComments();

       assertEquals(2, result.size());
       assertEquals(1L, result.get(0).getId());
       assertEquals("b", result.get(1).getText());
       verify(commentRepository, times(1)).findAll();
       verifyNoInteractions(ticketService);
    }
}

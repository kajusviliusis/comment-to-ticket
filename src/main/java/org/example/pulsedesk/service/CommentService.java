package org.example.pulsedesk.service;

import lombok.RequiredArgsConstructor;
import org.example.pulsedesk.client.HuggingFaceClient;
import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.repository.CommentRepository;
import org.example.pulsedesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TicketService ticketService;

    public Comment saveComment(Comment comment)
    {
        Comment saved = commentRepository.save(comment);
        try
        {
            ticketService.createTicketIfNeeded(saved);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return saved;
    }

    public List<Comment> getAllComments()
    {
        return commentRepository.findAll();
    }
}

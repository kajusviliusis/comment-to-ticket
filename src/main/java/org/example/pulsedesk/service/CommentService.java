package org.example.pulsedesk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pulsedesk.client.HuggingFaceClient;
import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.repository.CommentRepository;
import org.example.pulsedesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        } catch (Exception ex)
        {
            log.error("Unexpected error creating ticket for comment {}", saved.getId(), ex);
        }

        return saved;
    }

    public List<Comment> getAllComments()
    {
        return commentRepository.findAll();
    }
}

package org.example.pulsedesk.service;

import lombok.RequiredArgsConstructor;
import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment saveComment(Comment comment)
    {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments()
    {
        return commentRepository.findAll();
    }
}

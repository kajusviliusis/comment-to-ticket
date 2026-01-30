package org.example.pulsedesk.controller;

import lombok.RequiredArgsConstructor;
import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController{
    private final CommentService commentService;

    @PostMapping
    public Comment createComment(@RequestBody Comment comment){
        return commentService.saveComment(comment);
    }

    @GetMapping
    public List<Comment> getAllComments()
    {
        return commentService.getAllComments();
    }
}

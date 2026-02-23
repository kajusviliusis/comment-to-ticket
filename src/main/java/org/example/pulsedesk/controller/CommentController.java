package org.example.pulsedesk.controller;

import lombok.RequiredArgsConstructor;
import org.example.pulsedesk.dto.CommentResponseDto;
import org.example.pulsedesk.dto.SubmitCommentDto;
import org.example.pulsedesk.mapper.CommentMapper;
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
    public CommentResponseDto createComment(@RequestBody SubmitCommentDto dto){
        Comment comment = commentService.saveComment(CommentMapper.toEntity(dto));
        return CommentMapper.toDto(comment);
    }

    @GetMapping
    public List<CommentResponseDto> getAllComments()
    {
        return commentService.getAllComments()
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }
}

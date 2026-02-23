package org.example.pulsedesk.mapper;

import org.example.pulsedesk.dto.CommentResponseDto;
import org.example.pulsedesk.dto.SubmitCommentDto;
import org.example.pulsedesk.model.Comment;

public class CommentMapper {
    public static Comment toEntity(SubmitCommentDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.text());
        return comment;
    }

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt()
        );
    }
}

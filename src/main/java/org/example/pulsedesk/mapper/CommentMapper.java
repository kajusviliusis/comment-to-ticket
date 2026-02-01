package org.example.pulsedesk.mapper;

import org.example.pulsedesk.dto.SubmitCommentDTO;
import org.example.pulsedesk.model.Comment;

public class CommentMapper {
    public static Comment toEntity(SubmitCommentDTO dto) {
        Comment comment = new Comment();
        comment.setText(dto.text());
        return comment;
    }
}

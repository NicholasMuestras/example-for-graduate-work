package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.Comment;

import io.swagger.model.Comments;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Comment entity and DTO models
 */
@Component
public class CommentMapper {
    
    /**
     * Converts Comment entity to Comment DTO
     * @param comment Comment entity
     * @return Comment DTO
     */
    public io.swagger.model.Comment toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        io.swagger.model.Comment commentDto = new io.swagger.model.Comment();
        commentDto.setPk(comment.getPk());
        commentDto.setText(comment.getText());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setAuthorImage(comment.getAuthor().getImage());
        commentDto.setAuthorFirstName(comment.getAuthor().getFirstName());
        
        return commentDto;
    }
    
    /**
     * Converts list of Comment entities to Comments DTO
     * @param commentList list of Comment entities
     * @return Comments DTO
     */
    public Comments toCommentsDto(List<Comment> commentList) {
        if (commentList == null) {
            return null;
        }
        
        Comments commentsDto = new Comments();
        commentsDto.setCount(commentList.size());
        commentsDto.setResults(commentList.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList()));
        
        return commentsDto;
    }
}
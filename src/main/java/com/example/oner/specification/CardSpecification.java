package com.example.oner.specification;

import com.example.oner.entity.Card;
import com.example.oner.entity.Member;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CardSpecification {

    public static Specification<Card> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? null : criteriaBuilder.like(root.get("cardTitle"), "%" + title + "%");
    }

    public static Specification<Card> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Card> hasDueDateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null && to == null) return null;
            if (from != null && to != null) return criteriaBuilder.between(root.get("dueDate"), from, to);
            return from != null
                    ? criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), from)
                    : criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), to);
        };
    }

    public static Specification<Card> belongsToList(Long listId) {
        return (root, query, criteriaBuilder) ->
                listId == null ? null : criteriaBuilder.equal(root.get("list").get("id"), listId);
    }
    public static Specification<Card> belongsToBoard(Long boardId) {
        return (root, query, criteriaBuilder) -> {
            if (boardId == null) return null;
            Join<Object, Object> listJoin = root.join("list");
            return criteriaBuilder.equal(listJoin.get("board").get("id"), boardId);
        };

    }
//        public static Specification<Card> assignedToMember(Long memberId) {
//            return (root, query, criteriaBuilder) -> {
//                if (memberId == null) return null;
//                return criteriaBuilder.equal(root.get("member").get("id"), memberId);
//            };

}

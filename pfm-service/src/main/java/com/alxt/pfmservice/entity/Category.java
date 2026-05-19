package com.alxt.pfmservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@NamedQueries(
        @NamedQuery(
                name = "Category.findAllByUserIdAndTitleLikeIgniringCase",
                query = "select c from Category c where c.userId = :userId and c.title ilike :filter"
        )
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @Column(name = "operation_type")
    @NotNull
    private OperationType operationType;

    @Column(name = "user_id")
    @NotNull
    @Size(max = 36)
    private String userId;
}

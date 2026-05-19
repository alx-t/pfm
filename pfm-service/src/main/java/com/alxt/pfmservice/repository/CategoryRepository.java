package com.alxt.pfmservice.repository;

import com.alxt.pfmservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(name = "Category.findAllByUserIdAndTitleLikeIgniringCase")
    List<Category> findAllByUserIdAndTitleLikeIgnoreCase(
            @Param("userId") String userId,
            @Param("filter") String filter
    );

    List<Category> findAllByUserId(@Param("userId") String userId);

    Optional<Category> findByUserIdAndId(@Param("userId") String userId, @Param("id") Long id);

    @Modifying
    @Query("delete from Category c where c.userId = :userId and c.id = :id")
    void deleteByUserIdAndId(@Param("userId") String userId, @Param("id") Long id);
}

package com.alxt.pfmservice.repository;

import com.alxt.pfmservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(name = "Account.findAllByUserIdAndTitleLikeIgniringCase")
    Iterable<Account> findAllByUserIdAndTitleLikeIgnoreCase(
            @Param("userId") String userId,
            @Param("filter") String filter
    );

    Iterable<Account> findAllByUserId(@Param("userId") String userId);

    Optional<Account> findByUserIdAndId(@Param("userId") String userId, @Param("id") Long id);

    @Modifying
    @Query("delete from Account a where a.userId = :userId and a.id = :id")
    void deleteByUserIdAndId(@Param("userId") String userId, @Param("id") Long id);
}

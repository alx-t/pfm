package com.alxt.pfmservice.repository;

import com.alxt.pfmservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query(name = "Product.findAllByTitleLikeIgniringCase")
    Iterable<Account> findAllByTitleLikeIgnoreCase(@Param("filter") String filter);
}

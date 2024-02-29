package org.example.repository;

import org.example.entity.Fee;
import org.example.entity.RuleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RuleCategoryRepository extends JpaRepository<RuleCategory,Long> {
    @Query("SELECT r FROM RuleCategory r WHERE r.title LIKE %:keyword%")
    List<RuleCategory> findByTitleContaining(@Param("keyword")String keyword);

    Boolean existsByTitle(String title);
}

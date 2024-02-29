package org.example.repository;

import org.example.entity.Fee;
import org.example.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RuleRepository extends JpaRepository<Rule,Long> {
//    @Query("SELECT r FROM Rule r WHERE r.content LIKE %:keyword%")
//    List<Rule> findByContentContaining(@Param("keyword")String keyword);

    List<Rule> findByCategoriesId(Long categoryId);
}

package org.example.repository;

import org.example.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee,Long> {
//    @Query("SELECT r FROM Fee r WHERE r.feeSum LIKE %:keyword%")
//    List<Fee> findByFeeSumContaining(@Param("keyword")String keyword);
    Boolean existsByFeeSum(String feeSum);
}

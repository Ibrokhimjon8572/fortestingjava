package org.example.repository;

import org.example.entity.ScheduledText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchaduledTextRepository extends JpaRepository<ScheduledText,Long> {

    @Query(value = "select t from ScheduledText t where t.time =:time")
    List<ScheduledText> findAllByTime(@Param("time") String time);
}

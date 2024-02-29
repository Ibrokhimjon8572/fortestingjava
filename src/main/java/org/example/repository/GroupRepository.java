package org.example.repository;

import org.example.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface GroupRepository extends JpaRepository<Group,Long> {
    @Override
    Optional<Group> findById(Long aLong);
    @Query("select case when count(g) > 0 then true else false end from Group g where g.telegramGroupId = :chatId")
    Boolean isGroupExist(@Param("chatId") Long chatId);


}

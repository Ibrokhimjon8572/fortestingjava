package org.example.repository;

import org.example.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

    List<Text> findAllByEveryIsTrue();

    @Query("SELECT t FROM Text t LEFT JOIN FETCH t.groups LEFT JOIN FETCH t.images WHERE t.id = :textId")
    Optional<Text> findByIdWithGroupsAndImages(@Param("textId") Long textId);

    List<Text> findAllByEveryIsFalse();
}

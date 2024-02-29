package org.example.repository;

import org.example.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Override
    Optional<Image> findById(Long aLong);

    Page<Image> findAll(Pageable pageable);
}

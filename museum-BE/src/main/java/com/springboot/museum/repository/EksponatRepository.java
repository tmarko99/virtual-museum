package com.springboot.museum.repository;

import com.springboot.museum.entity.Eksponat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EksponatRepository extends JpaRepository<Eksponat, Long> {
}

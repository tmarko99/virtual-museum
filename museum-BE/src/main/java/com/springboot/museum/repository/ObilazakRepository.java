package com.springboot.museum.repository;

import com.springboot.museum.entity.Obilazak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObilazakRepository extends JpaRepository<Obilazak, Long> {
    List<Obilazak> findByUser_Id(long id);
    List<Obilazak> findByUserEmail(String email);
}

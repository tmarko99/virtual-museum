package com.springboot.museum.repository;

import com.springboot.museum.entity.Recenzija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecenzijaRepository extends JpaRepository<Recenzija, Long> {
    List<Recenzija> findByEksponat_Id(long id);
    List<Recenzija> findByUser_Id(long id);
}

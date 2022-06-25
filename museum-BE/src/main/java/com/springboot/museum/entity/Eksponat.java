package com.springboot.museum.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eksponati")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Eksponat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String naziv;
    @Column(columnDefinition="text", nullable = false)
    private String opis;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "cena_obilaska", nullable = false)
    private int cenaObilaska;

    @Column(name = "vreme_obilaska")
    private LocalDateTime vremeObilaska;

    @Column(name = "zemlja_porekla")
    private String zemljaPorekla;

    @OneToMany(mappedBy = "eksponat", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Recenzija> recenzije = new ArrayList<>();

    @OneToMany(mappedBy = "eksponat", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Obilazak> obilasci = new ArrayList<>();
}

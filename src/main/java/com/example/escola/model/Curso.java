package com.example.escola.model;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cursos")
public class Curso {
    
    @Column(name = "curso_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "carga_horaria")
    private int carga_horaria;

    @JsonIgnoreProperties("cursos")
    @ManyToMany(mappedBy = "cursos")
    private List<Professor> professores;

    @Builder.Default
    @JsonIgnoreProperties("cursos")
    @ManyToMany(mappedBy = "cursos")
    private List<Aluno> alunos = new ArrayList<>();

}

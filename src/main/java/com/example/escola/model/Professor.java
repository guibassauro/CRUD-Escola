package com.example.escola.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "professores")
@Entity
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "nome")
    private String nome;

    @Column(name = "idade")
    private int idade;

    @ManyToMany
    @JoinTable(
        name = "professor_curso",
        joinColumns = {@JoinColumn(name  = "professor_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "curso_id", referencedColumnName = "id")}
    )
    @JsonIgnoreProperties("professor")
    private List<Curso> cursos;

}

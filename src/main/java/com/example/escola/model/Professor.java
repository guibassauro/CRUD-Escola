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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professores")
@Entity
public class Professor {
    
    @Column(name = "professor_id")
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
        joinColumns = {@JoinColumn(name  = "professor_id")},
        inverseJoinColumns = {@JoinColumn(name = "curso_id")}
    )
    @JsonIgnoreProperties("professores")
    private List<Curso> cursos;

}

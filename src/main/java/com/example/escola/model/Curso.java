package com.example.escola.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cursos")
@Entity
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "carga_horaria")
    private int carga_horaria;

    @OneToMany(mappedBy = "curso")
    @JsonIgnoreProperties("curso")
    private List<Matricula> matriculas = new ArrayList<>();

    @ManyToMany(mappedBy = "cursos")
    @JsonIgnoreProperties("cursos")
    private List<Professor> professores;

    public void adicionarMatricula(Matricula matricula){
        this.matriculas.add(matricula);
    }

}

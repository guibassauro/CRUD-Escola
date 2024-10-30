package com.example.escola.dto.Response;

import java.util.List;

import com.example.escola.model.Aluno;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CriaCursoResponse {
    
    private Long id;
    private String nome;
    private String descricao;
    private Integer cargaHoraria;
    private List<Aluno> cursoAlunos;
}

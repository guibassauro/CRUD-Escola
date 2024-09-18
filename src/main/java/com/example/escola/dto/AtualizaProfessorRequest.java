package com.example.escola.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaProfessorRequest {
    
    private String nome;
    private int idade;
    private List<Long> cursos_id;
}

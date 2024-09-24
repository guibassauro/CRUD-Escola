package com.example.escola.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtualizaProfessorRequest {
    
    private String nome;
    private Integer idade;
    private List<Long> cursos_id;
}

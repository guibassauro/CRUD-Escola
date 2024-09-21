package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtualizaAlunoRequest {
    
    private String nome;
    private Integer idade;
    private String genero;
}

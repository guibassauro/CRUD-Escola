package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaAlunoRequest {
    
    private String nome;
    private int idade;
    private String genero;
}

package com.example.escola.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriaAlunoRequest {

    private String nome;
    private int idade;
    private String email;
    private String genero;
}

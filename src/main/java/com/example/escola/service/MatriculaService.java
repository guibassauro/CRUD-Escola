package com.example.escola.service;

import java.util.List;

import com.example.escola.dto.CriaMatriculaRequest;
import com.example.escola.model.Matricula;

public interface MatriculaService {
    
    List<Matricula> achaTodasAsMatriculas();

    Matricula matriculaAluno(CriaMatriculaRequest criaMatricula);

    void desmatriculaAluno(Long matricula_id);
}

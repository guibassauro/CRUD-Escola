package com.example.escola.service;

import java.util.List;

import com.example.escola.dto.AtualizaCursoRequest;
import com.example.escola.dto.CriaCursoRequest;
import com.example.escola.model.Curso;

public interface CursoService {
    
    List<Curso> achaTodosOsCurso();

    Curso criaNovoCurso(CriaCursoRequest criaCurso);

    Curso atualizaCurso(Long curso_id, AtualizaCursoRequest atualizaCurso);

    void deletaCurso(Long curso_id);
}

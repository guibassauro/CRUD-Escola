package com.example.escola.service;

import java.util.List;

import com.example.escola.dto.AtualizaProfessorRequest;
import com.example.escola.dto.CriaProfessorRequest;
import com.example.escola.model.Professor;

public interface ProfessorService {
    
    List<Professor> achaTodosOsProfessores();

    List<Professor> achaTodosOsProfessoresDeUmCurso(Long curso_id);

    Professor criaNovoProfessor(CriaProfessorRequest criaProfessor);

    Professor atualizaProfessor(Long professor_id, AtualizaProfessorRequest atualizaProfessor);
}

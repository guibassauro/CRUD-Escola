package com.example.escola.service;

import java.util.List;

import com.example.escola.dto.Request.AtualizaProfessorRequest;
import com.example.escola.dto.Request.CriaProfessorRequest;
import com.example.escola.dto.Response.AtualizaProfessorResponse;
import com.example.escola.dto.Response.CriaProfessorResponse;
import com.example.escola.model.Professor;

public interface ProfessorService {
    
    List<Professor> achaTodosOsProfessores();

    List<Professor> achaTodosOsProfessoresDeUmCurso(Long curso_id);

    CriaProfessorResponse criaNovoProfessor(CriaProfessorRequest criaProfessor);

    AtualizaProfessorResponse atualizaProfessor(Long professor_id, AtualizaProfessorRequest atualizaProfessor);
}

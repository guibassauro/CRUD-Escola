package com.example.escola.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.Request.AtualizaProfessorRequest;
import com.example.escola.dto.Request.CriaProfessorRequest;
import com.example.escola.dto.Response.AtualizaProfessorResponse;
import com.example.escola.dto.Response.CriaProfessorResponse;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Curso;
import com.example.escola.model.Professor;
import com.example.escola.repository.ProfessorRepository;
import com.example.escola.service.ProfessorService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProfessorServiceImpl implements ProfessorService{
    
    private final ProfessorRepository professorRepository;
    private final CursoServiceImpl cursoService;

    @Override
    public List<Professor> achaTodosOsProfessores(){
        return professorRepository.findAll();
    }

    @Override
    public List<Professor> achaTodosOsProfessoresDeUmCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoService.achaCursoPorId(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        return existeCurso.get().getProfessores();
    }

    @Override
    public CriaProfessorResponse criaNovoProfessor(CriaProfessorRequest criaProfessor){
        
        criaProfessor.getCursos_id().forEach(curso_id -> {
            Optional<Curso> existeCurso = cursoService.achaCursoPorId(curso_id);
            if(existeCurso.isEmpty()){
                throw new NotFoundException("Curso " + curso_id + " não encontrado");
            }
        });

        List<Curso> listaDeCursos = cursoService.achaTodosPorCursoId(criaProfessor.getCursos_id());
        
        Professor novoProfessor = Professor.builder()
        .nome(criaProfessor.getNome())
        .idade(criaProfessor.getIdade())
        .cursos(listaDeCursos)
        .build();

        professorRepository.save(novoProfessor);
        
        CriaProfessorResponse resposta = CriaProfessorResponse.builder()
        .id(novoProfessor.getId())
        .nome(novoProfessor.getNome())
        .idade(novoProfessor.getIdade())
        .cursos(novoProfessor.getCursos())
        .build();

        return resposta;
    }

    @Override
    public AtualizaProfessorResponse atualizaProfessor(Long professor_id, AtualizaProfessorRequest atualizaProfessor){
        Optional<Professor> existeProfessor = professorRepository.findById(professor_id);

        if(existeProfessor.isEmpty()){
            throw new NotFoundException("Professor não encontrado!");
        }

        if(!atualizaProfessor.getCursos_id().isEmpty()){
            atualizaProfessor.getCursos_id().forEach(curso_id -> {
                Optional<Curso> existeCurso = cursoService.achaCursoPorId(curso_id);
                if(existeCurso.isEmpty()){
                    throw new NotFoundException("Curso não encontrado");
                }
            });
        }

        List<Curso> listaDeCursos = cursoService.achaTodosPorCursoId(atualizaProfessor.getCursos_id());

        Professor professorAtualizado = Professor.builder()
        .id(existeProfessor.get().getId())
        .nome(atualizaProfessor.getNome() != null ? atualizaProfessor.getNome() : existeProfessor.get().getNome())
        .idade(atualizaProfessor.getIdade() != null ? atualizaProfessor.getIdade() : existeProfessor.get().getIdade())
        .cursos(!atualizaProfessor.getCursos_id().isEmpty() ? listaDeCursos : existeProfessor.get().getCursos())
        .build();
        

        professorRepository.save(professorAtualizado);

        AtualizaProfessorResponse resposta = AtualizaProfessorResponse.builder()
        .id(professorAtualizado.getId())
        .nome(professorAtualizado.getNome())
        .idade(professorAtualizado.getIdade())
        .cursos(professorAtualizado.getCursos())
        .build();

        return resposta;
    }
}

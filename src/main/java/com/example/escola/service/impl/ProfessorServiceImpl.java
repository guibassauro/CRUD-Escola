package com.example.escola.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.AtualizaProfessorRequest;
import com.example.escola.dto.CriaProfessorRequest;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Curso;
import com.example.escola.model.Professor;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.ProfessorRepository;
import com.example.escola.service.ProfessorService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProfessorServiceImpl implements ProfessorService{
    
    private final ProfessorRepository professorRepository;
    private final CursoRepository cursoRepository;

    @Override
    public List<Professor> achaTodosOsProfessores(){
        return professorRepository.findAll();
    }

    @Override
    public List<Professor> achaTodosOsProfessoresDeUmCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        return existeCurso.get().getProfessores();
    }

    @Override
    public Professor criaNovoProfessor(CriaProfessorRequest criaProfessor){
        
        criaProfessor.getCursos_id().forEach(curso_id -> {
            Optional<Curso> existeCurso = cursoRepository.findById(curso_id);
            if(existeCurso.isEmpty()){
                throw new NotFoundException("Curso " + curso_id + " não encontrado");
            }
        });

        List<Curso> listaDeCursos = cursoRepository.findAllById(criaProfessor.getCursos_id());
        
        Professor novoProfessor = new Professor();

        novoProfessor.setNome(criaProfessor.getNome());
        novoProfessor.setIdade(criaProfessor.getIdade());
        novoProfessor.setCursos(listaDeCursos);

        professorRepository.save(novoProfessor);
        return novoProfessor;
    }

    @Override
    public Professor atualizaProfessor(Long professor_id, AtualizaProfessorRequest atualizaProfessor){
        Optional<Professor> existeProfessor = professorRepository.findById(professor_id);

        if(existeProfessor.isEmpty()){
            throw new NotFoundException("Professor não encontrado!");
        }

        Professor professorAtualizado = existeProfessor.get();

        if(atualizaProfessor.getNome() != null){
            professorAtualizado.setNome(atualizaProfessor.getNome());
        }

        if(atualizaProfessor.getIdade() != null){
            professorAtualizado.setIdade(atualizaProfessor.getIdade());
        }

        if(atualizaProfessor.getCursos_id() != null){
            
            atualizaProfessor.getCursos_id().forEach(curso_id -> {
                Optional<Curso> existeCurso = cursoRepository.findById(curso_id);
                if(existeCurso.isEmpty()){
                    throw new NotFoundException("Curso " + curso_id + " não encontrado");
                }
            });

            List<Curso> listaDCursos = cursoRepository.findAllById(atualizaProfessor.getCursos_id());
            professorAtualizado.setCursos(listaDCursos);
        }

        professorRepository.save(professorAtualizado);

        return professorAtualizado;
    }
}

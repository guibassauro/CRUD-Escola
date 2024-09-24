package com.example.escola.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.AtualizaCursoRequest;
import com.example.escola.dto.CriaCursoRequest;
import com.example.escola.exception.BadRequestException;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.CursoRepository;
import com.example.escola.repository.MatriculaRepository;
import com.example.escola.service.CursoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CursoServiceImpl implements CursoService{

    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    
    @Override
    public List<Curso> achaTodosOsCurso(){
        return cursoRepository.findAll();
    }

    @Override
    public Curso criaNovoCurso(CriaCursoRequest criaCurso){

        Curso novoCurso = Curso.builder()
        .nome(criaCurso.getNome())
        .descricao(criaCurso.getDescricao())
        .carga_horaria(criaCurso.getCarga_horaria())
        .build();

        cursoRepository.save(novoCurso);

        return novoCurso;
    }

    @Override
    public Curso atualizaCurso(Long curso_id, AtualizaCursoRequest atualizaCurso){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        Curso cursoParaAtualizar = existeCurso.get();

        if(atualizaCurso.getDescricao() != null){
            cursoParaAtualizar.setDescricao(atualizaCurso.getDescricao());
        }

        if(atualizaCurso.getCarga_horaria() != null){
            cursoParaAtualizar.setCarga_horaria(atualizaCurso.getCarga_horaria());
        }

        cursoRepository.save(cursoParaAtualizar);

        return cursoParaAtualizar;
    }

    @Override
    public void deletaCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);
        List<Matricula> estaMatriculado = matriculaRepository.findAllByCurso_Id(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        if(!estaMatriculado.isEmpty()){
            throw new BadRequestException("Você não pode excluir um curso com matriculas em andamento");
        }

        cursoRepository.deleteById(curso_id);
    }
    
}

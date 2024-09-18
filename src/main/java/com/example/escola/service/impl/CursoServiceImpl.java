package com.example.escola.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.AtualizaCursoRequest;
import com.example.escola.dto.CriaCursoRequest;
import com.example.escola.exception.BadRequestException;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Curso;
import com.example.escola.repository.CursoRepository;
import com.example.escola.service.CursoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CursoServiceImpl implements CursoService{

    private final CursoRepository cursoRepository;
    
    @Override
    public List<Curso> achaTodosOsCurso(){
        return cursoRepository.findAll();
    }

    @Override
    public Curso criaNovoCurso(CriaCursoRequest criaCurso){

        Curso novoCurso = new Curso();

        novoCurso.setNome(criaCurso.getNome());
        novoCurso.setDescricao(criaCurso.getDescricao());
        novoCurso.setCarga_horaria(criaCurso.getCarga_horaria());

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

        cursoParaAtualizar.setDescricao(atualizaCurso.getDescricao());
        cursoParaAtualizar.setCarga_horaria(atualizaCurso.getCarga_horaria());

        cursoRepository.save(cursoParaAtualizar);

        return cursoParaAtualizar;
    }

    @Override
    public void deletaCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        if(!existeCurso.get().getMatriculas().isEmpty()){
            throw new BadRequestException("Você não pode excluir cursos com matriculas em andamento");
        }

        cursoRepository.deleteById(curso_id);
    }
    
}

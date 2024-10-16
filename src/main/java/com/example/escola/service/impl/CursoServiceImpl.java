package com.example.escola.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.Request.AtualizaCursoRequest;
import com.example.escola.dto.Request.CriaCursoRequest;
import com.example.escola.dto.Response.AtualizaCursoResponse;
import com.example.escola.dto.Response.CriaCursoResponse;
import com.example.escola.exception.BadRequestException;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.CursoRepository;
import com.example.escola.service.CursoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CursoServiceImpl implements CursoService{

    private final CursoRepository cursoRepository;
    private final MatriculaServiceImpl matriculaService;
    
    @Override
    public List<Curso> achaTodosOsCurso(){
        return cursoRepository.findAll();
    }

    @Override
    public CriaCursoResponse criaNovoCurso(CriaCursoRequest criaCurso){

        Curso novoCurso = Curso.builder()
        .nome(criaCurso.getNome())
        .descricao(criaCurso.getDescricao())
        .carga_horaria(criaCurso.getCarga_horaria())
        .build();

        cursoRepository.save(novoCurso);

        CriaCursoResponse resposta = CriaCursoResponse.builder()
        .id(novoCurso.getId())
        .nome(novoCurso.getNome())
        .descricao(novoCurso.getDescricao())
        .cargaHoraria(novoCurso.getCarga_horaria())
        .build();

        return resposta;
    }

    @Override
    public AtualizaCursoResponse atualizaCurso(Long curso_id, AtualizaCursoRequest atualizaCurso){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        Curso cursoParaAtualizar = Curso.builder()
        .id(existeCurso.get().getId())
        .nome(existeCurso.get().getNome())
        .descricao(atualizaCurso.getDescricao() != null ? atualizaCurso.getDescricao() : existeCurso.get().getDescricao())
        .carga_horaria(atualizaCurso.getCarga_horaria() != null ? atualizaCurso.getCarga_horaria() : existeCurso.get().getCarga_horaria())
        .build();

        cursoRepository.save(cursoParaAtualizar);

        AtualizaCursoResponse resposta = AtualizaCursoResponse.builder()
        .id(cursoParaAtualizar.getId())
        .nome(cursoParaAtualizar.getNome())
        .descricao(cursoParaAtualizar.getDescricao())
        .cargaHoraria(cursoParaAtualizar.getCarga_horaria())
        .build();

        return resposta;
    }

    @Override
    public void deletaCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);
        List<Matricula> estaMatriculado = matriculaService.achaTodasPorCursoId(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        if(!estaMatriculado.isEmpty()){
            throw new BadRequestException("Você não pode excluir um curso com matriculas em andamento");
        }

        cursoRepository.deleteById(curso_id);
    }

    public Optional<Curso> achaCursoPorId(Long curso_id){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);

        return existeCurso;
    }

    public List<Curso> achaTodosPorCursoId(List<Long> cursos_id){
        List<Curso> listaDeCursos = cursoRepository.findAllById(cursos_id);

        return listaDeCursos;
    }
    
}

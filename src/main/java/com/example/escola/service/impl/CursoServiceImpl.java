package com.example.escola.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.escola.dto.Request.AtualizaCursoRequest;
import com.example.escola.dto.Request.CriaCursoRequest;
import com.example.escola.dto.Response.AtualizaCursoResponse;
import com.example.escola.dto.Response.CriaCursoResponse;
import com.example.escola.dto.Response.MatriculaResponse;
import com.example.escola.exception.BadRequestException;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Aluno;
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
    public CriaCursoResponse criaNovoCurso(CriaCursoRequest criaCurso){

        Curso novoCurso = Curso.builder()
        .nome(criaCurso.getNome())
        .descricao(criaCurso.getDescricao())
        .carga_horaria(criaCurso.getCarga_horaria())
        .alunos(List.of())
        .build();

        cursoRepository.save(novoCurso);

        CriaCursoResponse resposta = CriaCursoResponse.builder()
        .id(novoCurso.getId())
        .nome(novoCurso.getNome())
        .descricao(novoCurso.getDescricao())
        .cargaHoraria(novoCurso.getCarga_horaria())
        .cursoAlunos(novoCurso.getAlunos())
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
        .cursoAlunos(cursoParaAtualizar.getAlunos())
        .build();

        return resposta;
    }

    @Override
    public void deletaCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        if(!existeCurso.get().getAlunos().isEmpty()){
            throw new BadRequestException("Você não pode deletar um curso com matriculas");
        }

        if(!existeCurso.get().getProfessores().isEmpty()){
            throw new BadRequestException("Você não pode deletar um curso com professores");
        }

        cursoRepository.deleteById(curso_id);
    }

    @Override
    public List<Aluno> listaAlunosDoCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoRepository.findById(curso_id);

        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não econtrado");
        }

        List<Aluno> alunos = existeCurso.get().getAlunos();

        return alunos;
    }

    public Optional<Curso> achaCursoPorId(Long curso_id){
        Optional<Curso> curso = cursoRepository.findById(curso_id);

        if(curso.isEmpty()){
            throw new NotFoundException("Curso não encontrado");
        }

        return curso;
    }

    public List<Curso> achaTodosPorCursoId(List<Long> cursos_id){
        List<Curso> lista = cursoRepository.findAllById(cursos_id);
        
        return lista;
    }

    public List<MatriculaResponse> listaMatriculas(){
        List<Curso> cursos = cursoRepository.findAll();
        List<MatriculaResponse> matriculas = new ArrayList<>();

        cursos.forEach(curso -> {
            List<Aluno> alunos = curso.getAlunos();
            alunos.forEach(aluno -> {
                MatriculaResponse matricula = MatriculaResponse.builder()
                .nomeAluno(aluno.getNome())
                .nomeCurso(curso.getNome()).build();
                matriculas.add(matricula);
            });
        });

        return matriculas;
    }
    
}

package com.example.escola.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.Request.AtualizaAlunoRequest;
import com.example.escola.dto.Request.CriaAlunoRequest;
import com.example.escola.dto.Request.CriaMatriculaRequest;
import com.example.escola.dto.Response.AtualizarAlunoResponse;
import com.example.escola.dto.Response.CriaAlunoResponse;
import com.example.escola.exception.BadRequestException;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.service.AlunoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoServiceImpl implements AlunoService{
    
    private final AlunoRepository alunoRepository;
    private final CursoServiceImpl cursoService;

    @Override
    public List<Aluno> achaTodosOsAlunos(){
        return alunoRepository.findAll();
    }

    @Override
    public List<Aluno> achaTodosOsAlunosDeUmCurso(Long curso_id){
        Optional<Curso> curso = cursoService.achaCursoPorId(curso_id);

        if(curso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        }

        List<Aluno> listaDeAlunos = cursoService.listaAlunosDoCurso(curso_id);

        return listaDeAlunos;
    }

    @Override
    public CriaAlunoResponse criaNovoAluno(CriaAlunoRequest criaAluno){
        Optional<Aluno> existeAluno = alunoRepository.findByEmail(criaAluno.getEmail());

        if(existeAluno.isPresent()){
            throw new BadRequestException("Email já cadastrado!");
        }

        Aluno novoAluno = Aluno.builder()
        .nome(criaAluno.getNome())
        .email(criaAluno.getEmail())
        .idade(criaAluno.getIdade())
        .genero(criaAluno.getGenero())
        .cursos(List.of()).build();

        alunoRepository.save(novoAluno);

        CriaAlunoResponse resposta = CriaAlunoResponse.builder()
        .id(novoAluno.getId())
        .nome(novoAluno.getNome())
        .email(novoAluno.getEmail())
        .idade(novoAluno.getIdade())
        .cursos(novoAluno.getCursos())
        .build();

        return resposta;
    }

    @Override
    public AtualizarAlunoResponse atualizaAluno(Long aluno_id, AtualizaAlunoRequest atualizaAluno){
        Optional<Aluno> existeAluno = alunoRepository.findById(aluno_id);

        if(existeAluno.isEmpty()){
            throw new NotFoundException("Aluno não encontrado!");
        }

        Aluno atualizarAluno = Aluno.builder()
        .id(existeAluno.get().getId())
        .nome(atualizaAluno.getNome() != null ? atualizaAluno.getNome() : existeAluno.get().getNome())
        .idade(atualizaAluno.getIdade() != null ? atualizaAluno.getIdade() : existeAluno.get().getIdade())
        .genero(atualizaAluno.getGenero() != null ? atualizaAluno.getGenero() : existeAluno.get().getGenero())
        .email(existeAluno.get().getEmail())
        .cursos(existeAluno.get().getCursos()).build();

        alunoRepository.save(atualizarAluno);

        AtualizarAlunoResponse resposta = AtualizarAlunoResponse.builder()
        .id(atualizarAluno.getId())
        .nome(atualizarAluno.getNome())
        .idade(atualizarAluno.getIdade())
        .email(atualizarAluno.getEmail())
        .alunoCursos(atualizarAluno.getCursos()).build();

        return resposta;
    }

    @Override
    public void deletaAluno(Long aluno_id){
        Optional<Aluno> existeAluno = alunoRepository.findById(aluno_id);

        if(existeAluno.isEmpty()){
            throw new NotFoundException("Aluno não encontrado!");
        }

        if(!existeAluno.get().getCursos().isEmpty()){
            throw new BadRequestException("Você não pode deletar um aluno com matriculas em andamento");
        }

        alunoRepository.deleteById(aluno_id);
    }

    @Override
    public String matriculaAluno(CriaMatriculaRequest criaMatricula){
        Aluno existeAluno = alunoRepository.findById(criaMatricula.getAluno_id()).get();
        Curso existeCurso = cursoService.achaCursoPorId(criaMatricula.getCurso_id()).get();

        List<Curso> cursos = existeAluno.getCursos();

        if(cursos.contains(existeCurso)){
            throw new BadRequestException(existeAluno.getNome() + " já está matriculado em " + existeCurso.getNome() + ".");
        }

        cursos.add(existeCurso);
        existeAluno.setCursos(cursos);

        alunoRepository.save(existeAluno);

        return existeAluno.getNome() + " foi matriculado no curso " + existeCurso.getNome() + ".";
    }

    @Override
    public String desmatriculaAluno(CriaMatriculaRequest deletaMatricula){
        Aluno existeAluno = alunoRepository.findById(deletaMatricula.getAluno_id()).get();
        Curso existeCurso = cursoService.achaCursoPorId(deletaMatricula.getCurso_id()).get();

        List<Curso> cursos = existeAluno.getCursos();

        if(!cursos.contains(existeCurso)){
            throw new BadRequestException(existeAluno.getNome() + " não está matriculado em " + existeCurso.getNome() + ".");
        }

        cursos.remove(existeCurso);
        existeAluno.setCursos(cursos);

        alunoRepository.save(existeAluno);

        return existeAluno.getNome() + " foi desmatriculado do curso " + existeCurso.getNome() + "."; 

    }

    public Optional<Aluno> achaPorAlunoId(Long aluno_id){
        Optional<Aluno> existeAluno = alunoRepository.findById(aluno_id);

        return existeAluno;
    }

}

package com.example.escola.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.escola.dto.Request.AtualizaAlunoRequest;
import com.example.escola.dto.Request.CriaAlunoRequest;
import com.example.escola.dto.Response.AtualizarAlunoResponse;
import com.example.escola.dto.Response.CriaAlunoResponse;
import com.example.escola.exception.BadRequestException;
import com.example.escola.exception.NotFoundException;
import com.example.escola.model.Aluno;
import com.example.escola.model.Curso;
import com.example.escola.model.Matricula;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.service.AlunoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoServiceImpl implements AlunoService{
    
    private final AlunoRepository alunoRepository;
    private final CursoServiceImpl cursoService;
    private final MatriculaServiceImpl matriculaService;

    @Override
    public List<Aluno> achaTodosOsAlunos(){
        return alunoRepository.findAll();
    }

    @Override
    public List<Aluno> achaTodosOsAlunosDeUmCurso(Long curso_id){
        Optional<Curso> existeCurso = cursoService.achaCursoPorId(curso_id);
        
        if(existeCurso.isEmpty()){
            throw new NotFoundException("Curso não encontrado!");
        
        }
        List<Matricula> listaDeMatriculas = matriculaService.achaTodasPorCursoId(curso_id);
        List<Aluno> listaDeAlunos = new ArrayList<>();

        listaDeMatriculas.forEach(matricula -> {
            listaDeAlunos.add(matricula.getAluno());
        });
        
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
        .idade(criaAluno.getIdade()).build();

        CriaAlunoResponse resposta = CriaAlunoResponse.builder()
        .nome(novoAluno.getNome())
        .email(novoAluno.getEmail())
        .idade(novoAluno.getIdade())
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
        .email(existeAluno.get().getEmail()).build();

        alunoRepository.save(atualizarAluno);

        
        AtualizarAlunoResponse resposta = AtualizarAlunoResponse.builder()
        .id(atualizarAluno.getId())
        .nome(atualizarAluno.getNome())
        .idade(atualizarAluno.getIdade())
        .email(atualizarAluno.getEmail())
        .build();

        return resposta;
    }

    @Override
    public void deletaAluno(Long aluno_id){
        Optional<Aluno> existeAluno = alunoRepository.findById(aluno_id);
        List<Matricula> estaMatriculado = matriculaService.achaTodosPorAlunoId(aluno_id);

        if(existeAluno.isEmpty()){
            throw new NotFoundException("Aluno não encontrado!");
        }

        if(!estaMatriculado.isEmpty()){
            throw new BadRequestException("Você não pode deletar um aluno com matriculas em andamento");
        }

        alunoRepository.deleteById(aluno_id);
    }

    public Optional<Aluno> achaPorAlunoId(Long aluno_id){
        Optional<Aluno> existeAluno = alunoRepository.findById(aluno_id);

        return existeAluno;
    }
}

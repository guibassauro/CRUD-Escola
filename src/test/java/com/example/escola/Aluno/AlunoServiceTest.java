package com.example.escola.Aluno;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.escola.dto.AtualizaAlunoRequest;
import com.example.escola.dto.CriaAlunoRequest;
import com.example.escola.model.Aluno;
import com.example.escola.repository.AlunoRepository;
import com.example.escola.service.impl.AlunoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testar Aluno Service")
public class AlunoServiceTest {
    
    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoServiceImpl alunoService;

    @Test
    @DisplayName("Teste para criar novo Aluno")
    void deveCriarAlunoValido(){
        CriaAlunoRequest criaAluno = new CriaAlunoRequest("Pedro", 22, "pedro@gmaile.com", "masculino");

        Aluno novoAluno = alunoService.criaNovoAluno(criaAluno);

        assertEquals(novoAluno.getNome(), "Pedro");
    }

    @Test
    @DisplayName("Teste para garantir que a exception é lançada ao tentar utilizar um email já cadastrado")
    void deveLancarExcepitonParaEmailRepetido(){
        CriaAlunoRequest criaAluno = new CriaAlunoRequest();
        criaAluno.setEmail("email@email.com");

        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setEmail("email@email.com");

        when(alunoRepository.findByEmail(criaAluno.getEmail()))
                            .thenReturn(Optional.of(alunoJaExistente));

        try{
            alunoService.criaNovoAluno(criaAluno);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Email já cadastrado!", e.getMessage());
        }
    }

    @Test
    @DisplayName("Teste para atualizar aluno existente")
    void deveAtualizarAluno(){
        AtualizaAlunoRequest atualizaAluno = new AtualizaAlunoRequest();
        atualizaAluno.setNome("Lucas");

        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setId((long) 1);
        alunoJaExistente.setNome("Pedro");

        when(alunoRepository.findById(alunoJaExistente.getId()))
                            .thenReturn(Optional.of(alunoJaExistente));

        alunoService.atualizaAluno((long)1, atualizaAluno);

        assertEquals(alunoJaExistente.getNome(), "Lucas");
    }

    @Test
    @DisplayName("Garantir Exception caso o id não exista no banco")
    void deveLancarExcepitonParaAlunoNãoExistente(){

        AtualizaAlunoRequest atualizaAluno = new AtualizaAlunoRequest();
        atualizaAluno.setNome("Lucas");
        
        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setId((long)1);
        alunoJaExistente.setNome("Pedro");

        when(alunoRepository.findById((long)2))
                            .thenReturn(Optional.empty());

        try{
            alunoService.atualizaAluno((long)2, atualizaAluno);
            fail("Não deu exception!");
        } catch(Exception e){
            assertEquals("Aluno não encontrado!", e.getMessage());
            assertEquals(alunoJaExistente.getNome(), "Pedro");
        }
    }

    @Test
    @DisplayName("Teste para deletar o Aluno")
    void deveDeletarOAluno(){
        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setId((long)1);

        when(alunoRepository.findById((long)1))
                            .thenReturn(Optional.of(alunoJaExistente));

        alunoService.deletaAluno((long)1);

        verify(alunoRepository).deleteById((long)1);
    }

    @Test
    @DisplayName("Deve testar a exception caso o Aluno não esteja no banco")
    void deveLançarException(){
        Aluno alunoJaExistente = new Aluno();
        alunoJaExistente.setId((long)1);

        when(alunoRepository.findById((long)2))
                            .thenReturn(Optional.empty());

        try{
            alunoService.deletaAluno((long)2);
            fail("Não deu exception");
        } catch(Exception e){
            assertEquals("Aluno não encontrado!", e.getMessage());
        }
    }
}

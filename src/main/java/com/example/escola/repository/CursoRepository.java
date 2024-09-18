package com.example.escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.escola.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long>{
    
}

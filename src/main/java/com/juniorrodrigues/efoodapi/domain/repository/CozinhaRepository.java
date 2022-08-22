package com.juniorrodrigues.efoodapi.domain.repository;

import com.juniorrodrigues.efoodapi.domain.model.Cozinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {
    List<Cozinha> findByNomeContaining(String nome);//busca por nome com like devido a keywould Containing
}

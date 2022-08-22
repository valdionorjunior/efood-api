package com.juniorrodrigues.efoodapi.domain.repository;

import com.juniorrodrigues.efoodapi.domain.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

}

package com.juniorrodrigues.efoodapi.domain.repository;

import com.juniorrodrigues.efoodapi.domain.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

}

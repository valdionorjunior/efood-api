package com.juniorrodrigues.efoodapi.domain.repository;

import com.juniorrodrigues.efoodapi.domain.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

}

package com.juniorrodrigues.efoodapi.domain.repository;

import com.juniorrodrigues.efoodapi.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

//    @Query("from Restaurante where nome like %:nome% and cozinha.id = :id") // -> enviada para o orm.xml dentro da META-INF
    List<Restaurante> consultaPorNomeECozinhaId(String nome, @Param("id") Long cozinha);
}

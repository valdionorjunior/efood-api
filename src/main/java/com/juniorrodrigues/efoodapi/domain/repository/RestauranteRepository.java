package com.juniorrodrigues.efoodapi.domain.repository;

import com.juniorrodrigues.efoodapi.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository
        extends CustomJpaRepository<Restaurante, Long>,
        RestauranteRepositoryQueries,
        JpaSpecificationExecutor<Restaurante> {


    @Query("from Restaurante r join fetch r.cozinha") // QUERY PARA MATAR O PROBLEMA DO N+1 usando um findAll customizado
    List<Restaurante> findAll();

    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

//    @Query("from Restaurante where nome like %:nome% and cozinha.id = :id") // -> enviada para o orm.xml dentro da META-INF
    List<Restaurante> consultaPorNomeECozinhaId(String nome, @Param("id") Long cozinha);

}

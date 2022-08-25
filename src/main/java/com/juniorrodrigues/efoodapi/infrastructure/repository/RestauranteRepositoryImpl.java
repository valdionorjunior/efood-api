package com.juniorrodrigues.efoodapi.infrastructure.repository;

/*
* Repositorio customizado
* */

import com.juniorrodrigues.efoodapi.domain.model.Restaurante;
import com.juniorrodrigues.efoodapi.domain.repository.RestauranteRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){

        /*
        * Criando consulta no banco de dados usando CriteriaQuery
        * */

        // Cria o nosso construtor pra a consulta - nossa factory
        var builder = manager.getCriteriaBuilder();

        // cria a query
        var  criteria = builder.createQuery(Restaurante.class);

        // jpql -> "from Restaurante"
        var root = criteria.from(Restaurante.class);

        var predicates = new ArrayList<Predicate>();

        // jpql implementando "like % :nome %" na consulta
        if(StringUtils.hasLength(nome)) {
            predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
        }
        if(taxaFreteInicial != null){
        // jpql implementando "taxaFrete >= taxaFreteInicial"
            predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
        }
        if(taxaFreteFinal != null) {
            // jpql implementando "taxaFrete <= taxaFreteFinal"
            predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
        }

        // jpql implementando "where da consulta passando os predicates
        //predicates.forEach((predicate) -> criteria.where(predicate));
         criteria.where(predicates.toArray(new Predicate[0])); // passa pro criterias a lista convertendo ela em array de predicados

        var query =  manager.createQuery(criteria);
        return query.getResultList();
    }
}

package com.juniorrodrigues.efoodapi.infrastructure.repository;

/*
* Repositorio customizado
* */

import com.juniorrodrigues.efoodapi.domain.model.Restaurante;
import com.juniorrodrigues.efoodapi.domain.repository.RestauranteRepository;
import com.juniorrodrigues.efoodapi.domain.repository.RestauranteRepositoryQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.juniorrodrigues.efoodapi.infrastructure.repository.spec.RestauranteSpecificationFactory.comFreteGratis;
import static com.juniorrodrigues.efoodapi.infrastructure.repository.spec.RestauranteSpecificationFactory.comNomeSemelhantes;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    @Lazy // faz com que não tomemos o erro de dependencia circular
    private RestauranteRepository restauranteRepository;

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

    @Override
    public List<Restaurante> findComFreteGratis(String nome) {
        return restauranteRepository.findAll(comFreteGratis()
                .and(comNomeSemelhantes(nome)));
    }
}

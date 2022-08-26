package com.juniorrodrigues.efoodapi.infrastructure.repository.spec;

import com.juniorrodrigues.efoodapi.domain.model.Restaurante;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

/*
* Padrao Specification do Spring implementation
* */
@AllArgsConstructor
public class RestaurantesComNomeSemelhanteSpec implements Specification<Restaurante> {

    private static final long serialVersionUID = 1L;

   private String nome;

    @Override
    public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {

        return builder.like(root.get("nome"), "%" + nome + "%");
    }
}

package com.juniorrodrigues.efoodapi.infrastructure.repository.spec;

import com.juniorrodrigues.efoodapi.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestauranteSpecificationFactory {

    public static Specification<Restaurante> comFreteGratis() {
        // retornado uma instancia anonima com lambida para o metodo toPredicate do Specification
        return (root, query, builder) ->
                builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
    }

    public static Specification<Restaurante> comNomeSemelhantes(String nome) {
        // retornado uma instancia anonima com lambida para o metodo toPredicate do Specification
        return  (root, query, builder) ->
                builder.like(root.get("nome"), "%" + nome + "%");
    }
}

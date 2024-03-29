package com.juniorrodrigues.efoodapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "taxa_frete", nullable = false)
    private BigDecimal taxaFrete;

//    @JsonIgnore
    //  @ManyToOne padrao defautl EAGER
    @ManyToOne //(fetch = FetchType.LAZY) // faz com que essas associação so carregue quando precisar -> FetchType.LAZY -> isso na busca - problema do N+1
    @JoinColumn(name = "cozinha_id", nullable = false)
    private Cozinha cozinha;

    @JsonIgnore
    @Embedded // imcorpora a classe endereço como parte na entidade Restaurante
    private Endereco endereco;

    @JsonIgnore
    @CreationTimestamp // atribui uma data atual no momento que a instancia for criada pela primeria vez
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataCadastro; // LocalDateTime representa uma data/hora sem o localTime

    @JsonIgnore
    @UpdateTimestamp // atribui uma data atual no momento que a instancia for atualizada
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataAtualizacao; // LocalDateTime representa uma data/hora sem o localTime

//    @JsonIgnore // para que não trazer essa proprieade a lista de restaurantes
    //  @ManyToMany padrao defautl LAZY - na pratica dificilmente se usa EAGER pois pode alterar a escalabilidade da consulta pra uma curva muito alta
    @JsonIgnore
    @ManyToMany //(fetch = FetchType.EAGER) // faz com que essas associação so carregue tudo de forma anciosa -> FetchType.EAGER -> isso na busca - problema do N+1
    @JoinTable(name = "restaurante_forma_pagamento", // customizar a tabela intermediaria para relacionamento NxN e propriedades
        joinColumns = @JoinColumn(name = "restaurante_id"), // chave da tabela restaurante
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id")  ) // chave da tabela forma de pagamento
    private List<FormaPagamento> formasPagamentos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "restaurante")
    private List<Produto> produtos = new ArrayList<>();

}

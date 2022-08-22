package com.juniorrodrigues.efoodapi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)//com esse parametro cria o hash somente da propriedade que eu deixar explicito
@Entity
public class Permissao {

    @EqualsAndHashCode.Include//cria o hash e equals somente do Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //GET , SET EQUAL E HASH_CODE sendo gerado pela biblioteca do projeto lombok
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;
}

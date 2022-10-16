package com.juniorrodrigues.efoodapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)//com esse parametro cria o hash somente da propriedade que eu deixar explicito
@Entity
public class Cozinha {

    @EqualsAndHashCode.Include//cria o hash e equals somente do Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    //GET , SET EQUAL E HASH_CODE sendo gerado pela biblioteca do projeto lombok
    @Column(nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @JsonIgnore // Ignora a propriedade abaixo na hora de serializar para evitar o erro de serialização recursiva bidirecial ex, cozinha qtem restaurante que tem cozinha que restaurante ...
    @OneToMany(mappedBy = "cozinha") // Associação bidirecional com a entidade restaurante onde a chave é a propriedade cozinha em restaurante
    private List<Restaurante> restaurantes = new ArrayList<>();

}

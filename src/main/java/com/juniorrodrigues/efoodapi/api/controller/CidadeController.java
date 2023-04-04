package com.juniorrodrigues.efoodapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Cidade;
import com.juniorrodrigues.efoodapi.domain.repository.CidadeRepository;
import com.juniorrodrigues.efoodapi.domain.service.CadastroCidadeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    ResponseEntity<List<Cidade>> listar(){
        List<Cidade> cidades = cidadeRepository.findAll();
        if(cidades.size() == 0){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cidades);
    }

    @GetMapping("/{cidadeId}")
    public Cidade buscar(@PathVariable Long cidadeId){
        return cadastroCidadeService.buscarOuFalhar(cidadeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade adicionar(@RequestBody Cidade cidade) {
        return cadastroCidadeService.salvar(cidade);
    }

    @PutMapping("/{cidadeId}")
    public Cidade atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {

        Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);
        BeanUtils.copyProperties(cidade, cidadeAtual, "id" );
        return cadastroCidadeService.salvar(cidadeAtual);
    }

    @DeleteMapping("/{cidadeId}")
    public void remover(@PathVariable Long cidadeId) {
        cadastroCidadeService.excluir(cidadeId);
    }

    @PatchMapping("/{cidadeId}")
    public Cidade atualizarParcial(@PathVariable Long cidadeId,
                                   @RequestBody Map<String, Object> campos) throws URISyntaxException {
        Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);

        mergeObject(campos, cidadeAtual);
        return atualizar(cidadeId, cidadeAtual);
    }

    private void mergeObject(Map<String, Object> dadosOrigem, Cidade restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper();
        Cidade cidadeOrigem = objectMapper.convertValue(dadosOrigem, Cidade.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Cidade.class, nomePropriedade);
            field.setAccessible(true);//deixa acessar asa propriedades privadas da classe cidade

            Object novoValor = ReflectionUtils.getField(field, cidadeOrigem);

            ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
    }
}

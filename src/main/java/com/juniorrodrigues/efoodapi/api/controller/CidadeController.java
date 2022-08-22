package com.juniorrodrigues.efoodapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Cidade;
import com.juniorrodrigues.efoodapi.domain.repository.CidadeRepository;
import com.juniorrodrigues.efoodapi.domain.service.CadastroCidadeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.net.URI;
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

    @PostMapping
    ResponseEntity<Cidade> salvar(@RequestBody Cidade cidade) throws URISyntaxException {
        Cidade cidadeCriada = cadastroCidadeService.salvar(cidade);
        URI location = new URI("/"+cidadeCriada.getId());
        return ResponseEntity.created(location).body(cidadeCriada);
    }

    @PutMapping("/{cidadeId}")
    ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
        try {
            Optional<Cidade> cidadeAtual = cidadeRepository.findById(cidadeId);
            if (!cidadeAtual.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id" );
            cadastroCidadeService.salvar(cidadeAtual.get());
            return ResponseEntity.ok(cidadeAtual.get());
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{cidadeId}")
    ResponseEntity<?> remover(@PathVariable Long cidadeId) {
        try{
        cadastroCidadeService.excluir(cidadeId);
        return ResponseEntity.noContent().build();
        }catch ( EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{cidadeId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long cidadeId,
                                              @RequestBody Map<String, Object> campos) throws URISyntaxException {
        Optional<Cidade> cidadeAtual = cidadeRepository.findById(cidadeId);
        if(!cidadeAtual.isPresent()){
            return ResponseEntity.notFound().build();
        }

        mergeObject(campos, cidadeAtual.get());
        return atualizar(cidadeId, cidadeAtual.get());
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

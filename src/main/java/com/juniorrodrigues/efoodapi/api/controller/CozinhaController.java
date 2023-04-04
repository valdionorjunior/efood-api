package com.juniorrodrigues.efoodapi.api.controller;

import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Cozinha;
import com.juniorrodrigues.efoodapi.domain.repository.CozinhaRepository;
import com.juniorrodrigues.efoodapi.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @GetMapping()
//    public List<Cozinha> listar(){
    public ResponseEntity<List<Cozinha>> listar(){
        List<Cozinha> cozinhas = cozinhaRepository.findAll();
        if(cozinhas.size() == 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cozinhas);
        }
        return ResponseEntity.ok(cozinhas);
    }

    @GetMapping("/{cozinhaId}")
    public Cozinha buscar(@PathVariable Long cozinhaId){
        return cadastroCozinha.buscarOuFalhar(cozinhaId);

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha) throws URISyntaxException {
        Cozinha cozinhaCriada = cadastroCozinha.salvar(cozinha);
        URI location = new URI("/"+cozinhaCriada.getId());
        return ResponseEntity.created(location).body(cozinhaCriada);
    }

    @PutMapping("/{cozinhaId}")
    public Cozinha atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha){

        Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);

        BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
        return cadastroCozinha.salvar(cozinhaAtual);
    }

    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cozinhaId){
        cadastroCozinha.excluir(cozinhaId);
    }
}

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
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId){
        Optional<Cozinha> cozinha =  cozinhaRepository.findById(cozinhaId);
//        return ResponseEntity.status(HttpStatus.OK).body(cozinha);
        if(cozinha.isPresent()){
            return ResponseEntity.ok(cozinha.get());
        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha) throws URISyntaxException {
        Cozinha cozinhaCriada = cadastroCozinha.salvar(cozinha);
        URI location = new URI("/"+cozinhaCriada.getId());
        return ResponseEntity.created(location).body(cozinhaCriada);
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha){
        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(cozinhaId);
        if(!cozinhaAtual.isPresent()){
            return ResponseEntity.notFound().build();
        }
//        cozinhaAtual.setNome(cozinha.getNome());
        BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");
        cadastroCozinha.salvar(cozinhaAtual.get());
        return ResponseEntity.ok(cozinhaAtual.get());
    }

//    @DeleteMapping("/{cozinhaId}")
//    public ResponseEntity<?> remover(@PathVariable Long cozinhaId){
//        try {
//            cadastroCozinha.excluir(cozinhaId);
//            return ResponseEntity.noContent().build();
//        }catch ( EntidadeNaoEncontradaException e){
//            return ResponseEntity.notFound().build();
//        }catch (EntidadeEmUsoException e ){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cozinhaId){
        cadastroCozinha.excluir(cozinhaId);
    }
}

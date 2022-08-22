package com.juniorrodrigues.efoodapi.api.controller;

import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Estado;
import com.juniorrodrigues.efoodapi.domain.repository.EstadoRepository;
import com.juniorrodrigues.efoodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<Estado> listar(){
        return estadoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Estado> adicionar(@RequestBody Estado estado) throws URISyntaxException {
        Estado estadoCriado= cadastroEstadoService.salvar(estado);
        URI location = new URI("/"+estadoCriado.getId());
        return ResponseEntity.created(location).body(estadoCriado);
    }

    @PutMapping("/{estadoId}")
    public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId, @RequestBody Estado estado){
        Optional<Estado> estadoAtual = estadoRepository.findById(estadoId);
        if(!estadoAtual.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        BeanUtils.copyProperties(estado, estadoAtual.get(), "id");
        cadastroEstadoService.salvar(estadoAtual.get());
        return ResponseEntity.ok().body(estadoAtual.get());

    }

    @DeleteMapping("/{estadoId}")
    public ResponseEntity<?> remover(@PathVariable Long estadoId){
        try {
            cadastroEstadoService.excluir(estadoId);
            return ResponseEntity.noContent().build();
        }catch ( EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

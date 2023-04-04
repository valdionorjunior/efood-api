package com.juniorrodrigues.efoodapi.api.controller;

import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Estado;
import com.juniorrodrigues.efoodapi.domain.repository.EstadoRepository;
import com.juniorrodrigues.efoodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{estadoId}")
    public Estado buscar(@PathVariable Long estadoId){
        return cadastroEstadoService.buscarOuFalhar(estadoId);
    }

    @PostMapping
    public ResponseEntity<Estado> adicionar(@RequestBody Estado estado) throws URISyntaxException {
        Estado estadoCriado= cadastroEstadoService.salvar(estado);
        URI location = new URI("/"+estadoCriado.getId());
        return ResponseEntity.created(location).body(estadoCriado);
    }

    @PutMapping("/{estadoId}")
    public Estado atualizar(@PathVariable Long estadoId, @RequestBody Estado estado){
        Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(estadoId);
        BeanUtils.copyProperties(estado, estadoAtual, "id");
        return cadastroEstadoService.salvar(estadoAtual);

    }

    @DeleteMapping("/{estadoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long estadoId){

        cadastroEstadoService.excluir(estadoId);

    }
}

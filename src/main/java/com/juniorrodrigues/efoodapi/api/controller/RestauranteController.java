package com.juniorrodrigues.efoodapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Restaurante;
import com.juniorrodrigues.efoodapi.domain.repository.RestauranteRepository;
import com.juniorrodrigues.efoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import static com.juniorrodrigues.efoodapi.infrastructure.repository.spec.RestauranteSpecificationFactory.comFreteGratis;
import static com.juniorrodrigues.efoodapi.infrastructure.repository.spec.RestauranteSpecificationFactory.comNomeSemelhantes;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @GetMapping
    public ResponseEntity<List<Restaurante>> listar(){
        List<Restaurante> restaurantes = restauranteRepository.findAll();

        if(restaurantes.size() == 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(restaurantes);
        }

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/por-taxa")
    public ResponseEntity<List<Restaurante>> listar(BigDecimal taxaInicial, BigDecimal taxaFinal){
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);

        if(restaurantes.size() == 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(restaurantes);
        }

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/por-nome-e-frete")
    public ResponseEntity<List<Restaurante>> listar(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal){
        List<Restaurante> restaurantes = restauranteRepository.find(nome, taxaInicial, taxaFinal);

        if(restaurantes.size() == 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(restaurantes);
        }

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/frete-gratis")
    public ResponseEntity<List<Restaurante>> listar(String nome){

        List<Restaurante> restaurantes = restauranteRepository.findAll(comFreteGratis()
                .and(comNomeSemelhantes(nome)));

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/por-nome-e-cozinha")
    public ResponseEntity<List<Restaurante>> consultaPorNomeECozinha(String nome, Long id){
        List<Restaurante> restaurantes = restauranteRepository.consultaPorNomeECozinhaId(nome, id);

        if(restaurantes.size() == 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(restaurantes);
        }

        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId){
        Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);

        if(!restaurante.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(restaurante.get());
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) throws URISyntaxException {
        try {
            Restaurante restauranteCriado = cadastroRestauranteService.salvar(restaurante);
            URI location = new URI("/"+restauranteCriado.getId());
            return ResponseEntity.created(location).body(restauranteCriado);
        }catch (EntidadeNaoEncontradaException e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) throws URISyntaxException {
        try {
            Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
            if(!restauranteAtual.isPresent()){
                return ResponseEntity.notFound().build();
            }

            BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id");
            cadastroRestauranteService.salvar(restauranteAtual.get());
            return ResponseEntity.ok(restauranteAtual.get());
        }catch (EntidadeNaoEncontradaException e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{restauranteId}")
    ResponseEntity<?> remover(@PathVariable Long restauranteId) {
        try{
            cadastroRestauranteService.excluir(restauranteId);
            return ResponseEntity.noContent().build();
        }catch ( EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
                                              @RequestBody Map<String, Object> campos) throws URISyntaxException {
        Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
        if(!restauranteAtual.isPresent()){
            return ResponseEntity.notFound().build();
        }

        mergeObject(campos, restauranteAtual.get());
        return atualizar(restauranteId, restauranteAtual.get());
    }

    private void mergeObject(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            field.setAccessible(true);//deixa acessar asa propriedades privadas da classe restaurante

            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

            ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
    }
}


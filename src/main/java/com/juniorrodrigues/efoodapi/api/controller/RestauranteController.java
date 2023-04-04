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

        List<Restaurante> restaurantes = restauranteRepository.findComFreteGratis(nome);

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

    @GetMapping("/busca-o-primeiro")
    public ResponseEntity<Restaurante> buscaPrimeiroRestaurante(){
        Optional<Restaurante> restaurante = restauranteRepository.findFirst();

        return ResponseEntity.ok(restaurante.get());
    }

    @GetMapping("/{restauranteId}")
    public Restaurante buscar(@PathVariable Long restauranteId){
        return cadastroRestauranteService.buscarOuFalhar(restauranteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante adicionar(@RequestBody Restaurante restaurante) {

        return cadastroRestauranteService.salvar(restaurante);

    }

    @PutMapping("/{restauranteId}")
    public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {

            Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);
            BeanUtils.copyProperties(restaurante, restauranteAtual,
                    "id", "formasPagamentos", "endereco", "dataCadastro", "produtos"); //dataAtualizacao não precisa colocar pois @UpdateTime ja ignora quando for PUT
            return cadastroRestauranteService.salvar(restauranteAtual);

    }

    @DeleteMapping("/{restauranteId}")
    public void remover(@PathVariable Long restauranteId) {
        cadastroRestauranteService.excluir(restauranteId);

    }

    @PatchMapping("/{restauranteId}")
    public Restaurante atualizarParcial(@PathVariable Long restauranteId,
                                              @RequestBody Map<String, Object> campos) throws URISyntaxException {
        Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);

        mergeObject(campos, restauranteAtual);
        return atualizar(restauranteId, restauranteAtual);
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


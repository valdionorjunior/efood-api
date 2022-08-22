package com.juniorrodrigues.efoodapi.domain.service;

import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Cozinha;
import com.juniorrodrigues.efoodapi.domain.model.Restaurante;
import com.juniorrodrigues.efoodapi.domain.repository.CozinhaRepository;
import com.juniorrodrigues.efoodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroRestauranteService {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante){
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format("Não existe um cadastro de cozinha com codigo %d.", cozinhaId)));

        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    };

    public void excluir(Long restauranteId) {
        try{
            restauranteRepository.deleteById(restauranteId);
        }catch (EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de restaurante codigo %d.", restauranteId)
            );
        }catch ( DataIntegrityViolationException e ){
            throw new EntidadeEmUsoException(
                    String.format("Restaurante de cogido %d nao pode ser removido, pois esta em uso.", restauranteId)
            );
        }
    }
}

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

    public static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe um cadastro de restaurante codigo %d.";
    public static final String MSG_RESTAURANTE_EM_USO = "Restaurante de cogido %d nao pode ser removido, pois esta em uso.";
    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    CozinhaRepository cozinhaRepository;

    @Autowired
    CadastroCozinhaService cadastroCozinhaService;

    public Restaurante salvar(Restaurante restaurante){
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);

        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    };

    public void excluir(Long restauranteId) {
        try{
            restauranteRepository.deleteById(restauranteId);
        }catch (EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
                    String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, restauranteId)
            );
        }catch ( DataIntegrityViolationException e ){
            throw new EntidadeEmUsoException(
                    String.format(MSG_RESTAURANTE_EM_USO, restauranteId)
            );
        }
    }

    public Restaurante buscarOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, restauranteId)));
    }
}

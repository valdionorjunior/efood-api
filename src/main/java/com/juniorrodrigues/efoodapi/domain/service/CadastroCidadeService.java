package com.juniorrodrigues.efoodapi.domain.service;

import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Cidade;
import com.juniorrodrigues.efoodapi.domain.model.Estado;
import com.juniorrodrigues.efoodapi.domain.repository.CidadeRepository;
import com.juniorrodrigues.efoodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroCidadeService {

    @Autowired
    CidadeRepository cidadeRepository;

    @Autowired
    EstadoRepository estadoRepository;

    public Cidade salvar (Cidade cidade){
        Long estadoId = cidade.getEstado().getId();
        Estado estado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format("Não existe um cadastro de Estado com codigo %d.", estadoId)
                ));

        cidade.setEstado(estado);
        return cidadeRepository.save(cidade);
    }

    public void excluir(Long cidadeId){
        try{
        cidadeRepository.deleteById(cidadeId);
        }catch (EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de cidade codigo %d.", cidadeId)
            );
        }catch ( DataIntegrityViolationException e ){
            throw new EntidadeEmUsoException(
                    String.format("Cidade de cogido %d nao pode ser removida, pois esta em uso.", cidadeId)
            );
        }
    }

}

package com.juniorrodrigues.efoodapi.domain.service;

import com.juniorrodrigues.efoodapi.domain.exception.EntidadeEmUsoException;
import com.juniorrodrigues.efoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.juniorrodrigues.efoodapi.domain.model.Estado;
import com.juniorrodrigues.efoodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

    @Autowired
    EstadoRepository estadoRepository;

    public Estado salvar(Estado estado){
        return estadoRepository.save(estado);
    }

    public void excluir(Long estadoId){
        try {
            estadoRepository.deleteById(estadoId);
        }catch (EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
                    String.format("Estado codigo %d não existe no banco.", estadoId)
            );
        }catch ( DataIntegrityViolationException e ){
            throw new EntidadeEmUsoException(
                    String.format("Estado de cogido %d nao pode ser removida, pois esta em uso.", estadoId)
            );
        }
    }
}

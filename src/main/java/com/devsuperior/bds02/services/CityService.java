package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.services.exceptions.DatabaseException;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository repository;

    @Transactional(readOnly = true)
    public List<CityDTO> findAll() {
        var result = repository.findAll();
        return result.stream().sorted(Comparator.comparing(City::getName)).map(c -> new CityDTO(c)).toList();
    }

    @Transactional
    public CityDTO insert(CityDTO dto) {
        City c = new City(null, dto.getName());
        c = repository.save(c);
        return new CityDTO(c);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Id não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {
            throw new DatabaseException("Falha na integridade referencial");
        }
    }
}

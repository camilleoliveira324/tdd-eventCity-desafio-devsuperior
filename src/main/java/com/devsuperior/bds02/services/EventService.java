package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public EventDTO update(EventDTO dto, Long id) {
        try {
            Event e = repository.getReferenceById(id);
            e.setName(dto.getName());
            e.setDate(dto.getDate());
            e.setUrl(dto.getUrl());

            var c = cityRepository.getReferenceById(dto.getCityId());
            e.setCity(c);
            e = repository.save(e);
            return new EventDTO(e);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }
}

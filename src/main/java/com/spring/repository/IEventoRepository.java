package com.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.model.Evento;

@Repository
public interface IEventoRepository extends CrudRepository<Evento, Long>{

}

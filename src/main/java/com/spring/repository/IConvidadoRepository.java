package com.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.model.Convidado;

@Repository
public interface IConvidadoRepository extends CrudRepository<Convidado, String>{

}

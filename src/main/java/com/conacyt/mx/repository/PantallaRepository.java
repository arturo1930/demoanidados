package com.conacyt.mx.repository;

import com.conacyt.mx.domain.Pantalla;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Pantalla entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PantallaRepository extends ReactiveMongoRepository<Pantalla, String> {}

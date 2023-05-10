package com.conacyt.mx.repository;

import com.conacyt.mx.domain.Nivel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Nivel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NivelRepository extends ReactiveMongoRepository<Nivel, String> {}

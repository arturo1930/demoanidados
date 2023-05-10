package com.conacyt.mx.repository;

import com.conacyt.mx.domain.Subdependencia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Subdependencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubdependenciaRepository extends ReactiveMongoRepository<Subdependencia, String> {}

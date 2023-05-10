package com.conacyt.mx.repository;

import com.conacyt.mx.domain.Dependencia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Dependencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DependenciaRepository extends ReactiveMongoRepository<Dependencia, String> {}

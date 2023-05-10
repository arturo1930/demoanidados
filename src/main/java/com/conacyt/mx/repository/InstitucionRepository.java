package com.conacyt.mx.repository;

import com.conacyt.mx.domain.Institucion;
import com.conacyt.mx.domain.Nivel;
import java.util.List;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Institucion entity.
 */
@Repository
public interface InstitucionRepository extends ReactiveMongoRepository<Institucion, String> {
    Flux<Institucion> findAllByNivelId(String nivelId);
}

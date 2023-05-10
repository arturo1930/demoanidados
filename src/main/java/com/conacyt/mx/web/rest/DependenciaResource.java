package com.conacyt.mx.web.rest;

import com.conacyt.mx.domain.Dependencia;
import com.conacyt.mx.repository.DependenciaRepository;
import com.conacyt.mx.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.conacyt.mx.domain.Dependencia}.
 */
@RestController
@RequestMapping("/api")
public class DependenciaResource {

    private final Logger log = LoggerFactory.getLogger(DependenciaResource.class);

    private static final String ENTITY_NAME = "dependencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DependenciaRepository dependenciaRepository;

    public DependenciaResource(DependenciaRepository dependenciaRepository) {
        this.dependenciaRepository = dependenciaRepository;
    }

    /**
     * {@code POST  /dependencias} : Create a new dependencia.
     *
     * @param dependencia the dependencia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dependencia, or with status {@code 400 (Bad Request)} if the dependencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dependencias")
    public Mono<ResponseEntity<Dependencia>> createDependencia(@RequestBody Dependencia dependencia) throws URISyntaxException {
        log.debug("REST request to save Dependencia : {}", dependencia);
        if (dependencia.getId() != null) {
            throw new BadRequestAlertException("A new dependencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return dependenciaRepository
            .save(dependencia)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/dependencias/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /dependencias/:id} : Updates an existing dependencia.
     *
     * @param id the id of the dependencia to save.
     * @param dependencia the dependencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependencia,
     * or with status {@code 400 (Bad Request)} if the dependencia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dependencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dependencias/{id}")
    public Mono<ResponseEntity<Dependencia>> updateDependencia(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Dependencia dependencia
    ) throws URISyntaxException {
        log.debug("REST request to update Dependencia : {}, {}", id, dependencia);
        if (dependencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dependenciaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return dependenciaRepository
                    .save(dependencia)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /dependencias/:id} : Partial updates given fields of an existing dependencia, field will ignore if it is null
     *
     * @param id the id of the dependencia to save.
     * @param dependencia the dependencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependencia,
     * or with status {@code 400 (Bad Request)} if the dependencia is not valid,
     * or with status {@code 404 (Not Found)} if the dependencia is not found,
     * or with status {@code 500 (Internal Server Error)} if the dependencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dependencias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Dependencia>> partialUpdateDependencia(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Dependencia dependencia
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dependencia partially : {}, {}", id, dependencia);
        if (dependencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dependenciaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Dependencia> result = dependenciaRepository
                    .findById(dependencia.getId())
                    .map(existingDependencia -> {
                        if (dependencia.getValor() != null) {
                            existingDependencia.setValor(dependencia.getValor());
                        }

                        return existingDependencia;
                    })
                    .flatMap(dependenciaRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /dependencias} : get all the dependencias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dependencias in body.
     */
    @GetMapping("/dependencias")
    public Mono<List<Dependencia>> getAllDependencias() {
        log.debug("REST request to get all Dependencias");
        return dependenciaRepository.findAll().collectList();
    }

    /**
     * {@code GET  /dependencias} : get all the dependencias as a stream.
     * @return the {@link Flux} of dependencias.
     */
    @GetMapping(value = "/dependencias", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Dependencia> getAllDependenciasAsStream() {
        log.debug("REST request to get all Dependencias as a stream");
        return dependenciaRepository.findAll();
    }

    /**
     * {@code GET  /dependencias/:id} : get the "id" dependencia.
     *
     * @param id the id of the dependencia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dependencia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dependencias/{id}")
    public Mono<ResponseEntity<Dependencia>> getDependencia(@PathVariable String id) {
        log.debug("REST request to get Dependencia : {}", id);
        Mono<Dependencia> dependencia = dependenciaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dependencia);
    }

    /**
     * {@code DELETE  /dependencias/:id} : delete the "id" dependencia.
     *
     * @param id the id of the dependencia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dependencias/{id}")
    public Mono<ResponseEntity<Void>> deleteDependencia(@PathVariable String id) {
        log.debug("REST request to delete Dependencia : {}", id);
        return dependenciaRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                        .build()
                )
            );
    }
}

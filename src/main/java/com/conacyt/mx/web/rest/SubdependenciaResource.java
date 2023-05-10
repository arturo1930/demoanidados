package com.conacyt.mx.web.rest;

import com.conacyt.mx.domain.Subdependencia;
import com.conacyt.mx.repository.SubdependenciaRepository;
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
 * REST controller for managing {@link com.conacyt.mx.domain.Subdependencia}.
 */
@RestController
@RequestMapping("/api")
public class SubdependenciaResource {

    private final Logger log = LoggerFactory.getLogger(SubdependenciaResource.class);

    private static final String ENTITY_NAME = "subdependencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubdependenciaRepository subdependenciaRepository;

    public SubdependenciaResource(SubdependenciaRepository subdependenciaRepository) {
        this.subdependenciaRepository = subdependenciaRepository;
    }

    /**
     * {@code POST  /subdependencias} : Create a new subdependencia.
     *
     * @param subdependencia the subdependencia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subdependencia, or with status {@code 400 (Bad Request)} if the subdependencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subdependencias")
    public Mono<ResponseEntity<Subdependencia>> createSubdependencia(@RequestBody Subdependencia subdependencia) throws URISyntaxException {
        log.debug("REST request to save Subdependencia : {}", subdependencia);
        if (subdependencia.getId() != null) {
            throw new BadRequestAlertException("A new subdependencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return subdependenciaRepository
            .save(subdependencia)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/subdependencias/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /subdependencias/:id} : Updates an existing subdependencia.
     *
     * @param id the id of the subdependencia to save.
     * @param subdependencia the subdependencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subdependencia,
     * or with status {@code 400 (Bad Request)} if the subdependencia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subdependencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subdependencias/{id}")
    public Mono<ResponseEntity<Subdependencia>> updateSubdependencia(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Subdependencia subdependencia
    ) throws URISyntaxException {
        log.debug("REST request to update Subdependencia : {}, {}", id, subdependencia);
        if (subdependencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subdependencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subdependenciaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return subdependenciaRepository
                    .save(subdependencia)
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
     * {@code PATCH  /subdependencias/:id} : Partial updates given fields of an existing subdependencia, field will ignore if it is null
     *
     * @param id the id of the subdependencia to save.
     * @param subdependencia the subdependencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subdependencia,
     * or with status {@code 400 (Bad Request)} if the subdependencia is not valid,
     * or with status {@code 404 (Not Found)} if the subdependencia is not found,
     * or with status {@code 500 (Internal Server Error)} if the subdependencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subdependencias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Subdependencia>> partialUpdateSubdependencia(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Subdependencia subdependencia
    ) throws URISyntaxException {
        log.debug("REST request to partial update Subdependencia partially : {}, {}", id, subdependencia);
        if (subdependencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subdependencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subdependenciaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Subdependencia> result = subdependenciaRepository
                    .findById(subdependencia.getId())
                    .map(existingSubdependencia -> {
                        if (subdependencia.getValor() != null) {
                            existingSubdependencia.setValor(subdependencia.getValor());
                        }

                        return existingSubdependencia;
                    })
                    .flatMap(subdependenciaRepository::save);

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
     * {@code GET  /subdependencias} : get all the subdependencias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subdependencias in body.
     */
    @GetMapping("/subdependencias")
    public Mono<List<Subdependencia>> getAllSubdependencias() {
        log.debug("REST request to get all Subdependencias");
        return subdependenciaRepository.findAll().collectList();
    }

    /**
     * {@code GET  /subdependencias} : get all the subdependencias as a stream.
     * @return the {@link Flux} of subdependencias.
     */
    @GetMapping(value = "/subdependencias", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Subdependencia> getAllSubdependenciasAsStream() {
        log.debug("REST request to get all Subdependencias as a stream");
        return subdependenciaRepository.findAll();
    }

    /**
     * {@code GET  /subdependencias/:id} : get the "id" subdependencia.
     *
     * @param id the id of the subdependencia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subdependencia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subdependencias/{id}")
    public Mono<ResponseEntity<Subdependencia>> getSubdependencia(@PathVariable String id) {
        log.debug("REST request to get Subdependencia : {}", id);
        Mono<Subdependencia> subdependencia = subdependenciaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subdependencia);
    }

    /**
     * {@code DELETE  /subdependencias/:id} : delete the "id" subdependencia.
     *
     * @param id the id of the subdependencia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subdependencias/{id}")
    public Mono<ResponseEntity<Void>> deleteSubdependencia(@PathVariable String id) {
        log.debug("REST request to delete Subdependencia : {}", id);
        return subdependenciaRepository
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

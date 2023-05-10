package com.conacyt.mx.web.rest;

import com.conacyt.mx.domain.Nivel;
import com.conacyt.mx.repository.NivelRepository;
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
 * REST controller for managing {@link com.conacyt.mx.domain.Nivel}.
 */
@RestController
@RequestMapping("/api")
public class NivelResource {

    private final Logger log = LoggerFactory.getLogger(NivelResource.class);

    private static final String ENTITY_NAME = "nivel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NivelRepository nivelRepository;

    public NivelResource(NivelRepository nivelRepository) {
        this.nivelRepository = nivelRepository;
    }

    /**
     * {@code POST  /nivels} : Create a new nivel.
     *
     * @param nivel the nivel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nivel, or with status {@code 400 (Bad Request)} if the nivel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nivels")
    public Mono<ResponseEntity<Nivel>> createNivel(@RequestBody Nivel nivel) throws URISyntaxException {
        log.debug("REST request to save Nivel : {}", nivel);
        if (nivel.getId() != null) {
            throw new BadRequestAlertException("A new nivel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return nivelRepository
            .save(nivel)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/nivels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /nivels/:id} : Updates an existing nivel.
     *
     * @param id the id of the nivel to save.
     * @param nivel the nivel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nivel,
     * or with status {@code 400 (Bad Request)} if the nivel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nivel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nivels/{id}")
    public Mono<ResponseEntity<Nivel>> updateNivel(@PathVariable(value = "id", required = false) final String id, @RequestBody Nivel nivel)
        throws URISyntaxException {
        log.debug("REST request to update Nivel : {}, {}", id, nivel);
        if (nivel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nivel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return nivelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return nivelRepository
                    .save(nivel)
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
     * {@code PATCH  /nivels/:id} : Partial updates given fields of an existing nivel, field will ignore if it is null
     *
     * @param id the id of the nivel to save.
     * @param nivel the nivel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nivel,
     * or with status {@code 400 (Bad Request)} if the nivel is not valid,
     * or with status {@code 404 (Not Found)} if the nivel is not found,
     * or with status {@code 500 (Internal Server Error)} if the nivel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nivels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Nivel>> partialUpdateNivel(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Nivel nivel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Nivel partially : {}, {}", id, nivel);
        if (nivel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nivel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return nivelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Nivel> result = nivelRepository
                    .findById(nivel.getId())
                    .map(existingNivel -> {
                        if (nivel.getValor() != null) {
                            existingNivel.setValor(nivel.getValor());
                        }

                        return existingNivel;
                    })
                    .flatMap(nivelRepository::save);

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
     * {@code GET  /nivels} : get all the nivels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nivels in body.
     */
    @GetMapping("/nivels")
    public Mono<List<Nivel>> getAllNivels() {
        log.debug("REST request to get all Nivels");
        return nivelRepository.findAll().collectList();
    }

    /**
     * {@code GET  /nivels} : get all the nivels as a stream.
     * @return the {@link Flux} of nivels.
     */
    @GetMapping(value = "/nivels", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Nivel> getAllNivelsAsStream() {
        log.debug("REST request to get all Nivels as a stream");
        return nivelRepository.findAll();
    }

    /**
     * {@code GET  /nivels/:id} : get the "id" nivel.
     *
     * @param id the id of the nivel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nivel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nivels/{id}")
    public Mono<ResponseEntity<Nivel>> getNivel(@PathVariable String id) {
        log.debug("REST request to get Nivel : {}", id);
        Mono<Nivel> nivel = nivelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(nivel);
    }

    /**
     * {@code DELETE  /nivels/:id} : delete the "id" nivel.
     *
     * @param id the id of the nivel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nivels/{id}")
    public Mono<ResponseEntity<Void>> deleteNivel(@PathVariable String id) {
        log.debug("REST request to delete Nivel : {}", id);
        return nivelRepository
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

package com.conacyt.mx.web.rest;

import com.conacyt.mx.domain.Institucion;
import com.conacyt.mx.domain.Nivel;
import com.conacyt.mx.repository.InstitucionRepository;
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
 * REST controller for managing {@link com.conacyt.mx.domain.Institucion}.
 */
@RestController
@RequestMapping("/api")
public class InstitucionResource {

    private final Logger log = LoggerFactory.getLogger(InstitucionResource.class);

    private static final String ENTITY_NAME = "institucion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstitucionRepository institucionRepository;

    public InstitucionResource(InstitucionRepository institucionRepository) {
        this.institucionRepository = institucionRepository;
    }

    /**
     * {@code POST  /institucions} : Create a new institucion.
     *
     * @param institucion the institucion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new institucion, or with status {@code 400 (Bad Request)} if the institucion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/institucions")
    public Mono<ResponseEntity<Institucion>> createInstitucion(@RequestBody Institucion institucion) throws URISyntaxException {
        log.debug("REST request to save Institucion : {}", institucion);
        if (institucion.getId() != null) {
            throw new BadRequestAlertException("A new institucion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return institucionRepository
            .save(institucion)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/institucions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /institucions/:id} : Updates an existing institucion.
     *
     * @param id the id of the institucion to save.
     * @param institucion the institucion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institucion,
     * or with status {@code 400 (Bad Request)} if the institucion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the institucion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/institucions/{id}")
    public Mono<ResponseEntity<Institucion>> updateInstitucion(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Institucion institucion
    ) throws URISyntaxException {
        log.debug("REST request to update Institucion : {}, {}", id, institucion);
        if (institucion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institucion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return institucionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return institucionRepository
                    .save(institucion)
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
     * {@code PATCH  /institucions/:id} : Partial updates given fields of an existing institucion, field will ignore if it is null
     *
     * @param id the id of the institucion to save.
     * @param institucion the institucion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institucion,
     * or with status {@code 400 (Bad Request)} if the institucion is not valid,
     * or with status {@code 404 (Not Found)} if the institucion is not found,
     * or with status {@code 500 (Internal Server Error)} if the institucion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/institucions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Institucion>> partialUpdateInstitucion(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Institucion institucion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Institucion partially : {}, {}", id, institucion);
        if (institucion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institucion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return institucionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Institucion> result = institucionRepository
                    .findById(institucion.getId())
                    .map(existingInstitucion -> {
                        if (institucion.getValor() != null) {
                            existingInstitucion.setValor(institucion.getValor());
                        }

                        return existingInstitucion;
                    })
                    .flatMap(institucionRepository::save);

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
     * {@code GET  /institucions} : get all the institucions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of institucions in body.
     */
    @GetMapping("/institucions")
    public Mono<List<Institucion>> getAllInstitucions() {
        log.debug("REST request to get all Institucions");
        return institucionRepository.findAll().collectList();
    }

    /**
     * {@code GET  /institucions} : get all the institucions as a stream.
     * @return the {@link Flux} of institucions.
     */
    @GetMapping(value = "/institucions", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Institucion> getAllInstitucionsAsStream() {
        log.debug("REST request to get all Institucions as a stream");
        return institucionRepository.findAll();
    }

    /**
     * {@code GET  /institucions/:id} : get the "id" institucion.
     *
     * @param id the id of the institucion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the institucion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/institucions/{id}")
    public Mono<ResponseEntity<Institucion>> getInstitucion(@PathVariable String id) {
        log.debug("REST request to get Institucion : {}", id);
        Mono<Institucion> institucion = institucionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(institucion);
    }

    /**
     * {@code DELETE  /institucions/:id} : delete the "id" institucion.
     *
     * @param id the id of the institucion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/institucions/{id}")
    public Mono<ResponseEntity<Void>> deleteInstitucion(@PathVariable String id) {
        log.debug("REST request to delete Institucion : {}", id);
        return institucionRepository
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

    /**
     * {@code GET  /institucions/:id} : get the "id" institucion.
     *
     * @param id the id of the institucion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the institucion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/institucions/nivel/{nivelId}")
    public Flux<Institucion> getInstitucionByNivel(@PathVariable String nivelId) {
        return institucionRepository.findAllByNivelId(nivelId);
    }
}

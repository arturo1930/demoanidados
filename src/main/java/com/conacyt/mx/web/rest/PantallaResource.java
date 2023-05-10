package com.conacyt.mx.web.rest;

import com.conacyt.mx.domain.Pantalla;
import com.conacyt.mx.repository.PantallaRepository;
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
 * REST controller for managing {@link com.conacyt.mx.domain.Pantalla}.
 */
@RestController
@RequestMapping("/api")
public class PantallaResource {

    private final Logger log = LoggerFactory.getLogger(PantallaResource.class);

    private static final String ENTITY_NAME = "pantalla";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PantallaRepository pantallaRepository;

    public PantallaResource(PantallaRepository pantallaRepository) {
        this.pantallaRepository = pantallaRepository;
    }

    /**
     * {@code POST  /pantallas} : Create a new pantalla.
     *
     * @param pantalla the pantalla to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pantalla, or with status {@code 400 (Bad Request)} if the pantalla has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pantallas")
    public Mono<ResponseEntity<Pantalla>> createPantalla(@RequestBody Pantalla pantalla) throws URISyntaxException {
        log.debug("REST request to save Pantalla : {}", pantalla);
        if (pantalla.getId() != null) {
            throw new BadRequestAlertException("A new pantalla cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pantallaRepository
            .save(pantalla)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/pantallas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /pantallas/:id} : Updates an existing pantalla.
     *
     * @param id the id of the pantalla to save.
     * @param pantalla the pantalla to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pantalla,
     * or with status {@code 400 (Bad Request)} if the pantalla is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pantalla couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pantallas/{id}")
    public Mono<ResponseEntity<Pantalla>> updatePantalla(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Pantalla pantalla
    ) throws URISyntaxException {
        log.debug("REST request to update Pantalla : {}, {}", id, pantalla);
        if (pantalla.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pantalla.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pantallaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pantallaRepository
                    .save(pantalla)
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
     * {@code PATCH  /pantallas/:id} : Partial updates given fields of an existing pantalla, field will ignore if it is null
     *
     * @param id the id of the pantalla to save.
     * @param pantalla the pantalla to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pantalla,
     * or with status {@code 400 (Bad Request)} if the pantalla is not valid,
     * or with status {@code 404 (Not Found)} if the pantalla is not found,
     * or with status {@code 500 (Internal Server Error)} if the pantalla couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pantallas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Pantalla>> partialUpdatePantalla(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Pantalla pantalla
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pantalla partially : {}, {}", id, pantalla);
        if (pantalla.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pantalla.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pantallaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Pantalla> result = pantallaRepository
                    .findById(pantalla.getId())
                    .map(existingPantalla -> {
                        return existingPantalla;
                    })
                    .flatMap(pantallaRepository::save);

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
     * {@code GET  /pantallas} : get all the pantallas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pantallas in body.
     */
    @GetMapping("/pantallas")
    public Mono<List<Pantalla>> getAllPantallas() {
        log.debug("REST request to get all Pantallas");
        return pantallaRepository.findAll().collectList();
    }

    /**
     * {@code GET  /pantallas} : get all the pantallas as a stream.
     * @return the {@link Flux} of pantallas.
     */
    @GetMapping(value = "/pantallas", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Pantalla> getAllPantallasAsStream() {
        log.debug("REST request to get all Pantallas as a stream");
        return pantallaRepository.findAll();
    }

    /**
     * {@code GET  /pantallas/:id} : get the "id" pantalla.
     *
     * @param id the id of the pantalla to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pantalla, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pantallas/{id}")
    public Mono<ResponseEntity<Pantalla>> getPantalla(@PathVariable String id) {
        log.debug("REST request to get Pantalla : {}", id);
        Mono<Pantalla> pantalla = pantallaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pantalla);
    }

    /**
     * {@code DELETE  /pantallas/:id} : delete the "id" pantalla.
     *
     * @param id the id of the pantalla to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pantallas/{id}")
    public Mono<ResponseEntity<Void>> deletePantalla(@PathVariable String id) {
        log.debug("REST request to delete Pantalla : {}", id);
        return pantallaRepository
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

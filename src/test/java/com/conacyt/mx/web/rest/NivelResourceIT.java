package com.conacyt.mx.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.conacyt.mx.IntegrationTest;
import com.conacyt.mx.domain.Nivel;
import com.conacyt.mx.repository.NivelRepository;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link NivelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NivelResourceIT {

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nivels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Nivel nivel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nivel createEntity() {
        Nivel nivel = new Nivel().valor(DEFAULT_VALOR);
        return nivel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nivel createUpdatedEntity() {
        Nivel nivel = new Nivel().valor(UPDATED_VALOR);
        return nivel;
    }

    @BeforeEach
    public void initTest() {
        nivelRepository.deleteAll().block();
        nivel = createEntity();
    }

    @Test
    void createNivel() throws Exception {
        int databaseSizeBeforeCreate = nivelRepository.findAll().collectList().block().size();
        // Create the Nivel
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nivel))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeCreate + 1);
        Nivel testNivel = nivelList.get(nivelList.size() - 1);
        assertThat(testNivel.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void createNivelWithExistingId() throws Exception {
        // Create the Nivel with an existing ID
        nivel.setId("existing_id");

        int databaseSizeBeforeCreate = nivelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nivel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllNivelsAsStream() {
        // Initialize the database
        nivelRepository.save(nivel).block();

        List<Nivel> nivelList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Nivel.class)
            .getResponseBody()
            .filter(nivel::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(nivelList).isNotNull();
        assertThat(nivelList).hasSize(1);
        Nivel testNivel = nivelList.get(0);
        assertThat(testNivel.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void getAllNivels() {
        // Initialize the database
        nivelRepository.save(nivel).block();

        // Get all the nivelList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(nivel.getId()))
            .jsonPath("$.[*].valor")
            .value(hasItem(DEFAULT_VALOR));
    }

    @Test
    void getNivel() {
        // Initialize the database
        nivelRepository.save(nivel).block();

        // Get the nivel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, nivel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(nivel.getId()))
            .jsonPath("$.valor")
            .value(is(DEFAULT_VALOR));
    }

    @Test
    void getNonExistingNivel() {
        // Get the nivel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNivel() throws Exception {
        // Initialize the database
        nivelRepository.save(nivel).block();

        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();

        // Update the nivel
        Nivel updatedNivel = nivelRepository.findById(nivel.getId()).block();
        updatedNivel.valor(UPDATED_VALOR);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedNivel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedNivel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
        Nivel testNivel = nivelList.get(nivelList.size() - 1);
        assertThat(testNivel.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void putNonExistingNivel() throws Exception {
        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();
        nivel.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, nivel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nivel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNivel() throws Exception {
        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();
        nivel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nivel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNivel() throws Exception {
        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();
        nivel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nivel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNivelWithPatch() throws Exception {
        // Initialize the database
        nivelRepository.save(nivel).block();

        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();

        // Update the nivel using partial update
        Nivel partialUpdatedNivel = new Nivel();
        partialUpdatedNivel.setId(nivel.getId());

        partialUpdatedNivel.valor(UPDATED_VALOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNivel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNivel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
        Nivel testNivel = nivelList.get(nivelList.size() - 1);
        assertThat(testNivel.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void fullUpdateNivelWithPatch() throws Exception {
        // Initialize the database
        nivelRepository.save(nivel).block();

        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();

        // Update the nivel using partial update
        Nivel partialUpdatedNivel = new Nivel();
        partialUpdatedNivel.setId(nivel.getId());

        partialUpdatedNivel.valor(UPDATED_VALOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNivel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNivel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
        Nivel testNivel = nivelList.get(nivelList.size() - 1);
        assertThat(testNivel.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void patchNonExistingNivel() throws Exception {
        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();
        nivel.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, nivel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nivel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNivel() throws Exception {
        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();
        nivel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nivel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNivel() throws Exception {
        int databaseSizeBeforeUpdate = nivelRepository.findAll().collectList().block().size();
        nivel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nivel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNivel() {
        // Initialize the database
        nivelRepository.save(nivel).block();

        int databaseSizeBeforeDelete = nivelRepository.findAll().collectList().block().size();

        // Delete the nivel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, nivel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Nivel> nivelList = nivelRepository.findAll().collectList().block();
        assertThat(nivelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

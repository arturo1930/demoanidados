package com.conacyt.mx.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.conacyt.mx.IntegrationTest;
import com.conacyt.mx.domain.Dependencia;
import com.conacyt.mx.repository.DependenciaRepository;
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
 * Integration tests for the {@link DependenciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DependenciaResourceIT {

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dependencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Dependencia dependencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependencia createEntity() {
        Dependencia dependencia = new Dependencia().valor(DEFAULT_VALOR);
        return dependencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependencia createUpdatedEntity() {
        Dependencia dependencia = new Dependencia().valor(UPDATED_VALOR);
        return dependencia;
    }

    @BeforeEach
    public void initTest() {
        dependenciaRepository.deleteAll().block();
        dependencia = createEntity();
    }

    @Test
    void createDependencia() throws Exception {
        int databaseSizeBeforeCreate = dependenciaRepository.findAll().collectList().block().size();
        // Create the Dependencia
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dependencia))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void createDependenciaWithExistingId() throws Exception {
        // Create the Dependencia with an existing ID
        dependencia.setId("existing_id");

        int databaseSizeBeforeCreate = dependenciaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDependenciasAsStream() {
        // Initialize the database
        dependenciaRepository.save(dependencia).block();

        List<Dependencia> dependenciaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Dependencia.class)
            .getResponseBody()
            .filter(dependencia::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(dependenciaList).isNotNull();
        assertThat(dependenciaList).hasSize(1);
        Dependencia testDependencia = dependenciaList.get(0);
        assertThat(testDependencia.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void getAllDependencias() {
        // Initialize the database
        dependenciaRepository.save(dependencia).block();

        // Get all the dependenciaList
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
            .value(hasItem(dependencia.getId()))
            .jsonPath("$.[*].valor")
            .value(hasItem(DEFAULT_VALOR));
    }

    @Test
    void getDependencia() {
        // Initialize the database
        dependenciaRepository.save(dependencia).block();

        // Get the dependencia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, dependencia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(dependencia.getId()))
            .jsonPath("$.valor")
            .value(is(DEFAULT_VALOR));
    }

    @Test
    void getNonExistingDependencia() {
        // Get the dependencia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.save(dependencia).block();

        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();

        // Update the dependencia
        Dependencia updatedDependencia = dependenciaRepository.findById(dependencia.getId()).block();
        updatedDependencia.valor(UPDATED_VALOR);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDependencia.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDependencia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void putNonExistingDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();
        dependencia.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dependencia.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();
        dependencia.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();
        dependencia.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dependencia))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDependenciaWithPatch() throws Exception {
        // Initialize the database
        dependenciaRepository.save(dependencia).block();

        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();

        // Update the dependencia using partial update
        Dependencia partialUpdatedDependencia = new Dependencia();
        partialUpdatedDependencia.setId(dependencia.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDependencia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDependencia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void fullUpdateDependenciaWithPatch() throws Exception {
        // Initialize the database
        dependenciaRepository.save(dependencia).block();

        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();

        // Update the dependencia using partial update
        Dependencia partialUpdatedDependencia = new Dependencia();
        partialUpdatedDependencia.setId(dependencia.getId());

        partialUpdatedDependencia.valor(UPDATED_VALOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDependencia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDependencia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void patchNonExistingDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();
        dependencia.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, dependencia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();
        dependencia.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().collectList().block().size();
        dependencia.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dependencia))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDependencia() {
        // Initialize the database
        dependenciaRepository.save(dependencia).block();

        int databaseSizeBeforeDelete = dependenciaRepository.findAll().collectList().block().size();

        // Delete the dependencia
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, dependencia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Dependencia> dependenciaList = dependenciaRepository.findAll().collectList().block();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

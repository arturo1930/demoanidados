package com.conacyt.mx.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.conacyt.mx.IntegrationTest;
import com.conacyt.mx.domain.Subdependencia;
import com.conacyt.mx.repository.SubdependenciaRepository;
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
 * Integration tests for the {@link SubdependenciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SubdependenciaResourceIT {

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subdependencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SubdependenciaRepository subdependenciaRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Subdependencia subdependencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subdependencia createEntity() {
        Subdependencia subdependencia = new Subdependencia().valor(DEFAULT_VALOR);
        return subdependencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subdependencia createUpdatedEntity() {
        Subdependencia subdependencia = new Subdependencia().valor(UPDATED_VALOR);
        return subdependencia;
    }

    @BeforeEach
    public void initTest() {
        subdependenciaRepository.deleteAll().block();
        subdependencia = createEntity();
    }

    @Test
    void createSubdependencia() throws Exception {
        int databaseSizeBeforeCreate = subdependenciaRepository.findAll().collectList().block().size();
        // Create the Subdependencia
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subdependencia))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Subdependencia testSubdependencia = subdependenciaList.get(subdependenciaList.size() - 1);
        assertThat(testSubdependencia.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void createSubdependenciaWithExistingId() throws Exception {
        // Create the Subdependencia with an existing ID
        subdependencia.setId("existing_id");

        int databaseSizeBeforeCreate = subdependenciaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subdependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSubdependenciasAsStream() {
        // Initialize the database
        subdependenciaRepository.save(subdependencia).block();

        List<Subdependencia> subdependenciaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Subdependencia.class)
            .getResponseBody()
            .filter(subdependencia::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(subdependenciaList).isNotNull();
        assertThat(subdependenciaList).hasSize(1);
        Subdependencia testSubdependencia = subdependenciaList.get(0);
        assertThat(testSubdependencia.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void getAllSubdependencias() {
        // Initialize the database
        subdependenciaRepository.save(subdependencia).block();

        // Get all the subdependenciaList
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
            .value(hasItem(subdependencia.getId()))
            .jsonPath("$.[*].valor")
            .value(hasItem(DEFAULT_VALOR));
    }

    @Test
    void getSubdependencia() {
        // Initialize the database
        subdependenciaRepository.save(subdependencia).block();

        // Get the subdependencia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, subdependencia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(subdependencia.getId()))
            .jsonPath("$.valor")
            .value(is(DEFAULT_VALOR));
    }

    @Test
    void getNonExistingSubdependencia() {
        // Get the subdependencia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSubdependencia() throws Exception {
        // Initialize the database
        subdependenciaRepository.save(subdependencia).block();

        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();

        // Update the subdependencia
        Subdependencia updatedSubdependencia = subdependenciaRepository.findById(subdependencia.getId()).block();
        updatedSubdependencia.valor(UPDATED_VALOR);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSubdependencia.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSubdependencia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
        Subdependencia testSubdependencia = subdependenciaList.get(subdependenciaList.size() - 1);
        assertThat(testSubdependencia.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void putNonExistingSubdependencia() throws Exception {
        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();
        subdependencia.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, subdependencia.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subdependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSubdependencia() throws Exception {
        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();
        subdependencia.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subdependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSubdependencia() throws Exception {
        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();
        subdependencia.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subdependencia))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSubdependenciaWithPatch() throws Exception {
        // Initialize the database
        subdependenciaRepository.save(subdependencia).block();

        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();

        // Update the subdependencia using partial update
        Subdependencia partialUpdatedSubdependencia = new Subdependencia();
        partialUpdatedSubdependencia.setId(subdependencia.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSubdependencia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSubdependencia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
        Subdependencia testSubdependencia = subdependenciaList.get(subdependenciaList.size() - 1);
        assertThat(testSubdependencia.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void fullUpdateSubdependenciaWithPatch() throws Exception {
        // Initialize the database
        subdependenciaRepository.save(subdependencia).block();

        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();

        // Update the subdependencia using partial update
        Subdependencia partialUpdatedSubdependencia = new Subdependencia();
        partialUpdatedSubdependencia.setId(subdependencia.getId());

        partialUpdatedSubdependencia.valor(UPDATED_VALOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSubdependencia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSubdependencia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
        Subdependencia testSubdependencia = subdependenciaList.get(subdependenciaList.size() - 1);
        assertThat(testSubdependencia.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void patchNonExistingSubdependencia() throws Exception {
        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();
        subdependencia.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, subdependencia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subdependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSubdependencia() throws Exception {
        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();
        subdependencia.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subdependencia))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSubdependencia() throws Exception {
        int databaseSizeBeforeUpdate = subdependenciaRepository.findAll().collectList().block().size();
        subdependencia.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subdependencia))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Subdependencia in the database
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSubdependencia() {
        // Initialize the database
        subdependenciaRepository.save(subdependencia).block();

        int databaseSizeBeforeDelete = subdependenciaRepository.findAll().collectList().block().size();

        // Delete the subdependencia
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, subdependencia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Subdependencia> subdependenciaList = subdependenciaRepository.findAll().collectList().block();
        assertThat(subdependenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

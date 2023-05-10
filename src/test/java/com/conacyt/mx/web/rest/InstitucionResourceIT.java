package com.conacyt.mx.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.conacyt.mx.IntegrationTest;
import com.conacyt.mx.domain.Institucion;
import com.conacyt.mx.repository.InstitucionRepository;
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
 * Integration tests for the {@link InstitucionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InstitucionResourceIT {

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/institucions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InstitucionRepository institucionRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Institucion institucion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institucion createEntity() {
        Institucion institucion = new Institucion().valor(DEFAULT_VALOR);
        return institucion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institucion createUpdatedEntity() {
        Institucion institucion = new Institucion().valor(UPDATED_VALOR);
        return institucion;
    }

    @BeforeEach
    public void initTest() {
        institucionRepository.deleteAll().block();
        institucion = createEntity();
    }

    @Test
    void createInstitucion() throws Exception {
        int databaseSizeBeforeCreate = institucionRepository.findAll().collectList().block().size();
        // Create the Institucion
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institucion))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeCreate + 1);
        Institucion testInstitucion = institucionList.get(institucionList.size() - 1);
        assertThat(testInstitucion.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void createInstitucionWithExistingId() throws Exception {
        // Create the Institucion with an existing ID
        institucion.setId("existing_id");

        int databaseSizeBeforeCreate = institucionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institucion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInstitucionsAsStream() {
        // Initialize the database
        institucionRepository.save(institucion).block();

        List<Institucion> institucionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Institucion.class)
            .getResponseBody()
            .filter(institucion::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(institucionList).isNotNull();
        assertThat(institucionList).hasSize(1);
        Institucion testInstitucion = institucionList.get(0);
        assertThat(testInstitucion.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    void getAllInstitucions() {
        // Initialize the database
        institucionRepository.save(institucion).block();

        // Get all the institucionList
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
            .value(hasItem(institucion.getId()))
            .jsonPath("$.[*].valor")
            .value(hasItem(DEFAULT_VALOR));
    }

    @Test
    void getInstitucion() {
        // Initialize the database
        institucionRepository.save(institucion).block();

        // Get the institucion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, institucion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(institucion.getId()))
            .jsonPath("$.valor")
            .value(is(DEFAULT_VALOR));
    }

    @Test
    void getNonExistingInstitucion() {
        // Get the institucion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInstitucion() throws Exception {
        // Initialize the database
        institucionRepository.save(institucion).block();

        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();

        // Update the institucion
        Institucion updatedInstitucion = institucionRepository.findById(institucion.getId()).block();
        updatedInstitucion.valor(UPDATED_VALOR);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedInstitucion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedInstitucion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
        Institucion testInstitucion = institucionList.get(institucionList.size() - 1);
        assertThat(testInstitucion.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void putNonExistingInstitucion() throws Exception {
        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();
        institucion.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, institucion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institucion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInstitucion() throws Exception {
        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();
        institucion.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institucion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInstitucion() throws Exception {
        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();
        institucion.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(institucion))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInstitucionWithPatch() throws Exception {
        // Initialize the database
        institucionRepository.save(institucion).block();

        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();

        // Update the institucion using partial update
        Institucion partialUpdatedInstitucion = new Institucion();
        partialUpdatedInstitucion.setId(institucion.getId());

        partialUpdatedInstitucion.valor(UPDATED_VALOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstitucion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitucion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
        Institucion testInstitucion = institucionList.get(institucionList.size() - 1);
        assertThat(testInstitucion.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void fullUpdateInstitucionWithPatch() throws Exception {
        // Initialize the database
        institucionRepository.save(institucion).block();

        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();

        // Update the institucion using partial update
        Institucion partialUpdatedInstitucion = new Institucion();
        partialUpdatedInstitucion.setId(institucion.getId());

        partialUpdatedInstitucion.valor(UPDATED_VALOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstitucion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitucion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
        Institucion testInstitucion = institucionList.get(institucionList.size() - 1);
        assertThat(testInstitucion.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    void patchNonExistingInstitucion() throws Exception {
        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();
        institucion.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, institucion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institucion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInstitucion() throws Exception {
        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();
        institucion.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institucion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInstitucion() throws Exception {
        int databaseSizeBeforeUpdate = institucionRepository.findAll().collectList().block().size();
        institucion.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(institucion))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Institucion in the database
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInstitucion() {
        // Initialize the database
        institucionRepository.save(institucion).block();

        int databaseSizeBeforeDelete = institucionRepository.findAll().collectList().block().size();

        // Delete the institucion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, institucion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Institucion> institucionList = institucionRepository.findAll().collectList().block();
        assertThat(institucionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

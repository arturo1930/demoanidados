package com.conacyt.mx.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.conacyt.mx.IntegrationTest;
import com.conacyt.mx.domain.Pantalla;
import com.conacyt.mx.repository.PantallaRepository;
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
 * Integration tests for the {@link PantallaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PantallaResourceIT {

    private static final String ENTITY_API_URL = "/api/pantallas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PantallaRepository pantallaRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Pantalla pantalla;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pantalla createEntity() {
        Pantalla pantalla = new Pantalla();
        return pantalla;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pantalla createUpdatedEntity() {
        Pantalla pantalla = new Pantalla();
        return pantalla;
    }

    @BeforeEach
    public void initTest() {
        pantallaRepository.deleteAll().block();
        pantalla = createEntity();
    }

    @Test
    void createPantalla() throws Exception {
        int databaseSizeBeforeCreate = pantallaRepository.findAll().collectList().block().size();
        // Create the Pantalla
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pantalla))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeCreate + 1);
        Pantalla testPantalla = pantallaList.get(pantallaList.size() - 1);
    }

    @Test
    void createPantallaWithExistingId() throws Exception {
        // Create the Pantalla with an existing ID
        pantalla.setId("existing_id");

        int databaseSizeBeforeCreate = pantallaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pantalla))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPantallasAsStream() {
        // Initialize the database
        pantallaRepository.save(pantalla).block();

        List<Pantalla> pantallaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Pantalla.class)
            .getResponseBody()
            .filter(pantalla::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(pantallaList).isNotNull();
        assertThat(pantallaList).hasSize(1);
        Pantalla testPantalla = pantallaList.get(0);
    }

    @Test
    void getAllPantallas() {
        // Initialize the database
        pantallaRepository.save(pantalla).block();

        // Get all the pantallaList
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
            .value(hasItem(pantalla.getId()));
    }

    @Test
    void getPantalla() {
        // Initialize the database
        pantallaRepository.save(pantalla).block();

        // Get the pantalla
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pantalla.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pantalla.getId()));
    }

    @Test
    void getNonExistingPantalla() {
        // Get the pantalla
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPantalla() throws Exception {
        // Initialize the database
        pantallaRepository.save(pantalla).block();

        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();

        // Update the pantalla
        Pantalla updatedPantalla = pantallaRepository.findById(pantalla.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPantalla.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPantalla))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
        Pantalla testPantalla = pantallaList.get(pantallaList.size() - 1);
    }

    @Test
    void putNonExistingPantalla() throws Exception {
        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();
        pantalla.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pantalla.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pantalla))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPantalla() throws Exception {
        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();
        pantalla.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pantalla))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPantalla() throws Exception {
        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();
        pantalla.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pantalla))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePantallaWithPatch() throws Exception {
        // Initialize the database
        pantallaRepository.save(pantalla).block();

        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();

        // Update the pantalla using partial update
        Pantalla partialUpdatedPantalla = new Pantalla();
        partialUpdatedPantalla.setId(pantalla.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPantalla.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPantalla))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
        Pantalla testPantalla = pantallaList.get(pantallaList.size() - 1);
    }

    @Test
    void fullUpdatePantallaWithPatch() throws Exception {
        // Initialize the database
        pantallaRepository.save(pantalla).block();

        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();

        // Update the pantalla using partial update
        Pantalla partialUpdatedPantalla = new Pantalla();
        partialUpdatedPantalla.setId(pantalla.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPantalla.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPantalla))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
        Pantalla testPantalla = pantallaList.get(pantallaList.size() - 1);
    }

    @Test
    void patchNonExistingPantalla() throws Exception {
        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();
        pantalla.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pantalla.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pantalla))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPantalla() throws Exception {
        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();
        pantalla.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pantalla))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPantalla() throws Exception {
        int databaseSizeBeforeUpdate = pantallaRepository.findAll().collectList().block().size();
        pantalla.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pantalla))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pantalla in the database
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePantalla() {
        // Initialize the database
        pantallaRepository.save(pantalla).block();

        int databaseSizeBeforeDelete = pantallaRepository.findAll().collectList().block().size();

        // Delete the pantalla
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pantalla.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Pantalla> pantallaList = pantallaRepository.findAll().collectList().block();
        assertThat(pantallaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

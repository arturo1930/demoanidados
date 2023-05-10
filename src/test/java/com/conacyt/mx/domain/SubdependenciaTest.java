package com.conacyt.mx.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.conacyt.mx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubdependenciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subdependencia.class);
        Subdependencia subdependencia1 = new Subdependencia();
        subdependencia1.setId("id1");
        Subdependencia subdependencia2 = new Subdependencia();
        subdependencia2.setId(subdependencia1.getId());
        assertThat(subdependencia1).isEqualTo(subdependencia2);
        subdependencia2.setId("id2");
        assertThat(subdependencia1).isNotEqualTo(subdependencia2);
        subdependencia1.setId(null);
        assertThat(subdependencia1).isNotEqualTo(subdependencia2);
    }
}

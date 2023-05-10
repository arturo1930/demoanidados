package com.conacyt.mx.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.conacyt.mx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NivelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nivel.class);
        Nivel nivel1 = new Nivel();
        nivel1.setId("id1");
        Nivel nivel2 = new Nivel();
        nivel2.setId(nivel1.getId());
        assertThat(nivel1).isEqualTo(nivel2);
        nivel2.setId("id2");
        assertThat(nivel1).isNotEqualTo(nivel2);
        nivel1.setId(null);
        assertThat(nivel1).isNotEqualTo(nivel2);
    }
}

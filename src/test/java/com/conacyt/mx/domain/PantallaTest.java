package com.conacyt.mx.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.conacyt.mx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PantallaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pantalla.class);
        Pantalla pantalla1 = new Pantalla();
        pantalla1.setId("id1");
        Pantalla pantalla2 = new Pantalla();
        pantalla2.setId(pantalla1.getId());
        assertThat(pantalla1).isEqualTo(pantalla2);
        pantalla2.setId("id2");
        assertThat(pantalla1).isNotEqualTo(pantalla2);
        pantalla1.setId(null);
        assertThat(pantalla1).isNotEqualTo(pantalla2);
    }
}

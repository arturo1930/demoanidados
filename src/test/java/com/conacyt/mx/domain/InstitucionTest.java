package com.conacyt.mx.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.conacyt.mx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstitucionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Institucion.class);
        Institucion institucion1 = new Institucion();
        institucion1.setId("id1");
        Institucion institucion2 = new Institucion();
        institucion2.setId(institucion1.getId());
        assertThat(institucion1).isEqualTo(institucion2);
        institucion2.setId("id2");
        assertThat(institucion1).isNotEqualTo(institucion2);
        institucion1.setId(null);
        assertThat(institucion1).isNotEqualTo(institucion2);
    }
}

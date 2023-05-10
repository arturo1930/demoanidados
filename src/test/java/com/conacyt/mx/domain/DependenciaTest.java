package com.conacyt.mx.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.conacyt.mx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependenciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dependencia.class);
        Dependencia dependencia1 = new Dependencia();
        dependencia1.setId("id1");
        Dependencia dependencia2 = new Dependencia();
        dependencia2.setId(dependencia1.getId());
        assertThat(dependencia1).isEqualTo(dependencia2);
        dependencia2.setId("id2");
        assertThat(dependencia1).isNotEqualTo(dependencia2);
        dependencia1.setId(null);
        assertThat(dependencia1).isNotEqualTo(dependencia2);
    }
}

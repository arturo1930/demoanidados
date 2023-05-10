package com.conacyt.mx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Pantalla.
 */
@Document(collection = "pantalla")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pantalla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("nivel")
    @JsonIgnoreProperties(value = { "institucions", "pantallas" }, allowSetters = true)
    private Nivel nivel;

    @Field("dependencia")
    @JsonIgnoreProperties(value = { "subdependencias", "pantallas", "institucion" }, allowSetters = true)
    private Dependencia dependencia;

    @Field("institucion")
    @JsonIgnoreProperties(value = { "dependencias", "pantallas", "nivel" }, allowSetters = true)
    private Institucion institucion;

    @Field("subdependencia")
    @JsonIgnoreProperties(value = { "pantallas", "dependencia" }, allowSetters = true)
    private Subdependencia subdependencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Pantalla id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Nivel getNivel() {
        return this.nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Pantalla nivel(Nivel nivel) {
        this.setNivel(nivel);
        return this;
    }

    public Dependencia getDependencia() {
        return this.dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Pantalla dependencia(Dependencia dependencia) {
        this.setDependencia(dependencia);
        return this;
    }

    public Institucion getInstitucion() {
        return this.institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Pantalla institucion(Institucion institucion) {
        this.setInstitucion(institucion);
        return this;
    }

    public Subdependencia getSubdependencia() {
        return this.subdependencia;
    }

    public void setSubdependencia(Subdependencia subdependencia) {
        this.subdependencia = subdependencia;
    }

    public Pantalla subdependencia(Subdependencia subdependencia) {
        this.setSubdependencia(subdependencia);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pantalla)) {
            return false;
        }
        return id != null && id.equals(((Pantalla) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pantalla{" +
            "id=" + getId() +
            "}";
    }
}

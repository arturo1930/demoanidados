package com.conacyt.mx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Dependencia.
 */
@Document(collection = "dependencia")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dependencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("valor")
    private String valor;

    @Field("subdependencia")
    @JsonIgnoreProperties(value = { "pantallas", "dependencia" }, allowSetters = true)
    private Set<Subdependencia> subdependencias = new HashSet<>();

    @Field("pantalla")
    @JsonIgnoreProperties(value = { "nivel", "dependencia", "institucion", "subdependencia" }, allowSetters = true)
    private Set<Pantalla> pantallas = new HashSet<>();

    @Field("institucion")
    @JsonIgnoreProperties(value = { "dependencias", "pantallas", "nivel" }, allowSetters = true)
    private Institucion institucion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Dependencia id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return this.valor;
    }

    public Dependencia valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Set<Subdependencia> getSubdependencias() {
        return this.subdependencias;
    }

    public void setSubdependencias(Set<Subdependencia> subdependencias) {
        if (this.subdependencias != null) {
            this.subdependencias.forEach(i -> i.setDependencia(null));
        }
        if (subdependencias != null) {
            subdependencias.forEach(i -> i.setDependencia(this));
        }
        this.subdependencias = subdependencias;
    }

    public Dependencia subdependencias(Set<Subdependencia> subdependencias) {
        this.setSubdependencias(subdependencias);
        return this;
    }

    public Dependencia addSubdependencia(Subdependencia subdependencia) {
        this.subdependencias.add(subdependencia);
        subdependencia.setDependencia(this);
        return this;
    }

    public Dependencia removeSubdependencia(Subdependencia subdependencia) {
        this.subdependencias.remove(subdependencia);
        subdependencia.setDependencia(null);
        return this;
    }

    public Set<Pantalla> getPantallas() {
        return this.pantallas;
    }

    public void setPantallas(Set<Pantalla> pantallas) {
        if (this.pantallas != null) {
            this.pantallas.forEach(i -> i.setDependencia(null));
        }
        if (pantallas != null) {
            pantallas.forEach(i -> i.setDependencia(this));
        }
        this.pantallas = pantallas;
    }

    public Dependencia pantallas(Set<Pantalla> pantallas) {
        this.setPantallas(pantallas);
        return this;
    }

    public Dependencia addPantalla(Pantalla pantalla) {
        this.pantallas.add(pantalla);
        pantalla.setDependencia(this);
        return this;
    }

    public Dependencia removePantalla(Pantalla pantalla) {
        this.pantallas.remove(pantalla);
        pantalla.setDependencia(null);
        return this;
    }

    public Institucion getInstitucion() {
        return this.institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Dependencia institucion(Institucion institucion) {
        this.setInstitucion(institucion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependencia)) {
            return false;
        }
        return id != null && id.equals(((Dependencia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dependencia{" +
            "id=" + getId() +
            ", valor='" + getValor() + "'" +
            "}";
    }
}

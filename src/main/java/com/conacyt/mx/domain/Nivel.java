package com.conacyt.mx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Nivel.
 */
@Document(collection = "nivel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Nivel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("valor")
    private String valor;

    @Field("institucion")
    @JsonIgnoreProperties(value = { "dependencias", "pantallas", "nivel" }, allowSetters = true)
    private Set<Institucion> institucions = new HashSet<>();

    @Field("pantalla")
    @JsonIgnoreProperties(value = { "nivel", "dependencia", "institucion", "subdependencia" }, allowSetters = true)
    private Set<Pantalla> pantallas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Nivel id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return this.valor;
    }

    public Nivel valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Set<Institucion> getInstitucions() {
        return this.institucions;
    }

    public void setInstitucions(Set<Institucion> institucions) {
        if (this.institucions != null) {
            this.institucions.forEach(i -> i.setNivel(null));
        }
        if (institucions != null) {
            institucions.forEach(i -> i.setNivel(this));
        }
        this.institucions = institucions;
    }

    public Nivel institucions(Set<Institucion> institucions) {
        this.setInstitucions(institucions);
        return this;
    }

    public Nivel addInstitucion(Institucion institucion) {
        this.institucions.add(institucion);
        institucion.setNivel(this);
        return this;
    }

    public Nivel removeInstitucion(Institucion institucion) {
        this.institucions.remove(institucion);
        institucion.setNivel(null);
        return this;
    }

    public Set<Pantalla> getPantallas() {
        return this.pantallas;
    }

    public void setPantallas(Set<Pantalla> pantallas) {
        if (this.pantallas != null) {
            this.pantallas.forEach(i -> i.setNivel(null));
        }
        if (pantallas != null) {
            pantallas.forEach(i -> i.setNivel(this));
        }
        this.pantallas = pantallas;
    }

    public Nivel pantallas(Set<Pantalla> pantallas) {
        this.setPantallas(pantallas);
        return this;
    }

    public Nivel addPantalla(Pantalla pantalla) {
        this.pantallas.add(pantalla);
        pantalla.setNivel(this);
        return this;
    }

    public Nivel removePantalla(Pantalla pantalla) {
        this.pantallas.remove(pantalla);
        pantalla.setNivel(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nivel)) {
            return false;
        }
        return id != null && id.equals(((Nivel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nivel{" +
            "id=" + getId() +
            ", valor='" + getValor() + "'" +
            "}";
    }
}

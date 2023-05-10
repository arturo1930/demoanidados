package com.conacyt.mx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Institucion.
 */
@Document(collection = "institucion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Institucion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("valor")
    private String valor;

    @Field("dependencia")
    @JsonIgnoreProperties(value = { "subdependencias", "pantallas", "institucion" }, allowSetters = true)
    private Set<Dependencia> dependencias = new HashSet<>();

    @Field("pantalla")
    @JsonIgnoreProperties(value = { "nivel", "dependencia", "institucion", "subdependencia" }, allowSetters = true)
    private Set<Pantalla> pantallas = new HashSet<>();

    @Field("nivel")
    @JsonIgnoreProperties(value = { "institucions", "pantallas" }, allowSetters = true)
    private Nivel nivel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Institucion id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return this.valor;
    }

    public Institucion valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Set<Dependencia> getDependencias() {
        return this.dependencias;
    }

    public void setDependencias(Set<Dependencia> dependencias) {
        if (this.dependencias != null) {
            this.dependencias.forEach(i -> i.setInstitucion(null));
        }
        if (dependencias != null) {
            dependencias.forEach(i -> i.setInstitucion(this));
        }
        this.dependencias = dependencias;
    }

    public Institucion dependencias(Set<Dependencia> dependencias) {
        this.setDependencias(dependencias);
        return this;
    }

    public Institucion addDependencia(Dependencia dependencia) {
        this.dependencias.add(dependencia);
        dependencia.setInstitucion(this);
        return this;
    }

    public Institucion removeDependencia(Dependencia dependencia) {
        this.dependencias.remove(dependencia);
        dependencia.setInstitucion(null);
        return this;
    }

    public Set<Pantalla> getPantallas() {
        return this.pantallas;
    }

    public void setPantallas(Set<Pantalla> pantallas) {
        if (this.pantallas != null) {
            this.pantallas.forEach(i -> i.setInstitucion(null));
        }
        if (pantallas != null) {
            pantallas.forEach(i -> i.setInstitucion(this));
        }
        this.pantallas = pantallas;
    }

    public Institucion pantallas(Set<Pantalla> pantallas) {
        this.setPantallas(pantallas);
        return this;
    }

    public Institucion addPantalla(Pantalla pantalla) {
        this.pantallas.add(pantalla);
        pantalla.setInstitucion(this);
        return this;
    }

    public Institucion removePantalla(Pantalla pantalla) {
        this.pantallas.remove(pantalla);
        pantalla.setInstitucion(null);
        return this;
    }

    public Nivel getNivel() {
        return this.nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Institucion nivel(Nivel nivel) {
        this.setNivel(nivel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Institucion)) {
            return false;
        }
        return id != null && id.equals(((Institucion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Institucion{" +
            "id=" + getId() +
            ", valor='" + getValor() + "'" +
            "}";
    }
}

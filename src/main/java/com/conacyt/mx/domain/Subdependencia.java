package com.conacyt.mx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Subdependencia.
 */
@Document(collection = "subdependencia")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Subdependencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("valor")
    private String valor;

    @Field("pantalla")
    @JsonIgnoreProperties(value = { "nivel", "dependencia", "institucion", "subdependencia" }, allowSetters = true)
    private Set<Pantalla> pantallas = new HashSet<>();

    @Field("dependencia")
    @JsonIgnoreProperties(value = { "subdependencias", "pantallas", "institucion" }, allowSetters = true)
    private Dependencia dependencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Subdependencia id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return this.valor;
    }

    public Subdependencia valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Set<Pantalla> getPantallas() {
        return this.pantallas;
    }

    public void setPantallas(Set<Pantalla> pantallas) {
        if (this.pantallas != null) {
            this.pantallas.forEach(i -> i.setSubdependencia(null));
        }
        if (pantallas != null) {
            pantallas.forEach(i -> i.setSubdependencia(this));
        }
        this.pantallas = pantallas;
    }

    public Subdependencia pantallas(Set<Pantalla> pantallas) {
        this.setPantallas(pantallas);
        return this;
    }

    public Subdependencia addPantalla(Pantalla pantalla) {
        this.pantallas.add(pantalla);
        pantalla.setSubdependencia(this);
        return this;
    }

    public Subdependencia removePantalla(Pantalla pantalla) {
        this.pantallas.remove(pantalla);
        pantalla.setSubdependencia(null);
        return this;
    }

    public Dependencia getDependencia() {
        return this.dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Subdependencia dependencia(Dependencia dependencia) {
        this.setDependencia(dependencia);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subdependencia)) {
            return false;
        }
        return id != null && id.equals(((Subdependencia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subdependencia{" +
            "id=" + getId() +
            ", valor='" + getValor() + "'" +
            "}";
    }
}

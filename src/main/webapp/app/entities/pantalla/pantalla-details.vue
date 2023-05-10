<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pantalla">
        <h2 class="jh-entity-heading" data-cy="pantallaDetailsHeading"><span>Pantalla</span> {{ pantalla.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nivel</span>
          </dt>
          <dd>
            <div v-if="pantalla.nivel">
              <router-link :to="{ name: 'NivelView', params: { nivelId: pantalla.nivel.id } }">{{ pantalla.nivel.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Dependencia</span>
          </dt>
          <dd>
            <div v-if="pantalla.dependencia">
              <router-link :to="{ name: 'DependenciaView', params: { dependenciaId: pantalla.dependencia.id } }">{{
                pantalla.dependencia.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Institucion</span>
          </dt>
          <dd>
            <div v-if="pantalla.institucion">
              <router-link :to="{ name: 'InstitucionView', params: { institucionId: pantalla.institucion.id } }">{{
                pantalla.institucion.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Subdependencia</span>
          </dt>
          <dd>
            <div v-if="pantalla.subdependencia">
              <router-link :to="{ name: 'SubdependenciaView', params: { subdependenciaId: pantalla.subdependencia.id } }">{{
                pantalla.subdependencia.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="pantalla.id" :to="{ name: 'PantallaEdit', params: { pantallaId: pantalla.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pantalla-details.component.ts"></script>

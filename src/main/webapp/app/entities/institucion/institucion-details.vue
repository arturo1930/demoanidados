<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="institucion">
        <h2 class="jh-entity-heading" data-cy="institucionDetailsHeading"><span>Institucion</span> {{ institucion.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Valor</span>
          </dt>
          <dd>
            <span>{{ institucion.valor }}</span>
          </dd>
          <dt>
            <span>Nivel</span>
          </dt>
          <dd>
            <div v-if="institucion.nivel">
              <router-link :to="{ name: 'NivelView', params: { nivelId: institucion.nivel.id } }">{{ institucion.nivel.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="institucion.id"
          :to="{ name: 'InstitucionEdit', params: { institucionId: institucion.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./institucion-details.component.ts"></script>

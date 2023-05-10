<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="subdependencia">
        <h2 class="jh-entity-heading" data-cy="subdependenciaDetailsHeading"><span>Subdependencia</span> {{ subdependencia.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Valor</span>
          </dt>
          <dd>
            <span>{{ subdependencia.valor }}</span>
          </dd>
          <dt>
            <span>Dependencia</span>
          </dt>
          <dd>
            <div v-if="subdependencia.dependencia">
              <router-link :to="{ name: 'DependenciaView', params: { dependenciaId: subdependencia.dependencia.id } }">{{
                subdependencia.dependencia.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="subdependencia.id"
          :to="{ name: 'SubdependenciaEdit', params: { subdependenciaId: subdependencia.id } }"
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

<script lang="ts" src="./subdependencia-details.component.ts"></script>

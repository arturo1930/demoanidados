<template>
  <div>
    <h2 id="page-heading" data-cy="DependenciaHeading">
      <span id="dependencia-heading">Dependencias</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DependenciaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-dependencia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Dependencia </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dependencias && dependencias.length === 0">
      <span>No dependencias found</span>
    </div>
    <div class="table-responsive" v-if="dependencias && dependencias.length > 0">
      <table class="table table-striped" aria-describedby="dependencias">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Valor</span></th>
            <th scope="row"><span>Institucion</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dependencia in dependencias" :key="dependencia.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DependenciaView', params: { dependenciaId: dependencia.id } }">{{ dependencia.id }}</router-link>
            </td>
            <td>{{ dependencia.valor }}</td>
            <td>
              <div v-if="dependencia.institucion">
                <router-link :to="{ name: 'InstitucionView', params: { institucionId: dependencia.institucion.id } }">{{
                  dependencia.institucion.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DependenciaView', params: { dependenciaId: dependencia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DependenciaEdit', params: { dependenciaId: dependencia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(dependencia)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="demoanidadosApp.dependencia.delete.question" data-cy="dependenciaDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-dependencia-heading">Are you sure you want to delete this Dependencia?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-dependencia"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDependencia()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./dependencia.component.ts"></script>

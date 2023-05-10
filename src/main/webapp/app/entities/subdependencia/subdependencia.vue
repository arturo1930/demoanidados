<template>
  <div>
    <h2 id="page-heading" data-cy="SubdependenciaHeading">
      <span id="subdependencia-heading">Subdependencias</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'SubdependenciaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-subdependencia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Subdependencia </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && subdependencias && subdependencias.length === 0">
      <span>No subdependencias found</span>
    </div>
    <div class="table-responsive" v-if="subdependencias && subdependencias.length > 0">
      <table class="table table-striped" aria-describedby="subdependencias">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Valor</span></th>
            <th scope="row"><span>Dependencia</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="subdependencia in subdependencias" :key="subdependencia.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SubdependenciaView', params: { subdependenciaId: subdependencia.id } }">{{
                subdependencia.id
              }}</router-link>
            </td>
            <td>{{ subdependencia.valor }}</td>
            <td>
              <div v-if="subdependencia.dependencia">
                <router-link :to="{ name: 'DependenciaView', params: { dependenciaId: subdependencia.dependencia.id } }">{{
                  subdependencia.dependencia.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SubdependenciaView', params: { subdependenciaId: subdependencia.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SubdependenciaEdit', params: { subdependenciaId: subdependencia.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(subdependencia)"
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
        ><span id="demoanidadosApp.subdependencia.delete.question" data-cy="subdependenciaDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-subdependencia-heading">Are you sure you want to delete this Subdependencia?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-subdependencia"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeSubdependencia()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./subdependencia.component.ts"></script>

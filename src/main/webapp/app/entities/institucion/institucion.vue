<template>
  <div>
    <h2 id="page-heading" data-cy="InstitucionHeading">
      <span id="institucion-heading">Institucions</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'InstitucionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-institucion"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Institucion </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && institucions && institucions.length === 0">
      <span>No institucions found</span>
    </div>
    <div class="table-responsive" v-if="institucions && institucions.length > 0">
      <table class="table table-striped" aria-describedby="institucions">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Valor</span></th>
            <th scope="row"><span>Nivel</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="institucion in institucions" :key="institucion.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'InstitucionView', params: { institucionId: institucion.id } }">{{ institucion.id }}</router-link>
            </td>
            <td>{{ institucion.valor }}</td>
            <td>
              <div v-if="institucion.nivel">
                <router-link :to="{ name: 'NivelView', params: { nivelId: institucion.nivel.id } }">{{ institucion.nivel.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'InstitucionView', params: { institucionId: institucion.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'InstitucionEdit', params: { institucionId: institucion.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(institucion)"
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
        ><span id="demoanidadosApp.institucion.delete.question" data-cy="institucionDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-institucion-heading">Are you sure you want to delete this Institucion?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-institucion"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeInstitucion()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./institucion.component.ts"></script>

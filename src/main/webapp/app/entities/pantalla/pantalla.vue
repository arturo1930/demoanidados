<template>
  <div>
    <h2 id="page-heading" data-cy="PantallaHeading">
      <span id="pantalla-heading">Pantallas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'PantallaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-pantalla"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Pantalla </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pantallas && pantallas.length === 0">
      <span>No pantallas found</span>
    </div>
    <div class="table-responsive" v-if="pantallas && pantallas.length > 0">
      <table class="table table-striped" aria-describedby="pantallas">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Nivel</span></th>
            <th scope="row"><span>Dependencia</span></th>
            <th scope="row"><span>Institucion</span></th>
            <th scope="row"><span>Subdependencia</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pantalla in pantallas" :key="pantalla.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PantallaView', params: { pantallaId: pantalla.id } }">{{ pantalla.id }}</router-link>
            </td>
            <td>
              <div v-if="pantalla.nivel">
                <router-link :to="{ name: 'NivelView', params: { nivelId: pantalla.nivel.id } }">{{ pantalla.nivel.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="pantalla.dependencia">
                <router-link :to="{ name: 'DependenciaView', params: { dependenciaId: pantalla.dependencia.id } }">{{
                  pantalla.dependencia.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="pantalla.institucion">
                <router-link :to="{ name: 'InstitucionView', params: { institucionId: pantalla.institucion.id } }">{{
                  pantalla.institucion.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="pantalla.subdependencia">
                <router-link :to="{ name: 'SubdependenciaView', params: { subdependenciaId: pantalla.subdependencia.id } }">{{
                  pantalla.subdependencia.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PantallaView', params: { pantallaId: pantalla.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PantallaEdit', params: { pantallaId: pantalla.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(pantalla)"
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
        ><span id="demoanidadosApp.pantalla.delete.question" data-cy="pantallaDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-pantalla-heading">Are you sure you want to delete this Pantalla?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-pantalla"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removePantalla()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./pantalla.component.ts"></script>

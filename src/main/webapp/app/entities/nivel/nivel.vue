<template>
  <div>
    <h2 id="page-heading" data-cy="NivelHeading">
      <span id="nivel-heading">Nivels</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'NivelCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-nivel"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Nivel </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && nivels && nivels.length === 0">
      <span>No nivels found</span>
    </div>
    <div class="table-responsive" v-if="nivels && nivels.length > 0">
      <table class="table table-striped" aria-describedby="nivels">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Valor</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="nivel in nivels" :key="nivel.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'NivelView', params: { nivelId: nivel.id } }">{{ nivel.id }}</router-link>
            </td>
            <td>{{ nivel.valor }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'NivelView', params: { nivelId: nivel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'NivelEdit', params: { nivelId: nivel.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(nivel)"
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
        ><span id="demoanidadosApp.nivel.delete.question" data-cy="nivelDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-nivel-heading">Are you sure you want to delete this Nivel?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-nivel"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeNivel()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./nivel.component.ts"></script>

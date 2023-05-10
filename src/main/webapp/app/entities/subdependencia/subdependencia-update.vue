<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="demoanidadosApp.subdependencia.home.createOrEditLabel" data-cy="SubdependenciaCreateUpdateHeading">
          Create or edit a Subdependencia
        </h2>
        <div>
          <div class="form-group" v-if="subdependencia.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="subdependencia.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="subdependencia-valor">Valor</label>
            <input
              type="text"
              class="form-control"
              name="valor"
              id="subdependencia-valor"
              data-cy="valor"
              :class="{ valid: !$v.subdependencia.valor.$invalid, invalid: $v.subdependencia.valor.$invalid }"
              v-model="$v.subdependencia.valor.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="subdependencia-dependencia">Dependencia</label>
            <select
              class="form-control"
              id="subdependencia-dependencia"
              data-cy="dependencia"
              name="dependencia"
              v-model="subdependencia.dependencia"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  subdependencia.dependencia && dependenciaOption.id === subdependencia.dependencia.id
                    ? subdependencia.dependencia
                    : dependenciaOption
                "
                v-for="dependenciaOption in dependencias"
                :key="dependenciaOption.id"
              >
                {{ dependenciaOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.subdependencia.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./subdependencia-update.component.ts"></script>

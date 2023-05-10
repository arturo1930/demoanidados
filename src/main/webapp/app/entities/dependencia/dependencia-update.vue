<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="demoanidadosApp.dependencia.home.createOrEditLabel" data-cy="DependenciaCreateUpdateHeading">
          Create or edit a Dependencia
        </h2>
        <div>
          <div class="form-group" v-if="dependencia.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dependencia.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="dependencia-valor">Valor</label>
            <input
              type="text"
              class="form-control"
              name="valor"
              id="dependencia-valor"
              data-cy="valor"
              :class="{ valid: !$v.dependencia.valor.$invalid, invalid: $v.dependencia.valor.$invalid }"
              v-model="$v.dependencia.valor.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="dependencia-institucion">Institucion</label>
            <select
              class="form-control"
              id="dependencia-institucion"
              data-cy="institucion"
              name="institucion"
              v-model="dependencia.institucion"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  dependencia.institucion && institucionOption.id === dependencia.institucion.id
                    ? dependencia.institucion
                    : institucionOption
                "
                v-for="institucionOption in institucions"
                :key="institucionOption.id"
              >
                {{ institucionOption.id }}
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
            :disabled="$v.dependencia.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./dependencia-update.component.ts"></script>

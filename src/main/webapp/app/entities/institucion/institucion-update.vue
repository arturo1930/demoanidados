<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="demoanidadosApp.institucion.home.createOrEditLabel" data-cy="InstitucionCreateUpdateHeading">
          Create or edit a Institucion
        </h2>
        <div>
          <div class="form-group" v-if="institucion.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="institucion.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="institucion-valor">Valor</label>
            <input
              type="text"
              class="form-control"
              name="valor"
              id="institucion-valor"
              data-cy="valor"
              :class="{ valid: !$v.institucion.valor.$invalid, invalid: $v.institucion.valor.$invalid }"
              v-model="$v.institucion.valor.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="institucion-nivel">Nivel</label>
            <select class="form-control" id="institucion-nivel" data-cy="nivel" name="nivel" v-model="institucion.nivel">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="institucion.nivel && nivelOption.id === institucion.nivel.id ? institucion.nivel : nivelOption"
                v-for="nivelOption in nivels"
                :key="nivelOption.id"
              >
                {{ nivelOption.id }}
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
            :disabled="$v.institucion.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./institucion-update.component.ts"></script>

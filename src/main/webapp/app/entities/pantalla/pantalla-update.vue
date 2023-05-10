<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="demoanidadosApp.pantalla.home.createOrEditLabel" data-cy="PantallaCreateUpdateHeading">Create or edit a Pantalla</h2>
        <div>
          <div class="form-group" v-if="pantalla.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="pantalla.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pantalla-nivel">Nivel</label>
            <select
              class="form-control"
              id="pantalla-nivel"
              data-cy="nivel"
              name="nivel"
              v-model="pantalla.nivel"
              v-on:change="llamarInstitucion()"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="pantalla.nivel && nivelOption.id === pantalla.nivel.id ? pantalla.nivel : nivelOption"
                v-for="nivelOption in nivels"
                :key="nivelOption.id"
              >
                {{ nivelOption.valor }}
              </option>
            </select>
            <button type="button" class="btn btn-secondary" v-on:click="llamarInstitucion()">Llamar servicio</button>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pantalla-institucion">Institucion</label>
            <select class="form-control" id="pantalla-institucion" data-cy="institucion" name="institucion" v-model="pantalla.institucion">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  pantalla.institucion && institucionOption.id === pantalla.institucion.id ? pantalla.institucion : institucionOption
                "
                v-for="institucionOption in institucions"
                :key="institucionOption.id"
              >
                {{ institucionOption.valor }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pantalla-dependencia">Dependencia</label>
            <select class="form-control" id="pantalla-dependencia" data-cy="dependencia" name="dependencia" v-model="pantalla.dependencia">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  pantalla.dependencia && dependenciaOption.id === pantalla.dependencia.id ? pantalla.dependencia : dependenciaOption
                "
                v-for="dependenciaOption in dependencias"
                :key="dependenciaOption.id"
              >
                {{ dependenciaOption.valor }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pantalla-subdependencia">Subdependencia</label>
            <select
              class="form-control"
              id="pantalla-subdependencia"
              data-cy="subdependencia"
              name="subdependencia"
              v-model="pantalla.subdependencia"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  pantalla.subdependencia && subdependenciaOption.id === pantalla.subdependencia.id
                    ? pantalla.subdependencia
                    : subdependenciaOption
                "
                v-for="subdependenciaOption in subdependencias"
                :key="subdependenciaOption.id"
              >
                {{ subdependenciaOption.valor }}
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
            :disabled="$v.pantalla.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./pantalla-update.component.ts"></script>

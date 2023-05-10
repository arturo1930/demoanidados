<template>
  <div>
    <b-dropdown-item to="/pantalla">
      <font-awesome-icon icon="asterisk" />
      <span>Pantalla</span>
    </b-dropdown-item>
    <b-dropdown-item to="/nivel">
      <font-awesome-icon icon="asterisk" />
      <span>Nivel</span>
    </b-dropdown-item>
    <b-dropdown-item to="/institucion">
      <font-awesome-icon icon="asterisk" />
      <span>Institucion</span>
    </b-dropdown-item>
    <b-dropdown-item to="/dependencia">
      <font-awesome-icon icon="asterisk" />
      <span>Dependencia</span>
    </b-dropdown-item>
    <b-dropdown-item to="/subdependencia">
      <font-awesome-icon icon="asterisk" />
      <span>Subdependencia</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>

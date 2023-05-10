import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import PantallaService from './pantalla/pantalla.service';
import NivelService from './nivel/nivel.service';
import InstitucionService from './institucion/institucion.service';
import DependenciaService from './dependencia/dependencia.service';
import SubdependenciaService from './subdependencia/subdependencia.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('pantallaService') private pantallaService = () => new PantallaService();
  @Provide('nivelService') private nivelService = () => new NivelService();
  @Provide('institucionService') private institucionService = () => new InstitucionService();
  @Provide('dependenciaService') private dependenciaService = () => new DependenciaService();
  @Provide('subdependenciaService') private subdependenciaService = () => new SubdependenciaService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}

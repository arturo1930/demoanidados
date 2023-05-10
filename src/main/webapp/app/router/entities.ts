import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Pantalla = () => import('@/entities/pantalla/pantalla.vue');
// prettier-ignore
const PantallaUpdate = () => import('@/entities/pantalla/pantalla-update.vue');
// prettier-ignore
const PantallaDetails = () => import('@/entities/pantalla/pantalla-details.vue');
// prettier-ignore
const Nivel = () => import('@/entities/nivel/nivel.vue');
// prettier-ignore
const NivelUpdate = () => import('@/entities/nivel/nivel-update.vue');
// prettier-ignore
const NivelDetails = () => import('@/entities/nivel/nivel-details.vue');
// prettier-ignore
const Institucion = () => import('@/entities/institucion/institucion.vue');
// prettier-ignore
const InstitucionUpdate = () => import('@/entities/institucion/institucion-update.vue');
// prettier-ignore
const InstitucionDetails = () => import('@/entities/institucion/institucion-details.vue');
// prettier-ignore
const Dependencia = () => import('@/entities/dependencia/dependencia.vue');
// prettier-ignore
const DependenciaUpdate = () => import('@/entities/dependencia/dependencia-update.vue');
// prettier-ignore
const DependenciaDetails = () => import('@/entities/dependencia/dependencia-details.vue');
// prettier-ignore
const Subdependencia = () => import('@/entities/subdependencia/subdependencia.vue');
// prettier-ignore
const SubdependenciaUpdate = () => import('@/entities/subdependencia/subdependencia-update.vue');
// prettier-ignore
const SubdependenciaDetails = () => import('@/entities/subdependencia/subdependencia-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'pantalla',
      name: 'Pantalla',
      component: Pantalla,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pantalla/new',
      name: 'PantallaCreate',
      component: PantallaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pantalla/:pantallaId/edit',
      name: 'PantallaEdit',
      component: PantallaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pantalla/:pantallaId/view',
      name: 'PantallaView',
      component: PantallaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'nivel',
      name: 'Nivel',
      component: Nivel,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'nivel/new',
      name: 'NivelCreate',
      component: NivelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'nivel/:nivelId/edit',
      name: 'NivelEdit',
      component: NivelUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'nivel/:nivelId/view',
      name: 'NivelView',
      component: NivelDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'institucion',
      name: 'Institucion',
      component: Institucion,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'institucion/new',
      name: 'InstitucionCreate',
      component: InstitucionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'institucion/:institucionId/edit',
      name: 'InstitucionEdit',
      component: InstitucionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'institucion/:institucionId/view',
      name: 'InstitucionView',
      component: InstitucionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'dependencia',
      name: 'Dependencia',
      component: Dependencia,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'dependencia/new',
      name: 'DependenciaCreate',
      component: DependenciaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'dependencia/:dependenciaId/edit',
      name: 'DependenciaEdit',
      component: DependenciaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'dependencia/:dependenciaId/view',
      name: 'DependenciaView',
      component: DependenciaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subdependencia',
      name: 'Subdependencia',
      component: Subdependencia,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subdependencia/new',
      name: 'SubdependenciaCreate',
      component: SubdependenciaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subdependencia/:subdependenciaId/edit',
      name: 'SubdependenciaEdit',
      component: SubdependenciaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subdependencia/:subdependenciaId/view',
      name: 'SubdependenciaView',
      component: SubdependenciaDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

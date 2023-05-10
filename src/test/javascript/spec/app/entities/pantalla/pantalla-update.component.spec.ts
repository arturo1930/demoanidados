/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PantallaUpdateComponent from '@/entities/pantalla/pantalla-update.vue';
import PantallaClass from '@/entities/pantalla/pantalla-update.component';
import PantallaService from '@/entities/pantalla/pantalla.service';

import NivelService from '@/entities/nivel/nivel.service';

import DependenciaService from '@/entities/dependencia/dependencia.service';

import InstitucionService from '@/entities/institucion/institucion.service';

import SubdependenciaService from '@/entities/subdependencia/subdependencia.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Pantalla Management Update Component', () => {
    let wrapper: Wrapper<PantallaClass>;
    let comp: PantallaClass;
    let pantallaServiceStub: SinonStubbedInstance<PantallaService>;

    beforeEach(() => {
      pantallaServiceStub = sinon.createStubInstance<PantallaService>(PantallaService);

      wrapper = shallowMount<PantallaClass>(PantallaUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          pantallaService: () => pantallaServiceStub,
          alertService: () => new AlertService(),

          nivelService: () =>
            sinon.createStubInstance<NivelService>(NivelService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          dependenciaService: () =>
            sinon.createStubInstance<DependenciaService>(DependenciaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          institucionService: () =>
            sinon.createStubInstance<InstitucionService>(InstitucionService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          subdependenciaService: () =>
            sinon.createStubInstance<SubdependenciaService>(SubdependenciaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 'ABC' };
        comp.pantalla = entity;
        pantallaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pantallaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.pantalla = entity;
        pantallaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pantallaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPantalla = { id: 'ABC' };
        pantallaServiceStub.find.resolves(foundPantalla);
        pantallaServiceStub.retrieve.resolves([foundPantalla]);

        // WHEN
        comp.beforeRouteEnter({ params: { pantallaId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.pantalla).toBe(foundPantalla);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});

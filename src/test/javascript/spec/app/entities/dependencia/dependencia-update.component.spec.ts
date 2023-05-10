/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DependenciaUpdateComponent from '@/entities/dependencia/dependencia-update.vue';
import DependenciaClass from '@/entities/dependencia/dependencia-update.component';
import DependenciaService from '@/entities/dependencia/dependencia.service';

import SubdependenciaService from '@/entities/subdependencia/subdependencia.service';

import PantallaService from '@/entities/pantalla/pantalla.service';

import InstitucionService from '@/entities/institucion/institucion.service';
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
  describe('Dependencia Management Update Component', () => {
    let wrapper: Wrapper<DependenciaClass>;
    let comp: DependenciaClass;
    let dependenciaServiceStub: SinonStubbedInstance<DependenciaService>;

    beforeEach(() => {
      dependenciaServiceStub = sinon.createStubInstance<DependenciaService>(DependenciaService);

      wrapper = shallowMount<DependenciaClass>(DependenciaUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          dependenciaService: () => dependenciaServiceStub,
          alertService: () => new AlertService(),

          subdependenciaService: () =>
            sinon.createStubInstance<SubdependenciaService>(SubdependenciaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          pantallaService: () =>
            sinon.createStubInstance<PantallaService>(PantallaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          institucionService: () =>
            sinon.createStubInstance<InstitucionService>(InstitucionService, {
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
        comp.dependencia = entity;
        dependenciaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dependenciaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.dependencia = entity;
        dependenciaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dependenciaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDependencia = { id: 'ABC' };
        dependenciaServiceStub.find.resolves(foundDependencia);
        dependenciaServiceStub.retrieve.resolves([foundDependencia]);

        // WHEN
        comp.beforeRouteEnter({ params: { dependenciaId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.dependencia).toBe(foundDependencia);
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

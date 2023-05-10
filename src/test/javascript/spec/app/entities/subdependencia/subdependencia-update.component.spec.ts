/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import SubdependenciaUpdateComponent from '@/entities/subdependencia/subdependencia-update.vue';
import SubdependenciaClass from '@/entities/subdependencia/subdependencia-update.component';
import SubdependenciaService from '@/entities/subdependencia/subdependencia.service';

import PantallaService from '@/entities/pantalla/pantalla.service';

import DependenciaService from '@/entities/dependencia/dependencia.service';
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
  describe('Subdependencia Management Update Component', () => {
    let wrapper: Wrapper<SubdependenciaClass>;
    let comp: SubdependenciaClass;
    let subdependenciaServiceStub: SinonStubbedInstance<SubdependenciaService>;

    beforeEach(() => {
      subdependenciaServiceStub = sinon.createStubInstance<SubdependenciaService>(SubdependenciaService);

      wrapper = shallowMount<SubdependenciaClass>(SubdependenciaUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          subdependenciaService: () => subdependenciaServiceStub,
          alertService: () => new AlertService(),

          pantallaService: () =>
            sinon.createStubInstance<PantallaService>(PantallaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          dependenciaService: () =>
            sinon.createStubInstance<DependenciaService>(DependenciaService, {
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
        comp.subdependencia = entity;
        subdependenciaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subdependenciaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.subdependencia = entity;
        subdependenciaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subdependenciaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSubdependencia = { id: 'ABC' };
        subdependenciaServiceStub.find.resolves(foundSubdependencia);
        subdependenciaServiceStub.retrieve.resolves([foundSubdependencia]);

        // WHEN
        comp.beforeRouteEnter({ params: { subdependenciaId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.subdependencia).toBe(foundSubdependencia);
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

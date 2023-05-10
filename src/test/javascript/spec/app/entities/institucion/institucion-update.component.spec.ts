/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import InstitucionUpdateComponent from '@/entities/institucion/institucion-update.vue';
import InstitucionClass from '@/entities/institucion/institucion-update.component';
import InstitucionService from '@/entities/institucion/institucion.service';

import DependenciaService from '@/entities/dependencia/dependencia.service';

import PantallaService from '@/entities/pantalla/pantalla.service';

import NivelService from '@/entities/nivel/nivel.service';
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
  describe('Institucion Management Update Component', () => {
    let wrapper: Wrapper<InstitucionClass>;
    let comp: InstitucionClass;
    let institucionServiceStub: SinonStubbedInstance<InstitucionService>;

    beforeEach(() => {
      institucionServiceStub = sinon.createStubInstance<InstitucionService>(InstitucionService);

      wrapper = shallowMount<InstitucionClass>(InstitucionUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          institucionService: () => institucionServiceStub,
          alertService: () => new AlertService(),

          dependenciaService: () =>
            sinon.createStubInstance<DependenciaService>(DependenciaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          pantallaService: () =>
            sinon.createStubInstance<PantallaService>(PantallaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          nivelService: () =>
            sinon.createStubInstance<NivelService>(NivelService, {
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
        comp.institucion = entity;
        institucionServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(institucionServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.institucion = entity;
        institucionServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(institucionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundInstitucion = { id: 'ABC' };
        institucionServiceStub.find.resolves(foundInstitucion);
        institucionServiceStub.retrieve.resolves([foundInstitucion]);

        // WHEN
        comp.beforeRouteEnter({ params: { institucionId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.institucion).toBe(foundInstitucion);
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

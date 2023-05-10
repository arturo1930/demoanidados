/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import NivelUpdateComponent from '@/entities/nivel/nivel-update.vue';
import NivelClass from '@/entities/nivel/nivel-update.component';
import NivelService from '@/entities/nivel/nivel.service';

import InstitucionService from '@/entities/institucion/institucion.service';

import PantallaService from '@/entities/pantalla/pantalla.service';
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
  describe('Nivel Management Update Component', () => {
    let wrapper: Wrapper<NivelClass>;
    let comp: NivelClass;
    let nivelServiceStub: SinonStubbedInstance<NivelService>;

    beforeEach(() => {
      nivelServiceStub = sinon.createStubInstance<NivelService>(NivelService);

      wrapper = shallowMount<NivelClass>(NivelUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          nivelService: () => nivelServiceStub,
          alertService: () => new AlertService(),

          institucionService: () =>
            sinon.createStubInstance<InstitucionService>(InstitucionService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          pantallaService: () =>
            sinon.createStubInstance<PantallaService>(PantallaService, {
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
        comp.nivel = entity;
        nivelServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(nivelServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.nivel = entity;
        nivelServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(nivelServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundNivel = { id: 'ABC' };
        nivelServiceStub.find.resolves(foundNivel);
        nivelServiceStub.retrieve.resolves([foundNivel]);

        // WHEN
        comp.beforeRouteEnter({ params: { nivelId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.nivel).toBe(foundNivel);
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

/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import NivelDetailComponent from '@/entities/nivel/nivel-details.vue';
import NivelClass from '@/entities/nivel/nivel-details.component';
import NivelService from '@/entities/nivel/nivel.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Nivel Management Detail Component', () => {
    let wrapper: Wrapper<NivelClass>;
    let comp: NivelClass;
    let nivelServiceStub: SinonStubbedInstance<NivelService>;

    beforeEach(() => {
      nivelServiceStub = sinon.createStubInstance<NivelService>(NivelService);

      wrapper = shallowMount<NivelClass>(NivelDetailComponent, {
        store,
        localVue,
        router,
        provide: { nivelService: () => nivelServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundNivel = { id: 'ABC' };
        nivelServiceStub.find.resolves(foundNivel);

        // WHEN
        comp.retrieveNivel('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.nivel).toBe(foundNivel);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundNivel = { id: 'ABC' };
        nivelServiceStub.find.resolves(foundNivel);

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

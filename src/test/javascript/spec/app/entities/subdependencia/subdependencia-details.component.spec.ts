/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import SubdependenciaDetailComponent from '@/entities/subdependencia/subdependencia-details.vue';
import SubdependenciaClass from '@/entities/subdependencia/subdependencia-details.component';
import SubdependenciaService from '@/entities/subdependencia/subdependencia.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Subdependencia Management Detail Component', () => {
    let wrapper: Wrapper<SubdependenciaClass>;
    let comp: SubdependenciaClass;
    let subdependenciaServiceStub: SinonStubbedInstance<SubdependenciaService>;

    beforeEach(() => {
      subdependenciaServiceStub = sinon.createStubInstance<SubdependenciaService>(SubdependenciaService);

      wrapper = shallowMount<SubdependenciaClass>(SubdependenciaDetailComponent, {
        store,
        localVue,
        router,
        provide: { subdependenciaService: () => subdependenciaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSubdependencia = { id: 'ABC' };
        subdependenciaServiceStub.find.resolves(foundSubdependencia);

        // WHEN
        comp.retrieveSubdependencia('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.subdependencia).toBe(foundSubdependencia);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSubdependencia = { id: 'ABC' };
        subdependenciaServiceStub.find.resolves(foundSubdependencia);

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

/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DependenciaDetailComponent from '@/entities/dependencia/dependencia-details.vue';
import DependenciaClass from '@/entities/dependencia/dependencia-details.component';
import DependenciaService from '@/entities/dependencia/dependencia.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Dependencia Management Detail Component', () => {
    let wrapper: Wrapper<DependenciaClass>;
    let comp: DependenciaClass;
    let dependenciaServiceStub: SinonStubbedInstance<DependenciaService>;

    beforeEach(() => {
      dependenciaServiceStub = sinon.createStubInstance<DependenciaService>(DependenciaService);

      wrapper = shallowMount<DependenciaClass>(DependenciaDetailComponent, {
        store,
        localVue,
        router,
        provide: { dependenciaService: () => dependenciaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDependencia = { id: 'ABC' };
        dependenciaServiceStub.find.resolves(foundDependencia);

        // WHEN
        comp.retrieveDependencia('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.dependencia).toBe(foundDependencia);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDependencia = { id: 'ABC' };
        dependenciaServiceStub.find.resolves(foundDependencia);

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

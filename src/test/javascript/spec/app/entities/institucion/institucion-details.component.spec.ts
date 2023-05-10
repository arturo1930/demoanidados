/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import InstitucionDetailComponent from '@/entities/institucion/institucion-details.vue';
import InstitucionClass from '@/entities/institucion/institucion-details.component';
import InstitucionService from '@/entities/institucion/institucion.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Institucion Management Detail Component', () => {
    let wrapper: Wrapper<InstitucionClass>;
    let comp: InstitucionClass;
    let institucionServiceStub: SinonStubbedInstance<InstitucionService>;

    beforeEach(() => {
      institucionServiceStub = sinon.createStubInstance<InstitucionService>(InstitucionService);

      wrapper = shallowMount<InstitucionClass>(InstitucionDetailComponent, {
        store,
        localVue,
        router,
        provide: { institucionService: () => institucionServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundInstitucion = { id: 'ABC' };
        institucionServiceStub.find.resolves(foundInstitucion);

        // WHEN
        comp.retrieveInstitucion('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.institucion).toBe(foundInstitucion);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundInstitucion = { id: 'ABC' };
        institucionServiceStub.find.resolves(foundInstitucion);

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

/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PantallaDetailComponent from '@/entities/pantalla/pantalla-details.vue';
import PantallaClass from '@/entities/pantalla/pantalla-details.component';
import PantallaService from '@/entities/pantalla/pantalla.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Pantalla Management Detail Component', () => {
    let wrapper: Wrapper<PantallaClass>;
    let comp: PantallaClass;
    let pantallaServiceStub: SinonStubbedInstance<PantallaService>;

    beforeEach(() => {
      pantallaServiceStub = sinon.createStubInstance<PantallaService>(PantallaService);

      wrapper = shallowMount<PantallaClass>(PantallaDetailComponent, {
        store,
        localVue,
        router,
        provide: { pantallaService: () => pantallaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPantalla = { id: 'ABC' };
        pantallaServiceStub.find.resolves(foundPantalla);

        // WHEN
        comp.retrievePantalla('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.pantalla).toBe(foundPantalla);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPantalla = { id: 'ABC' };
        pantallaServiceStub.find.resolves(foundPantalla);

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

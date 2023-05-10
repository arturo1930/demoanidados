/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PantallaComponent from '@/entities/pantalla/pantalla.vue';
import PantallaClass from '@/entities/pantalla/pantalla.component';
import PantallaService from '@/entities/pantalla/pantalla.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Pantalla Management Component', () => {
    let wrapper: Wrapper<PantallaClass>;
    let comp: PantallaClass;
    let pantallaServiceStub: SinonStubbedInstance<PantallaService>;

    beforeEach(() => {
      pantallaServiceStub = sinon.createStubInstance<PantallaService>(PantallaService);
      pantallaServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<PantallaClass>(PantallaComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          pantallaService: () => pantallaServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      pantallaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllPantallas();
      await comp.$nextTick();

      // THEN
      expect(pantallaServiceStub.retrieve.called).toBeTruthy();
      expect(comp.pantallas[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      pantallaServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      expect(pantallaServiceStub.retrieve.callCount).toEqual(1);

      comp.removePantalla();
      await comp.$nextTick();

      // THEN
      expect(pantallaServiceStub.delete.called).toBeTruthy();
      expect(pantallaServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});

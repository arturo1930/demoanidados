/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import InstitucionComponent from '@/entities/institucion/institucion.vue';
import InstitucionClass from '@/entities/institucion/institucion.component';
import InstitucionService from '@/entities/institucion/institucion.service';
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
  describe('Institucion Management Component', () => {
    let wrapper: Wrapper<InstitucionClass>;
    let comp: InstitucionClass;
    let institucionServiceStub: SinonStubbedInstance<InstitucionService>;

    beforeEach(() => {
      institucionServiceStub = sinon.createStubInstance<InstitucionService>(InstitucionService);
      institucionServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<InstitucionClass>(InstitucionComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          institucionService: () => institucionServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      institucionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllInstitucions();
      await comp.$nextTick();

      // THEN
      expect(institucionServiceStub.retrieve.called).toBeTruthy();
      expect(comp.institucions[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      institucionServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      expect(institucionServiceStub.retrieve.callCount).toEqual(1);

      comp.removeInstitucion();
      await comp.$nextTick();

      // THEN
      expect(institucionServiceStub.delete.called).toBeTruthy();
      expect(institucionServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});

/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import SubdependenciaComponent from '@/entities/subdependencia/subdependencia.vue';
import SubdependenciaClass from '@/entities/subdependencia/subdependencia.component';
import SubdependenciaService from '@/entities/subdependencia/subdependencia.service';
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
  describe('Subdependencia Management Component', () => {
    let wrapper: Wrapper<SubdependenciaClass>;
    let comp: SubdependenciaClass;
    let subdependenciaServiceStub: SinonStubbedInstance<SubdependenciaService>;

    beforeEach(() => {
      subdependenciaServiceStub = sinon.createStubInstance<SubdependenciaService>(SubdependenciaService);
      subdependenciaServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<SubdependenciaClass>(SubdependenciaComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          subdependenciaService: () => subdependenciaServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      subdependenciaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllSubdependencias();
      await comp.$nextTick();

      // THEN
      expect(subdependenciaServiceStub.retrieve.called).toBeTruthy();
      expect(comp.subdependencias[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      subdependenciaServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      expect(subdependenciaServiceStub.retrieve.callCount).toEqual(1);

      comp.removeSubdependencia();
      await comp.$nextTick();

      // THEN
      expect(subdependenciaServiceStub.delete.called).toBeTruthy();
      expect(subdependenciaServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});

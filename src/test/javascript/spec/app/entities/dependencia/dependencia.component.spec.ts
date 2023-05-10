/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DependenciaComponent from '@/entities/dependencia/dependencia.vue';
import DependenciaClass from '@/entities/dependencia/dependencia.component';
import DependenciaService from '@/entities/dependencia/dependencia.service';
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
  describe('Dependencia Management Component', () => {
    let wrapper: Wrapper<DependenciaClass>;
    let comp: DependenciaClass;
    let dependenciaServiceStub: SinonStubbedInstance<DependenciaService>;

    beforeEach(() => {
      dependenciaServiceStub = sinon.createStubInstance<DependenciaService>(DependenciaService);
      dependenciaServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DependenciaClass>(DependenciaComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          dependenciaService: () => dependenciaServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      dependenciaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllDependencias();
      await comp.$nextTick();

      // THEN
      expect(dependenciaServiceStub.retrieve.called).toBeTruthy();
      expect(comp.dependencias[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      dependenciaServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      expect(dependenciaServiceStub.retrieve.callCount).toEqual(1);

      comp.removeDependencia();
      await comp.$nextTick();

      // THEN
      expect(dependenciaServiceStub.delete.called).toBeTruthy();
      expect(dependenciaServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});

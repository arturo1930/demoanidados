/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import NivelComponent from '@/entities/nivel/nivel.vue';
import NivelClass from '@/entities/nivel/nivel.component';
import NivelService from '@/entities/nivel/nivel.service';
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
  describe('Nivel Management Component', () => {
    let wrapper: Wrapper<NivelClass>;
    let comp: NivelClass;
    let nivelServiceStub: SinonStubbedInstance<NivelService>;

    beforeEach(() => {
      nivelServiceStub = sinon.createStubInstance<NivelService>(NivelService);
      nivelServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<NivelClass>(NivelComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          nivelService: () => nivelServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      nivelServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllNivels();
      await comp.$nextTick();

      // THEN
      expect(nivelServiceStub.retrieve.called).toBeTruthy();
      expect(comp.nivels[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      nivelServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      expect(nivelServiceStub.retrieve.callCount).toEqual(1);

      comp.removeNivel();
      await comp.$nextTick();

      // THEN
      expect(nivelServiceStub.delete.called).toBeTruthy();
      expect(nivelServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});

import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import InstitucionService from '@/entities/institucion/institucion.service';
import { IInstitucion } from '@/shared/model/institucion.model';

import PantallaService from '@/entities/pantalla/pantalla.service';
import { IPantalla } from '@/shared/model/pantalla.model';

import { INivel, Nivel } from '@/shared/model/nivel.model';
import NivelService from './nivel.service';

const validations: any = {
  nivel: {
    valor: {},
  },
};

@Component({
  validations,
})
export default class NivelUpdate extends Vue {
  @Inject('nivelService') private nivelService: () => NivelService;
  @Inject('alertService') private alertService: () => AlertService;

  public nivel: INivel = new Nivel();

  @Inject('institucionService') private institucionService: () => InstitucionService;

  public institucions: IInstitucion[] = [];

  @Inject('pantallaService') private pantallaService: () => PantallaService;

  public pantallas: IPantalla[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.nivelId) {
        vm.retrieveNivel(to.params.nivelId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.nivel.id) {
      this.nivelService()
        .update(this.nivel)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Nivel is updated with identifier ' + param.id;
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.nivelService()
        .create(this.nivel)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Nivel is created with identifier ' + param.id;
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveNivel(nivelId): void {
    this.nivelService()
      .find(nivelId)
      .then(res => {
        this.nivel = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.institucionService()
      .retrieve()
      .then(res => {
        this.institucions = res.data;
      });
    this.pantallaService()
      .retrieve()
      .then(res => {
        this.pantallas = res.data;
      });
  }
}

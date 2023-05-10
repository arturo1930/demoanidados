import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import DependenciaService from '@/entities/dependencia/dependencia.service';
import { IDependencia } from '@/shared/model/dependencia.model';

import PantallaService from '@/entities/pantalla/pantalla.service';
import { IPantalla } from '@/shared/model/pantalla.model';

import NivelService from '@/entities/nivel/nivel.service';
import { INivel } from '@/shared/model/nivel.model';

import { IInstitucion, Institucion } from '@/shared/model/institucion.model';
import InstitucionService from './institucion.service';

const validations: any = {
  institucion: {
    valor: {},
  },
};

@Component({
  validations,
})
export default class InstitucionUpdate extends Vue {
  @Inject('institucionService') private institucionService: () => InstitucionService;
  @Inject('alertService') private alertService: () => AlertService;

  public institucion: IInstitucion = new Institucion();

  @Inject('dependenciaService') private dependenciaService: () => DependenciaService;

  public dependencias: IDependencia[] = [];

  @Inject('pantallaService') private pantallaService: () => PantallaService;

  public pantallas: IPantalla[] = [];

  @Inject('nivelService') private nivelService: () => NivelService;

  public nivels: INivel[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.institucionId) {
        vm.retrieveInstitucion(to.params.institucionId);
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
    if (this.institucion.id) {
      this.institucionService()
        .update(this.institucion)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Institucion is updated with identifier ' + param.id;
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
      this.institucionService()
        .create(this.institucion)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Institucion is created with identifier ' + param.id;
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

  public retrieveInstitucion(institucionId): void {
    this.institucionService()
      .find(institucionId)
      .then(res => {
        this.institucion = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.dependenciaService()
      .retrieve()
      .then(res => {
        this.dependencias = res.data;
      });
    this.pantallaService()
      .retrieve()
      .then(res => {
        this.pantallas = res.data;
      });
    this.nivelService()
      .retrieve()
      .then(res => {
        this.nivels = res.data;
      });
  }
}

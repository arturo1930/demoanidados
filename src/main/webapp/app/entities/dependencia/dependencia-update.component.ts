import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import SubdependenciaService from '@/entities/subdependencia/subdependencia.service';
import { ISubdependencia } from '@/shared/model/subdependencia.model';

import PantallaService from '@/entities/pantalla/pantalla.service';
import { IPantalla } from '@/shared/model/pantalla.model';

import InstitucionService from '@/entities/institucion/institucion.service';
import { IInstitucion } from '@/shared/model/institucion.model';

import { IDependencia, Dependencia } from '@/shared/model/dependencia.model';
import DependenciaService from './dependencia.service';

const validations: any = {
  dependencia: {
    valor: {},
  },
};

@Component({
  validations,
})
export default class DependenciaUpdate extends Vue {
  @Inject('dependenciaService') private dependenciaService: () => DependenciaService;
  @Inject('alertService') private alertService: () => AlertService;

  public dependencia: IDependencia = new Dependencia();

  @Inject('subdependenciaService') private subdependenciaService: () => SubdependenciaService;

  public subdependencias: ISubdependencia[] = [];

  @Inject('pantallaService') private pantallaService: () => PantallaService;

  public pantallas: IPantalla[] = [];

  @Inject('institucionService') private institucionService: () => InstitucionService;

  public institucions: IInstitucion[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dependenciaId) {
        vm.retrieveDependencia(to.params.dependenciaId);
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
    if (this.dependencia.id) {
      this.dependenciaService()
        .update(this.dependencia)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Dependencia is updated with identifier ' + param.id;
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
      this.dependenciaService()
        .create(this.dependencia)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Dependencia is created with identifier ' + param.id;
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

  public retrieveDependencia(dependenciaId): void {
    this.dependenciaService()
      .find(dependenciaId)
      .then(res => {
        this.dependencia = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.subdependenciaService()
      .retrieve()
      .then(res => {
        this.subdependencias = res.data;
      });
    this.pantallaService()
      .retrieve()
      .then(res => {
        this.pantallas = res.data;
      });
    this.institucionService()
      .retrieve()
      .then(res => {
        this.institucions = res.data;
      });
  }
}

import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import PantallaService from '@/entities/pantalla/pantalla.service';
import { IPantalla } from '@/shared/model/pantalla.model';

import DependenciaService from '@/entities/dependencia/dependencia.service';
import { IDependencia } from '@/shared/model/dependencia.model';

import { ISubdependencia, Subdependencia } from '@/shared/model/subdependencia.model';
import SubdependenciaService from './subdependencia.service';

const validations: any = {
  subdependencia: {
    valor: {},
  },
};

@Component({
  validations,
})
export default class SubdependenciaUpdate extends Vue {
  @Inject('subdependenciaService') private subdependenciaService: () => SubdependenciaService;
  @Inject('alertService') private alertService: () => AlertService;

  public subdependencia: ISubdependencia = new Subdependencia();

  @Inject('pantallaService') private pantallaService: () => PantallaService;

  public pantallas: IPantalla[] = [];

  @Inject('dependenciaService') private dependenciaService: () => DependenciaService;

  public dependencias: IDependencia[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.subdependenciaId) {
        vm.retrieveSubdependencia(to.params.subdependenciaId);
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
    if (this.subdependencia.id) {
      this.subdependenciaService()
        .update(this.subdependencia)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Subdependencia is updated with identifier ' + param.id;
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
      this.subdependenciaService()
        .create(this.subdependencia)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Subdependencia is created with identifier ' + param.id;
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

  public retrieveSubdependencia(subdependenciaId): void {
    this.subdependenciaService()
      .find(subdependenciaId)
      .then(res => {
        this.subdependencia = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.pantallaService()
      .retrieve()
      .then(res => {
        this.pantallas = res.data;
      });
    this.dependenciaService()
      .retrieve()
      .then(res => {
        this.dependencias = res.data;
      });
  }
}

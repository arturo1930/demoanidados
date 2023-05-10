import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import NivelService from '@/entities/nivel/nivel.service';
import { INivel } from '@/shared/model/nivel.model';

import DependenciaService from '@/entities/dependencia/dependencia.service';
import { IDependencia } from '@/shared/model/dependencia.model';

import InstitucionService from '@/entities/institucion/institucion.service';
import { IInstitucion } from '@/shared/model/institucion.model';

import SubdependenciaService from '@/entities/subdependencia/subdependencia.service';
import { ISubdependencia } from '@/shared/model/subdependencia.model';

import { IPantalla, Pantalla } from '@/shared/model/pantalla.model';
import PantallaService from './pantalla.service';

const validations: any = {
  pantalla: {},
};

@Component({
  validations,
})
export default class PantallaUpdate extends Vue {
  @Inject('pantallaService') private pantallaService: () => PantallaService;
  @Inject('alertService') private alertService: () => AlertService;

  public pantalla: IPantalla = new Pantalla();

  @Inject('nivelService') private nivelService: () => NivelService;

  public nivels: INivel[] = [];

  @Inject('dependenciaService') private dependenciaService: () => DependenciaService;

  public dependencias: IDependencia[] = [];

  @Inject('institucionService') private institucionService: () => InstitucionService;

  public institucions: IInstitucion[] = [];

  @Inject('subdependenciaService') private subdependenciaService: () => SubdependenciaService;

  public subdependencias: ISubdependencia[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.pantallaId) {
        vm.retrievePantalla(to.params.pantallaId);
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
    if (this.pantalla.id) {
      this.pantallaService()
        .update(this.pantalla)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Pantalla is updated with identifier ' + param.id;
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
      this.pantallaService()
        .create(this.pantalla)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Pantalla is created with identifier ' + param.id;
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

  public retrievePantalla(pantallaId): void {
    this.pantallaService()
      .find(pantallaId)
      .then(res => {
        this.pantalla = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.nivelService()
      .retrieve()
      .then(res => {
        this.nivels = res.data;
      });
    this.dependenciaService()
      .retrieve()
      .then(res => {
        this.dependencias = res.data;
      });
    this.institucionService()
      .retrieve()
      .then(res => {
        this.institucions = res.data;
      });
    this.subdependenciaService()
      .retrieve()
      .then(res => {
        this.subdependencias = res.data;
      });
  }
  public retriveByNivel(id): void {
    this.institucionService()
      .retriveByNivel(id)
      .then(res => {
        console.log(res);
        this.institucions = res;
      });
  }
  public llamarInstitucion(): void {
    console.log('llamar institución');
    console.log(this.pantalla.nivel.id);
    this.retriveByNivel(this.pantalla.nivel.id);
  }
}

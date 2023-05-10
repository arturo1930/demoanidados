import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDependencia } from '@/shared/model/dependencia.model';

import DependenciaService from './dependencia.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Dependencia extends Vue {
  @Inject('dependenciaService') private dependenciaService: () => DependenciaService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: string = null;

  public dependencias: IDependencia[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllDependencias();
  }

  public clear(): void {
    this.retrieveAllDependencias();
  }

  public retrieveAllDependencias(): void {
    this.isFetching = true;
    this.dependenciaService()
      .retrieve()
      .then(
        res => {
          this.dependencias = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IDependencia): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeDependencia(): void {
    this.dependenciaService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Dependencia is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllDependencias();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}

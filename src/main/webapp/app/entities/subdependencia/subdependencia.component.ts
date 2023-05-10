import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ISubdependencia } from '@/shared/model/subdependencia.model';

import SubdependenciaService from './subdependencia.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Subdependencia extends Vue {
  @Inject('subdependenciaService') private subdependenciaService: () => SubdependenciaService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: string = null;

  public subdependencias: ISubdependencia[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllSubdependencias();
  }

  public clear(): void {
    this.retrieveAllSubdependencias();
  }

  public retrieveAllSubdependencias(): void {
    this.isFetching = true;
    this.subdependenciaService()
      .retrieve()
      .then(
        res => {
          this.subdependencias = res.data;
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

  public prepareRemove(instance: ISubdependencia): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeSubdependencia(): void {
    this.subdependenciaService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Subdependencia is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllSubdependencias();
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

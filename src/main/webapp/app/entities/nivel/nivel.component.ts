import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { INivel } from '@/shared/model/nivel.model';

import NivelService from './nivel.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Nivel extends Vue {
  @Inject('nivelService') private nivelService: () => NivelService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: string = null;

  public nivels: INivel[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllNivels();
  }

  public clear(): void {
    this.retrieveAllNivels();
  }

  public retrieveAllNivels(): void {
    this.isFetching = true;
    this.nivelService()
      .retrieve()
      .then(
        res => {
          this.nivels = res.data;
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

  public prepareRemove(instance: INivel): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeNivel(): void {
    this.nivelService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Nivel is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllNivels();
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

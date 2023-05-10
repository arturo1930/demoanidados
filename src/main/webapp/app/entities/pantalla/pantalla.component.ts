import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IPantalla } from '@/shared/model/pantalla.model';

import PantallaService from './pantalla.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Pantalla extends Vue {
  @Inject('pantallaService') private pantallaService: () => PantallaService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: string = null;

  public pantallas: IPantalla[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllPantallas();
  }

  public clear(): void {
    this.retrieveAllPantallas();
  }

  public retrieveAllPantallas(): void {
    this.isFetching = true;
    this.pantallaService()
      .retrieve()
      .then(
        res => {
          this.pantallas = res.data;
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

  public prepareRemove(instance: IPantalla): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePantalla(): void {
    this.pantallaService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Pantalla is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllPantallas();
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

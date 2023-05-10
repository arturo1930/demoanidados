import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IInstitucion } from '@/shared/model/institucion.model';

import InstitucionService from './institucion.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Institucion extends Vue {
  @Inject('institucionService') private institucionService: () => InstitucionService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: string = null;

  public institucions: IInstitucion[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllInstitucions();
  }

  public clear(): void {
    this.retrieveAllInstitucions();
  }

  public retrieveAllInstitucions(): void {
    this.isFetching = true;
    this.institucionService()
      .retrieve()
      .then(
        res => {
          this.institucions = res.data;
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

  public prepareRemove(instance: IInstitucion): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeInstitucion(): void {
    this.institucionService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Institucion is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllInstitucions();
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

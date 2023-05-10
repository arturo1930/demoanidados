import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPantalla } from '@/shared/model/pantalla.model';
import PantallaService from './pantalla.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PantallaDetails extends Vue {
  @Inject('pantallaService') private pantallaService: () => PantallaService;
  @Inject('alertService') private alertService: () => AlertService;

  public pantalla: IPantalla = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.pantallaId) {
        vm.retrievePantalla(to.params.pantallaId);
      }
    });
  }

  public retrievePantalla(pantallaId) {
    this.pantallaService()
      .find(pantallaId)
      .then(res => {
        this.pantalla = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

import { Component, Vue, Inject } from 'vue-property-decorator';

import { INivel } from '@/shared/model/nivel.model';
import NivelService from './nivel.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class NivelDetails extends Vue {
  @Inject('nivelService') private nivelService: () => NivelService;
  @Inject('alertService') private alertService: () => AlertService;

  public nivel: INivel = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.nivelId) {
        vm.retrieveNivel(to.params.nivelId);
      }
    });
  }

  public retrieveNivel(nivelId) {
    this.nivelService()
      .find(nivelId)
      .then(res => {
        this.nivel = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

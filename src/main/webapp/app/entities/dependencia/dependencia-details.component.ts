import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDependencia } from '@/shared/model/dependencia.model';
import DependenciaService from './dependencia.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DependenciaDetails extends Vue {
  @Inject('dependenciaService') private dependenciaService: () => DependenciaService;
  @Inject('alertService') private alertService: () => AlertService;

  public dependencia: IDependencia = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dependenciaId) {
        vm.retrieveDependencia(to.params.dependenciaId);
      }
    });
  }

  public retrieveDependencia(dependenciaId) {
    this.dependenciaService()
      .find(dependenciaId)
      .then(res => {
        this.dependencia = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

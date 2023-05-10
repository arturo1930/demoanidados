import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISubdependencia } from '@/shared/model/subdependencia.model';
import SubdependenciaService from './subdependencia.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class SubdependenciaDetails extends Vue {
  @Inject('subdependenciaService') private subdependenciaService: () => SubdependenciaService;
  @Inject('alertService') private alertService: () => AlertService;

  public subdependencia: ISubdependencia = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.subdependenciaId) {
        vm.retrieveSubdependencia(to.params.subdependenciaId);
      }
    });
  }

  public retrieveSubdependencia(subdependenciaId) {
    this.subdependenciaService()
      .find(subdependenciaId)
      .then(res => {
        this.subdependencia = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

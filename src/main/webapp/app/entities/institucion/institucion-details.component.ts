import { Component, Vue, Inject } from 'vue-property-decorator';

import { IInstitucion } from '@/shared/model/institucion.model';
import InstitucionService from './institucion.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class InstitucionDetails extends Vue {
  @Inject('institucionService') private institucionService: () => InstitucionService;
  @Inject('alertService') private alertService: () => AlertService;

  public institucion: IInstitucion = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.institucionId) {
        vm.retrieveInstitucion(to.params.institucionId);
      }
    });
  }

  public retrieveInstitucion(institucionId) {
    this.institucionService()
      .find(institucionId)
      .then(res => {
        this.institucion = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

import { INivel } from '@/shared/model/nivel.model';
import { IDependencia } from '@/shared/model/dependencia.model';
import { IInstitucion } from '@/shared/model/institucion.model';
import { ISubdependencia } from '@/shared/model/subdependencia.model';

export interface IPantalla {
  id?: string;
  nivel?: INivel | null;
  dependencia?: IDependencia | null;
  institucion?: IInstitucion | null;
  subdependencia?: ISubdependencia | null;
}

export class Pantalla implements IPantalla {
  constructor(
    public id?: string,
    public nivel?: INivel | null,
    public dependencia?: IDependencia | null,
    public institucion?: IInstitucion | null,
    public subdependencia?: ISubdependencia | null
  ) {}
}

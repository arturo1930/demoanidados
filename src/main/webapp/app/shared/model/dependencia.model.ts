import { ISubdependencia } from '@/shared/model/subdependencia.model';
import { IPantalla } from '@/shared/model/pantalla.model';
import { IInstitucion } from '@/shared/model/institucion.model';

export interface IDependencia {
  id?: string;
  valor?: string | null;
  subdependencias?: ISubdependencia[] | null;
  pantallas?: IPantalla[] | null;
  institucion?: IInstitucion | null;
}

export class Dependencia implements IDependencia {
  constructor(
    public id?: string,
    public valor?: string | null,
    public subdependencias?: ISubdependencia[] | null,
    public pantallas?: IPantalla[] | null,
    public institucion?: IInstitucion | null
  ) {}
}

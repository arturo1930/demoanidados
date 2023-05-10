import { IDependencia } from '@/shared/model/dependencia.model';
import { IPantalla } from '@/shared/model/pantalla.model';
import { INivel } from '@/shared/model/nivel.model';

export interface IInstitucion {
  id?: string;
  valor?: string | null;
  dependencias?: IDependencia[] | null;
  pantallas?: IPantalla[] | null;
  nivel?: INivel | null;
}

export class Institucion implements IInstitucion {
  constructor(
    public id?: string,
    public valor?: string | null,
    public dependencias?: IDependencia[] | null,
    public pantallas?: IPantalla[] | null,
    public nivel?: INivel | null
  ) {}
}

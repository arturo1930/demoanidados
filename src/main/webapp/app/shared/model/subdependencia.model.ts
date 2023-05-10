import { IPantalla } from '@/shared/model/pantalla.model';
import { IDependencia } from '@/shared/model/dependencia.model';

export interface ISubdependencia {
  id?: string;
  valor?: string | null;
  pantallas?: IPantalla[] | null;
  dependencia?: IDependencia | null;
}

export class Subdependencia implements ISubdependencia {
  constructor(
    public id?: string,
    public valor?: string | null,
    public pantallas?: IPantalla[] | null,
    public dependencia?: IDependencia | null
  ) {}
}

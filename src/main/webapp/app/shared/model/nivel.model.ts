import { IInstitucion } from '@/shared/model/institucion.model';
import { IPantalla } from '@/shared/model/pantalla.model';

export interface INivel {
  id?: string;
  valor?: string | null;
  institucions?: IInstitucion[] | null;
  pantallas?: IPantalla[] | null;
}

export class Nivel implements INivel {
  constructor(
    public id?: string,
    public valor?: string | null,
    public institucions?: IInstitucion[] | null,
    public pantallas?: IPantalla[] | null
  ) {}
}

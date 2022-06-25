import { RecenzijaPayload } from "./recenzija.payload";

export interface EksponatiResponsePayload{
    id: number;
    naziv: string;
    opis: string;
    photoUrl: string;
    cenaObilaska: number;
    vremeObilaska: Date;
    zemljaPorekla: string;
    recenzije?: RecenzijaPayload[];
}

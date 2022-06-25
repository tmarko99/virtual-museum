import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Obilazak } from '../model/obilazak';

@Injectable({
  providedIn: 'root'
})
export class ObilazakService {

  constructor(private httpClient: HttpClient) { }


  createObilazak(idEksponata: number): any{
    return this.httpClient.post(`http://localhost:8080/api/eksponat/${idEksponata}/obilasci`, null);
  }

  mojiObilasci(){
    return this.httpClient.get<Obilazak[]>('http://localhost:8080/api/eksponat/me/obilasci')
  }

  zavrsiObilazak(id: number){
    return this.httpClient.put(`http://localhost:8080/api/obilazak/${id}/zavrsen`, id);
  }

  otkaziObilazak(id: number){
    return this.httpClient.put(`http://localhost:8080/api/obilazak/${id}/otkazan`, id);
  }

  oceniObilazak(ocena: number, idObilaska: number){
    return this.httpClient.patch(`http://localhost:8080/api/obilazak/${idObilaska}/oceni`, ocena);
  }

}

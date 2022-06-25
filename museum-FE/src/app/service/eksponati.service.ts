import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { EksponatRequestPayload } from '../model/eksponat-request.payload';
import { EksponatiResponsePayload } from '../model/eksponati-response.payload';
import { RecenzijaPayload } from '../model/recenzija.payload';

@Injectable({
  providedIn: 'root'
})
export class EksponatiService {

  constructor(private httpClient: HttpClient) { }


  findAll(params): Observable<any>{
    /* let params = new HttpParams();

    params.append('pageNumber', String(page));
    params.append('pageSize', String(size)); */

    return this.httpClient.get('http://localhost:8080/api/eksponati', {params});
  }


  /* findAll(){
    return this.httpClient.get<EksponatiResponsePayload[]>('http://localhost:8080/api/eksponati');
  } */

  createEksponat(eksponatRequestPayload: EksponatRequestPayload){
    return this.httpClient.post<EksponatiResponsePayload>('http://localhost:8080/api/eksponati', eksponatRequestPayload);
  }

  deleteEksponat(eksponatId: number){
    return this.httpClient.delete(`http://localhost:8080/api/eksponati/${eksponatId}`);
  }

  findById(id: number): Observable<EksponatiResponsePayload>{
    return this.httpClient.get<EksponatiResponsePayload>('http://localhost:8080/api/eksponati/' + id);
  }

  createRecenzija(recenzijaPayload: RecenzijaPayload, idEksponata: number){
    return this.httpClient.post<RecenzijaPayload>(`http://localhost:8080/api/eksponat/${idEksponata}/recenzije`, recenzijaPayload);
  }

  deleteRecenzija(idEksponata: number, idRecenzije: number){
    return this.httpClient.delete(`http://localhost:8080/api/eksponat/${idEksponata}/recenzije/${idRecenzije}`);
  }
}



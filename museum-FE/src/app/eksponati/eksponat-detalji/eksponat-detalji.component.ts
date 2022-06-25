import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { EksponatiResponsePayload } from 'src/app/model/eksponati-response.payload';
import { RecenzijaPayload } from 'src/app/model/recenzija.payload';
import { AuthService } from 'src/app/service/auth.service';
import { EksponatiService } from 'src/app/service/eksponati.service';

@Component({
  selector: 'app-eksponat-detalji',
  templateUrl: './eksponat-detalji.component.html',
  styleUrls: ['./eksponat-detalji.component.css']
})
export class EksponatDetaljiComponent implements OnInit {

  id: number;
  eksponat: EksponatiResponsePayload;
  recenzijaPayload: RecenzijaPayload;
  recenzijaForm: FormGroup;
  isLoggedIn: boolean;
  subscription: Subscription;
  prosecnaOcena: number = 0;


  constructor(private eksponatService: EksponatiService, private route: ActivatedRoute,
    private authService: AuthService, private toastr: ToastrService) {
    this.recenzijaPayload = {
      komentar: '',
      ocena: null,
      userIme: ''
    }

   }

  ngOnInit(): void {
    this.recenzijaForm = new FormGroup({
      komentar: new FormControl('', Validators.required),
      ocena: new FormControl('', [Validators.required, Validators.min(1), Validators.max(10)])
    });

    this.id = this.route.snapshot.params['id'];

    this.subscription = this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
    this.isLoggedIn = this.authService.isLoggedIn();

    this.sveRecenzije();
  }

  izracunaProsecnuOcenu(recenzije){

    let ocena = recenzije.reduce((ukupnaOcena, ocena) =>
       ukupnaOcena + ocena.ocena
    , 0);

    return (ocena / 2) / recenzije.length;
  }

  sveRecenzije(){
    this.eksponatService.findById(this.id).subscribe(data => {
      this.eksponat = data;
      this.prosecnaOcena = this.izracunaProsecnuOcenu(data.recenzije);
    });
  }

  postRecenzija(){
    this.recenzijaPayload.komentar = this.recenzijaForm.get('komentar').value;
    this.recenzijaPayload.ocena = this.recenzijaForm.get('ocena').value;

    this.eksponatService.createRecenzija(this.recenzijaPayload, this.id).subscribe(data => {
      this.recenzijaForm.get('komentar').setValue('');
      this.recenzijaForm.get('ocena').setValue('');
      this.sveRecenzije();
    });

    this.izracunaProsecnuOcenu(this.eksponat.recenzije);
  }

  obrisiRecenziju(idRecenzije: number){
    this.eksponatService.deleteRecenzija(this.id, idRecenzije).subscribe(() => {
      this.toastr.success('Recenzija uspesno obrisana');
      this.sveRecenzije();
    })
  }

  onRecenzijeUpdated(){
    this.sveRecenzije();
  }


}

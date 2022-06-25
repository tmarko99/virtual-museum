import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EksponatRequestPayload } from '../model/eksponat-request.payload';
import { EksponatiService } from '../service/eksponati.service';

@Component({
  selector: 'app-new-eksponat',
  templateUrl: './new-eksponat.component.html',
  styleUrls: ['./new-eksponat.component.css']
})
export class NewEksponatComponent implements OnInit {

  eksponatRequestPayload: EksponatRequestPayload;
  newEksponatForm: FormGroup;

  constructor(private eksponatService: EksponatiService, private router: Router) {
    this.eksponatRequestPayload = {
      naziv: '',
      opis: '',
      photoUrl: '',
      cenaObilaska: 0,
      vremeObilaska: null,
      zemljaPorekla: ''
    };

   }

  ngOnInit(): void {
    this.newEksponatForm = new FormGroup({
      naziv: new FormControl('', [Validators.required, Validators.minLength(5)]),
      opis: new FormControl('', [Validators.required, Validators.minLength(10)]),
      photoUrl: new FormControl(''),
      cenaObilaska: new FormControl('', [Validators.nullValidator, Validators.min(1)]),
      vremeObilaska: new FormControl(''),
      zemljaPorekla: new FormControl('')
    });
  }


  addEksponat(){
    let datum = this.newEksponatForm.get('vremeObilaska').value;
    datum = datum.replaceAll('T', ' ');

    this.eksponatRequestPayload.naziv = this.newEksponatForm.get('naziv').value;
    this.eksponatRequestPayload.opis = this.newEksponatForm.get('opis').value;
    this.eksponatRequestPayload.photoUrl = this.newEksponatForm.get('photoUrl').value;
    this.eksponatRequestPayload.cenaObilaska = this.newEksponatForm.get('cenaObilaska').value;
    this.eksponatRequestPayload.vremeObilaska = datum;
    this.eksponatRequestPayload.zemljaPorekla = this.newEksponatForm.get('zemljaPorekla').value;

    this.eksponatService.createEksponat(this.eksponatRequestPayload).subscribe(() => {
      this.router.navigate(['/eksponati'], {queryParams: {created: 'true'}});
    });


  }

}

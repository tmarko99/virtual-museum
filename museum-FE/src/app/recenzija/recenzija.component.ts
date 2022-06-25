import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RecenzijaPayload } from '../model/recenzija.payload';
import { EksponatiService } from '../service/eksponati.service';

@Component({
  selector: 'app-recenzija',
  templateUrl: './recenzija.component.html',
  styleUrls: ['./recenzija.component.css']
})
export class RecenzijaComponent implements OnInit {

  @Input() id: number;
  recenzijaPayload: RecenzijaPayload;
  recenzijaForm: FormGroup;
  @Output() recenzijeUpdated = new EventEmitter<boolean>();

  constructor(private eksponatService: EksponatiService, private route: ActivatedRoute) {
    this.recenzijaPayload = {
      komentar: '',
      ocena: null,
      userIme: ''
    }

   }

  ngOnInit(): void {
    this.recenzijaForm = new FormGroup({
      komentar: new FormControl('', Validators.required),
      ocena: new FormControl('', Validators.required),
    });

  }


  postRecenzija(){
    this.recenzijaPayload.komentar = this.recenzijaForm.get('komentar').value;
    this.recenzijaPayload.ocena = this.recenzijaForm.get('ocena').value;

    this.eksponatService.createRecenzija(this.recenzijaPayload, this.id).subscribe(() => {
      this.recenzijeUpdated.emit(true);
      this.recenzijaForm.get('komentar').setValue('');
      this.recenzijaForm.get('ocena').setValue('');
    });
  }

}

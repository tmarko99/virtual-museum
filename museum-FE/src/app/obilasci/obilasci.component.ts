import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Obilazak } from '../model/obilazak';
import { Status } from '../model/status';
import { ObilazakService } from '../service/obilazak.service';

@Component({
  selector: 'app-obilasci',
  templateUrl: './obilasci.component.html',
  styleUrls: ['./obilasci.component.css']
})
export class ObilasciComponent implements OnInit {

  oceni: FormGroup;
  ocena = 0;
  obilasci: Obilazak[] = [];


  constructor(private obilasciService: ObilazakService, private toastr: ToastrService) { }


  ngOnInit(): void {
    this.oceni = new FormGroup({
      ocena: new FormControl('', [Validators.required, Validators.min(1), Validators.max(10)])
    });

    this.mojiObilasci();
  }

  mojiObilasci(){
    return this.obilasciService.mojiObilasci().subscribe(data => {
      this.obilasci = data;
    });
  }

  zavrsiObilazak(id: number){
    this.obilasciService.zavrsiObilazak(id).subscribe(data => {
      this.mojiObilasci();
    });
  }


  otkaziObilazak(id: number){
    this.obilasciService.otkaziObilazak(id).subscribe(data => {
      this.mojiObilasci();
    });
  }


  oceniObilazak(id:number){
    this.ocena = this.oceni.get('ocena').value;

    this.obilasciService.oceniObilazak(this.ocena, id).subscribe(data => {
      this.toastr.success("Uspesno dodata ocena!");
      this.oceni.get('ocena').setValue('');
      this.mojiObilasci();
    });
  }



}

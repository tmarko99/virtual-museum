import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { SignupRequestPayload } from '../model/signup-request.payload';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: any = {};

  constructor(private authService: AuthService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.getUserProfile();
  }

  getUserProfile(){
    this.authService.getUserProfile().subscribe(data => {
      this.user = data;
      console.log(this.user);
    })
  }

  updateProfile(form: NgForm){
    var model: SignupRequestPayload = {
      "ime": form.value.ime,
      "prezime": form.value.prezime,
      "telefon": form.value.telefon,
      "adresa": form.value.adresa,
      "email": form.value.email,
      "password": form.value.password,
    }

    this.authService.updateProfile(model).subscribe(() => {
      this.toastr.success("Uspesno azurirano")
    });

  }
}

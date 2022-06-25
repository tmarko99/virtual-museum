import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SignupRequestPayload } from 'src/app/model/signup-request.payload';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  signupRequestPayload: SignupRequestPayload;
  signupForm: FormGroup;

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) {
    this.signupRequestPayload = {
      ime: '',
      prezime: '',
      telefon: '',
      adresa: '',
      email: '',
      password: ''
    }
   }

  ngOnInit(): void {
    this.signupForm = new FormGroup({
      ime: new FormControl('', [Validators.required, Validators.minLength(2)]),
      prezime: new FormControl('', Validators.required),
      telefon: new FormControl('', Validators.required),
      adresa: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    });
  }


  signup(){
      this.signupRequestPayload.ime  = this.signupForm.get('ime').value;
      this.signupRequestPayload.prezime  = this.signupForm.get('prezime').value;
      this.signupRequestPayload.telefon  = this.signupForm.get('telefon').value;
      this.signupRequestPayload.adresa  = this.signupForm.get('adresa').value;
      this.signupRequestPayload.email  = this.signupForm.get('email').value;
      this.signupRequestPayload.password  = this.signupForm.get('password').value;


      this.authService.signup(this.signupRequestPayload).subscribe(() => {
        this.router.navigate(['/login'], {queryParams: {registered: 'true'}});
      });
  }

  hasErrors(controlName: string, error: string){
    const control = this.signupForm.get(controlName);

    return control && control.hasError(error) && (control.dirty || control.touched);
  }

}

import { HttpClient } from '@angular/common/http';
import { Injectable, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { LocalStorageService } from 'ngx-webstorage';
import { LoginRequestPayload } from '../model/login-request.payload';
import { LoginResponse } from '../model/login-response.payload';
import { map, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { SignupRequestPayload } from '../model/signup-request.payload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  @Output() adminLogged: EventEmitter<boolean> = new EventEmitter();

  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService) {}


  signup(signupRequestPayload: SignupRequestPayload): Observable<any>{
    return this.httpClient.post('http://localhost:8080/api/auth/signup', signupRequestPayload, {responseType: 'text'});
  }


  login(loginRequestPayload: LoginRequestPayload): Observable<boolean>{
    return this.httpClient.post<LoginResponse>('http://localhost:8080/api/auth/login', loginRequestPayload)
                .pipe(map(user => {
                      if(user.accessToken){
                        console.log(user);

                        this.localStorage.store('accessToken', user.accessToken);
                        this.localStorage.store('userRole', user.authorities);

                        if(this.isAdmin()){
                          this.adminLogged.emit(true);
                        }

                        this.loggedIn.emit(true);

                        return true;
                      }
                }));
  }

  updateProfile(signupRequestPayload: SignupRequestPayload){
    return this.httpClient.put('http://localhost:8080/api/users/myProfile', signupRequestPayload);
  }

  getUserProfile(){
    return this.httpClient.get<SignupRequestPayload>('http://localhost:8080/api/users/myProfile');
  }

  isLoggedIn(): boolean{
    return this.getJwtToken() != null;
  }

  getJwtToken(){
    return this.localStorage.retrieve('accessToken');
  }

  getUserRoles(){
    return this.localStorage.retrieve('userRole');
  }

  isAdmin(): boolean{
    const roles = this.localStorage.retrieve('userRole');

    for(const key in roles){
      var user_role = roles[key];

      if(user_role.authority === 'ROLE_ADMIN'){
        return true;
      }
    }

    return false;
  }

  logout(){
    this.localStorage.clear('accessToken');
    this.localStorage.clear('userRole');
  }
}

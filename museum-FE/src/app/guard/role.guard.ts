import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { AuthService } from '../service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router, 
    private localStorage: LocalStorageService){ }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    
     
      const userRoles = this.localStorage.retrieve('userRole');

      for (let index = 0; index < route.data.roles.length; index++) {
        var role = route.data.roles[index];

        for(const key in userRoles){
          var user_role = userRoles[key];

          if (role.authority === user_role.authority) {
            return true;
          }
        }
      }
      
      this.router.navigate(['/login']);
      return false;
  }
  
}

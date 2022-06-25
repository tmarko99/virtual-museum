import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  isLoggedIn: boolean;
  isAdmin: boolean;
  subscription: Subscription;
  

  constructor(private authService: AuthService, private router: Router) { }
  
  ngOnInit(): void {
    this.subscription = this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
    this.isLoggedIn = this.authService.isLoggedIn();
    
    this.subscription = this.authService.adminLogged.subscribe((data: boolean) => this.isAdmin = data);
    this.isAdmin = this.authService.isAdmin();

  }


  onLogout(){
    this.authService.logout();
    this.isLoggedIn = false;
    this.isAdmin = false;
    this.router.navigateByUrl('/').then(() => {
      window.location.reload();
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }


}

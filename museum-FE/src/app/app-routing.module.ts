import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { SignUpComponent } from './auth/sign-up/sign-up.component';
import { EksponatDetaljiComponent } from './eksponati/eksponat-detalji/eksponat-detalji.component';
import { EksponatiComponent } from './eksponati/eksponati.component';
import { AuthGuard } from './guard/auth.guard';
import { RoleGuard } from './guard/role.guard';
import { NewEksponatComponent } from './new-eksponat/new-eksponat.component';
import { ObilasciComponent } from './obilasci/obilasci.component';
import { ProfileComponent } from './profile/profile.component';
import { RecenzijaComponent } from './recenzija/recenzija.component';

const routes: Routes = [
  { path: '', redirectTo: "/eksponati", pathMatch: "full"},
  { path: 'eksponati', component: EksponatiComponent},
  { path: 'eksponat/:id', component: EksponatDetaljiComponent, pathMatch: 'full'},
  { path: 'eksponat/:id', component: RecenzijaComponent, canActivate: [AuthGuard]},
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  { path: 'obilasci', component: ObilasciComponent, canActivate: [AuthGuard]},
  { path: 'new-eksponat', component: NewEksponatComponent, canActivate: [RoleGuard], 
        data: {
          roles: [           
              { authority: 'ROLE_ADMIN' }
            ]
        }},
  { path: 'login', component: LoginComponent},
  { path: 'register', component: SignUpComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { Options } from '@angular-slider/ngx-slider';
import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { HeaderComponent } from '../header/header/header.component';
import { EksponatiResponsePayload } from '../model/eksponati-response.payload';
import { AuthService } from '../service/auth.service';
import { EksponatiService } from '../service/eksponati.service';
import { ObilazakService } from '../service/obilazak.service';

@Component({
  selector: 'app-eksponati',
  templateUrl: './eksponati.component.html',
  styleUrls: ['./eksponati.component.css']
})
export class EksponatiComponent implements OnInit {

  constructor(private eksponatiService: EksponatiService, private router: Router,
    private obilazakService: ObilazakService, private toastr: ToastrService,
    private activatedRoute: ActivatedRoute, private authService: AuthService) { }

  eksponati: EksponatiResponsePayload[] = [];
  eksponatiPrikaz: EksponatiResponsePayload[] = [];
  isAdmin: boolean;
  subscription: Subscription;
  totalPages = 0;
  totalElements: number;
  pageSize: number = 10;
  currentPage = 0;
  pageSizes = [1, 3, 6, 9];
  max: number;
  ocene: number = 0;


  minValuePrice: number = 1;
  maxValuePrice: number = 2000;
  countryValue: string = '';
  searchValue: string = '';

  zemlje: string[] = [];

  optionsPrice: Options = {
    floor: 0,
    ceil: this.maxValuePrice
  };


  ngOnInit(): void {
    this.findAll();
    this.activatedRoute.queryParams.subscribe(params => {
      if(params.created !== undefined && params.created === 'true'){
        this.toastr.success("Eksponat uspesno dodat!");
      }
    });

    this.subscription = this.authService.adminLogged.subscribe((data: boolean) => this.isAdmin = data);
    this.isAdmin = this.authService.isAdmin();
  }

  getRequestParams(page, pageSize) {
    let params = {};

    if (page) {
      params[`pageNumber`] = page - 1;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }


    return params;
  }

  findAll(){
    const params = this.getRequestParams(this.currentPage, this.pageSize);

    this.eksponatiService.findAll(params).subscribe(data => {
      this.eksponati = data['content'];
      this.eksponatiPrikaz = this.eksponati;
      this.totalPages = data['totalPages'];
      this.pageSize = data['pageSize'];
      this.totalElements =  data['totalElements'];
      this.zemlje = [...new Set(this.eksponati.map((eksponat) => eksponat.zemljaPorekla))];
      this.maxValuePrice = this.setupPriceSlider(this.eksponati);
      this.optionsPrice = {
        floor: 0,
        ceil: this.maxValuePrice
      };
    });
  }

  eksponatDetalji(id: number){
    this.router.navigate(['eksponat', id]);
  }

  kreirajObilazak(id: number){
    this.obilazakService.createObilazak(id).subscribe(() => {
      this.toastr.success('Obilazak uspesno kreiran');
    });
  }


  handlePageChange(event) {
    this.currentPage = event;
    this.findAll();
  }

  handlePageSizeChange(event) {
    this.pageSize = event.target.value;
    this.currentPage = 1;
    this.findAll();
  }

  searchByPrice(){

    let arr = this.eksponatiPrikaz

    arr = arr.filter((eksponat) => {
      return (
        eksponat.cenaObilaska <= this.maxValuePrice &&
        eksponat.cenaObilaska >= this.minValuePrice
      );
    });

    this.eksponati = arr;
  }

  search(){
    let arr = this.eksponatiPrikaz;

    let search = this.searchValue.trim().toLowerCase();

    if(search != ""){
      arr = arr.filter((eksponat) => eksponat.naziv.toLowerCase().includes(search));
    }

    if (this.countryValue.trim() != "") {
      arr = arr.filter((eksponat) => { return eksponat.zemljaPorekla == this.countryValue});
    }

    this.eksponati = arr;
  }

  obrisiEksponat(eksponatId: number){
    this.eksponatiService.deleteEksponat(eksponatId).subscribe(() => {
      this.toastr.success("Eksponat uspesno obrisan!");
      this.findAll();
    })
  }

  highestPriced(eksponati: EksponatiResponsePayload[]){
     return eksponati.reduce((prev, curr) => {
      return prev.cenaObilaska > curr.cenaObilaska ? prev : curr;
     });
  }

  setupPriceSlider(eksponati: EksponatiResponsePayload[]): number{
    let maxPrice = (this.highestPriced(eksponati)).cenaObilaska;
    if(maxPrice != null){
      return maxPrice;
    }
    else{
      return 10000;
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}

import { Routes } from '@angular/router';
import {ReservationCreateComponent, ReservationListComponent, GuestCreateComponent} from "./presentation/pages";

export const routes: Routes = [

  {
    path: 'reservations',
    component: ReservationListComponent
  },
  {
    path: 'reservation-create',
    component: ReservationCreateComponent
  },
  {
    path: 'guest-create',
    component: GuestCreateComponent
  },
  {
    path: '',
    redirectTo: '/reservations',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/reservations',
    pathMatch: 'full'
  }

];

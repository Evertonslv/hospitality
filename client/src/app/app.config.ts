import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {RESERVATION_REPOSITORY} from "./domain/repositories";
import {ReservationHttpRepository} from "./infrastructure/http";
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {ApiInterceptor} from "./infrastructure/interceptor";
import {MAT_SNACK_BAR_DEFAULT_OPTIONS} from "@angular/material/snack-bar";
import {GUEST_REPOSITORY} from "./domain/repositories";
import {GuestHttpRepository} from "./infrastructure/http";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from "@angular/material/core";
import {MomentDateAdapter} from "@angular/material-moment-adapter";

export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'YYYY-MM-DD',
    monthYearLabel: 'YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'YYYY',
  },
}

export const appConfig: ApplicationConfig = {
  providers: [
    {
      provide: RESERVATION_REPOSITORY,
      useClass: ReservationHttpRepository,
    },
    {
      provide: GUEST_REPOSITORY,
      useClass: GuestHttpRepository,
    },
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: {duration: 2500}
    },
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE]
    },
    {
      provide: MAT_DATE_FORMATS,
      useValue: MY_FORMATS
    },
    provideHttpClient(withInterceptors([ApiInterceptor])),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient()
  ]
};

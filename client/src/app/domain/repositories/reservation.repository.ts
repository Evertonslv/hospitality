import {ReservationCheckinoutRequestModel, ReservationModel, ReservationRequestModel} from "../models"
import {Observable} from "rxjs"
import {InjectionToken} from "@angular/core"
import {CheckoutModel} from "../models/checkout.model";

export interface ReservationRepository {
  findPending: () => Observable<ReservationResponseParams.Model[]>
  findCheckIn: () => Observable<ReservationResponseParams.Model[]>
  checkIn: (checkinoutRequestModel: CheckInOutRequestParams.Model) => Observable<void>
  checkOut: (checkinoutRequestModel: CheckInOutRequestParams.Model) => Observable<CheckOutResponseParams.Model>
  save: (checkinoutRequestModel: ReservationRequestParams.Model) => Observable<ReservationResponseParams.Model>
}

export namespace ReservationResponseParams {
  export type Model = ReservationModel
}

export namespace ReservationRequestParams {
  export type Model = ReservationRequestModel
}

export namespace CheckInOutRequestParams {
  export type Model = ReservationCheckinoutRequestModel
}

export namespace CheckOutResponseParams {
  export type Model = CheckoutModel
}

export const RESERVATION_REPOSITORY = new InjectionToken<ReservationRepository>('reservation-repository-token');

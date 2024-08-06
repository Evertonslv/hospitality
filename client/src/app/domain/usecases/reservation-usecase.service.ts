import {Inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {
  CheckInOutRequestParams, CheckOutResponseParams,
  RESERVATION_REPOSITORY,
  ReservationRepository,
  ReservationResponseParams,
  ReservationRequestParams
} from "../repositories";

@Injectable({ providedIn: 'root' })
export class ReservationUsecaseService {

  constructor(@Inject(RESERVATION_REPOSITORY) private reservationRepository: ReservationRepository) { }

  public findPending(): Observable<ReservationResponseParams.Model[]> {
    return this.reservationRepository.findPending()
  }

  public findCheckIn(): Observable<ReservationResponseParams.Model[]> {
    return this.reservationRepository.findCheckIn()
  }

  public checkIn(checkinoutRequest: CheckInOutRequestParams.Model): Observable<void> {
    return this.reservationRepository.checkIn(checkinoutRequest)
  }

  public checkOut(checkinoutRequest: CheckInOutRequestParams.Model): Observable<CheckOutResponseParams.Model> {
    return this.reservationRepository.checkOut(checkinoutRequest)
  }

  public save(reservationRequest: ReservationRequestParams.Model): Observable<ReservationResponseParams.Model> {
    return this.reservationRepository.save(reservationRequest)
  }
}

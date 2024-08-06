import {Injectable} from "@angular/core"
import {
  CheckOutResponseParams,
  ReservationRepository,
  ReservationResponseParams,
  ReservationRequestParams
} from "../../domain/repositories"
import {catchError, Observable, throwError} from "rxjs"
import {HttpClient, HttpErrorResponse} from "@angular/common/http"
import {ReservationCheckinoutRequestModel} from "../../domain/models"

@Injectable({
  providedIn: 'root'
})
export class ReservationHttpRepository implements ReservationRepository {

  private baseUrl: string = 'http://localhost:8080/reservation'

  constructor(private http: HttpClient) { }

  findCheckIn(): Observable<ReservationResponseParams.Model[]> {
    return this.http.get<ReservationResponseParams.Model[]>(`${this.baseUrl}/find-checkin`).pipe(
      catchError(this.handleError)
    );
  }

  findPending(): Observable<ReservationResponseParams.Model[]> {
    return this.http.get<ReservationResponseParams.Model[]>(`${this.baseUrl}/find-pending`).pipe(
      catchError(this.handleError)
    );
  }

  checkIn(checkinoutRequest: ReservationCheckinoutRequestModel): Observable<void> {
    return this.http.post<any>(`${this.baseUrl}/checkin`, checkinoutRequest).pipe(
      catchError(this.handleError)
    );
  }

  checkOut(checkinoutRequest: ReservationCheckinoutRequestModel): Observable<CheckOutResponseParams.Model> {
    return this.http.post<CheckOutResponseParams.Model>(`${this.baseUrl}/checkout`, checkinoutRequest).pipe(
      catchError(this.handleError)
    );
  }

  save(reservationRequest: ReservationRequestParams.Model): Observable<ReservationResponseParams.Model> {
    return this.http.post<ReservationResponseParams.Model>(this.baseUrl, reservationRequest).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMessage: string = error.message
    return throwError(errorMessage)
  }

}


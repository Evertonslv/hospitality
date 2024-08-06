import {Injectable} from "@angular/core"
import {catchError, Observable, throwError} from "rxjs"
import {HttpClient, HttpErrorResponse} from "@angular/common/http"
import {
  CreateGuestRequestParams,
  GuestFilterRequestParams,
  GuestRepository,
  GuestResponseParams
} from "../../domain/repositories"

@Injectable({
  providedIn: 'root'
})
export class GuestHttpRepository implements GuestRepository {

  private baseUrl: string = 'http://localhost:8080/guest'

  constructor(private http: HttpClient) { }

  findByFilter(filter: GuestFilterRequestParams.Model): Observable<GuestResponseParams.Model[]> {
    return this.http.post<GuestResponseParams.Model[]>(`${this.baseUrl}/find-filter`, filter)
  }

  save(guest: CreateGuestRequestParams.Model): Observable<GuestResponseParams.Model> {
    return this.http.post<GuestResponseParams.Model>(this.baseUrl, guest).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMessage: string = error.message
    return throwError(errorMessage)
  }
}


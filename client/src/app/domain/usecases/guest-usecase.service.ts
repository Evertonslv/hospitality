import {Inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {
  CreateGuestRequestParams,
  GUEST_REPOSITORY,
  GuestFilterRequestParams,
  GuestRepository,
  GuestResponseParams
} from "../repositories";

@Injectable({ providedIn: 'root' })
export class GuestUsecaseService {

  constructor(@Inject(GUEST_REPOSITORY) private guestRepository: GuestRepository) { }

  save(createGuest: CreateGuestRequestParams.Model): Observable<GuestResponseParams.Model> {
    return this.guestRepository.save(createGuest)
  }

  findByFilter(filter: GuestFilterRequestParams.Model): Observable<GuestResponseParams.Model[]> {
    return this.guestRepository.findByFilter(filter)
  }
}

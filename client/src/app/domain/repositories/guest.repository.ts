import {CreateGuestModel, GuestFilterRequestModel, GuestModel} from "../models"
import {Observable} from "rxjs"
import {InjectionToken} from "@angular/core"

export interface GuestRepository {
  save: (guest: CreateGuestRequestParams.Model) => Observable<GuestResponseParams.Model>
  findByFilter: (filter: GuestFilterRequestParams.Model) => Observable<GuestResponseParams.Model[]>
}

export namespace GuestResponseParams {
  export type Model = GuestModel
}

export namespace CreateGuestRequestParams {
  export type Model = CreateGuestModel
}

export namespace GuestFilterRequestParams {
  export type Model = GuestFilterRequestModel
}

export const GUEST_REPOSITORY = new InjectionToken<GuestRepository>('guest-repository-token')

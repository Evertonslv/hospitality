import {GuestModel} from "./guest.model";
import {ReservationStatus} from "./reservation.status";
import {CheckoutDetailModel} from "./checkout-detail.model";

export type CheckoutModel = {
  id: number
  guest: GuestModel
  chargeDetail: CheckoutDetailModel[]
  checkInDate: Date
  checkOutDate: Date
  checkInTime: Date
  checkOutTime: Date
  hasCar: boolean
  reservationDate: Date
  status: ReservationStatus
}

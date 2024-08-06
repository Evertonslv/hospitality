import {GuestModel} from "./guest.model"
import {ReservationStatus} from "./reservation.status"

export type ReservationModel = {
  id: number
  guest : GuestModel
  checkInDate: Date
  checkOutDate: Date
  checkInTime?: Date
  checkOutTime?: Date
  reservationDate: Date
  hasCar: boolean
  status: ReservationStatus
}

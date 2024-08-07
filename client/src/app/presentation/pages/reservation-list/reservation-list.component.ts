import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core'
import {RouterOutlet} from "@angular/router"
import {FooterComponent, HeaderComponent, TableComponent} from "../../components"
import {MatTableDataSource} from "@angular/material/table"
import {ReservationUsecaseService} from "../../../domain/usecases"
import {ReservationCheckinoutRequestModel, ReservationModel} from "../../../domain/models"
import {MatFormField, MatHint, MatLabel, MatOption, MatSelect} from "@angular/material/select"
import {CurrencyPipe, formatDate, NgForOf, NgIf} from "@angular/common"
import {MatSnackBar} from "@angular/material/snack-bar"
import {MatButton, MatIconButton} from "@angular/material/button"
import {MatButtonToggle, MatButtonToggleChange, MatButtonToggleGroup} from "@angular/material/button-toggle"
import {MatDialog, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatInput} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatCard} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {CheckoutModel} from "../../../domain/models/checkout.model";
import {CheckoutDetailModel} from "../../../domain/models/checkout-detail.model";

@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [
    RouterOutlet, HeaderComponent,
    FooterComponent, TableComponent,
    MatSelect, MatOption, NgForOf,
    MatFormField, MatLabel, MatButton,
    MatButtonToggleGroup, MatButtonToggle, MatDialogContent, MatDialogTitle, MatHint, MatInput, ReactiveFormsModule, MatCard, MatIcon, MatIconButton, NgIf, CurrencyPipe
  ],
  templateUrl: './reservation-list.component.html',
  styleUrl: './reservation-list.component.scss'
})
export class ReservationListComponent implements OnInit {
  @ViewChild('detailTotalAmountModal') detailTotalAmountModal!: TemplateRef<any>

  dataSource: MatTableDataSource<any>
  displayedColumns: string[] = ['name', 'document', 'phone', 'reservationDate']
  selectedButton = '1'
  configButtonTable: { label: string, onClick: (element: any) => void }
  detailCheckOut: CheckoutDetailModel[];

  constructor(
    public dialog: MatDialog,
    private reservationList: ReservationUsecaseService,
    private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.searchReservation()
  }

  onSelectionChange(event: MatButtonToggleChange) {
    this.selectedButton = event.value
    this.searchReservation()
  }

  searchReservation() {
    if (this.selectedButton === '1') {
      this.configButtonTable = {label: 'CheckIn', onClick: this.executeCheckIn}
      this.reservationList.findPending().subscribe(
        (reservations: ReservationModel[]) => this.handler(reservations)
      )
    } else {
      this.configButtonTable = {label: 'CheckOut', onClick: this.executeCheckOut}
      this.reservationList.findCheckIn().subscribe(
        (reservations: ReservationModel[]) => this.handler(reservations)
      )
    }
  }

  private handler(reservations: ReservationModel[]) {
    const data = reservations.map((reservation: ReservationModel) => {
      return {
        id: reservation.id,
        name: reservation.guest.name,
        document: reservation.guest.document,
        phone: reservation.guest.phone,
        reservationDate: formatDate(reservation.reservationDate, 'dd/MM/YYYY HH:mm', 'en')
      }
    })

    this.dataSource = new MatTableDataSource(data)
  }

  executeCheckIn = (event: any) => {
    const checkinoutRequestModel: ReservationCheckinoutRequestModel = {
      reservationId: event.id,
      checkInOutDateTime: new Date()
    }

    this.reservationList.checkIn(checkinoutRequestModel).subscribe({
      next: () => this.showSnackBar('Checkin realizado com sucesso'),
      error: (err) => this.showSnackBar(err),
      complete: () => this.searchReservation()
    })
  }

  executeCheckOut = (event: any) => {
    const checkinoutRequestModel: ReservationCheckinoutRequestModel = {
      reservationId: event.id,
      checkInOutDateTime: new Date(2024, 8, 6, 10, 0)
    }

    this.reservationList.checkOut(checkinoutRequestModel).subscribe({
      next: (checkout: CheckoutModel) => {
        this.detailCheckOut = checkout.chargeDetail
        this.dialog.open(this.detailTotalAmountModal, {
          width: '50%',
          maxWidth: '600px'
        })
      },
      error: (err) => this.showSnackBar(err),
      complete: () => this.searchReservation()
    })
  }

  private showSnackBar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 4000,
      verticalPosition: 'top',
      horizontalPosition: 'right',
      panelClass: ['warning-snackbar']
    })
  }

  closeModal() {
    this.dialog.closeAll()
  }

  getTotalAmount(): number {
    return this.detailCheckOut.reduce((total, detail) => total + detail.amount, 0);
  }
}

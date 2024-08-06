import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core'
import {FooterComponent, HeaderComponent, TableComponent} from "../../components"
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms"
import {MatCard, MatCardContent, MatCardTitle} from "@angular/material/card"
import {MatFormField, MatHint, MatSuffix} from "@angular/material/form-field"
import {MatLabel, MatOption, MatSelect} from "@angular/material/select"
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker,
  MatEndDate,
  MatStartDate
} from "@angular/material/datepicker"
import {MatInput} from "@angular/material/input"
import {MatCheckbox} from "@angular/material/checkbox"
import {MatButton, MatIconButton} from "@angular/material/button"
import {GuestModel} from "../../../domain/models"
import {MatDialog, MatDialogContent, MatDialogTitle} from "@angular/material/dialog"
import {MatCell, MatHeaderCell, MatTableDataSource} from "@angular/material/table"
import {NgIf} from "@angular/common"
import {MatIcon} from "@angular/material/icon"
import {GuestUsecaseService, ReservationUsecaseService} from "../../../domain/usecases"
import {MatSnackBar} from "@angular/material/snack-bar"


@Component({
  selector: 'app-reservation-create',
  standalone: true,
  imports: [
    FooterComponent, HeaderComponent, ReactiveFormsModule,
    MatCardTitle, MatCard, MatCardContent, MatFormField,
    MatSelect, MatOption, MatDatepickerToggle, MatInput,
    MatDatepickerInput, MatDatepicker, MatCheckbox,
    MatButton, MatDialogTitle, FormsModule, MatDialogContent,
    MatHeaderCell, MatCell, TableComponent, MatLabel,
    NgIf, MatHint, MatDateRangeInput, MatStartDate,
    MatEndDate, MatDateRangePicker, MatSuffix,
    MatIconButton, MatIcon
  ],
  templateUrl: './reservation-create.component.html',
  styleUrl: './reservation-create.component.scss'
})
export class ReservationCreateComponent implements OnInit {
  @ViewChild('searchGuestModal') searchGuestModal!: TemplateRef<any>

  reservationForm: FormGroup
  selectedGuest: GuestModel | null = null
  guestFilterRequest = { filter: '' }
  searchResults: MatTableDataSource<any>
  displayedColumns: string[] = ['name', 'document', 'phone']

  constructor(private fb: FormBuilder,
              public dialog: MatDialog,
              private reservationUsecaseService: ReservationUsecaseService,
              private guestUsecaseService: GuestUsecaseService,
              private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.clearForm()
  }

  onSubmit() {
    if (this.reservationForm.valid) {
      this.reservationUsecaseService.save(this.reservationForm.value).subscribe({
        next: () => this.showSnackBar('Reserva criada com sucesso'),
        error: err => this.showSnackBar(err),
        complete: () => this.clearForm()
      })
    }
  }

  openSearchGuestModal() {
    this.dialog.open(this.searchGuestModal, {
      width: '80%',
      maxWidth: '800px',
      maxHeight: '80%'
    })
  }

  searchGuests() {
    this.guestUsecaseService.findByFilter(this.guestFilterRequest).subscribe({
      next: (guests: GuestModel[]) => {
        const data = guests.map((guest: GuestModel) => {
          return {
            id: guest.id,
            name: guest.name,
            document: guest.document,
            phone: guest.phone,
          }
        })

        this.searchResults = new MatTableDataSource(data)
      }
    })


  }

  selectGuest = (event: any) => {
    this.selectedGuest = event
    this.reservationForm.patchValue({
      guestId: event.id
    })

    this.dialog.closeAll()
  }

  deleteGuest() {
    this.selectedGuest = null
    this.reservationForm.patchValue({
      guestId: null
    })
  }

  private clearForm() {
    this.selectedGuest = null
    this.reservationForm = this.fb.group({
      guestId: [null, Validators.required],
      checkInDate: [null, Validators.required],
      checkOutDate: [null, Validators.required],
      hasCar: [false]
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
}

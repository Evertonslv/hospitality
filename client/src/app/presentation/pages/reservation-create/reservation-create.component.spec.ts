import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {of, throwError} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ReservationCreateComponent} from './reservation-create.component';
import {GuestUsecaseService, ReservationUsecaseService} from "../../../domain/usecases";
import {ReservationModel, ReservationStatus} from "../../../domain/models";
import {ActivatedRoute} from "@angular/router";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, NativeDateAdapter} from "@angular/material/core";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'YYYY-MM-DD',
    monthYearLabel: 'YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'YYYY',
  },
}

describe('ReservationCreateComponent', () => {
  let component: ReservationCreateComponent;
  let fixture: ComponentFixture<ReservationCreateComponent>;
  let reservationUsecaseServiceSpy: jasmine.SpyObj<ReservationUsecaseService>;
  let guestUsecaseServiceSpy: jasmine.SpyObj<GuestUsecaseService>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;
  let formBuilder: FormBuilder;

  beforeEach(async () => {
    const reservationUsecaseServiceMock = jasmine.createSpyObj('ReservationUsecaseService', ['save']);
    const guestUsecaseServiceMock = jasmine.createSpyObj('GuestUsecaseService', ['findByFilter']);
    const snackBarMock = jasmine.createSpyObj('MatSnackBar', ['open']);
    const dialogMock = jasmine.createSpyObj('MatDialog', ['open', 'closeAll']);

    await TestBed.configureTestingModule({
      imports: [ReservationCreateComponent, ReactiveFormsModule, BrowserAnimationsModule ],
      providers: [
        FormBuilder,
        { provide: ReservationUsecaseService, useValue: reservationUsecaseServiceMock },
        { provide: GuestUsecaseService, useValue: guestUsecaseServiceMock },
        { provide: MatSnackBar, useValue: snackBarMock },
        { provide: MatDialog, useValue: dialogMock },
        { provide: ActivatedRoute, useValue: {} },
        { provide: DateAdapter, useClass: NativeDateAdapter },
        { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
        { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReservationCreateComponent);
    component = fixture.componentInstance;
    reservationUsecaseServiceSpy = TestBed.inject(ReservationUsecaseService) as jasmine.SpyObj<ReservationUsecaseService>;
    guestUsecaseServiceSpy = TestBed.inject(GuestUsecaseService) as jasmine.SpyObj<GuestUsecaseService>;
    snackBarSpy = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;
    dialogSpy = TestBed.inject(MatDialog) as jasmine.SpyObj<MatDialog>;
    formBuilder = TestBed.inject(FormBuilder);

    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('clearForm', () => {
    it('should reset the form to default values', () => {
      component.clearForm();
      expect(component.reservationForm.value).toEqual({
        guestId: null,
        checkInDate: null,
        checkOutDate: null,
        hasCar: false
      });
    });
  });

  describe('showSnackBar', () => {
    it('should call snackBar.open with correct parameters', () => {
      const message = 'Test message';
      component['showSnackBar'](message);

      expect(snackBarSpy.open).toHaveBeenCalledWith(message, 'Close', {
        duration: 4000,
        verticalPosition: 'top',
        horizontalPosition: 'right',
        panelClass: ['warning-snackbar']
      });
    });
  });

  describe('openSearchGuestModal', () => {
    it('should open the search guest modal', () => {
      const dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['afterClosed']);
      dialogSpy.open.and.returnValue(dialogRefSpy);

      component.openSearchGuestModal();

      expect(dialogSpy.open).toHaveBeenCalledWith(component.searchGuestModal, {
        width: '80%',
        maxWidth: '800px',
        maxHeight: '80%'
      });
    });
  });

  describe('searchGuests', () => {
    it('should update searchResults with guests data', () => {
      const guests = [{ id: 1, name: 'John Doe', document: '123456789', phone: '987654321' }];
      guestUsecaseServiceSpy.findByFilter.and.returnValue(of(guests));

      component.searchGuests();

      expect(guestUsecaseServiceSpy.findByFilter).toHaveBeenCalledWith(component.guestFilterRequest);
      expect(component.searchResults.data).toEqual([
        { id: 1, name: 'John Doe', document: '123456789', phone: '987654321' }
      ]);
    });
  });

  describe('deleteGuest', () => {
    it('should reset selectedGuest and guestId in form', () => {
      component.selectedGuest = { id: 1, name: 'John Doe', document: '123456789', phone: '987654321' };
      component.reservationForm.patchValue({ guestId: 1 });

      component.deleteGuest();

      expect(component.selectedGuest).toBeNull();
      expect(component.reservationForm.get('guestId')?.value).toBeNull();
    });
  });

});

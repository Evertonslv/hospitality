import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of, throwError } from 'rxjs';
import { GuestCreateComponent } from './guest-create.component';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import {GuestUsecaseService} from "../../../domain/usecases";
import {ActivatedRoute} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {GuestModel} from "../../../domain/models";

describe('GuestCreateComponent', () => {
  let component: GuestCreateComponent;
  let fixture: ComponentFixture<GuestCreateComponent>;
  let guestUsecaseServiceSpy: jasmine.SpyObj<GuestUsecaseService>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;
  let formBuilder: FormBuilder;

  beforeEach(async () => {
    const guestUsecaseServiceMock = jasmine.createSpyObj('GuestUsecaseService', ['save']);
    const snackBarMock = jasmine.createSpyObj('MatSnackBar', ['open']);

    await TestBed.configureTestingModule({
      imports: [ GuestCreateComponent, ReactiveFormsModule, BrowserAnimationsModule ],
      providers: [
        FormBuilder,
        { provide: GuestUsecaseService, useValue: guestUsecaseServiceMock },
        { provide: MatSnackBar, useValue: snackBarMock },
        { provide: ActivatedRoute, useValue: {} }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(GuestCreateComponent);
    component = fixture.componentInstance;
    guestUsecaseServiceSpy = TestBed.inject(GuestUsecaseService) as jasmine.SpyObj<GuestUsecaseService>;
    snackBarSpy = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;
    formBuilder = TestBed.inject(FormBuilder);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('onSubmit', () => {
    it('should call save and show success snackBar on success', () => {
      const formValues = {
        name: 'John Doe',
        document: '123456789',
        phone: '987654321'
      };

      const gustsMock: GuestModel = {
        id: 1,
        name: 'John Doe',
        document: '123456789',
        phone: '987654321'
      }

      component.cadastroForm.setValue(formValues);
      guestUsecaseServiceSpy.save.and.returnValue(of(gustsMock));

      component.onSubmit();

      expect(guestUsecaseServiceSpy.save).toHaveBeenCalledWith(formValues);
      expect(snackBarSpy.open).toHaveBeenCalledWith('Hóspede criado com sucesso', 'Close', jasmine.any(Object));
    });

    it('should handle save error and show error snackBar', () => {
      const errorMessage = 'Error occurred';
      guestUsecaseServiceSpy.save.and.returnValue(throwError(errorMessage));
      component.cadastroForm.setValue({
        name: 'John Doe',
        document: '123456789',
        phone: '987654321'
      });

      spyOn(component as any, 'showSnackBar');
      component.onSubmit();

      expect(guestUsecaseServiceSpy.save).toHaveBeenCalled();
      expect(component['showSnackBar']).toHaveBeenCalledWith(errorMessage);
    });

    it('should not call save if form is invalid', () => {
      component.cadastroForm.setValue({
        name: '',
        document: '',
        phone: ''
      });

      guestUsecaseServiceSpy.save.and.stub();

      component.onSubmit();

      expect(guestUsecaseServiceSpy.save).not.toHaveBeenCalled();
    });

  });

  describe('clearForm', () => {
    it('should reset the form to default values', () => {
      component.clearForm();
      expect(component.cadastroForm.value).toEqual({
        name: '',
        document: '',
        phone: ''
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
});

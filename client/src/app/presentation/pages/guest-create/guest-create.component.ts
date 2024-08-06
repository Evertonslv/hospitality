import {Component, OnInit} from '@angular/core'
import {FooterComponent, HeaderComponent, TableComponent} from "../../components"
import {MatFormField} from "@angular/material/form-field"
import {MatOption} from "@angular/material/autocomplete"
import {MatLabel, MatSelect} from "@angular/material/select"
import {NgForOf} from "@angular/common"
import {MatCard, MatCardContent, MatCardTitle} from "@angular/material/card"
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms"
import {MatInput} from "@angular/material/input"
import {MatButton} from "@angular/material/button"
import {GuestUsecaseService} from "../../../domain/usecases"
import {MatSnackBar} from "@angular/material/snack-bar"

@Component({
  selector: 'app-guest-create',
  standalone: true,
  imports: [
    FooterComponent, HeaderComponent, MatFormField,
    MatOption, MatSelect, NgForOf, TableComponent,
    MatCard, MatCardTitle, MatCardContent, ReactiveFormsModule,
    MatInput, MatButton, MatLabel
  ],
  templateUrl: './guest-create.component.html',
  styleUrl: './guest-create.component.scss'
})
export class GuestCreateComponent implements OnInit {
  cadastroForm: FormGroup

  constructor(private fb: FormBuilder,
              private guestUsecaseService: GuestUsecaseService,
              private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.clearForm()
  }

  onSubmit() {
    if (this.cadastroForm.valid) {
      this.guestUsecaseService.save(this.cadastroForm.value).subscribe({
        next: () => this.showSnackBar('Hóspede criado com sucesso'),
        error: err => this.showSnackBar(err),
        complete: () => this.clearForm()
      })
    }
  }

  private clearForm() {
    this.cadastroForm = this.fb.group({
      name: ['', Validators.required],
      document: ['', Validators.required],
      phone: ['', Validators.required]
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

import { Component } from '@angular/core'
import {MatToolbar} from "@angular/material/toolbar"
import {MatAnchor, MatButton} from "@angular/material/button"
import {RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [
    MatToolbar,
    MatAnchor,
    MatButton,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {

}

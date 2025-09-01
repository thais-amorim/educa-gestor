import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './component/navbar/navbar.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class App {
  protected readonly title = signal('Educa Gestor');
}

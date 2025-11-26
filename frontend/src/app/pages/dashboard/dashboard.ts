import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header';
import { Sidebar } from "../../components/sidebar/sidebar";
import { RouterModule } from "@angular/router";

@Component({
  selector: 'app-dashboard',
  imports: [HeaderComponent, Sidebar, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {

}

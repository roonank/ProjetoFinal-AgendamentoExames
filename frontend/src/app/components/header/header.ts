import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserResponseDTO } from '../../core/auth/auth-service';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class HeaderComponent {
  user: UserResponseDTO | null = null;

  constructor(private router: Router) {}

  ngOnInit() {
    const userData = localStorage.getItem('user');
    this.user = userData ? JSON.parse(userData) : null;
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.router.navigate(['/']);
  }
}

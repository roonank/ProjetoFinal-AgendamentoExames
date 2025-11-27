import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { UserResponseDTO } from '../../core/auth/auth-service';
import { filter } from 'rxjs/internal/operators/filter';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class HeaderComponent {
  user: UserResponseDTO | null = null;
  pageTitle = 'Agendamento de Exames';

  constructor(private router: Router) {}

  ngOnInit() {
    const userData = localStorage.getItem('user');
    this.user = userData ? JSON.parse(userData) : null;

    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        this.updateTitle();
      });

    this.updateTitle();
  }

  updateTitle() {
    const route = this.router.url;

    if (route.includes('exams')) {
      this.pageTitle = 'Gerenciar Exames';
    } else if (route.includes('lab-units')) {
      this.pageTitle = 'Gerenciar Unidades de Laboratório';
    } else if (route.includes('appointments')) {
      this.pageTitle = 'Agendamentos';
    } else if (route.includes('profile')) {
      this.pageTitle = 'Meu Perfil';
    } else {
      this.pageTitle = 'Agendamento de Exames';
    }
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.router.navigate(['/']);
  }
}

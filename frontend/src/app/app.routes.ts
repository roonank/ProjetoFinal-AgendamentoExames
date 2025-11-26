import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth-guard';
import { LoginComponent } from './pages/login/login';
import { adminGuard } from './core/auth/admin-guard';

export const routes: Routes = [
  { path: '', component: LoginComponent },

  {
    path: 'register',
    loadComponent: () =>
      import('./pages/register/register').then(
        (m) => m.Register,
      ),
  },

  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/dashboard/dashboard').then(
        (m) => m.Dashboard
      ),

      children: [
      { path: '', redirectTo: 'appointments', pathMatch: 'full' },

      { path: 'appointments',
        loadComponent: () => 
          import('./pages/appointments/appointments').then(m => m.Appointments)
      },

      { path: 'exams',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./pages/exams/exams').then(m => m.Exams)
      },

      { path: 'lab-units',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./pages/lab-units/lab-units').then(m => m.LabUnits)
      },
    ]
  },

  { path: '**', redirectTo: '' },
];

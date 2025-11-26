import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserResponseDTO } from './auth-service';

export const adminGuard: CanActivateFn = () => {
  const router = inject(Router);
  const userData = localStorage.getItem('user');

  if (!userData) {
    return router.createUrlTree(['/']);
  }

  const user = JSON.parse(userData) as UserResponseDTO;

  if (user.role === 'ADMIN') {
    return true;
  }

  return router.createUrlTree(['/dashboard']);
};

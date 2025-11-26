import { HttpInterceptorFn } from '@angular/common/http';


const apiUrl = 'http://localhost:8081';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('auth_token');

  if (!token || !req.url.startsWith(apiUrl)) {
    return next(req);
  }

  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authReq);
};

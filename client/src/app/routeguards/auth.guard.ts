import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../services/login.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router: Router = inject(Router);
  const loginService: LoginService = inject(LoginService);
  let isAuthenticated: boolean = false;
  loginService.isAuthenticated.subscribe(data => isAuthenticated = data);

  if (!isAuthenticated) {
    router.navigateByUrl("/login");
  }

  return isAuthenticated;
};

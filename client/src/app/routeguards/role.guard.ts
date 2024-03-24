import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Member } from '../common/member';

export const roleGuard: CanActivateFn = (route, state) => {
  const router: Router = inject(Router);
  const storage: Storage = sessionStorage;
  let member: Member = new Member();

  if (storage.getItem("memberDetails")) {
    member = JSON.parse(storage.getItem("memberDetails")!);
  }

  if (member.role != "ROLE_ADMIN") {
    router.navigateByUrl("/");
  }

  return member.role == "ROLE_ADMIN";
};

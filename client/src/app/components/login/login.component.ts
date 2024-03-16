import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Member } from '../../common/member';
import { Router } from '@angular/router';
import { getCookie } from 'typescript-cookie';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  member!: Member;
  storage: Storage = sessionStorage;
  loginFormGroup!: FormGroup;
  showErrorMessage: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private router: Router
  ) { }

  ngOnInit(): void {

    this.loginFormGroup = this.formBuilder.group({
      member: this.formBuilder.group({
        email: new FormControl('',
          [
            Validators.required,
            Validators.pattern('^[A-Za-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$'),
          ]
        ),
        password: new FormControl('',
          [Validators.required]
        )
      })
    });
  }

  onSubmit() {
    console.log("Handling the login button");
    console.log(this.loginFormGroup.value);

    if (this.loginFormGroup.invalid) {
      this.loginFormGroup.markAllAsTouched();
      return;
    }

    this.member = this.loginFormGroup.get("member")?.value;
    this.showErrorMessage = false;

    this.loginService.validateLoginDetails(this.member).subscribe({
      next: response => {
        this.member.name = response.name;

        // if login success, we publish isAuthenticated is true.
        this.loginService.successAuthentication()

        // store member details
        this.loginService.member = this.member;

        // store csrf token
        let xsrf = getCookie("XSRF-TOKEN")!;
        this.storage.setItem("XSRF-TOKEN", xsrf);
        this.router.navigateByUrl("/products");
      },
      error: err => {
        this.showErrorMessage = true;
      }
    });

  }

  // getter functions for form control
  get email() { return this.loginFormGroup.get("member.email"); }
  get password() { return this.loginFormGroup.get("member.password"); }
}
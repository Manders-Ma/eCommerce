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
        // 把需要的資訊記錄下來，但不要接收敏感資訊。
        this.member.password = "";
        this.member.name = response.body?.name!;
        this.member.email = response.body?.email!;
        this.member.role = response.body?.role!;
        this.storage.setItem("memberDetails", JSON.stringify(this.member));


        // if login success, we publish isAuthenticated is true.
        // login service 會因為重整頁面所以重新創建，會丟失最新的isAuthenticated狀態
        // 所以把他存到session storage，讓login service的constructor去抓到最新狀態。
        this.storage.setItem("isAuthenticated", JSON.stringify(1));
        this.loginService.successAuthentication();

        // store jwt token
        this.storage.setItem("Authorization", response.headers.get("Authorization")!);

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
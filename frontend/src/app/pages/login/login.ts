import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService, LoginRequestDTO } from '../../core/auth/auth-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  templateUrl: './login.html',
})
export class LoginComponent {
  form: FormGroup;
  loading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.form.invalid || this.loading) return;

    this.errorMessage = '';
    this.loading = true;

    const payload: LoginRequestDTO = {
      email: this.form.value.email,
      password: this.form.value.password,
    };

    this.authService.login(payload).subscribe({
      next: (res) => {
        this.loading = false;

        localStorage.setItem('auth_token', res.token);
        localStorage.setItem('user', JSON.stringify(res.user));

        console.log('Login OK, usuário:', res.user);

        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        console.error(err);

        if (err.status === 401) {
          this.errorMessage = 'Credenciais inválidas.';
        } else {
          this.errorMessage = 'Erro ao fazer login. Tente novamente mais tarde.';
        }
      },
    });
  }
}

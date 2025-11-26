import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface LoginRequestDTO {
  email: string;
  password: string;
}

export interface LoginResponseDTO {
  token: string;
  user: UserResponseDTO;
}

export interface UserResponseDTO{
  id: number;
  fullName: string;
  email: string;
  cpf: string;
  phone: string;
  birthDate: string;
  address: string;
  gender: string;
  role: 'ADMIN' | 'CLIENT' ;
  active: boolean;
}

export interface UserRegisterDTO {
  fullName: string;
  email: string;
  password: string;
  cpf: string;
  phone: string;
  birthDate: string;
  address?: string;
  gender?: string;
}


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8081/api/auth';
  private readonly USERS_API = 'http://localhost:8081/api/users';

  constructor(private http: HttpClient) {}

  login(payload: LoginRequestDTO): Observable<LoginResponseDTO> {
    return this.http.post<LoginResponseDTO>(`${this.API_URL}/login`, payload);
  }

  register(payload: UserRegisterDTO): Observable<UserResponseDTO> {
    return this.http.post<UserResponseDTO>(`${this.USERS_API}/register`, payload);
  }
}

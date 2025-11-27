import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ExamResponseDTO {
  id: number;
  code: string;
  name: string;
  description?: string;
  deliverTimeInDays?: number;
  instructions?: string;
  price?: number;
}

export interface ExamCreateUpdateDTO {
  code: string;
  name: string;
  description?: string;
  deliverTimeInDays?: number;
  instructions?: string;
  price?: number;
}

@Injectable({
  providedIn: 'root',
})
export class ExamService {
  private readonly EXAMS_URL = 'http://localhost:8081/api/exams';

  constructor(private http: HttpClient) {}

  list(): Observable<ExamResponseDTO[]> {
    return this.http.get<ExamResponseDTO[]>(this.EXAMS_URL);
  }

  getById(id: number): Observable<ExamResponseDTO> {
    return this.http.get<ExamResponseDTO>(`${this.EXAMS_URL}/${id}`);
  }

  create(payload: ExamCreateUpdateDTO): Observable<ExamResponseDTO> {
    return this.http.post<ExamResponseDTO>(this.EXAMS_URL, payload);
  }

  update(id: number, payload: ExamCreateUpdateDTO): Observable<ExamResponseDTO> {
    return this.http.put<ExamResponseDTO>(`${this.EXAMS_URL}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.EXAMS_URL}/${id}`);
  }
  
}

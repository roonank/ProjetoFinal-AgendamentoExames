import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

export interface AppointmentExamCreateDTO {
  examId: number;
  note?: string;
}

export interface AppointmentCreateDTO {
  labUnitId: number;
  scheduledAt: string;
  notes?: string;
  exams: AppointmentExamCreateDTO[];
}

export interface AppointmentExamResponseDTO {
  id: number;
  examId: number;
  examCode: string;
  examName: string;
  deliverInDays: number;
  note?: string;
  price: number;
  status: string;
}

export interface AppointmentDetailDTO {
  id: number;
  userId: number;
  userName: string;

  labUnitId: number;
  labUnitName: string;
  labUnitAddress: string;

  scheduledAt: string;
  totalDurationDays: number;

  status: string;
  notes?: string;
  cancelReason?: string;

  exams: AppointmentExamResponseDTO[];
}

export interface AppointmentSummaryDTO {
  id: number;
  scheduledAt: string;
  status: string;
  labUnitName: string;
  examNames: string[];
}

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {
  private readonly API_URL = 'http://localhost:8081/api/appointments';

  constructor(private http: HttpClient) {}

  private withUserId(userId: number): HttpParams {
    return new HttpParams().set('userId', userId);
  }

  listMyAppointments(userId: number): Observable<AppointmentSummaryDTO[]> {
    return this.http.get<AppointmentSummaryDTO[]>(this.API_URL, {
      params: this.withUserId(userId),
    });
  }

  getDetail(id: number, userId: number): Observable<AppointmentDetailDTO> {
    return this.http.get<AppointmentDetailDTO>(`${this.API_URL}/${id}`, {
      params: this.withUserId(userId),
    });
  }

  create(userId: number, payload: AppointmentCreateDTO): Observable<AppointmentDetailDTO> {
    return this.http.post<AppointmentDetailDTO>(this.API_URL, payload, {
      params: this.withUserId(userId),
    });
  }

  cancel(id: number, userId: number, reason?: string): Observable<void> {
    let params = this.withUserId(userId);
    if (reason) {
      params = params.set('reason', reason);
    }
    return this.http.post<void>(`${this.API_URL}/${id}/cancel`, null, { params });
  }
}

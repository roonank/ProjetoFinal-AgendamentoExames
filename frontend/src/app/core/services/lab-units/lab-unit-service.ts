import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface LabUnitResponseDTO {
  id: number;
  name: string;
  address?: string;
  phone?: string;
  openingHours?: string;
}

export interface LabUnitCreateUpdateDTO {
  name: string;
  address?: string;
  phone?: string;
  openingHours?: string;
}

@Injectable({
  providedIn: 'root',
})
export class LabUnitService {
  private readonly LAB_UNITS_URL = 'http://localhost:8081/api/lab-units';

  constructor(private http: HttpClient) {}

  list(): Observable<LabUnitResponseDTO[]> {
    return this.http.get<LabUnitResponseDTO[]>(this.LAB_UNITS_URL);
  }

  getById(id: number): Observable<LabUnitResponseDTO> {
    return this.http.get<LabUnitResponseDTO>(`${this.LAB_UNITS_URL}/${id}`);
  }

  create(payload: LabUnitCreateUpdateDTO): Observable<LabUnitResponseDTO> {
    return this.http.post<LabUnitResponseDTO>(this.LAB_UNITS_URL, payload);
  }

  update(id: number, payload: LabUnitCreateUpdateDTO): Observable<LabUnitResponseDTO> {
    return this.http.put<LabUnitResponseDTO>(`${this.LAB_UNITS_URL}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.LAB_UNITS_URL}/${id}`);
  }
}

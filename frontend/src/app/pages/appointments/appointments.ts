import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { LabUnitResponseDTO, LabUnitService } from '../../core/services/lab-units/lab-unit-service';
import { ExamResponseDTO, ExamService } from '../../core/services/exams/exam-service';
import { AppointmentCreateDTO, AppointmentDetailDTO, AppointmentExamCreateDTO, AppointmentService, AppointmentSummaryDTO } from '../../core/services/appointment/appointment-service';

@Component({
  selector: 'app-appointments',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './appointments.html',
  styleUrls: ['./appointments.css'],
})
export class Appointments implements OnInit {
  appointmentForm: FormGroup;

  labUnits: LabUnitResponseDTO[] = [];
  exams: ExamResponseDTO[] = [];

  examsToAdd: AppointmentExamCreateDTO[] = [];
  selectedExamId: number | null = null;
  selectedExamNote: string = '';

  appointments: AppointmentSummaryDTO[] = [];
  loading = false;
  saving = false;
  errorMessage = '';

  currentUserId!: number;

  showDetailModal = false;
  appointmentDetail: AppointmentDetailDTO | null = null;

  showCancelModal = false;
  appointmentToCancel: AppointmentSummaryDTO | null = null;
  cancelReason = '';

  constructor(
    private fb: FormBuilder,
    private appointmentService: AppointmentService,
    private examService: ExamService,
    private labUnitService: LabUnitService
  ) {
    this.appointmentForm = this.fb.group({
      labUnitId: [null, Validators.required],
      scheduledAt: [null, Validators.required],
      notes: [''],
    });
  }

  ngOnInit(): void {
    const userData = localStorage.getItem('user');
    if (!userData) {
      this.errorMessage = 'Usuário não encontrado. Faça login novamente.';
      return;
    }
    const user = JSON.parse(userData);
    this.currentUserId = user.id;

    this.loadLabUnits();
    this.loadExams();
    this.loadAppointments();
  }

  loadLabUnits(): void {
    this.labUnitService.list().subscribe({
      next: (data) => (this.labUnits = data),
      error: () => (this.errorMessage = 'Erro ao carregar unidades.'),
    });
  }

  loadExams(): void {
    this.examService.list().subscribe({
      next: (data) => (this.exams = data),
      error: () => (this.errorMessage = 'Erro ao carregar exames.'),
    });
  }

  loadAppointments(): void {
    this.loading = true;
    this.appointmentService.listMyAppointments(this.currentUserId).subscribe({
      next: (data) => {
        this.appointments = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar agendamentos.';
        this.loading = false;
      },
    });
  }

  get examsArray(): FormArray {
    return this.appointmentForm.get('exams') as FormArray;
  }

  addExam(): void {
    if (!this.selectedExamId) {
      return;
    }

    const alreadyAdded = this.examsToAdd.some(
      (e) => e.examId === this.selectedExamId
    );
    if (alreadyAdded) {
      return;
    }

    this.examsToAdd.push({
      examId: this.selectedExamId,
      note: this.selectedExamNote || undefined,
    });

    this.selectedExamId = null;
    this.selectedExamNote = '';
  }

  removeExam(index: number): void {
    this.examsToAdd.splice(index, 1);
  }

  submit(): void {
    if (this.appointmentForm.invalid || this.examsToAdd.length === 0) {
      if (this.examsToAdd.length === 0) {
        this.errorMessage = 'Adicione pelo menos um exame ao agendamento.';
      }
      this.appointmentForm.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.errorMessage = '';

    const { labUnitId, scheduledAt, notes } = this.appointmentForm.value;

    const payload: AppointmentCreateDTO = {
      labUnitId,
      scheduledAt,
      notes,
      exams: this.examsToAdd, // 👈 vem do array
    };

    this.appointmentService.create(this.currentUserId, payload).subscribe({
      next: () => {
        this.saving = false;
        this.resetForm();
        this.loadAppointments();
      },
      error: () => {
        this.errorMessage = 'Erro ao criar agendamento.';
        this.saving = false;
      },
    });
  }

  resetForm(): void {
    this.appointmentForm.reset({
      labUnitId: null,
      scheduledAt: null,
      notes: '',
    });
    this.examsToAdd = [];
  }

  openDetail(id: number): void {
    this.appointmentService.getDetail(id, this.currentUserId).subscribe({
      next: (detail) => {
        this.appointmentDetail = detail;
        this.showDetailModal = true;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar detalhes do agendamento.';
      },
    });
  }

  closeDetailModal(): void {
    this.showDetailModal = false;
    this.appointmentDetail = null;
  }

  openCancelModal(appointment: AppointmentSummaryDTO): void {
    this.appointmentToCancel = appointment;
    this.cancelReason = '';
    this.showCancelModal = true;
  }

  closeCancelModal(): void {
    this.showCancelModal = false;
    this.appointmentToCancel = null;
    this.cancelReason = '';
  }

  confirmCancel(): void {
    if (!this.appointmentToCancel) return;

    this.appointmentService
      .cancel(this.appointmentToCancel.id, this.currentUserId, this.cancelReason)
      .subscribe({
        next: () => {
          this.closeCancelModal();
          this.loadAppointments();
        },
        error: () => {
          this.errorMessage = 'Erro ao cancelar agendamento.';
          this.closeCancelModal();
        },
      });
  }

  getExamName(examId: number | string | null | undefined): string {
    if (examId == null) return '';

    const id = Number(examId);
    const exam = this.exams.find(e => e.id === id);

    return exam ? `${exam.code} - ${exam.name}` : `Exame #${id}`;
  }

}

import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ExamCreateUpdateDTO, ExamResponseDTO, ExamService } from '../../core/services/exams/exam-service';

@Component({
  selector: 'app-exams',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './exams.html',
  styleUrl: './exams.css',
})
export class Exams implements OnInit {
  examForm: FormGroup;
  exams: ExamResponseDTO[] = [];
  loading = false;
  saving = false;
  editingId: number | null = null;
  errorMessage = '';
  showDeleteModal = false;
  examToDelete: ExamResponseDTO | null = null;

  constructor(
    private fb: FormBuilder,
    private examService: ExamService
  ) {
    this.examForm = this.fb.group({
      code: ['', Validators.required],
      name: ['', Validators.required],
      description: [''],
      deliverTimeInDays: [0, [Validators.min(0)]],
      instructions: [''],
      price: [0, [Validators.min(0)]],
    });
  }

  ngOnInit(): void {
    this.loadExams();
  }

  loadExams(): void {
    this.loading = true;
    this.examService.list().subscribe({
      next: (data) => {
        this.exams = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar exames.';
        this.loading = false;
      },
    });
  }

  submit(): void {
    if (this.examForm.invalid) {
      this.examForm.markAllAsTouched();
      return;
    }

    this.saving = true;
    const payload: ExamCreateUpdateDTO = this.examForm.value;

    const request$ = this.editingId
      ? this.examService.update(this.editingId, payload)
      : this.examService.create(payload);

    request$.subscribe({
      next: () => {
        this.saving = false;
        this.resetForm();
        this.loadExams();
      },
      error: () => {
        this.errorMessage = 'Erro ao salvar exame.';
        this.saving = false;
      },
    });
  }

  edit(exam: ExamResponseDTO): void {
    this.editingId = exam.id;
    this.examForm.patchValue({
      code: exam.code,
      name: exam.name,
      description: exam.description,
      deliverTimeInDays: exam.deliverTimeInDays,
      instructions: exam.instructions,
      price: exam.price,
    });
  }

  openDeleteModal(exam: ExamResponseDTO): void {
      this.examToDelete = exam;
      this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.examToDelete = null;
  }

  confirmDelete(): void {
    if (!this.examToDelete) return;

    const id = this.examToDelete.id;

    this.examService.delete(id).subscribe({
      next: () => {
        this.closeDeleteModal();
        this.loadExams();
      },
      error: () => {
        this.errorMessage = 'Erro ao excluir exame.';
        this.closeDeleteModal();
      },
    });
  }

  resetForm(): void {
    this.editingId = null;
    this.examForm.reset({
      code: '',
      name: '',
      description: '',
      deliverTimeInDays: 0,
      instructions: '',
      price: 0,
    });
  }
}

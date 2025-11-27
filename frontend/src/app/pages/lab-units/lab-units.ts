import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LabUnitCreateUpdateDTO, LabUnitResponseDTO, LabUnitService } from '../../core/services/lab-units/lab-unit-service';

@Component({
  selector: 'app-lab-units',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './lab-units.html',
  styleUrl: './lab-units.css',
})
export class LabUnits implements OnInit {
  labUnitForm: FormGroup;
  labUnits: LabUnitResponseDTO[] = [];
  loading = false;
  saving = false;
  editingId: number | null = null;
  errorMessage = '';

  showDeleteModal = false;
  labUnitToDelete: LabUnitResponseDTO | null = null;

  constructor(
    private fb: FormBuilder,
    private labUnitService: LabUnitService
  ) {
    this.labUnitForm = this.fb.group({
      name: ['', Validators.required],
      address: [''],
      phone: [''],
      openingHours: [''],
    });
  }

  ngOnInit(): void {
    this.loadLabUnits();
  }

  loadLabUnits(): void {
    this.loading = true;
    this.labUnitService.list().subscribe({
      next: (data) => {
        this.labUnits = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar unidades.';
        this.loading = false;
      },
    });
  }

  submit(): void {
    if (this.labUnitForm.invalid) {
      this.labUnitForm.markAllAsTouched();
      return;
    }

    this.saving = true;
    const payload: LabUnitCreateUpdateDTO = this.labUnitForm.value;

    const request$ = this.editingId
      ? this.labUnitService.update(this.editingId, payload)
      : this.labUnitService.create(payload);

    request$.subscribe({
      next: () => {
        this.saving = false;
        this.resetForm();
        this.loadLabUnits();
      },
      error: () => {
        this.errorMessage = 'Erro ao salvar unidade.';
        this.saving = false;
      },
    });
  }

  edit(unit: LabUnitResponseDTO): void {
    this.editingId = unit.id;
    this.labUnitForm.patchValue({
      name: unit.name,
      address: unit.address,
      phone: unit.phone,
      openingHours: unit.openingHours,
    });
  }

  openDeleteModal(unit: LabUnitResponseDTO): void {
    this.labUnitToDelete = unit;
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.labUnitToDelete = null;
  }

  confirmDelete(): void {
    if (!this.labUnitToDelete) return;

    const id = this.labUnitToDelete.id;

    this.labUnitService.delete(id).subscribe({
      next: () => {
        this.closeDeleteModal();
        this.loadLabUnits();
      },
      error: () => {
        this.errorMessage = 'Erro ao excluir unidade.';
        this.closeDeleteModal();
      },
    });
  }

  resetForm(): void {
    this.editingId = null;
    this.labUnitForm.reset({
      name: '',
      address: '',
      phone: '',
      openingHours: '',
    });
  }
}

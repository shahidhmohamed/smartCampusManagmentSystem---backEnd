import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { MarkingStatus } from 'app/entities/enumerations/marking-status.model';
import { AssignmentFileService } from '../service/assignment-file.service';
import { IAssignmentFile } from '../assignment-file.model';
import { AssignmentFileFormGroup, AssignmentFileFormService } from './assignment-file-form.service';

@Component({
  selector: 'jhi-assignment-file-update',
  templateUrl: './assignment-file-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssignmentFileUpdateComponent implements OnInit {
  isSaving = false;
  assignmentFile: IAssignmentFile | null = null;
  markingStatusValues = Object.keys(MarkingStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected assignmentFileService = inject(AssignmentFileService);
  protected assignmentFileFormService = inject(AssignmentFileFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssignmentFileFormGroup = this.assignmentFileFormService.createAssignmentFileFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assignmentFile }) => {
      this.assignmentFile = assignmentFile;
      if (assignmentFile) {
        this.updateForm(assignmentFile);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assignmentFile = this.assignmentFileFormService.getAssignmentFile(this.editForm);
    if (assignmentFile.id !== null) {
      this.subscribeToSaveResponse(this.assignmentFileService.update(assignmentFile));
    } else {
      this.subscribeToSaveResponse(this.assignmentFileService.create(assignmentFile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssignmentFile>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(assignmentFile: IAssignmentFile): void {
    this.assignmentFile = assignmentFile;
    this.assignmentFileFormService.resetForm(this.editForm, assignmentFile);
  }
}

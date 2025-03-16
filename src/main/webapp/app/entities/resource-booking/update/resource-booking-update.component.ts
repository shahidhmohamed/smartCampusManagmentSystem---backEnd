import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';
import { Status } from 'app/entities/enumerations/status.model';
import { ResourceBookingService } from '../service/resource-booking.service';
import { IResourceBooking } from '../resource-booking.model';
import { ResourceBookingFormGroup, ResourceBookingFormService } from './resource-booking-form.service';

@Component({
  selector: 'jhi-resource-booking-update',
  templateUrl: './resource-booking-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResourceBookingUpdateComponent implements OnInit {
  isSaving = false;
  resourceBooking: IResourceBooking | null = null;
  statusValues = Object.keys(Status);

  resourcesSharedCollection: IResource[] = [];

  protected resourceBookingService = inject(ResourceBookingService);
  protected resourceBookingFormService = inject(ResourceBookingFormService);
  protected resourceService = inject(ResourceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResourceBookingFormGroup = this.resourceBookingFormService.createResourceBookingFormGroup();

  compareResource = (o1: IResource | null, o2: IResource | null): boolean => this.resourceService.compareResource(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourceBooking }) => {
      this.resourceBooking = resourceBooking;
      if (resourceBooking) {
        this.updateForm(resourceBooking);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resourceBooking = this.resourceBookingFormService.getResourceBooking(this.editForm);
    if (resourceBooking.id !== null) {
      this.subscribeToSaveResponse(this.resourceBookingService.update(resourceBooking));
    } else {
      this.subscribeToSaveResponse(this.resourceBookingService.create(resourceBooking));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceBooking>>): void {
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

  protected updateForm(resourceBooking: IResourceBooking): void {
    this.resourceBooking = resourceBooking;
    this.resourceBookingFormService.resetForm(this.editForm, resourceBooking);

    this.resourcesSharedCollection = this.resourceService.addResourceToCollectionIfMissing<IResource>(
      this.resourcesSharedCollection,
      resourceBooking.resource,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resourceService
      .query()
      .pipe(map((res: HttpResponse<IResource[]>) => res.body ?? []))
      .pipe(
        map((resources: IResource[]) =>
          this.resourceService.addResourceToCollectionIfMissing<IResource>(resources, this.resourceBooking?.resource),
        ),
      )
      .subscribe((resources: IResource[]) => (this.resourcesSharedCollection = resources));
  }
}

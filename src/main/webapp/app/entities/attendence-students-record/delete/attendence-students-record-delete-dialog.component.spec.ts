jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AttendenceStudentsRecordService } from '../service/attendence-students-record.service';

import { AttendenceStudentsRecordDeleteDialogComponent } from './attendence-students-record-delete-dialog.component';

describe('AttendenceStudentsRecord Management Delete Component', () => {
  let comp: AttendenceStudentsRecordDeleteDialogComponent;
  let fixture: ComponentFixture<AttendenceStudentsRecordDeleteDialogComponent>;
  let service: AttendenceStudentsRecordService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AttendenceStudentsRecordDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(AttendenceStudentsRecordDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AttendenceStudentsRecordDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AttendenceStudentsRecordService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});

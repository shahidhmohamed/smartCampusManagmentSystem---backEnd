import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'campus-event',
    data: { pageTitle: 'CampusEvents' },
    loadChildren: () => import('./campus-event/campus-event.routes'),
  },
  {
    path: 'resource',
    data: { pageTitle: 'Resources' },
    loadChildren: () => import('./resource/resource.routes'),
  },
  {
    path: 'resource-booking',
    data: { pageTitle: 'ResourceBookings' },
    loadChildren: () => import('./resource-booking/resource-booking.routes'),
  },
  {
    path: 'file',
    data: { pageTitle: 'Files' },
    loadChildren: () => import('./file/file.routes'),
  },
  {
    path: 'folder',
    data: { pageTitle: 'Folders' },
    loadChildren: () => import('./folder/folder.routes'),
  },
  {
    path: 'course',
    data: { pageTitle: 'Courses' },
    loadChildren: () => import('./course/course.routes'),
  },
  {
    path: 'course-registration',
    data: { pageTitle: 'CourseRegistrations' },
    loadChildren: () => import('./course-registration/course-registration.routes'),
  },
  {
    path: 'module',
    data: { pageTitle: 'Modules' },
    loadChildren: () => import('./module/module.routes'),
  },
  {
    path: 'class-schedule',
    data: { pageTitle: 'ClassSchedules' },
    loadChildren: () => import('./class-schedule/class-schedule.routes'),
  },
  {
    path: 'assignment',
    data: { pageTitle: 'Assignments' },
    loadChildren: () => import('./assignment/assignment.routes'),
  },
  {
    path: 'assignment-file',
    data: { pageTitle: 'AssignmentFiles' },
    loadChildren: () => import('./assignment-file/assignment-file.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

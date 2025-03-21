export interface ICourseRegistration {
  id: string;
  studentId?: string | null;
  courseId?: string | null;
  courseCode?: string | null;
  duration?: string | null;
  registrationDate?: string | null;
}

export type NewCourseRegistration = Omit<ICourseRegistration, 'id'> & { id: null };

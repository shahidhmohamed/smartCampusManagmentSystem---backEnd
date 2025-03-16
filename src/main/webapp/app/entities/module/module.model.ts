export interface IModule {
  id: string;
  moduleName?: string | null;
  moduleCode?: string | null;
  courseId?: string | null;
  semester?: string | null;
  lecturerId?: string | null;
  duration?: string | null;
}

export type NewModule = Omit<IModule, 'id'> & { id: null };

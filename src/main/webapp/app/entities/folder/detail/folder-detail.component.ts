import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFolder } from '../folder.model';

@Component({
  selector: 'jhi-folder-detail',
  templateUrl: './folder-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FolderDetailComponent {
  folder = input<IFolder | null>(null);

  previousState(): void {
    window.history.back();
  }
}

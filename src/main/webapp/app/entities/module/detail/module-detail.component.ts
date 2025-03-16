import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IModule } from '../module.model';

@Component({
  selector: 'jhi-module-detail',
  templateUrl: './module-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ModuleDetailComponent {
  module = input<IModule | null>(null);

  previousState(): void {
    window.history.back();
  }
}

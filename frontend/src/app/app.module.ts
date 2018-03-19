import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { MatFormFieldModule } from '@angular/material/form-field';
import {
  MatInputModule,
  MatButtonModule,
  MatSelectModule,
  MatIconModule,
  MatListModule
} from '@angular/material';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';

import { AppComponent } from './app.component';
import { AppService } from './services/app.service';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatSelectModule,
    MatSnackBarModule,
    MatTableModule
  ],
  providers: [AppService],
  bootstrap: [AppComponent]
})
export class AppModule { }

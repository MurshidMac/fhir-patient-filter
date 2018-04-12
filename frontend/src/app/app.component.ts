import { Component, ViewChild } from '@angular/core';
import { AppService } from './services/app.service';

import { MatTableDataSource } from '@angular/material';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Patient } from './models/patient';
import { Gender } from './models/gender.enum';
import { Encounter } from './models/encounter';
import { Observation } from './models/observation';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  @ViewChild('table') table;
  _patients: Patient[];
  dataSource: MatTableDataSource<Patient>;
  filterVals = {
    name: 'Name',
    active: 'Active',
    gender: 'Gender',
    birthDate: 'Birth date',
    deceased: 'Deceased',
    encounters: 'Encounters',
    deceasedOn: 'Deceased on',
    observations: 'Observations'
  };
  displayedColumns = Object.keys(this.filterVals);
  // displayedColumns = ['name', 'active', 'gender', 'birthDate', 'deceased', 'deceasedOn', 'fullId']
  currentFilter: string;
  genders = Object.keys(Gender).filter(g => isNaN(Number.parseInt(g)));

  constructor(
    private appService: AppService,
    private snackBar: MatSnackBar
  ) {
    this.getPatients()
  }

  getPatients(search?: string) {
    this.appService.getPatients(this.currentFilter, search).subscribe(
      data => {
        this.patients = data;
        this.getEncounters();
      },
      err => this.snackBar.open(err, '', {
        duration: 3000
      })
    );
    if (this.table != null) this.table.renderRows();
  }

  getEncounters() {
    this.patients.map(patient => {
      this.appService.getEncounters(patient.id)
        .subscribe(
          data => {
            patient.encounters = data;
          },
          err => this.snackBar.open(err, '', {
            duration: 3000
          })
        )
    })
  }

  getObservations(encounter: Encounter, element) {
    this.appService.getObservations(encounter.id)
      .subscribe(
        data => {
          element.observations = data;
          this.table.renderRows();
        },
        err => this.snackBar.open(err, '', {
          duration: 3000
        })
      )
  }

  populateDB() {
    this.appService.populate().subscribe(
      data => {
        this.getPatients();
        this.snackBar.open('Successfully updated!', '', {
          duration: 3000
        })
      },
      err => {
        this.snackBar.open(err, '', {
          duration: 3000
        })
      }
    )
  }

  get patients(): Patient[] {
    return this._patients;
  }

  set patients(value: Patient[]) {
    this._patients = value;
    this.dataSource = new MatTableDataSource(value);
  }

}

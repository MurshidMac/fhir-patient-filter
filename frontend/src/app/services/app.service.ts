import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { map } from 'rxjs/operators'

import { Patient } from '../models/patient';
import { Encounter } from '../models/encounter';
import { Observation } from '../models/observation';

@Injectable()
export class AppService {

  constructor(
    private http: HttpClient
  ) { }

  getPatients(filter: string, value: string): Observable<Patient[]> {
    let url = '/api/patients';
    if (filter != null && value != null && filter.trim() != '' && value.trim() != '') {
      url = `${url}/search/${filter}?q=${value}`
    }
    return this.http.get<Patient[]>(url).pipe(map(
      patients => patients['_embedded']['patients']
    ))
  }

  getEncounters(url: string): Observable<Encounter[]> {
    return this.http.get<Encounter[]>(url)
  }

  getObservations(url: string): Observable<Observation[]> {
    return this.http.get<Observation[]>(url)
  }

  populate(): Observable<any> {
    return this.http.get('/api/core/restock')
  }

}

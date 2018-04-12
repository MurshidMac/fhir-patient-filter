import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  headers = new HttpHeaders().append('Accept', 'text/event-stream');

  getPatients(filter: string, value: string): Observable<Patient[]> {
    let url = '/api/patients';
    if (filter != null && value != null && filter.trim() != '' && value.trim() != '') {
      url = `${url}/search/${filter}?q=${value}`
    }
    return this.http.get<Patient[]>(url, { headers: this.headers })
  }

  getEncounters(patientId: string): Observable<Encounter[]> {
    let url = `/api/encounters/patient/${patientId}`
    return this.http.get<Encounter[]>(url, { headers: this.headers })
  }

  getObservations(encounterId: string): Observable<Observation[]> {
    let url = `/api/observations/encounter/${encounterId}`
    return this.http.get<Observation[]>(url, { headers: this.headers })
  }

  populate(): Observable<any> {
    return this.http.get('/api/core/restock', { headers: this.headers })
  }

}

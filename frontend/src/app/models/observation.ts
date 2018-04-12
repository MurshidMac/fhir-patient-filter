import { Encounter } from './encounter';
import { Patient } from './patient';

export interface Observation {
  id: string
  fullId: string
  status: string
  effectiveStartPeriod: Date
  encounter: Encounter
  patient: Patient
}

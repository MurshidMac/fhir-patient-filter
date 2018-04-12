import { Observation } from './observation';
import { Patient } from './patient';

export interface Encounter {
  id: string
  fullId: string
  status: string
  type: string
  startPeriod: Date
  reason?: string
  patient: Patient
  observations?: Observation[]
}

import { Encounter } from './encounter';

export interface Observation {
  id: string
  fullId: string
  status: string
  effectiveStartPeriod: Date
  encounter: Encounter
}

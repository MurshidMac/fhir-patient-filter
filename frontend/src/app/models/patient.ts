import { Encounter } from './encounter';
import { Gender } from './gender.enum';

export interface Patient {
  fullId: string
  name?: string
  active: boolean
  gender?: Gender
  birthDate?: Date
  deceased: boolean
  deceasedOn?: Date
  encounters: Encounter[]
  _links: {
    patient: {
      href: string
    }
    encounters: {
      href: string
    }
  }
}

import { Encounter } from './encounter';
import { Gender } from './gender.enum';

export interface Patient {
  id: string
  fullId: string
  name?: string
  active: boolean
  gender?: Gender
  birthDate?: Date
  deceased: boolean
  deceasedOn?: Date
  encounters?: Encounter[]
}

import {Event, EventDate, Note, Photo} from "../../types";
import {List} from "immutable";
import axios from "axios";


export interface EventsHttpApi {
    readonly fetchEvents: () => Promise<List<Event>>
    readonly addNote: (note: string, date: EventDate) => Promise<any>
    readonly addPhoto: (description: string, photo: File, date: EventDate) => Promise<any>
}

export interface PhotoResponse {
    readonly date: string;
    readonly id: string;
    readonly properties: {
        readonly description: string,
        readonly photo: string
    }
}

export interface NoteResponse {
    readonly date: string;
    readonly id: string;
    readonly properties: {
        readonly note: string
    }
}

export type EventResponse = PhotoResponse | NoteResponse

const Api: EventsHttpApi = {
    fetchEvents: () => {
        return axios.get<[EventResponse]>(`http://localhost:8080/events`)
            .then(res => {
                    return List(res.data)
                        .map((responseEvent: EventResponse) => {
                                if ('note' in responseEvent.properties) {
                                    return new Note(
                                        responseEvent.id,
                                        EventDate.ofZoneDateAndTime(responseEvent.date),
                                        responseEvent.properties.note
                                    )
                                }
                                if ('description' in responseEvent.properties && 'photo' in responseEvent.properties) {
                                    return new Photo(
                                        responseEvent.id,
                                        EventDate.ofZoneDateAndTime(responseEvent.date),
                                        responseEvent.properties.description,
                                        responseEvent.properties.photo
                                    )
                                }
                                return Object()
                            }
                        );
                }
            )
    },
    addNote:
        (note: string, date: EventDate) => {
            return axios.post(`http://localhost:8080/note`, {
                "payload": {
                    "note": note
                },
                "date": date.toISODateTime()
            })
                .then(res => res.data)
        },
    addPhoto:
        (description: string, photo: File, date: EventDate) => {
            const formData: FormData = new FormData()
            formData.append("date", date.toISODateTime())
            formData.append("payload.description", description)
            formData.append("payload.photo", photo)
            return axios.post(`http://localhost:8080/photo`, formData)
                .then(res => res.data)
        }
}

export {Api as EventsApi}
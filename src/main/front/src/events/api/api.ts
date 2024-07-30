import {Event, EventDate} from "../../types";
import {List} from "immutable";
import axios from "axios";


export interface EventsHttpApi {
    readonly fetchEvents: () => Promise<List<Event>>
    readonly addEvent: (note: string, date: EventDate) => Promise<any>
    readonly addPhoto: (description: string, photo: File, date: EventDate) => Promise<any>
}

const Api: EventsHttpApi = {
    fetchEvents: () => {
        return axios.get<[{
            note: string,
            date: string,
            id: string
        }]>(`http://localhost:8080/events`)
            .then(res => List(res.data)
                .map((responseEvent: {
                    note: string,
                    date: string,
                    id: string
                }) => {
                    return {
                        note: responseEvent.note,
                        date: EventDate.ofZoneDateAndTime(responseEvent.date),
                        id: responseEvent.id
                    }
                })
            )
    },
    addEvent: (note: string, date: EventDate) => {
        return axios.post(`http://localhost:8080/event`, {
            "payload": {
                "note": note
            },
            "date": date.toISODateTime()
        })
            .then(res => res.data)
    },
    addPhoto: (description: string, photo: File, date: EventDate) => {
        const formData: FormData = new FormData()
        formData.append("date", date.toISODateTime())
        formData.append("payload.description", description)
        formData.append("payload.photo", photo)
        // TODO mateusz.brycki  czy to się uda
        return axios.post(`http://localhost:8080/photo`, formData)
            .then(res => res.data)
    }
}

export {Api as EventsApi}
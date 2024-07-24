import {Event} from "../../types";
import {List} from "immutable";
import axios from "axios";


export interface EventsHttpApi {
    readonly fetchEvents: () => Promise<List<Event>>
    readonly addEvent: (note: string, date: string) => Promise<any>
}

const Api: EventsHttpApi = {
    fetchEvents: () => {
        return axios.get(`http://localhost:8080/events`)
            .then(res => res.data)
    },
    addEvent: (note: string, date: string) => {
        return axios.post(`http://localhost:8080/event`, {"note": note, "date": date})
            .then(res => res.data)
    }
}

export {Api as EventsApi}
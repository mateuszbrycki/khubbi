import {Event} from "../../types";
import {List} from "immutable";
import axios from "axios";


export interface EventsHttpApi {
    readonly fetchEvents: () => Promise<List<Event>>
}

const Api: EventsHttpApi = {
    fetchEvents: () => {
        return axios.get(`http://localhost:8080/events`)
            .then(res => res.data)
    }
}

export {Api as EventsApi}
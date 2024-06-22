import {Shelf} from "../../types";
import {List} from "immutable";
import axios from "axios";


export interface ShelvesHttpApi {
    readonly fetchShelves: () => Promise<List<Shelf>>
}

const Api: ShelvesHttpApi = {
    fetchShelves: () => {
        return axios.get(`http://localhost:8080/shelves`)
            .then(res => res.data)
    }
}

export {Api as ShelvesApi}
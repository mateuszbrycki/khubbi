import {Shelf} from "../../types";
import {List} from "immutable";


export interface ShelvesHttpApi {
    readonly getShelves: () => List<Shelf>
}

const Api: ShelvesHttpApi = {
    getShelves: () => List.of()
}

export {Api as ShelvesApi}
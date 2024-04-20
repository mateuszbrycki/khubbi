import {Action} from "redux";
import {ShelvesHttpApi} from "../api/api";
import {put, takeEvery, select} from '@redux-saga/core/effects'
import {List} from "immutable";

function* onLoadShelves(api: ShelvesHttpApi): IterableIterator<unknown> {
/*     yield takeEvery((a: Action): a is LoadRides => a.type === Types.LoadRides, function* () {
        const rides: List<Ride> = yield api.getRides()
        yield put(RidesLoadedAction(rides))

        const rideTypes = rides.flatMap(ride => ride.rideTypes).toSet()

        const cities = rides.map(ride => ride.city).toSet().sort().map(city => {
            return {name: city, id: city, key: city}
        })

        const times = rides.map(ride => ride.time).toSet().sort((timeA, timeB) => timeA.compareTo(timeB)).map(time => time.format(TIME_FORMATTER)).map(time => {
            return {name: time, id: time, key: time}
        })

        const days = rides.map(ride => ride.day).toSet().sort((dayA, dayB) => dayA.number > dayB.number ? 1 : -1).map(day => {
            return {name: day.name, id: day.number, key: day.name}
        })

        yield put(FilterConfigLoadedAction(rideTypes, cities, times, days))
    }) */

}

export {
    onLoadShelves
}
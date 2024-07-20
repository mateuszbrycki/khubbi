import React from "react";
import {Event} from "../../types";
import {List} from "immutable";

export interface DashboardProps {
    readonly events: List<Event>
}

export interface DashboardActionProps {
    readonly loadEvents: () => void
}

const Dashboard: React.FC<DashboardProps & DashboardActionProps> = (props) => {
    const {events, loadEvents} = props

    React.useEffect((): void => {
        loadEvents()
    }, [loadEvents])


    return <>
        <h1>Events</h1>
        {events.map(event => <li key={event.name}>{event.name}</li>)}
    </>
}

export default Dashboard
import {List} from "immutable";
import {Event} from "../../types";
import React from "react";
import {Row} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import EventEntry from "./EventEntry";

export interface EventsTimelineProps {
    readonly events: List<Event>
}

export interface EventsTimelineActionProps {
    readonly loadEvents: () => void
}

const EventsTimeline: React.FC<EventsTimelineProps & EventsTimelineActionProps> = (props) => {
    const {events, loadEvents} = props

    React.useEffect((): void => {
        loadEvents()
    }, [loadEvents])

    return (<Container className="justify-content-left">
        <Row>
            <h1>Events</h1>
        </Row>
        <Row>
            {events ? events.map(event => <EventEntry event={event}/>) : <></>}
        </Row>
    </Container>)

}

export default EventsTimeline
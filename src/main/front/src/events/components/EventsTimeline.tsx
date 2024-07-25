import {List} from "immutable";
import {Event} from "../../types";
import React from "react";
import {Row} from "react-bootstrap";
import Container from "react-bootstrap/Container";

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
            {events ? events.map(event => <li
                key={event.id}>{event.note} ({event.date.format("dd-MM-yyyy HH:mm")})</li>) : <></>}
        </Row>
    </Container>)

}

export default EventsTimeline
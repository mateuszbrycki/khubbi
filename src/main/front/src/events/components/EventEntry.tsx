import React from "react";
import {Event} from "../../types";
import Card from 'react-bootstrap/Card';

export interface EventEntryProps {
    readonly event: Event

}

export interface EventEntryActionProps {
}

const EventEntry: React.FC<EventEntryProps & EventEntryActionProps> = (props) => {
    const {event} = props

    return <Card className="p-0 mb-3">
        <Card.Body>
            <Card.Text>
                {event.note}
            </Card.Text>
        </Card.Body>
        <Card.Footer>
            {event.date.format("dd-MM-yyyy, HH:mm")}
        </Card.Footer>
    </Card>
}

export default EventEntry
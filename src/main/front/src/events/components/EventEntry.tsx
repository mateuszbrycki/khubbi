import React from "react";
import {Event, Note, Photo} from "../../types";
import Card from 'react-bootstrap/Card';
import * as Icon from "react-bootstrap-icons";
import ImageWithAuth from "../../common/ImageWithAuth";

export interface EventEntryProps {
    readonly event: Event
}

export interface EventEntryActionProps {
}

const EventEntry: React.FC<EventEntryProps & EventEntryActionProps> = (props) => {
    const {event} = props

    const toNote = (note: Note) => <Card className="p-0 mb-3">
        <Card.Header><Icon.FileEarmarkPostFill className="me-2"/></Card.Header>
        <Card.Body>
            <Card.Text>
                {note.note}
            </Card.Text>
        </Card.Body>
        <Card.Footer>
            {note.date.format("dd-MM-yyyy, HH:mm")}
        </Card.Footer>
    </Card>

    const toPhoto = (photo: Photo) => <Card className="p-0 mb-3">
        <Card.Header><Icon.ImageFill className="me-2"/> {photo.photo}</Card.Header>
        <Card.Body>
            <ImageWithAuth url={photo.photo}></ImageWithAuth>
            <Card.Text>
                {photo.description}
            </Card.Text>
        </Card.Body>
        <Card.Footer>
            {photo.date.format("dd-MM-yyyy, HH:mm")}
        </Card.Footer>
    </Card>


    const toCard = (event: Event) => {
        switch (event.constructor) {
            case Note:
                return toNote(event as Note);
            case Photo:
                return toPhoto(event as Photo);
            default:
                return <></>;
        }
    }

    return <>
        {toCard(event)}
    </>
}

export default EventEntry
import {List} from "immutable"
import React from "react";
import Button from "react-bootstrap/Button";

import * as Icon from "react-bootstrap-icons";
import {Col, Row} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import {EventDate, EventForms} from "../../types";
import AddNoteForm from "./AddNoteForm";
import AddPhotoForm from "./AddPhotoForm";

export interface EventsManagementProps {
    readonly showEventForm: EventForms | null
}

export interface EventsManagementActionProps {
    readonly openAddEventForm: (type: EventForms) => void,
    readonly closeAddEventForm: () => void,
    readonly addNote: (note: string, date: EventDate) => void
    readonly addPhoto: (description: string, photo: File, date: EventDate) => void
}

const EventsManagement: React.FC<EventsManagementProps & EventsManagementActionProps> = (props) => {

    const {showEventForm, openAddEventForm, closeAddEventForm, addNote, addPhoto} = props

    const buttons: List<JSX.Element> = List.of({
        eventForm: EventForms.NOTE,
        icon: <Icon.FileEarmarkPostFill className="me-2"/>
    }, {
        eventForm: EventForms.PICTURE,
        icon: <Icon.ImageFill className="me-2"/>
    }, {
        eventForm: EventForms.EVENT,
        icon: <Icon.CalendarEventFill className="me-2"/>
    }, {
        eventForm: EventForms.EXPENSE,
        icon: <Icon.WalletFill className="me-2"/>
    }, {
        eventForm: EventForms.REMINDER,
        icon: <Icon.StopwatchFill className="me-2"/>
    }).map(form => <Col><Button
        className={`btn-light d-inline m-3 ${showEventForm === form.eventForm ? " active" : ""}`}
        onClick={() => showEventForm === form.eventForm ? closeAddEventForm() : openAddEventForm(form.eventForm)}
        data-testid={`show-add-${form.eventForm.name}-button`}
    >{form.eventForm.name} {form.icon}</Button></Col>)

    const renderForm = (form: EventForms) => {
        switch (form) {
            case EventForms.NOTE:
                return <AddNoteForm
                    addNote={addNote}
                    currentDateProvider={() => EventDate.now()}/>
            case EventForms.PICTURE:
                return <AddPhotoForm
                    data-testid="addphoto-form"
                    addPhoto={addPhoto}
                    currentDateProvider={() => EventDate.now()}/>
            default:
                return <>No form yet</>
        }
    }

    return <>
        <Container className="justify-content-center">
            <Row>
                {buttons}
            </Row>

            <Row className="justify-content-center">
                <div className="col-6">
                    <Row className="justify-content-end mb-3">
                        {showEventForm !== null ?
                            <Icon.XCircle
                                data-testid="close-add-event-form-button"
                                className="col-1"
                                type="submit"
                                onClick={() => closeAddEventForm()}/>
                            : <></>}
                    </Row>
                    <Row>
                        {showEventForm !== null ? renderForm(showEventForm) : <></>}
                    </Row>
                </div>
            </Row>

        </Container>
    </>
}

export default EventsManagement
import React, {useState} from "react";
import {Col, Row} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {EventDate} from "../../types";

export const DATE_FORMAT = "yyyy-MM-dd";
export const TIME_FORMAT = "HH:mm";

export interface AddNoteFormProps {
}

export interface AddNoteFormActionProps {
    readonly addNote: (note: string, date: EventDate) => void
    readonly currentDateProvider: () => EventDate
}

const AddNoteForm: React.FC<AddNoteFormProps & AddNoteFormActionProps> = (props) => {

    const {addNote, currentDateProvider} = props

    const [note, setNote] = useState<string>("")
    const [date, setDate] = useState<string>(currentDateProvider().format(DATE_FORMAT))
    const [time, setTime] = useState<string>(currentDateProvider().format(TIME_FORMAT))

    const resetFormValues = () => {
        setNote("");
        setDate(currentDateProvider().format(DATE_FORMAT));
        setTime(currentDateProvider().format(TIME_FORMAT));
    }

    return <Form onSubmit={event => {
        addNote(note, EventDate.ofDateAndTime(`${date}T${time}`));
        event.preventDefault();
        resetFormValues();
    }}>
        <Row className="mb-3">
            <Form.Group as={Col} controlId="note">
                <Form.Control as="textarea"
                              data-testid="note-textarea"
                              rows={5}
                              value={note}
                              onChange={(event) => setNote(event.target.value)}
                />
            </Form.Group>
        </Row>
        <Row className="mb-3">
            <Form.Group as={Col} controlId="noteAddDate">
                <Form.Control type="date"
                              data-testid="note-add-date"
                              value={date}
                              onChange={(event) => setDate(event.target.value)}
                />

            </Form.Group>
            <Form.Group as={Col} controlId="noteAddTime">
                <Form.Control type="time"
                              data-testid="note-add-time"
                              value={time}
                              onChange={(event) => setTime(event.target.value)}
                />
            </Form.Group>
            <Form.Group as={Col} controlId="submitForm">
                <Button type="submit" data-testid="add-note-button">Add note</Button>
            </Form.Group>
        </Row>
    </Form>

}

export default AddNoteForm

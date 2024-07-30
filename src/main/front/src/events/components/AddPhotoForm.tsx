import React, {useState} from "react";
import {EventDate} from "../../types";
import {Col, Row} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {DATE_FORMAT, TIME_FORMAT} from "./AddNoteForm";
import {DateTimeFormatter, Instant, LocalDateTime, ZonedDateTime, ZoneId, ZoneOffset} from "js-joda";

export interface AddPhotoFormProps {
}

export interface AddPhotoFormActionProps {
    readonly addPhoto: (description: string, photo: File, date: EventDate) => void
    readonly currentDateProvider: () => EventDate
}


const AddPhotoForm: React.FC<AddPhotoFormProps & AddPhotoFormActionProps> = (props) => {

    const {addPhoto, currentDateProvider} = props

    const [description, setDescription] = useState<string>("")
    const [file, setFile] = useState<File | null>(null)
    const [date, setDate] = useState<string>(currentDateProvider().format(DATE_FORMAT))
    const [time, setTime] = useState<string>(currentDateProvider().format(TIME_FORMAT))
    const [overridePhotoDate, setOverridePhotoDate] = useState<boolean>(false)

    const resetFormValues = () => {
        setDescription("");
        setDate(currentDateProvider().format(DATE_FORMAT));
        setTime(currentDateProvider().format(TIME_FORMAT));
        setFile(null);
        setOverridePhotoDate(false);
    }

    return <Form onSubmit={event => {
        if (file) {
            addPhoto(description, file, EventDate.ofDateAndTime(`${date}T${time}`));
            event.preventDefault();
            resetFormValues();
        }
    }}>
        <Row className="mb-3">
            <Form.Group controlId="formFile" className="mb-3">
                <Form.Control type="file"
                              data-testid="photo-file"
                              onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                                  if (event.target.files) {
                                      const file: File = event.target.files[0]
                                      const fileModifiedDate: ZonedDateTime =
                                          ZonedDateTime.ofLocal(
                                              LocalDateTime.ofInstant(
                                                  Instant.ofEpochMilli(file.lastModified)), ZoneId.SYSTEM, null)

                                      setFile(file);
                                      setDate(fileModifiedDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                                      setTime(fileModifiedDate.format(DateTimeFormatter.ofPattern(TIME_FORMAT)))
                                  }
                              }}
                              accept=".png,.jpg,.jpeg,.webp"
                />
            </Form.Group>
        </Row>
        <Row className="mb-3">
            <Form.Group as={Col} controlId="description">
                <Form.Control as="textarea"
                              data-testid="photo-description"
                              rows={5}
                              value={description}
                              placeholder="Photo description"
                              onChange={(event) => setDescription(event.target.value)}
                />
            </Form.Group>
        </Row>
        <Row className="mb-3">
            <Form.Group as={Col} controlId="overridePhotoDate">
                <Form.Check
                    checked={overridePhotoDate}
                    data-testid="photo-overridePhotoDate"
                    type="checkbox"
                    id="overridePhotoDate-checkbox"
                    label="Override photo date"
                    onChange={(event) => setOverridePhotoDate(event.target.checked)}
                />
            </Form.Group>
        </Row>
        <Row className="mb-3">
            <Form.Group as={Col} controlId="photoAddDate">
                <Form.Control type="date"
                              disabled={!overridePhotoDate}
                              data-testid="photo-add-date"
                              value={date}
                              onChange={(event) => setDate(event.target.value)}
                />

            </Form.Group>
            <Form.Group as={Col} controlId="photoAddTime">
                <Form.Control type="time"
                              disabled={!overridePhotoDate}
                              data-testid="photo-add-time"
                              value={time}
                              onChange={(event) => setTime(event.target.value)}
                />
            </Form.Group>

            <Form.Group as={Col} controlId="submitForm">
                <Button type="submit" data-testid="add-photo-button">Add photo</Button>
            </Form.Group>
        </Row>
    </Form>

}

export default AddPhotoForm
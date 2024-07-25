import AddNoteForm, {DATE_FORMAT, TIME_FORMAT} from "../AddNoteForm";
import {act, fireEvent, render} from "@testing-library/react";
import ShallowRenderer from "react-test-renderer/shallow";
import {EventDate} from "../../../types";

const getTodayDate = () => EventDate.now();
const testCurrentDateProvider = () => EventDate.ofDateAndTime("2024-07-12T20:00")

const addNoteMock = jest.fn(() => Promise.resolve());


it('renders form', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<AddNoteForm addNote={addNoteMock} currentDateProvider={testCurrentDateProvider}/>)

    expect(renderer.getRenderOutput()).toMatchSnapshot()
});

test('should call add note', async () => {
    const {getByTestId} = render(<AddNoteForm addNote={addNoteMock} currentDateProvider={testCurrentDateProvider}/>);

    await act(async () => {
        fireEvent.change(getByTestId("note-textarea"), {target: {value: "test-note"}})
        fireEvent.change(getByTestId("note-add-date"), {target: {value: "2023-03-12"}})
        fireEvent.change(getByTestId("note-add-time"), {target: {value: "18:47"}})

        getByTestId("add-note-button").click();
    })

    expect(addNoteMock).toHaveBeenCalledWith("test-note", EventDate.ofDateAndTime("2023-03-12T18:47"));
})

test('should setup today`s date and time', async () => {
    const {getByTestId} = render(<AddNoteForm addNote={addNoteMock} currentDateProvider={getTodayDate}/>);

    expect(getByTestId("note-add-date").getAttribute("value")).toEqual(getTodayDate().format(DATE_FORMAT));
    expect(getByTestId("note-add-time").getAttribute("value")).toEqual(getTodayDate().format(TIME_FORMAT));
})

test('should setup today`s date and time after submit', async () => {
    const {getByTestId} = render(<AddNoteForm addNote={addNoteMock} currentDateProvider={testCurrentDateProvider}/>);

    await act(async () => {
        fireEvent.change(getByTestId("note-textarea"), {target: {value: "test-note"}})
        fireEvent.change(getByTestId("note-add-date"), {target: {value: "2023-03-12"}})
        fireEvent.change(getByTestId("note-add-time"), {target: {value: "18:47"}})

        getByTestId("add-note-button").click();
    })

    expect(addNoteMock).toHaveBeenCalledWith("test-note", EventDate.ofDateAndTime("2023-03-12T18:47"));

    expect(getByTestId("note-add-date").getAttribute("value")).toEqual(testCurrentDateProvider().format(DATE_FORMAT));
    expect(getByTestId("note-add-time").getAttribute("value")).toEqual(testCurrentDateProvider().format(TIME_FORMAT));
})

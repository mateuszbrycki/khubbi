import AddNoteForm from "../AddNoteForm";
import {act, fireEvent, render} from "@testing-library/react";
import ShallowRenderer from "react-test-renderer/shallow";

const getTodayDate = () => new Date().toISOString().substr(0, 10);
const testCurrentDateProvider = () => "2024-07-23"

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

        getByTestId("add-note-button").click();
    })

    expect(addNoteMock).toHaveBeenCalledWith("test-note", "2023-03-12");
})

test('should setup today`s date', async () => {
    const {getByTestId} = render(<AddNoteForm addNote={addNoteMock} currentDateProvider={getTodayDate}/>);

    expect(getByTestId("note-add-date").getAttribute("value")).toEqual(getTodayDate());
})

test('should setup today`s date after submit', async () => {
    const {getByTestId} = render(<AddNoteForm addNote={addNoteMock} currentDateProvider={testCurrentDateProvider}/>);

    await act(async () => {
        fireEvent.change(getByTestId("note-textarea"), {target: {value: "test-note"}})
        fireEvent.change(getByTestId("note-add-date"), {target: {value: "2023-03-12"}})

        getByTestId("add-note-button").click();
    })

    expect(addNoteMock).toHaveBeenCalledWith("test-note", "2023-03-12");

    expect(getByTestId("note-add-date").getAttribute("value")).toEqual(testCurrentDateProvider());
})

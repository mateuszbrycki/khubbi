import ShallowRenderer from "react-test-renderer/shallow";
import AddPhotoForm from "../AddPhotoForm";
import {EventDate} from "../../../types";
import {act, fireEvent, render} from "@testing-library/react";
import {DATE_FORMAT, TIME_FORMAT} from "../AddNoteForm";

const testCurrentDateProvider = () => EventDate.ofDateAndTime("2024-07-12T20:00")
const addPhotoMock = jest.fn(() => Promise.resolve());
const file = new File(["(⌐□_□)"], "image.png", {type: "image/png", lastModified: 1722023600022});

test('should render form', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<AddPhotoForm addPhoto={addPhotoMock} currentDateProvider={testCurrentDateProvider}/>)

    expect(renderer.getRenderOutput()).toMatchSnapshot()
})

test('should call add photo', async () => {
    const {getByTestId} = render(<AddPhotoForm addPhoto={addPhotoMock} currentDateProvider={testCurrentDateProvider}/>)

    await act(async () => {
        fireEvent.change(getByTestId("photo-file"), {target: {files: [file]}})
        fireEvent.change(getByTestId("photo-description"), {target: {value: "test-description"}})

        getByTestId("add-photo-button").click();
    })

    expect(addPhotoMock).toHaveBeenCalledWith("test-description", file, EventDate.ofDateAndTime("2024-07-26T21:53"));

})

test('should call add photo with override date', async () => {
    const {getByTestId} = render(<AddPhotoForm addPhoto={addPhotoMock} currentDateProvider={testCurrentDateProvider}/>)

    await act(async () => {
        fireEvent.change(getByTestId("photo-file"), {target: {files: [file]}})
        fireEvent.change(getByTestId("photo-description"), {target: {value: "test-description"}})
        fireEvent.change(getByTestId("photo-overridePhotoDate"), {target: {checked: true}})
        fireEvent.change(getByTestId("photo-add-date"), {target: {value: "2023-03-12"}})
        fireEvent.change(getByTestId("photo-add-time"), {target: {value: "18:47"}})

        getByTestId("add-photo-button").click();
    })

    expect(addPhotoMock).toHaveBeenCalledWith("test-description", file, EventDate.ofDateAndTime("2023-03-12T18:47"));

})

test('should reset form after submit', async () => {
    const {getByTestId} = render(<AddPhotoForm addPhoto={addPhotoMock} currentDateProvider={testCurrentDateProvider}/>)

    await act(async () => {
        fireEvent.change(getByTestId("photo-file"), {target: {files: [file]}})
        fireEvent.change(getByTestId("photo-description"), {target: {value: "test-description"}})
        fireEvent.change(getByTestId("photo-overridePhotoDate"), {target: {checked: true}})
        fireEvent.change(getByTestId("photo-add-date"), {target: {value: "2023-03-12"}})
        fireEvent.change(getByTestId("photo-add-time"), {target: {value: "18:47"}})

        getByTestId("add-photo-button").click();
    })

    expect(addPhotoMock).toHaveBeenCalledWith("test-description", file, EventDate.ofDateAndTime("2023-03-12T18:47"));

    expect(getByTestId("photo-add-date").getAttribute("value")).toEqual(testCurrentDateProvider().format(DATE_FORMAT));
    expect(getByTestId("photo-add-time").getAttribute("value")).toEqual(testCurrentDateProvider().format(TIME_FORMAT));
})
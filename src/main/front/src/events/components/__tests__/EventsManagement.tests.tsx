import ShallowRenderer from "react-test-renderer/shallow";
import EventsManagement from "../EventsManagement";
import {EventForms} from "../../../types";
import {act, fireEvent, render} from "@testing-library/react";

describe('renders management bar', () => {
    it('without any form open', () => {
        const renderer = ShallowRenderer.createRenderer()
        renderer.render(<EventsManagement
            showEventForm={null}
            openAddEventForm={() => null}
            closeAddEventForm={() => null}
            addNote={() => null}
            addPhoto={() => null}
        />)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    });

    it('with AddNote form open', () => {
        const renderer = ShallowRenderer.createRenderer()
        renderer.render(<EventsManagement
            showEventForm={EventForms.NOTE}
            openAddEventForm={() => null}
            closeAddEventForm={() => null}
            addNote={() => null}
            addPhoto={() => null}
        />)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    });

    it('with AddPhoto form open', () => {
        const renderer = ShallowRenderer.createRenderer()
        renderer.render(<EventsManagement
            showEventForm={EventForms.PICTURE}
            openAddEventForm={() => null}
            closeAddEventForm={() => null}
            addNote={() => null}
            addPhoto={() => null}
        />)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    });
})

test("should open the form after clicking at the button and mark it active", async () => {
    const showEventFormMock = jest.fn();

    const {getByTestId} = render(<EventsManagement
        showEventForm={null}
        openAddEventForm={showEventFormMock}
        closeAddEventForm={() => null}
        addNote={() => null}
        addPhoto={() => null}
    />);

    await act(async () => {
        getByTestId("show-add-Note-button").click();
    })

    expect(showEventFormMock).toHaveBeenCalledWith(EventForms.NOTE);
})

test("button for open form for should have `active` class", async () => {

    const {getByTestId} = render(<EventsManagement
        showEventForm={EventForms.NOTE}
        openAddEventForm={() => null}
        closeAddEventForm={() => null}
        addNote={() => null}
        addPhoto={() => null}
    />);

    expect(getByTestId("show-add-Note-button").getAttribute("class")).toContain("active")

})

test("should close the form after clicking at the button", async () => {
    const closeAddEventFormMock = jest.fn();

    const {getByTestId} = render(<EventsManagement
        showEventForm={EventForms.NOTE}
        openAddEventForm={() => null}
        closeAddEventForm={closeAddEventFormMock}
        addNote={() => null}
        addPhoto={() => null}
    />);

    await act(async () => {
        getByTestId("show-add-Note-button").click();
    })

    expect(closeAddEventFormMock).toHaveBeenCalled();
})
test("should close the form after clicking at the close button", async () => {
    const closeAddEventFormMock = jest.fn();

    const {getByTestId} = render(<EventsManagement
        showEventForm={EventForms.NOTE}
        openAddEventForm={() => null}
        closeAddEventForm={closeAddEventFormMock}
        addNote={() => null}
        addPhoto={() => null}
    />);

    await act(async () => {
        fireEvent.click(getByTestId("close-add-event-form-button"))
    })

    expect(closeAddEventFormMock).toHaveBeenCalled();
})
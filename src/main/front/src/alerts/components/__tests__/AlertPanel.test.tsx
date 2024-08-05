import ShallowRenderer from "react-test-renderer/shallow";
import {AlertPanel} from "../AlertPanel";
import {List} from "immutable";
import {AlertMessage, AlertType} from "../../../types";
import {render} from "@testing-library/react";
import {setTimeout} from 'timers/promises';

describe('Alert Panel', () => {
    const renderer = ShallowRenderer.createRenderer()
    const alerts = List.of(new AlertMessage("id-1", "test-error", AlertType.ERROR),
        new AlertMessage("id-2", "test-success", AlertType.SUCCESS),
        new AlertMessage("id-3", "test-warning", AlertType.WARNING),
    )

    it('renders empty list when no alerts', () => {
        renderer.render(<AlertPanel alerts={List.of()} hideAlert={() => null} alertTimeoutMs={1}/>)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    })

    it('renders alerts', () => {
        renderer.render(<AlertPanel alerts={alerts} hideAlert={() => null} alertTimeoutMs={1}/>)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    })

    it('calls hide alert when closing the toast', async () => {
        const hideAlertMock = jest.fn(() => Promise.resolve());
        render(<AlertPanel alerts={alerts} hideAlert={hideAlertMock} alertTimeoutMs={1}/>)

        await setTimeout(2);

        expect(hideAlertMock).toHaveBeenCalled()

    })

})
import ShallowRenderer from "react-test-renderer/shallow";
import App from "../App";
import {waitFor} from "@testing-library/dom";
import {List} from "immutable";
import {AlertMessage, AlertType} from "../../../types";

describe('application component', () => {
    const renderer = ShallowRenderer.createRenderer()

    const authenticationCheckInterval = 10
    const checkIfUserIsAuthenticated = jest.fn()

    describe('user is authenticated', () => {

        const isAuthenticated = true
        it('renders correctly', () => {
            renderer.render(<App logoutUser={() => null}
                                 isAuthenticated={isAuthenticated}
                                 authenticationCheckInterval={authenticationCheckInterval}
                                 alerts={List.of()}
                                 checkIfUserIsAuthenticated={checkIfUserIsAuthenticated}
                                 hideAlert={() => null}
            />)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        });


        it('checks if user is still authenticated', () => {

            renderer.render(<App logoutUser={() => null}
                                 isAuthenticated={isAuthenticated}
                                 authenticationCheckInterval={authenticationCheckInterval}
                                 alerts={List.of()}
                                 checkIfUserIsAuthenticated={checkIfUserIsAuthenticated}
                                 hideAlert={() => null}
            />)

            waitFor(() => expect(checkIfUserIsAuthenticated).toHaveBeenCalled())

        });
    })
    describe('user is not authenticated', () => {
        const isAuthenticated = false

        it('renders correctly', () => {
            renderer.render(<App logoutUser={() => null}
                                 isAuthenticated={isAuthenticated}
                                 authenticationCheckInterval={authenticationCheckInterval}
                                 alerts={List.of()}
                                 checkIfUserIsAuthenticated={checkIfUserIsAuthenticated}
                                 hideAlert={() => null}
            />)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        });
    })

    describe('renders alerts', () => {
        const isAuthenticated = false


        it('renders correctly', () => {
            renderer.render(<App logoutUser={() => null}
                                 isAuthenticated={isAuthenticated}
                                 authenticationCheckInterval={authenticationCheckInterval}
                                 alerts={List.of(new AlertMessage("id-1", "test-alert-error", AlertType.ERROR),
                                     new AlertMessage("id-2", "test-alert-warning", AlertType.WARNING),
                                     new AlertMessage("id-3", "test-alert-success", AlertType.SUCCESS))}
                                 checkIfUserIsAuthenticated={checkIfUserIsAuthenticated}
                                 hideAlert={() => null}
            />)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        });
    })


})

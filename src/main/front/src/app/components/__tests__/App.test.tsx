import ShallowRenderer from "react-test-renderer/shallow";
import App from "../App";
import {waitFor} from "@testing-library/dom";

describe('application component', () => {
    const renderer = ShallowRenderer.createRenderer()

    const authenticationCheckInterval = 10
    const checkIfUserIsAuthenticated = jest.fn()

    describe('user is authenticated', () => {

        const isAuthenticated = true
        it('renders correctly', () => {
            renderer.render(<App logoutUser={() => null} isAuthenticated={isAuthenticated}
                                 authenticationCheckInterval={authenticationCheckInterval}
                                 checkIfUserIsAuthenticated={checkIfUserIsAuthenticated}/>)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        });


        it('checks if user is still authenticated', () => {

            renderer.render(<App logoutUser={() => null} isAuthenticated={isAuthenticated}
                                 authenticationCheckInterval={authenticationCheckInterval}
                                 checkIfUserIsAuthenticated={checkIfUserIsAuthenticated}/>)

            waitFor(() => expect(checkIfUserIsAuthenticated).toHaveBeenCalled())

        });
    })
    describe('user is not authenticated', () => {
        const isAuthenticated = false

        it('renders correctly', () => {
            renderer.render(<App logoutUser={() => null} isAuthenticated={isAuthenticated}
                                 authenticationCheckInterval={authenticationCheckInterval}
                                 checkIfUserIsAuthenticated={checkIfUserIsAuthenticated}/>)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        });
    })


})

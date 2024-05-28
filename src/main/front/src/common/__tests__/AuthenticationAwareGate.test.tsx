import ShallowRenderer from "react-test-renderer/shallow";
import AuthenticationAwareGate from "../AuthenticationAwareGate";

describe('authentication aware gate', () => {
    describe('when user is authenticated', () => {
        const isAuthenticated = true

        it('renders test child without redirections', () => {
            const renderer = ShallowRenderer.createRenderer()
            renderer.render(<AuthenticationAwareGate children={<h1>test child</h1>}
                                                     isAuthenticated={() => isAuthenticated}/>)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        })

        it('renders redirects to dashboard', () => {
            const renderer = ShallowRenderer.createRenderer()
            renderer.render(<AuthenticationAwareGate children={<h1>test child</h1>}
                                                     isAuthenticated={() => isAuthenticated}
                                                     redirectToIfLogged={"/dashboard"}/>)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        })
    });

    describe('when user is not authenticated', () => {
        const isAuthenticated = false

        it('navigates to login', () => {
            const renderer = ShallowRenderer.createRenderer()
            renderer.render(<AuthenticationAwareGate children={<h1>test child</h1>}
                                                     isAuthenticated={() => isAuthenticated}/>)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        })

        it('navigates to given url', () => {
            const renderer = ShallowRenderer.createRenderer()
            renderer.render(<AuthenticationAwareGate children={<h1>test child</h1>}
                                                     isAuthenticated={() => isAuthenticated}
                                                     redirectToIfNotLogged={"/login-test"}/>)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        })
    });
})

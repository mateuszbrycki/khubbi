import ShallowRenderer from "react-test-renderer/shallow";
import App from "../App";

describe('application component', () => {
    describe('user is authenticated', () => {
        const isAuthenticated = true

        it('renders correctly', () => {
            const renderer = ShallowRenderer.createRenderer()
            renderer.render(<App logoutUser={() => null} isAuthenticated={isAuthenticated}/>)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        });
    })
    describe('user is not authenticated', () => {
        const isAuthenticated = false

        it('renders correctly', () => {
            const renderer = ShallowRenderer.createRenderer()
            renderer.render(<App logoutUser={() => null} isAuthenticated={isAuthenticated}/>)
            expect(renderer.getRenderOutput()).toMatchSnapshot()
        });
    })
})

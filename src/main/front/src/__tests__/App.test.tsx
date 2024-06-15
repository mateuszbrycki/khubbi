import ShallowRenderer from 'react-test-renderer/shallow'; // ES6
import App from "../App";

describe('user not authenticated', () => {
    const isAuthenticated = () => false;

    it('renders correctly', () => {
        const renderer = ShallowRenderer.createRenderer()
        renderer.render(<App isAuthenticated={isAuthenticated}/>)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    });

    it('renders correctly with child component', () => {
        const renderer = ShallowRenderer.createRenderer()
        renderer.render(<App isAuthenticated={isAuthenticated}><h1>Child Component</h1></App>)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    });
})

describe('user authenticated', () => {
    const isAuthenticated = () => true;

    it('renders correctly', () => {
        const renderer = ShallowRenderer.createRenderer()
        renderer.render(<App isAuthenticated={isAuthenticated}/>)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    });

    it('renders correctly with child component', () => {
        const renderer = ShallowRenderer.createRenderer()
        renderer.render(<App isAuthenticated={isAuthenticated}><h1>Child Component</h1></App>)
        expect(renderer.getRenderOutput()).toMatchSnapshot()
    });
})

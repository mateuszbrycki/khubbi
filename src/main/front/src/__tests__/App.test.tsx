import ShallowRenderer from 'react-test-renderer/shallow'; // ES6
import App from "../App";

it('renders correctly', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<App/>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});

it('renders correctly with child component', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<App><h1>Child Component</h1></App>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});
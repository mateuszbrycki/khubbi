import renderer from "react-test-renderer";
import LoginForm from "../LoginForm";

it('renders correctly', () => {
    const tree = renderer
        .create(<LoginForm user={null} loginUser={(email, password) => null}/>)
        .toJSON();
    expect(tree).toMatchSnapshot();
});
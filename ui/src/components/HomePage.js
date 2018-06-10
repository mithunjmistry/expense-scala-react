import React from 'react';
import { Col, Row} from 'react-bootstrap';
import Expenses from "./Expenses";
import Statistics from "./Statistics";

export default class HomePage extends React.Component{

  state = {
      message: false
  };

  handleButtonClick = () => {
      this.setState((prevState) => ({message: !prevState.message}));
  };

  render(){

    const {message} = this.state;

    return (
      <div className={"minimum-height container-fluid"}>
        <Row>
          <Col lg={10} md={10}>
            <Expenses />
          </Col>

          <Col lg={2} md={2} xsHidden mdHidden>
            <Statistics/>
          </Col>
        </Row>
      </div>
    )
  }
}

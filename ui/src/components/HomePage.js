import React from 'react';
import { Grid, Col, Row, Button, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';
import Expenses from "./Expenses";
import Statistics from "./Statistics";

const SelectGroup = ({options, label, ...props}) => (
  <FormGroup>
    <ControlLabel>{label}</ControlLabel>
    <FormControl componentClass="select" {...props}>
      {options.map((v) => (
        <option value={v} key={v}>{v}</option>
      ))}
    </FormControl>
  </FormGroup>
);

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

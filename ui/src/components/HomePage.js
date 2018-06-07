import React from 'react';
import { Grid, Col, Row, Button } from 'react-bootstrap';

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
      <Grid className={"minimum-height"}>
        <Row>
          <Col lg={12} md={12}>
            <p>This is from React</p>
            <Button onClick={this.handleButtonClick}>Click me</Button>
            {message && <p>Button clicked.</p>}
          </Col>
        </Row>
      </Grid>
    )
  }
}

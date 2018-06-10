import React from 'react';
import { Col, Row, Panel} from 'react-bootstrap';

const StatisticsPanel = ({heading, stat}) => (
  <Panel>
    <Panel.Heading>{heading}</Panel.Heading>
    <Panel.Body>{stat}</Panel.Body>
  </Panel>
);

export default class Statistics extends React.Component {

  render() {

    return (
      <div>
        <Row>
          <Col lg={12} md={12}>
            <h4 className={"text-center"}>Statistics</h4>
          </Col>

          <Col lg={12} md={12}>
            <StatisticsPanel heading={"Total"} stat={"$500"}/>
          </Col>

          <Col lg={12} md={12}>
            <StatisticsPanel heading={"Maximum Purchase"} stat={"$500"}/>
          </Col>
        </Row>
      </div>
    )
  }
}

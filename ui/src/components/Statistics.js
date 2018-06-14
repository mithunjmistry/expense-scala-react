import React from 'react';
import { Col, Row, Panel} from 'react-bootstrap';
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";
import {getStatisticsAPI} from "../api/apiURLs";
import axios from "../api/axiosInstance";

const StatisticsPanel = ({heading, stat}) => (
  <Panel>
    <Panel.Heading>{heading}</Panel.Heading>
    <Panel.Body>${stat}</Panel.Body>
  </Panel>
);

class Statistics extends React.Component {

  state = {
    maximum: "",
    total: ""
  };

  getStatistics = (month) => {
    let params = {};
    if(month !== "all"){
      params = {...params, date: month}
    }

    axios.get(getStatisticsAPI, {
      params
    }).then((response) => {
      const data = response.data;
      this.setState(() => ({
        maximum: data[0].toFixed(2),
        total: data[1].toFixed(2)
      }));
    }).catch((error) => {
      this.setState(() => ({
        maximum: "no expenses",
        total: "no expenses"
      }));
    });
  };

  componentDidMount(){
    const {month} = this.props.filter;
    this.getStatistics(month);
  }

  componentWillReceiveProps(nextProps){
    if(this.props.filter.month !== nextProps.filter.month){
      this.getStatistics(nextProps.filter.month);
    }
  }

  render() {

    return (
      <div>
        <Row>
          <Col lg={12} md={12}>
            <h4 className={"text-center"}>Statistics</h4>
          </Col>

          <Col lg={12} md={12}>
            <StatisticsPanel heading={"Total"} stat={this.state.total}/>
          </Col>

          <Col lg={12} md={12}>
            <StatisticsPanel heading={"Maximum Purchase"} stat={this.state.maximum}/>
          </Col>
        </Row>
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    filter: state.filter
  };
};

export default connect(mapStateToProps)(withRouter(Statistics));

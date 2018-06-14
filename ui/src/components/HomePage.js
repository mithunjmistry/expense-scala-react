import React from 'react';
import { Col, Row} from 'react-bootstrap';
import Expenses from "./Expenses";
import Statistics from "./Statistics";
import {getFilterAttributesAPI} from "../api/apiURLs";
import axios from "../api/axiosInstance";

export default class HomePage extends React.Component{

  state = {
      message: false,
      expenseFilterMenu: [],
      monthFilterMenu: []
  };

  handleButtonClick = () => {
      this.setState((prevState) => ({message: !prevState.message}));
  };

  componentDidMount(){
    axios.get(getFilterAttributesAPI).then((response) => {
      const data = response.data;
      const dates = data["dates"];
      const expenseTypes = data["expenseTypes"];
      this.setState((prevState) => ({
        expenseFilterMenu: ["all"].concat(expenseTypes),
        monthFilterMenu: ["all"].concat(dates)
      }));
    }).catch((error) => {
      console.log(error.response);
    });
  }

  render(){

    const {message} = this.state;

    return (
      <div className={"minimum-height container-fluid"}>
        <Row>
          <Col lg={10} md={10}>
            <Expenses expenseTypeFilter={this.state.expenseFilterMenu} dateFilter={this.state.monthFilterMenu}/>
          </Col>

          <Col lg={2} md={2} xsHidden mdHidden>
            <Statistics/>
          </Col>
        </Row>
      </div>
    )
  }
}

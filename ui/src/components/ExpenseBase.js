import React from 'react';
import { Col, Row, Button, Glyphicon, FormGroup, FormControl, ControlLabel, Form, InputGroup} from 'react-bootstrap';
import { withRouter } from 'react-router-dom';
import {ADD_EXPENSE, EDIT_EXPENSE} from "../api/strings";
import DatePicker from 'react-datepicker';
import moment from "moment";
import 'react-datepicker/dist/react-datepicker-cssmodules.css'

const FieldGroup = ({id, label, componentType, ...props}) => (
  <FormGroup controlId={id}>
    <ControlLabel>{label}</ControlLabel>
    <FormControl componentClass={componentType} {...props} />
  </FormGroup>
);

class ExpenseBase extends React.Component {

  state = {
      title: "Expense",
      date: moment(),
      expenseTypes: ["general", "grocery"],
      otherExpenseType: false,
      expenseTypeGlyph: true,
      error: false
  };

  componentDidMount(){
    let action = this.props.match.params.eaction;
    if(action === "add"){
      this.setState(() => ({title: ADD_EXPENSE}));
    }
    else if(action === "edit"){
      let expenseID = this.props.match.params.expenseID;
      this.setState(() => ({title: EDIT_EXPENSE}));
    }
    else{
      this.props.history.push("/");
    }
  }

  changeRoute = (routeURL) => {
    this.props.history.push(routeURL);
  };

  handleChange = (date) => {
    this.setState(() => ({date}))
  };

  toggleOtherExpenseType = () => {
    this.setState((prevState) => ({otherExpenseType: !prevState.otherExpenseType, expenseTypeGlyph: !prevState.expenseTypeGlyph}));
  };

  handleFormSubmit = (e) => {
      e.preventDefault();
      const expenseName = e.target.expenseName.value;
      const description = e.target.description.value;
      const amount = e.target.amount.value;
      const date = this.state.date;
      let expenseType = e.target.expenseType.value;
      if(this.state.otherExpenseType){
        expenseType = e.target.otherExpenseType.value;
      }

      if(expenseName.length > 0 && amount.length > 0){
        this.setState(() => ({error: false}));
      }
      else{
        this.setState(() => ({error: true}));
      }
      console.log(`${expenseName} ${description} ${amount} ${date} ${expenseType}`);
  };

  render() {

    const {title, error} = this.state;

    return (
      <div className={"minimum-height container-fluid"}>
        <Row>
          <Col lgOffset={1} mdOffset={1} lg={10} md={10} xs={12} sm={12}>
            <Button bsStyle={"link"} onClick={() => this.changeRoute("/")}>
              <Glyphicon glyph={"chevron-left"}/> Back
            </Button>
            <h4 className={"gray-underline"}>{title}</h4>
            <Form onSubmit={this.handleFormSubmit}>
              <FieldGroup
                id={"expense-name"}
                label={"expense name"}
                componentType={"input"}
                name={"expenseName"}
              />

              <FieldGroup
                id={"description"}
                label={"description"}
                componentType={"textarea"}
                name={"description"}
              />

              <FieldGroup
                id={"amount"}
                label={"amount (in $)"}
                componentType={"input"}
                name={"amount"}
                type="number"
                step="0.01"
                min={0}
              />

              <Row>
                <Col lg={4} md={4} sm={12} xs={12}>
                  <FormGroup>
                    <ControlLabel>date</ControlLabel>
                    <DatePicker
                      selected={this.state.date}
                      onChange={this.handleChange}
                      className={"form-control"}
                    />
                  </FormGroup>
                </Col>
                <Col lg={4} md={4} sm={12} xs={12}>
                  <FormGroup>
                    <ControlLabel>expense type</ControlLabel>
                    <InputGroup>
                      <FormControl componentClass={"select"} name={"expenseType"}>
                        {this.state.expenseTypes.map((v) => (
                          <option key={v}>{v}</option>
                        ))}
                      </FormControl>
                      <InputGroup.Button>
                        <Button onClick={this.toggleOtherExpenseType}>
                          <Glyphicon glyph={this.state.expenseTypeGlyph ? "plus" : "remove"}/>
                        </Button>
                      </InputGroup.Button>
                    </InputGroup>
                  </FormGroup>
                </Col>
                {this.state.otherExpenseType &&
                <Col lg={4} md={4} sm={12} xs={12}>
                  <FieldGroup
                    id={"other-expense-type"}
                    label={"other expense type"}
                    componentType={"input"}
                    name={"otherExpenseType"}
                  />
                </Col>}

                {title === EDIT_EXPENSE &&
                <Row className={"margin-bottom-one"}>
                  <Col lg={12} md={12}>
                    <Button bsStyle={"link"} className={"red-color"}>
                      <Glyphicon glyph={"minus"}/> delete expense
                    </Button>
                  </Col>
                </Row>}

              </Row>

              {error &&
              <Row>
                <Col lg={12} md={12} sm={12} xs={12}>
                  <p className={"red-color"}>Expense Name and Amount are required.</p>
                </Col>
              </Row>}

              <Button bsStyle={"primary"} type={"submit"}>
                Save
              </Button>
              <Button className={"margin-left-one"} onClick={() => this.changeRoute("/")}>
                Cancel
              </Button>
            </Form>
          </Col>
        </Row>
      </div>
    )
  }
}

export default withRouter(ExpenseBase);

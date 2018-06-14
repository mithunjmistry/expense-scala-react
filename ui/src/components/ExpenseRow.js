import React from 'react';
import { Glyphicon, Button, Overlay, Popover } from 'react-bootstrap';
import {withRouter} from "react-router-dom";


class ExpenseRow extends React.Component{

  state = {
    showRemoveConfirmation: false
  };

  changeRoute = (routeURL) => {
    this.props.history.push(routeURL);
  };

  handleRemoveClick = e => {
    this.setState({ target: e.target, showRemoveConfirmation: !this.state.showRemoveConfirmation });
  };

  handleConfirmationCancel = () => {
    this.setState({showRemoveConfirmation: false})
  };

  render(){

    return (
      <tr>
        <td>{this.props.iden}</td>
        <td>{this.props.expense_name}</td>
        <td>{this.props.expense_type_name}</td>
        <td>{this.props.description}</td>
        <td>{this.props.created_at}</td>
        <td>{this.props.amount}</td>
        <td>
          <Button bsSize="xsmall" className={"no-border"} onClick={() => this.changeRoute(`/expense/edit/${this.props.iden}`)}>
            <Glyphicon glyph="pencil" />
          </Button>

          <Button bsSize="xsmall" className={"margin-left-five no-border"} onClick={() => this.props.deleteExpense(this.props.iden)}>
            <Glyphicon glyph="remove" />
          </Button>

          <Overlay
            show={this.state.showRemoveConfirmation}
            target={this.state.target}
            placement="top"
            container={this}
          >
            <Popover id="popover-contained" title="Are you sure?">
                <span>
                    <Button className={"btn-sm"} bsStyle={"danger"} onClick={() => this.props.deleteExpense(this.props.iden)}>Yes</Button>
                    <Button className={"btn-sm"} bsStyle={"link"} onClick={this.handleConfirmationCancel}>Cancel</Button>
                </span>
            </Popover>
          </Overlay>
        </td>
      </tr>
    )
  }
}

export default withRouter(ExpenseRow);

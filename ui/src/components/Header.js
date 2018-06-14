import React from 'react';
import { Link } from "react-router-dom";

const Header = () => (
  <div className={"header-div"}>
    <Link to={"/"}>
      <span className={"header-span"}>Expense Management</span>
    </Link>

    <a href={"https://github.com/mithunjmistry/expense-scala-react"} target={"_blank"}>
      <span className={"header-span light-silver"}>GitHub</span>
    </a>
  </div>
);

export default Header;

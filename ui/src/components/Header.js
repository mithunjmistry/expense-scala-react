import React from 'react';
import { Link } from "react-router-dom";

const Header = () => (
  <div className={"header-div"}>
    <Link to={"/"}>
      <span className={"header-span"}>Mithun Mistry's expenses</span>
    </Link>
  </div>
);

export default Header;

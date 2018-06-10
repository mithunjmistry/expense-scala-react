import {ALL, CHANGE_FILTER, DATE} from "../api/strings";

const filterDefaultState = {
    sortBy: DATE,
    month: ALL,
    type: ALL
};

export default (state = filterDefaultState, action) => {
  switch (action.type) {
    case CHANGE_FILTER:
      return {
        ...action.payload
      };
    default:
      return state;
  }
}

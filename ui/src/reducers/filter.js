import {ALL, CHANGE_MONTH, CHANGE_SORT_BY, CHANGE_TYPE, DATE} from "../api/strings";

const filterDefaultState = {
    sortBy: DATE,
    month: ALL,
    type: ALL
};

export default (state = filterDefaultState, action) => {
  switch (action.type) {
    case CHANGE_SORT_BY:
      return {
        ...state,
        sortBy: action.payload
      };
    case CHANGE_TYPE:
      return {
        ...state,
        type: action.payload
      };
    case CHANGE_MONTH:
      return {
        ...state,
        month: action.payload
      };
    default:
      return state;
  }
}

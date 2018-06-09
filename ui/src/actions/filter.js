import {CHANGE_MONTH, CHANGE_SORT_BY, CHANGE_TYPE} from "../api/strings";

export const changeSortBy = ({sortBy}) => ({
  type: CHANGE_SORT_BY,
  payload: sortBy
});

export const changeMonth = ({month}) => ({
  type: CHANGE_MONTH,
  payload: month
});

export const changeType = ({type}) => ({
  type: CHANGE_TYPE,
  payload: type
});

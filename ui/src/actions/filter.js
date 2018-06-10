import {CHANGE_FILTER} from "../api/strings";

export const changeFilters = (payload) => ({
  type: CHANGE_FILTER,
  payload
});

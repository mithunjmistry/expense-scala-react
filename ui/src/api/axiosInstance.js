import axios from "axios"

const hostname = window.location.hostname;
const baseURL = `http://${hostname}:3000/`;

const axiosInstance = axios.create({
  baseURL: baseURL,
  timeout: 8000,
  headers: {'Csrf-Token': "nocheck", 'X-Requested-With': 'XMLHttpRequest'}
});

export default axiosInstance;

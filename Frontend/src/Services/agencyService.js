import axios from "axios";

const API_URL = "http://localhost:8090/api/agency";

export const searchAgencies = (searchData) => {
  return axios.post(`${API_URL}/search`, searchData);
};

export const getAgencyById = (agencyId) => {
  return axios.get(`${API_URL}/${agencyId}`);
}
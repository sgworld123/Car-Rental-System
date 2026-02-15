import api from "./api";

export const searchAgencies = (searchData) => {
  return api.post("/api/agency/search", searchData);
};

export const getAgencyById = (agencyId) => {
  return api.get(`/api/agency/${agencyId}`);
};

export const getVehicleById = (vehicleId) => {
  return api.get(`/api/agency/vehicle/${vehicleId}`);
};

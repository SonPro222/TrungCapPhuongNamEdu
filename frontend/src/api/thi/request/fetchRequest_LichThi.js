import apiClient from '../../apiConfig';

const ENDPOINT = '/api/thi/lich-thi';

export const createLichThi = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLichThi = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLichThi = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLichThi,
  updateLichThi,
  deleteLichThi,
};

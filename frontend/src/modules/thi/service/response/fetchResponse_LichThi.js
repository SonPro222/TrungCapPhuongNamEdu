import apiClient from '../../apiConfig';

const ENDPOINT = '/api/thi/lich-thi';

export const getAllLichThi = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLichThiById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLichThi,
  getLichThiById,
};

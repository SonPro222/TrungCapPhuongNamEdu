import apiClient from '../../apiConfig';

const ENDPOINT = '/api/thi/sinh-vien-du-thi';

export const getAllSinhVienDuThi = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getSinhVienDuThiById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllSinhVienDuThi,
  getSinhVienDuThiById,
};

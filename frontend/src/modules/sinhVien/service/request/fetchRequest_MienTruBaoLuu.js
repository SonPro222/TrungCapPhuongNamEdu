import apiClient from '../../apiConfig';

const ENDPOINT = '/api/sinh-vien/mien-tru-bao-luu';

export const createMienTruBaoLuu = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateMienTruBaoLuu = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteMienTruBaoLuu = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createMienTruBaoLuu,
  updateMienTruBaoLuu,
  deleteMienTruBaoLuu,
};

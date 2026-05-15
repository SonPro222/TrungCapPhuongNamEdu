import apiClient from '@/core/api/apiClient';

export const unwrapResponse = (response) => {
  const payload = response?.data ?? response;
  return payload?.data ?? payload?.result ?? payload;
};

export const unwrapListResponse = (response) => {
  const payload = unwrapResponse(response);

  if (Array.isArray(payload)) return payload;
  if (Array.isArray(payload?.content)) return payload.content;
  if (Array.isArray(payload?.items)) return payload.items;
  if (Array.isArray(payload?.data)) return payload.data;
  if (Array.isArray(payload?.result)) return payload.result;

  return [];
};

export default apiClient;

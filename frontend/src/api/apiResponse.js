export const unwrapApiData = (response) => {
  const payload = response?.data ?? response;

  if (Array.isArray(payload)) return payload;
  if (Array.isArray(payload?.data)) return payload.data;
  if (Array.isArray(payload?.result)) return payload.result;
  if (Array.isArray(payload?.content)) return payload.content;
  if (Array.isArray(payload?.data?.content)) return payload.data.content;
  if (Array.isArray(payload?.data?.items)) return payload.data.items;

  return payload?.data ?? payload?.result ?? payload ?? null;
};

export const unwrapApiList = (response) => {
  const data = unwrapApiData(response);
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content;
  if (Array.isArray(data?.items)) return data.items;
  return [];
};

export const getErrorMessage = (error) => {
  return error?.response?.data?.message
    || error?.response?.data?.error
    || error?.message
    || 'Có lỗi xảy ra';
};

export const normalizeText = (value) => String(value ?? '').toLowerCase().trim();

export const matchKeyword = (item, keyword, fields = []) => {
  const key = normalizeText(keyword);
  if (!key) return true;

  if (!fields.length) {
    return Object.values(item || {}).some((value) => normalizeText(value).includes(key));
  }

  return fields.some((field) => normalizeText(item?.[field]).includes(key));
};

export const toNumberOrNull = (value) => {
  if (value === '' || value === null || value === undefined) return null;
  const numberValue = Number(value);
  return Number.isNaN(numberValue) ? null : numberValue;
};

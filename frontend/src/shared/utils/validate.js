export const isEmpty = (value) => value === null || value === undefined || String(value).trim() === '';
export const isEmail = (value) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(String(value || '').trim());
export const isPhone = (value) => /^(0|\+84)[0-9]{9,10}$/.test(String(value || '').trim());

export const validateRequiredFields = (model = {}, fields = []) =>
  fields.reduce((errors, field) => {
    const key = typeof field === 'string' ? field : field.key;
    const label = typeof field === 'string' ? field : field.label || field.key;
    if (isEmpty(model[key])) errors[key] = `${label} là bắt buộc`;
    return errors;
  }, {});

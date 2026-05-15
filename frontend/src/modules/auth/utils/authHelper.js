export const isValidLoginForm = ({ username, email, password } = {}) => Boolean((username || email) && password);

export const buildLoginPayload = ({ username, email, password } = {}) => ({
  username: username || email || '',
  password: password || '',
});

export const getAuthErrorMessage = (error) => error?.response?.data?.message || error?.message || 'Có lỗi xảy ra khi xác thực.';

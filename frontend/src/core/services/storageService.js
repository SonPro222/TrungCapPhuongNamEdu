const safeStorage = (storage) => ({
  get(key, fallback = null) {
    try {
      return storage.getItem(key) ?? fallback;
    } catch {
      return fallback;
    }
  },
  set(key, value) {
    try {
      storage.setItem(key, value == null ? '' : String(value));
      return true;
    } catch {
      return false;
    }
  },
  remove(key) {
    try {
      storage.removeItem(key);
      return true;
    } catch {
      return false;
    }
  },
  clear(keys = []) {
    try {
      if (keys.length) keys.forEach((key) => storage.removeItem(key));
      else storage.clear();
      return true;
    } catch {
      return false;
    }
  },
});

export const localStorageService = safeStorage(window.localStorage);
export const sessionStorageService = safeStorage(window.sessionStorage);
export default localStorageService;

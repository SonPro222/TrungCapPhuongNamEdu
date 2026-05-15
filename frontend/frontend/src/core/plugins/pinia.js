// Project hiện chưa khai báo dependency Pinia trong package.json.
// File này giữ interface plugin để có thể app.use(piniaPlugin) mà không làm vỡ build.
export const piniaPlugin = {
  install(app) {
    app.config.globalProperties.$storeReady = true;
  },
};

export default piniaPlugin;

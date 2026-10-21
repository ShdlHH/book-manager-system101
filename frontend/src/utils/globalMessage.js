function getApp() {
  const el = document.querySelector('#app')
  return el && el.__vue_app__
}

function getMessage() {
  const app = getApp()
  return app?.config?.globalProperties?.$message
}

export const message = {
  success: (msg) => getMessage()?.success(msg),
  warning: (msg) => getMessage()?.warning(msg),
  info: (msg) => getMessage()?.info(msg),
  error: (msg) => getMessage()?.error(msg)
}

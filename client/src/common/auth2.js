import { getStore } from "./store";
import CuentaRepository from "../repositories/CuentaRepository";

export default {
  login,
  register,
  logout,
  getToken,
  isAdmin,
  isAuthenticationChecked,
  isAuthenticated,
  getUser
};

async function login(credentials) {
  const response = await CuentaRepository.authenticate(credentials);
  _saveToken(response.token);
  return _authenticate();
}

async function register(credentials) {
  return await CuentaRepository.registerAccount(credentials);
}

function logout() {
  _removeToken();
  const store = getStore();
  store.state.usuario.nombreUsuario = "";
  store.state.usuario.authority = "";
  store.state.usuario.logged = false;
  (store.state.usuario.id = ""), (store.state.usuario.favoritos = []);
}

function isAdmin() {
  return getStore().state.usuario.authority == "ADMIN";
}

function getToken() {
  return localStorage.getItem("token");
}

function _saveToken(token) {
  localStorage.setItem("token", token);
}

function _removeToken() {
  localStorage.removeItem("token");
}

async function _authenticate() {
  const response = await CuentaRepository.getAccount();
  const store = getStore();

  store.state.usuario = {
    nombreUsuario: response.nombreUsuario,
    authority: response.authority,
    logged: true,
    id: response.id,
    favoritos: response.favoritos
  };
  return store.state.usuario;
}

function isAuthenticationChecked() {
  return new Promise((resolve) => {
    if (getToken()) {
      _authenticate()
        .catch(() => logout())
        .finally(() => resolve(true));
    } else {
      resolve(true);
    }
  });
}

function isAuthenticated() {
  return getStore().state.usuario.logged || false;
}

// Nueva función para obtener el usuario actual
function getUser() {
  return getStore().state.usuario;
}

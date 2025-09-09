import HTTP from "../common/http";

export default {
  async authenticate(credentials) {
    return (await HTTP.post(`/cuenta/authenticate`, credentials)).data;
  },

  async getAccount() {
    return (await HTTP.get(`/cuenta/account`)).data;
  },

  async registerAccount(usuario) {
    return (await HTTP.post(`/cuenta/register`, usuario)).data;
  },
  async updateAccount(usuario) {
    return (await HTTP.put(`/cuenta/update`, usuario)).data;
  },
  async updateAccountWithPassword(usuario) {
    return (await HTTP.put(`/cuenta/account`, usuario)).data;
  }
};

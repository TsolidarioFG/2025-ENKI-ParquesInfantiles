import HTTP from "@/common/http";

const resource = "usuarios";

export default {
  async añadirFavoritos(usuarioId, parkId) {
    return (await HTTP.post(`${resource}/${usuarioId}/favoritos/${parkId}`)).data;
  },
  async eliminarFavoritos(usuarioId, parkId) {
    return (await HTTP.delete(`${resource}/${usuarioId}/favoritos/${parkId}`)).data;
  },
  async getCurrentUserFavorites() {
    return (await HTTP.get(`${resource}/me/favoritos`)).data;
  },
  async findAll(){
    return (await HTTP.get(`${resource}`)).data;
  }
};

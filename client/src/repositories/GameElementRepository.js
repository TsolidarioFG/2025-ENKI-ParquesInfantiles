import HTTP from "@/common/http";

const resource = "gameElements";

export default {
  async findByParkId(parkId) {
    const response = await HTTP.get(`${resource}/park/${parkId}`);
    return response.data;
  },

  async findById(id) {
    return (await HTTP.get(`${resource}/${id}`)).data;
  },
  async save(element) {
    if (element.id) {
      return (await HTTP.put(`${resource}/${element.id}`, element)).data;
    } else {
      return (await HTTP.post(`${resource}`, element)).data;
    }
  },
  async deleteById(id) {
    return await HTTP.delete(`${resource}/${id}`);
  },
  async saveGameElementImage(id, file) {
    const formData = new FormData();
    formData.append("file", file);
    const response = await HTTP.post(`${resource}/${id}/imagen`, formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    });
    return response.data;
  },
};

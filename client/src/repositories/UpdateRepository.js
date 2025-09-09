import HTTP from "@/common/http";

const resource = "updates";

export default {
  async findAll() {
    const response = await HTTP.get(`${resource}`);
    return response.data;
  },

  async findById(id) {
    return (await HTTP.get(`${resource}/${id}`)).data;
  },

  async findUpdatesByParkId(parkId) {
    return (await HTTP.get(`${resource}/park/${parkId}`)).data;
  },

  async deleteById(id) {
    return await HTTP.delete(`${resource}/${id}`);
  },

  async save(update) {
    const payload = {
        id: update.id,
        title: update.title,
        description: update.description,
        park: update.park,
        userId: update.userId,
        userName: update.nombreUsuario
    };

    try {
        if (update.id) {
            return (await HTTP.put(`${resource}/${update.id}`, payload)).data;
        } else {
            return (await HTTP.post(`${resource}`, payload)).data;
        }
    } catch (error) {
        console.error("Error saving update:", error);
        throw error;
    }
}
};

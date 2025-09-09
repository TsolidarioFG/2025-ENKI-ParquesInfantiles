import HTTP from "@/common/http";

const resource = "reviews";


export default {
  async findAll() {
    const response = await HTTP.get(`${resource}`);
    return response.data;
  },
  async getParkReviews(parkId) {
    try {
      const response = await HTTP.get(`${resource}/park/${parkId}`);
      return response.data;
    } catch (error) {
      console.error("Error al obtener las reviews", error);
      throw error;
    }
  },
  async createReview(parkId, review) {
    try {
      const response = await HTTP.post(`${resource}/park/${parkId}`, review);
      return response.data;
    } catch (error) {
      console.error("Error al crear la review:", error);
      throw error;
    }
  },
  async delete(id) {
    return await HTTP.delete(`${resource}/${id}`);
  },

  async acceptReview(id) {
  try {
    await HTTP.put(`${resource}/${id}/accept`);
  } catch (error) {
    console.error("Error al aceptar la reseña:", error);
    throw error;
  }
},
async rejectReview(id) {
  try {
    await HTTP.put(`${resource}/${id}/reject`);
  } catch (error) {
    console.error("Error al rechazar la reseña:", error);
    throw error;
  }
}

};

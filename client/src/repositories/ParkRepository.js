import HTTP from "@/common/http";

const resource = "parks";

export default {
  async findAll() {
    const response = await HTTP.get(`${resource}`);
    return response.data;
  },
  async findById(id) {
    return (await HTTP.get(`${resource}/${id}`)).data;
  },
  async findByName(name) {
    return (await HTTP.get(`${resource}/search?name=${encodeURIComponent(name)}`)).data;
  },
  async deleteById(id) {
    return await HTTP.delete(`${resource}/${id}`);
  },
  async saveParkImage(id, file) {
    const formData = new FormData();
    formData.append("file", file);
    const response = await HTTP.post(`${resource}/${id}/imagen`, formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    });
    return response.data;
  },
  async save(park) {
    if (park.id) {
      console.log("Updating park with ID:", park.id);
      return (await HTTP.put(`${resource}/${park.id}`, park)).data;
    } else {
      console.log("Creating new park:", park);
      return (await HTTP.post(`${resource}`, park)).data;
    }
  },
}

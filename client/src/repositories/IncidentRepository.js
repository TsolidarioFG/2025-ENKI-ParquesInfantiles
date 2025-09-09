import HTTP from "@/common/http";

const resource = "incidents";

export default {
    async findAll() {
        const response = await HTTP.get(`${resource}`);
        return response.data;
      },

      async findById(id) {
        return (await HTTP.get(`${resource}/${id}`)).data;
      },
      async findIncidentsByParkId(parkId) {
        return (await HTTP.get(`${resource}/park/${parkId}`)).data;
      },
      
      async deleteById(id) {
        return await HTTP.delete(`${resource}/${id}`);
      },
      async save(incident) {
        if (incident.id) {
          console.log("Updating incident with ID:", incident.id);
          return (await HTTP.put(`${resource}/${incident.id}`, incident)).data;
        } else {
          console.log("Creating new incident:", incident);
          return (await HTTP.post(`${resource}`, incident)).data;
        }
      },
};
import HTTP from "@/common/http";

const resource = "enums";

export default {
  async getAreaUbiOptions() {
    return (await HTTP.get(`${resource}/areaUbi`)).data;
  },
  async getCierrmdOptions() {
    return (await HTTP.get(`${resource}/cierrmd`)).data;
  },
  async getCierrmtOptions() {
    return (await HTTP.get(`${resource}/cierrmt`)).data;
  },
  async getParkTypeOptions() {
    return (await HTTP.get(`${resource}/parkType`)).data;
  },
  async getPavimtpOptions() {
    return (await HTTP.get(`${resource}/pavimtp`)).data;
  },
  async getSignMaterialOptions() {
    return (await HTTP.get(`${resource}/signMaterial`)).data;
  },
  async getAccessibilityOptions() {
    return (await HTTP.get(`${resource}/accessibility`)).data;
  },
  async getIncidentTypeOptions() {
    return (await HTTP.get(`${resource}/incidentType`)).data;
  },
};

<template>
  <div class="incident-list">
    <div class="header-container">
      <h2>{{ $t("incidentList.title") }}</h2>
      
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchQuery" 
          :placeholder="$t('incidentList.searchPlaceholder')" 
          class="search-input"
        />
        <i class="search-icon bi bi-search"></i>
      </div>
      
      <router-link 
        :to="{ name: 'IncidentCreate' }" 
        class="btn-create-incident"
      >
        {{ $t("incidentList.create") }}
      </router-link>
    </div>

    <!-- Filtro por tipo de incidente -->
    <div class="type-filter">
      <select v-model="filterType" class="form-select">
        <option value="">{{ $t("incidentList.allTypes") }}</option>
        <option 
          v-for="type in Object.keys(incidentTypes)" 
          :key="type" 
          :value="type"
        >
          {{ $t(`incidentForm.incidentTypes.${type}`) }}
        </option>
      </select>
    </div>

    <div v-if="loading" class="status-message">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">{{ $t("incidentList.loading") }}</span>
      </div>
    </div>

    <div v-else-if="filteredIncidents.length === 0" class="status-message">
      {{ $t("incidentList.noData") }}
    </div>

    <div v-else class="table-container">
      <table class="incident-table">
        <thead>
          <tr>
            <th>{{ $t("incidentList.id") }}</th>
            <th>{{ $t("incidentList.park") }}</th>
            <th>{{ $t("incidentList.type") }}</th>
            <th>{{ $t("incidentList.description") }}</th>
            <th>{{ $t("incidentList.actions") }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="incident in filteredIncidents" :key="incident.id">
            <td>{{ incident.id }}</td>
            <td>{{ incident.parkName }}</td>
            <td>{{ formatIncidentType(incident.incidentType) }}</td>
            <td>{{ truncateText(incident.description, 100) }}</td>
            <td>
              <div class="action-buttons">
                <router-link
                  :to="{ name: 'IncidentEdit', params: { incidentId: incident.id }}"
                  class="btn-edit"
                >
                  {{ $t("incidentList.edit") }}
                </router-link>
                <button
                  class="btn-delete"
                  @click="confirmDelete(incident)"
                >
                  {{ $t("incidentList.delete") }}
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true" ref="deleteModal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ $t("incidentList.confirmTitle") }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            {{ $t("incidentList.confirmMessage") }}
          </div>
          <div class="modal-footer">
            <button type="button" class="btn-cancel" data-bs-dismiss="modal">
              {{ $t("incidentList.cancel") }}
            </button>
            <button type="button" class="btn-confirm-delete" @click="deleteIncident">
              {{ $t("incidentList.delete") }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import IncidentRepository from "@/repositories/IncidentRepository";
import { Modal } from 'bootstrap';

export default {
  data() {
    return {
      incidents: [],
      loading: true,
      selectedIncident: null,
      deleteModal: null,
      searchQuery: '',
      filterType: '',
      incidentTypes: {
        NA: "No Aplicable",
        AREA_DE_JUEGO: "Área de Juego",
        CARTELERÍA: "Cartelería",
        CIERRE: "Cierre",
        EDAD: "Edad",
        ELEMENTOS_DE_JUEGO: "Elementos de Juego",
        ESTADO: "Estado",
        NOMBRE_ÁREA_DE_JUEGO: "Nombre del Área de Juego",
        NÚMERO_DE_ELEMENTOS: "Número de Elementos",
        PAVIMENTO: "Pavimento",
        PLANO: "Plano",
        SEÑALIZACIÓN: "Señalización",
        GAME_ELEMENTS: "Elementos de Juego",
        PAVEMENT: "Pavimento",
        AGE: "Edad"
      }
    };
  },
  computed: {
    filteredIncidents() {
      let filtered = [...this.incidents];

      // Filtro por búsqueda (solo por nombre de parque)
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        filtered = filtered.filter(incident => 
          incident.parkName && incident.parkName.toLowerCase().includes(query)
        );
      }

      // Filtro por tipo de incidente
      if (this.filterType) {
        filtered = filtered.filter(incident => incident.incidentType === this.filterType);
      }

      return filtered;
    }
  },
  methods: {
    async loadIncidents() {
      this.loading = true;
      try {
        this.incidents = await IncidentRepository.findAll();
      } catch (error) {
        console.error("Error loading incidents:", error);
      } finally {
        this.loading = false;
      }
    },
    formatIncidentType(type) {
      return this.$t(`incidentForm.incidentTypes.${type}`) || type;
    },
    truncateText(text, maxLength) {
      if (!text) return '';
      if (text.length <= maxLength) return text;
      return text.substring(0, maxLength) + '...';
    },
    confirmDelete(incident) {
      this.selectedIncident = incident;
      this.deleteModal.show();
    },
    async deleteIncident() {
      if (!this.selectedIncident) return;
      
      try {
        await IncidentRepository.deleteById(this.selectedIncident.id);
        this.deleteModal.hide();
        this.loadIncidents();
      } catch (error) {
        console.error("Error deleting incident:", error);
      }
    }
  },
  mounted() {
    this.loadIncidents();
    this.deleteModal = new Modal(this.$refs.deleteModal);
  }
};
</script>

<style scoped>
.incident-list {
  margin-top: 30px;
  text-align: left;
  background-color: #009DE0;
  padding: 20px;
  border-radius: 8px;
  color: white;
}

.header-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.header-container h2 {
  color: white;
  margin: 0;
}

.search-box {
  position: relative;
  flex-grow: 1;
  max-width: 400px;
}

.search-input {
  width: 100%;
  padding: 10px 15px 10px 35px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #009DE0;
  box-shadow: 0 0 0 2px rgba(0, 157, 224, 0.2);
}

.search-icon {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
}

.type-filter {
  background-color: white;
  padding: 10px 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.form-select {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ced4da;
  border-radius: 8px;
  font-size: 1rem;
}

.btn-create-incident {
  display: inline-block;
  padding: 10px 20px;
  background-color: transparent;
  color: #FFFFFF;
  border: 2px solid #FFFFFF;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition: all 0.3s ease;
}

.btn-create-incident:hover {
  background-color: #FFFFFF;
  color: #009DE0;
}

.status-message {
  padding: 20px;
  text-align: center;
  background-color: rgba(0, 157, 224, 0.1);
  border-radius: 6px;
  margin: 20px 0;
}

.table-container {
  margin-top: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.incident-table {
  width: 100%;
  border-collapse: collapse;
}

.incident-table th,
.incident-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
  color: black;
}

.incident-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #333;
}

.incident-table tr:hover {
  background-color: rgba(0, 157, 224, 0.05);
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.btn-edit,
.btn-delete {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
  border: none;
  cursor: pointer;
}

.btn-edit {
  background-color: #009DE0;
  color: white;
}

.btn-edit:hover {
  background-color: #0085c7;
}

.btn-delete {
  background-color: #f5f5f5;
  color: #ff4444;
  border: 1px solid #ff4444;
}

.btn-delete:hover {
  background-color: #ff4444;
  color: white;
}

/* Modal styles */
.modal-content {
  border-radius: 8px;
}

.modal-header {
  border-bottom: 1px solid #e0e0e0;
}

.btn-cancel {
  background-color: #f5f5f5;
  color: #333;
  border: none;
  border-radius: 6px;
  padding: 8px 16px;
}

.btn-confirm-delete {
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 8px 16px;
}

.btn-cancel:hover {
  background-color: #e0e0e0;
}

.btn-confirm-delete:hover {
  background-color: #cc0000;
}

@media (max-width: 768px) {
  .header-container {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box {
    max-width: 100%;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 5px;
  }
}
</style>
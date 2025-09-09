<template>
  <div class="container-fluid mt-4">
    <!-- Success Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="showSuccessAlert"
      style="background-color: #d4edda; color: #155724;"
    >
      <strong>{{ $t("incidentForm.successTitle") }}</strong> {{ $t("incidentForm.successMessage") }}
      <button type="button" class="btn-close" @click="showSuccessAlert = false"></button>
    </div>

    <!-- Error Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="errorMessage"
      style="background-color: #f8d7da; color: #721c24;"
    >
      <strong>{{ $t("incidentForm.errorTitle") }}</strong> {{ errorMessage }}
      <button type="button" class="btn-close" @click="errorMessage = ''"></button>
    </div>

    <div class="row">
      <div class="col-md-8 offset-md-2">
        <div class="card shadow-sm">
          <div class="card-header text-center text-white" style="background-color: #009DE0;">
            <h3>{{ incident.id ? $t("incidentForm.editTitle") : $t("incidentForm.newTitle") }}</h3>
          </div>
          <div class="card-body">
            <form @submit.prevent="handleSubmit">
              <!-- Park Search Section -->
              <div class="mb-3">
                <label class="form-label">{{$t("incidentForm.searchPark")}}</label>
                <div class="input-group">
                  <input 
                    type="text" 
                    class="form-control border-end-0" 
                    v-model="searchQuery" 
                    :placeholder="$t('incidentForm.searchPlaceholder')"
                    @input="searchParks"
                    :disabled="parkPreselected"
                  />
                  <button 
                    class="btn btn-outline-secondary border-start-0 d-flex align-items-center gap-1" 
                    type="button"
                    @click="clearSearch"
                    :disabled="parkPreselected"
                  >
                    <i class="bi bi-x"></i>
                    <span>{{ $t("incidentForm.clearButton") || "Clear" }}</span>
                  </button>
                  <button 
                    class="btn d-flex align-items-center gap-2 px-3" 
                    type="button"
                    @click="searchParks"
                    :disabled="!searchQuery || parkPreselected"
                    style="background-color: #009DE0; color: white;"
                  >
                    <i class="bi bi-search"></i>
                    <span>{{ $t("incidentForm.searchButton") }}</span>
                  </button>
                </div>
                
                <!-- Search results container -->
                <div class="search-results mt-1" v-if="searchResults.length > 0 && !parkLoaded">
                  <div 
                    v-for="park in searchResults" 
                    :key="park.id" 
                    class="search-result-item"
                    @click="selectPark(park)"
                  >
                    <strong>{{ park.name }}</strong>
                  </div>
                </div>
              </div>

              <!-- Hidden Park ID -->
              <input type="hidden" v-model="incident.parkId" required />

              <!-- Park Name Display -->
              <div class="mb-3">
                <label class="form-label">{{ $t("incidentForm.parkName") }}</label>
                <input 
                  type="text" 
                  class="form-control" 
                  v-model="incident.parkName" 
                  required 
                  readonly
                />
                <div v-if="parkLoaded" class="form-text" style="color: #009DE0;">
                  <i class="bi bi-check-circle-fill"></i> {{ $t("incidentForm.parkFound") }}
                </div>
              </div>

              <!-- Incident Type -->
              <div class="mb-3">
                <label class="form-label">{{ $t("incidentForm.incidentType") }}</label>
                <select class="form-select" v-model="incident.incidentType" required>
                  <option value="" disabled selected>{{ $t("incidentForm.selectIncidentType") }}</option>
                  <option v-for="option in incidentTypeOptions" :key="option" :value="option">
                    {{ $t(`incidentForm.incidentTypes.${option}`) }}
                  </option>
                </select>
              </div>

              <!-- Description -->
              <div class="mb-3">
                <label class="form-label">{{ $t("incidentForm.description") }}</label>
                <textarea 
                  class="form-control" 
                  v-model="incident.description" 
                  rows="4" 
                  maxlength="1000" 
                  required
                  :placeholder="$t('incidentForm.descriptionPlaceholder')"
                ></textarea>
                <div class="d-flex justify-content-between">
                  <small class="text-muted">{{ $t("incidentForm.maxCharacters") }}</small>
                  <small :class="characterCountClass">{{ incident.description.length }}/1000</small>
                </div>
              </div>

              <!-- Action Buttons -->
              <div class="d-flex gap-2">
                <button 
                  type="submit" 
                  class="btn flex-grow-1" 
                  :disabled="!isFormValid"
                  style="background-color: #009DE0; color: white;"
                >
                  {{ incident.id ? $t("incidentForm.editButton") : $t("incidentForm.createButton") }}
                </button>
                <button 
                  type="button" 
                  class="btn btn-outline-secondary" 
                  @click="$router.push({ name: 'IncidentList' })"
                >
                  {{ $t("incidentForm.cancelButton") }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import IncidentRepository from "@/repositories/IncidentRepository";
import ParkRepository from "@/repositories/ParkRepository";
import EnumRepository from "@/repositories/EnumsRepository";

export default {
  data() {
    return {
      incident: {
        id: null,
        parkId: null,
        parkName: "",
        incidentType: "",
        description: ""
      },
      previousRoute: null,
      incidentTypeOptions: [],
      parkLoaded: false,
      parkPreselected: false,
      showSuccessAlert: false,
      errorMessage: "",
      searchQuery: "",
      searchResults: [],
      searchTimeout: null
    };
  },
  computed: {
    isFormValid() {
      return (
        this.incident.parkId && 
        this.incident.parkName && 
        this.incident.incidentType && 
        this.incident.description.trim().length > 0
      );
    },
    characterCountClass() {
      if (this.incident.description.length > 900) {
        return "text-danger";
      } else if (this.incident.description.length > 700) {
        return "text-warning";
      }
      return "text-muted";
    }
  },
  methods: {
    async loadEnumValues() {
      try {
        this.incidentTypeOptions = await EnumRepository.getIncidentTypeOptions();
      } catch (error) {
        console.error("Error loading incident types:", error);
        this.errorMessage = this.$t("incidentForm.errorLoadingTypes");
      }
    },
    
    async searchParks() {
      if (this.parkPreselected || this.parkLoaded) return;
      
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }
      
      if (!this.searchQuery.trim()) {
        this.searchResults = [];
        return;
      }
      
      this.searchTimeout = setTimeout(async () => {
        try {
          const results = await ParkRepository.findByName(this.searchQuery);
          this.searchResults = results;
          
          if (results.length === 0 && this.searchQuery.trim()) {
            this.errorMessage = this.$t("incidentForm.noParksFound");
          }
        } catch (error) {
          console.error("Error searching parks:", error);
          this.errorMessage = this.$t("incidentForm.errorSearchingParks");
        }
      }, 300);
    },
    
    async loadIncident(id) {
      try {
        const data = await IncidentRepository.findById(id);
        this.incident = {
          id: data.id,
          parkId: data.parkId,
          parkName: data.parkName || "",
          incidentType: data.incidentType,
          description: data.description
        };

        if (!this.incident.parkName && this.$route.query.parkName) {
          this.incident.parkName = this.$route.query.parkName;
        }

        this.parkLoaded = true;
        this.parkPreselected = true;
        this.searchQuery = this.incident.parkName;
      } catch (error) {
        this.errorMessage = this.$t("incidentForm.errorLoadingIncident");
      }
    },

    selectPark(park) {
      this.incident.parkId = park.id;
      this.incident.parkName = park.name;
      this.parkLoaded = true;
      this.searchResults = [];
      this.searchQuery = park.name;
    },
    
    clearSearch() {
      this.searchQuery = "";
      this.searchResults = [];
      if (this.parkLoaded) {
        this.incident.parkId = null;
        this.incident.parkName = "";
        this.parkLoaded = false;
      }
    },
    
    async handleSubmit() {
      if (!this.isFormValid) return;

      try {
        if (this.incident.id) {
          await IncidentRepository.save(this.incident);
          this.showSuccessAlert = true;
          setTimeout(() => {
            this.$router.push(this.previousRoute || { name: 'IncidentList' });
          }, 2000);
        } else {
          this.incident.id = null;
          await IncidentRepository.save(this.incident);
          this.showSuccessAlert = true;
          setTimeout(() => {
            this.$router.push(this.previousRoute || { name: 'IncidentList' });
          }, 2000);
        }
      } catch (error) {
        console.error("Error saving incident:", error);
        this.errorMessage = this.$t("incidentForm.errorSavingIncident");
      }
    },

    checkForPreselectedPark() {
      const parkId = this.$route.query.parkId;
      const parkName = this.$route.query.parkName;
      
      if (parkId && parkName) {
        this.incident.parkId = parkId;
        this.incident.parkName = parkName;
        this.parkPreselected = true;
        this.parkLoaded = true;
        this.searchQuery = parkName;
      }
    }
  },
  mounted() {
    this.previousRoute = this.$route.query.redirectFrom || null;
    this.loadEnumValues();
    this.checkForPreselectedPark();  
    
    const incidentId = this.$route.params.incidentId;
    if (incidentId) {
      this.loadIncident(incidentId);
    }
  }
};
</script>

<style scoped>
.card {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.card-header {
  border-radius: 8px 8px 0 0 !important;
}

.btn {
  border-radius: 6px;
  padding: 8px 16px;
  transition: all 0.2s;
}

.btn:hover {
  opacity: 0.9;
}

.btn-outline-secondary {
  border-color: #ccc;
}

.btn-outline-secondary:hover {
  background-color: #f8f9fa;
}

.form-control, .form-select {
  border-radius: 6px;
  padding: 8px 12px;
}

.form-control:focus, 
.form-select:focus {
  border-color: #009DE0;
  box-shadow: 0 0 0 0.2rem rgba(0, 157, 224, 0.25);
}

.search-results {
  position: absolute;
  width: calc(100% - 30px);
  max-height: 200px;
  overflow-y: auto;
  background-color: white;
  border: 1px solid #ced4da;
  border-radius: 6px;
  z-index: 1000;
  margin-top: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-result-item {
  padding: 8px 12px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.2s;
}

.search-result-item:hover {
  background-color: #f8f9fa;
}

.search-result-item:last-child {
  border-bottom: none;
}

.alert {
  border-radius: 6px;
  margin-bottom: 20px;
}

.text-danger {
  color: #dc3545 !important;
}

.text-warning {
  color: #ffc107 !important;
}

.text-success {
  color: #009DE0 !important;
}

.input-group {
  display: flex;
  align-items: stretch;
}

.input-group .form-control {
  flex: 1 1 auto;
  min-height: 38px;
}

.input-group .btn {
  flex: 0 0 auto;
  min-height: 38px;
}

.bi {
  line-height: 1;
}
</style>
<template>  
  <div class="park-list">
    <div class="header-section">
      <h2>{{ $t("parkList.title") }}</h2>
      
      <div class="controls">
        <div class="filter-controls">
          <div class="toggle-switch">
            <input type="checkbox" id="mostrar-accesibles" v-model="showAccessible" class="switch-input" />
            <label for="mostrar-accesibles" class="switch-label">
              <span class="switch-button"></span>
              <span class="switch-text">{{ $t("parkList.showAccessible") }}</span>
            </label>
          </div>
          
          <div class="toggle-switch">
            <input type="checkbox" id="ordenar-nombre" v-model="ordenarNombre" class="switch-input" />
            <label for="ordenar-nombre" class="switch-label">
              <span class="switch-button"></span>
              <span class="switch-text">{{ $t("parkList.sortByName") }}</span>
            </label>
          </div>
        </div>
        
        <div class="search-box">
          <input 
            type="text" 
            v-model="searchQuery" 
            :placeholder="$t('parkList.searchPlaceholder')" 
            class="search-input"
          />
          <i class="search-icon bi bi-search"></i>
        </div>
      </div>
      
      <!-- Botón para crear parque -->
      <router-link 
        :to="{ name: 'ParkCreate' }" 
        class="btn-create-park"
      >
        {{ $t("parkList.createButton") }}
      </router-link>
    </div>

    <div v-if="loading">{{ $t("parkList.loading") }}</div>
    <div v-else-if="error">{{ $t("parkList.error") }}</div>
    <div v-else-if="filteredParks.length > 0" class="parks-container">
      <ParkCard
        v-for="park in filteredParks"
        :key="park.id"
        :park="park"
        @refreshParks="fetchParks"
      />
    </div>
    <div v-else class="empty-state">
      <i class="empty-icon bi bi-emoji-frown"></i>
      <h3>{{ $t("parkList.noParksFound") }}</h3>
      <p>{{ $t("parkList.tryAdjustingFilters") }}</p>
    </div>
  </div>
</template>

<script>
import ParkCard from "@/components/ParkCard.vue";
import ParkRepository from "@/repositories/ParkRepository";

export default {
  components: {
    ParkCard
  },
  data() {
    return {
      parks: [],
      loading: true,
      error: false,
      showAccessible: false,
      ordenarNombre: true,
      searchQuery: ''
    };
  },
  computed: {
    filteredParks() {
      let parquesFiltrados = [...this.parks];

      // Filtro por accesibilidad
      if (this.showAccessible) {
        parquesFiltrados = parquesFiltrados.filter(park => 
          park.generalAccessibility==true
        );
      }

      // Filtro por búsqueda
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        parquesFiltrados = parquesFiltrados.filter(park => 
          park.name.toLowerCase().includes(query) ||
          park.address.toLowerCase().includes(query) ||
          park.type.toLowerCase().includes(query)
        );
      }

      // Ordenación
      if (this.ordenarNombre) {
        parquesFiltrados.sort((a, b) => {
          const nameA = a.name?.toLowerCase() || '';
          const nameB = b.name?.toLowerCase() || '';
          return nameA.localeCompare(nameB);
        });
      }

      return parquesFiltrados;
    }
  },
  mounted() {
    this.fetchParks();
  },
  methods: {
    async fetchParks() {
      this.loading = true;
      this.error = false;
      try {
        this.parks = await ParkRepository.findAll();
      } catch (e) {
        console.error(e);
        this.error = true;
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>
<style scoped>
.park-list {
  margin-top: 30px;
  text-align: left;
  background-color: #009DE0;
  padding: 20px;
  border-radius: 8px;
  color: white;
}

.header-section {
  margin-bottom: 20px;
}

.header-section h2 {
  color: white;
  margin-bottom: 20px;
  font-size: 2rem;
}

.controls {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.filter-controls {
  display: flex;
  gap: 15px;
}

.search-box {
  position: relative;
  flex-grow: 1;
  max-width: 300px;
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

.toggle-switch {
  position: relative;
}

.switch-input {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.switch-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: white;
}

.switch-button {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
  background-color: #ccc;
  border-radius: 12px;
  margin-right: 8px;
  transition: background-color 0.3s;
}

.switch-button:after {
  content: '';
  position: absolute;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: white;
  top: 2px;
  left: 2px;
  transition: transform 0.3s;
}

.switch-input:checked + .switch-label .switch-button {
  background-color: white;
}

.switch-input:checked + .switch-label .switch-button:after {
  transform: translateX(26px);
  background-color: #009DE0;
}

/* Contenedor de parques modificado */
.parks-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.btn-create-park {
  display: inline-block;
  padding: 10px 20px;
  margin-top: 15px;
  background-color: transparent;
  color: #FFFFFF;
  border: 2px solid #FFFFFF;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition: all 0.3s ease;
}

.btn-create-park:hover {
  background-color: #FFFFFF;
  color: #009DE0;
}

.empty-state {
  text-align: center;
  padding: 30px;
  background-color: white;
  border-radius: 8px;
  margin-top: 20px;
  color: #333;
}

.empty-icon {
  font-size: 2.5rem;
  margin-bottom: 15px;
  color: #009DE0;
}

.empty-state h3 {
  color: #009DE0;
  margin-bottom: 10px;
}

.empty-state p {
  color: #666;
  margin: 0;
}

@media (max-width: 768px) {
  .controls {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-controls {
    flex-direction: column;
    gap: 10px;
  }
  
  .search-box {
    max-width: 100%;
  }
}
</style>
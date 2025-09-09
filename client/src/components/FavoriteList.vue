<template>
  <div class="park-list">
    <div class="header-section">
      <h2>
        <svg class="star-icon" viewBox="0 0 24 24" width="24" height="24">
          <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" 
                fill="#FFD700" />
        </svg>
        {{ $t('favorites.title') || 'My Favorite Parks' }}
      </h2>
      
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
            <input type="checkbox" id="ordenar-nombre" v-model="sortByName" class="switch-input" />
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
    </div>

    <div v-if="loading">{{ $t("parkList.loading") }}</div>
    <div v-else-if="error">{{ $t("parkList.error") }}</div>
    <div v-else>
      <div v-if="filteredFavoriteParks.length === 0" class="empty-state">
        <i class="empty-icon bi bi-emoji-frown"></i>
        <h3>{{ $t('favorites.none') || 'No favorites yet.' }}</h3>
        <p>{{ $t("parkList.tryAdjustingFilters") }}</p>
      </div>

      <div class="parks-container">
        <ParkCard
          v-for="park in filteredFavoriteParks"
          :key="park.id"
          :park="park"
          :isFavorite="true"
          @refreshFavorites="loadFavorites"
        />
      </div>
    </div>
  </div>
</template>

<script>
import FavoritosRepository from "@/repositories/FavoritosRepository";
import ParkCard from "@/components/ParkCard.vue";
import auth2 from "@/common/auth2";

export default {
  components: { ParkCard },
  data() {
    return {
      favoriteParks: [],
      loading: true,
      error: false,
      showAccessible: false,
      sortByName: true,
      searchQuery: ''
    };
  },
  computed: {
    filteredFavoriteParks() {
      let filtered = [...this.favoriteParks];

      // Filtro por accesibilidad
      if (this.showAccessible) {
        filtered = filtered.filter(park => park.generalAccessibility === true);
      }

      // Filtro por búsqueda
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        filtered = filtered.filter(park => 
          park.name.toLowerCase().includes(query) ||
          (park.address && park.address.toLowerCase().includes(query)) ||
          (park.type && park.type.toLowerCase().includes(query))
        );
      }

      // Ordenación
      if (this.sortByName) {
        filtered.sort((a, b) => {
          const nameA = a.name?.toLowerCase() || '';
          const nameB = b.name?.toLowerCase() || '';
          return nameA.localeCompare(nameB);
        });
      } else {
        // Orden por fecha de favorito si está disponible, o por ID como fallback
        filtered.sort((a, b) => (b.favoriteDate || b.id) - (a.favoriteDate || a.id));
      }

      return filtered;
    }
  },
  methods: {
    async loadFavorites() {
      this.loading = true;
      this.error = false;
      try {
        const user = auth2.getUser();
        if (!user) {
          this.favoriteParks = [];
          return;
        }

        this.favoriteParks = await FavoritosRepository.getCurrentUserFavorites();
      } catch (err) {
        console.error("Failed to load favorites:", err);
        this.error = true;
        this.favoriteParks = [];
      } finally {
        this.loading = false;
      }
    }
  },
  mounted() {
    this.loadFavorites();
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

.star-icon {
  vertical-align: middle;
  margin-right: 8px;
  filter: drop-shadow(0 1px 1px rgba(0,0,0,0.2));
}

.header-section {
  margin-bottom: 20px;
}

.header-section h2 {
  color: white;
  margin-bottom: 20px;
  font-size: 2rem;
  display: flex;
  align-items: center;
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

.parks-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
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
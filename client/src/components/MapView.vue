<template>
  <div id="map-container">
     <!-- Success Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="showSuccessAlert"
      style="background-color: #d4edda; color: #155724; position: absolute; top: 80px; left: 50%; transform: translateX(-50%); z-index: 1001; width: 90%; max-width: 500px;"
    >
      <strong>{{ $t("map.successTitle") }}</strong> {{ $t("map.successMessage") }}
      <button type="button" class="btn-close" @click="showSuccessAlert = false"></button>
    </div>

    <!-- Error Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="errorMessage"
      style="background-color: #f8d7da; color: #721c24; position: absolute; top: 80px; left: 50%; transform: translateX(-50%); z-index: 1001; width: 90%; max-width: 500px;"
    >
      <strong>{{ $t("map.errorTitle") }}</strong> {{ errorMessage }}
      <button type="button" class="btn-close" @click="errorMessage = ''"></button>
    </div>
    <div class="search-bar">
      <input
        type="text"
        v-model="searchQuery"
        @input="searchParks"
        :placeholder="$t('map.searchPlaceholder')"
        class="search-input"
      />

      <div v-if="searchResults.length > 0" class="search-results">
        <div
          v-for="park in searchResults"
          :key="park.id"
          class="search-result-item"
          @click="openParkModal(park)"
        >
          {{ park.name }}
        </div>
      </div>
    </div>
    
    <!-- Location button moved to top right -->
    <button class="locate-btn" @click="locateUser" :title="$t('map.showLocation')">
      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#009DE0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
        <circle cx="12" cy="10" r="3"></circle>
      </svg>
    </button>

    <div id="map"></div>

    <!-- Park Details Modal -->
    <div v-if="selectedPark" class="park-modal">
      <div class="modal-content">
        <span class="close-btn" @click="selectedPark = null">&times;</span>
        <h3 class="park-title">{{ selectedPark.name }}</h3>
        
        <div class="park-info">
          <p><strong>{{ $t('map.address') }}:</strong> {{ selectedPark.address }}</p>
          <p><strong>{{ $t('map.postalCode') }}:</strong> {{ selectedPark.postalCode }}</p>
          <p><strong>{{ $t('map.type') }}:</strong> {{ $t(`parkDetail.parkTypes.${selectedPark.type}`) }}</p>
        </div>
        
        <div class="modal-actions">
          <button 
            class="btn-primary"
            @click="goToParkDetail(selectedPark.id)"
          >
            {{ $t('map.viewDetails') }}
          </button>
          <a
            class="btn-secondary"
            :href="getGoogleMapsLink(selectedPark)"
            target="_blank"
          >
            {{ $t('map.openInGoogleMaps') }}
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref, nextTick, watch } from "vue";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import ParkRepository from "@/repositories/ParkRepository";
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});
L.Marker.prototype.options.icon = DefaultIcon;

export default {
  setup() {
     const showSuccessAlert = ref(false);
    const errorMessage = ref("");
    const router = useRouter();
    const { t, locale } = useI18n();
    const map = ref(null);
    const searchQuery = ref("");
    const searchResults = ref([]);
    const parks = ref([]);
    const selectedPark = ref(null);
    let userMarker = ref(null);

    const loadMap = async () => {
      try {
        await nextTick();
        
        if (!document.getElementById('map')) {
          console.error("Map container not found");
          return;
        }

        if (map.value) {
          map.value.off();
          map.value.remove();
        }

        map.value = L.map('map', {
          preferCanvas: true,
          zoomControl: false
        }).setView([43.3623, -8.4115], 13);

        L.control.zoom({
          position: 'topright'
        }).addTo(map.value);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
          maxZoom: 19,
          detectRetina: true
        }).addTo(map.value);

        parks.value = await ParkRepository.findAll();
        
        parks.value.forEach((park) => {
          const coordinates = [park.latitude, park.longitude];
          const marker = L.marker(coordinates, {
            title: park.name
          }).addTo(map.value);
          
          marker.on('click', () => {
            openParkModal(park);
          });
        });
      } catch (error) {
        console.error("Error loading map:", error);
        throw error;
      }
    };

    const searchParks = async () => {
      if (!searchQuery.value.trim()) {
        searchResults.value = [];
        return;
      }
      try {
        const results = await ParkRepository.findByName(searchQuery.value);
        searchResults.value = results;
      } catch (error) {
        console.error("Error searching parks:", error);
      }
    };

    const openParkModal = (park) => {
      selectedPark.value = park;
      map.value.flyTo([park.latitude, park.longitude], 16, {
        duration: 1
      });
      searchResults.value = [];
    };

    const goToParkDetail = (parkId) => {
      router.push({ name: "ParkDetail", params: { parkId } });
      selectedPark.value = null;
    };

    const getGoogleMapsLink = (park) => {
      return `https://www.google.com/maps?q=${park.latitude},${park.longitude}`;
    };

   const locateUser = () => {
      if (!navigator.geolocation) {
        errorMessage.value = t("map.locationNotSupported");
        return;
      }

      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          const userLatLng = [latitude, longitude];

          map.value.flyTo(userLatLng, 15, {
            duration: 1
          });

          if (userMarker.value) {
            map.value.removeLayer(userMarker.value);
          }

          userMarker.value = L.circleMarker(userLatLng, {
            radius: 8,
            fillColor: "#009DE0",
            color: "#fff",
            weight: 2,
            opacity: 1,
            fillOpacity: 0.9
          }).addTo(map.value)
            .bindPopup(t("map.ubicationPopup"))
            .openPopup();
         showSuccessAlert.value = true;
          setTimeout(() => {
            showSuccessAlert.value = false;
          }, 3000);
        },
        (error) => {
          errorMessage.value = t("map.locationError");
          console.error(error);
        }
      );
    };

    onMounted(() => {
      loadMap().catch(error => {
        console.error("Failed to load map:", error);
      });
      
      watch(locale, () => {
        if (userMarker.value) {
          userMarker.value.setPopupContent(t("map.ubicationPopup"));
        }
      });
    });

    return {
      searchQuery,
      searchResults,
      selectedPark,
      searchParks,
      openParkModal,
      goToParkDetail,
      getGoogleMapsLink,
      showSuccessAlert,
      errorMessage,
      locateUser
    };
  }
};
</script>

<style scoped>
#map-container {
  position: relative;
  height: 100vh;
  width: 100%;
  overflow: hidden;
  background: #f5f5f5;
}

.search-bar {
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  width: 90%;
  max-width: 500px;
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  outline: none;
  background: white;
  color: #333;
}

.search-input:focus {
  border-color: #009DE0;
  box-shadow: 0 0 0 2px rgba(0, 157, 224, 0.2);
}

.search-results {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 0 0 6px 6px;
  max-height: 300px;
  overflow-y: auto;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-top: -1px;
}

.search-result-item {
  padding: 12px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.search-result-item:last-child {
  border-bottom: none;
}

.search-result-item:hover {
  background-color: #f8f8f8;
}

#map {
  height: 100%;
  width: 100%;
  min-height: 400px;
}

/* Modal styles */
.park-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
  backdrop-filter: blur(3px);
}

.modal-content {
  background-color: white;
  padding: 25px;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  position: relative;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  animation: modalFadeIn 0.3s ease-out;
  color: #333;
}

.park-title {
  color: #009DE0;
  margin-bottom: 20px;
  font-size: 1.6rem;
  font-weight: 600;
}

@keyframes modalFadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.close-btn {
  position: absolute;
  top: 15px;
  right: 20px;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  transition: color 0.2s;
}

.close-btn:hover {
  color: #333;
}

.park-info {
  margin: 20px 0;
  line-height: 1.6;
  font-size: 1.05rem;
}

.park-info p {
  margin-bottom: 10px;
}

.modal-actions {
  display: flex;
  gap: 12px;
  margin-top: 25px;
  flex-wrap: wrap;
}

.btn-primary {
  padding: 12px 18px;
  border-radius: 6px;
  background-color: #009DE0;
  color: white;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 1.3rem;
  font-weight: 500;
  flex: 1;
  min-width: 120px;
  text-align: center;
}

.btn-primary:hover {
  background-color: #0085c7;
  transform: translateY(-1px);
}

.btn-secondary {
  padding: 10px 18px;
  border-radius: 6px;
  border: 1px solid #009DE0;
  color: #009DE0;
  background: white;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 15px;
  flex: 1;
  min-width: 120px;
  text-align: center;
  text-decoration: none;
}

.btn-secondary:hover {
  background-color: #f0f8ff;
  transform: translateY(-1px);
}

/* Location button - moved to top right */
.locate-btn {
  position: absolute;
  top: 20px;
  right: 60px;
  z-index: 1000;
  background-color: white;
  border: 1px solid #e0e0e0;
  padding: px;
  border-radius: 6px;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
}

.locate-btn:hover {
  background-color: #f8f8f8;
  transform: scale(1.05);
}

/* Responsive styles */
@media (max-width: 768px) {
  .search-bar {
    width: 95%;
    top: 10px;
  }
  
  .modal-actions {
    flex-direction: column;
  }
  
  .btn-primary, .btn-secondary {
    width: 100%;
  }

  .locate-btn {
    top: 10px;
    right: 15px;
    width: 36px;
    height: 36px;
  }
}
</style>
<template>
  <div class="park-card">
    <img class="park-image" :src="getImageSrc" alt="Park image" @click="goToParkDetail" />
    <div class="park-info" @click="goToParkDetail">
      <h5 class="park-title">{{ park.name }}</h5>
      <p class="park-address">{{ park.address }}</p>
      <p><strong>{{ $t("park.postalCode") }}:</strong> {{ park.postalCode }}</p>
      <p><strong>{{ $t("park.type") }}:</strong> {{ $t(`parkDetail.parkTypes.${park.type}`) }}</p>
      <p><strong>{{ $t("park.accessibility") }}:</strong> {{ $t(`parkDetail.accessibilityTypes.${park.accessibility}`) }}</p>
    </div>
    
    <div class="action-buttons">
      <button
        class="icon-btn like-btn"
        @click.stop="toggleFavorite"
        :title="favorited ? 'Remove from favorites' : 'Add to favorites'"
      >
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 21.35L10.55 20.03C5.4 15.36 2 12.28 2 8.5C2 5.42 4.42 3 7.5 3C9.24 3 10.91 3.81 12 5.09C13.09 3.81 14.76 3 16.5 3C19.58 3 22 5.42 22 8.5C22 12.28 18.6 15.36 13.45 20.03L12 21.35Z" 
                :fill="favorited ? '#FF4081' : 'none'" 
                :stroke="favorited ? '#FF4081' : '#666'" 
                stroke-width="1.5"/>
        </svg>
      </button>
      
      <button
        class="btn-edit"
        @click.stop="editPark"
        v-if="isAdmin"
      >
        {{ $t("parkList.edit") }}
      </button>
      
      <button
        class="btn-delete"
        @click.stop="deletePark"
        v-if="isAdmin"
      >
        {{ $t("parkList.delete") }}
      </button>
    </div>
  </div>
</template>

<script>
import ParkRepository from "@/repositories/ParkRepository";
import auth2 from "@/common/auth2";
import { BACKEND_URL } from "../constants.js";
import FavoritosRepository from "@/repositories/FavoritosRepository";

export default {
  props: {
    park: { type: Object, required: true },
    isFavorite: { type: Boolean, default: false }
  },
  data() {
    return {
      isAdmin: auth2.isAdmin(),
      favorited: false,
      userId: null
    };
  },
  computed: {
    getImageSrc() {
      if (this.park?.hasImages) {
        return `${BACKEND_URL}/parks/${this.park.id}/imagen`;
      }
      return "/placeholder.png";
    }
  },
  methods: {
    goToParkDetail() {
      this.$router.push({ name: "ParkDetail", params: { parkId: this.park.id } });
    },
    async deletePark() {
      if (confirm(this.$t("park.confirmDelete", { name: this.park.name }))) {
        try {
          await ParkRepository.deleteById(this.park.id);
          this.$emit("refreshParks");
        } catch (error) {
          console.error("Error deleting park:", error);
        }
      }
    },
    editPark() {
      this.$router.push({ name: "ParkEdit", params: { parkId: this.park.id } });
    },
    async toggleFavorite() {
      try {
        if (this.favorited) {
          await FavoritosRepository.eliminarFavoritos(this.userId, this.park.id);
        } else {
          await FavoritosRepository.añadirFavoritos(this.userId, this.park.id);
        }
        this.favorited = !this.favorited;
        this.$emit("refreshFavorites");
      } catch (err) {
        console.error("Error toggling favorite:", err);
      }
    }
  },
  async mounted() {
    try {
      const user = auth2.getUser();
      if (!user) return;
      
      this.userId = user.id;
      
      const favorites = await FavoritosRepository.getCurrentUserFavorites();
      this.favorited = favorites.some(fav => fav.id === this.park.id);
    } catch (error) {
      console.error("Error checking favorite status:", error);
    }
  }
};
</script>
<style scoped>
.park-card {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 16px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  position: relative;
  margin-bottom: 16px;
}

.park-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.park-image {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  margin-right: 16px;
  border: 1px solid #e0e0e0;
}

.park-info {
  flex: 1;
  text-align: left;
  color: #333;
}

.park-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 6px 0;
  color: #009DE0;
}

.park-address {
  font-size: 14px;
  color: #666;
  margin-bottom: 6px;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-left: 16px;
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
  white-space: nowrap;
}

.btn-edit {
  background-color: #009DE0;
  color: white;
}

.btn-edit:hover:not(:disabled) {
  background-color: #0085c7;
}

.btn-delete {
  background-color: #f5f5f5;
  color: #ff4444;
  border: 1px solid #ff4444;
}

.btn-delete:hover:not(:disabled) {
  background-color: #ff4444;
  color: white;
}

.btn-edit:disabled,
.btn-delete:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.icon-btn {
  background: transparent;
  border: none;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  cursor: pointer;
  transition: background-color 0.2s ease;
  margin-bottom: 8px;
  align-self: center;
}

.icon-btn:hover:not(:disabled) {
  background-color: #f5f5f5;
}

.icon-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.like-btn:hover svg {
  stroke: #FF4081;
}
</style>
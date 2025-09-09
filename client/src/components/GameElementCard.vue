<template>
  <div class="game-element-card">
    <!-- Success Alert -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="showSuccessAlert"
      style="background-color: #d4edda; color: #155724;"
    >
      <strong>{{ successTitle }}</strong> {{ successMessage }}
      <button type="button" class="btn-close" @click="showSuccessAlert = false"></button>
    </div>
    
    <!-- Error Alert -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="errorMessage"
      style="background-color: #f8d7da; color: #721c24;"
    >
      <strong>{{ errorTitle }}</strong> {{ errorMessage }}
      <button type="button" class="btn-close" @click="errorMessage = ''"></button>
    </div>

    <img class="game-element-image" :src="getImageSrc" alt="Game Element image" @click="goToGameElementDetail" />
    <div class="game-element-info" @click="goToGameElementDetail">
      <h5 class="game-element-title">{{ gameElement.elementName }}</h5>
      <p>
        <strong>{{ $t('accessibility') }}:</strong>
        {{ gameElement.accessibilityDegree }}
      </p>      
    </div>
    
    <div class="action-buttons">
      <button
        class="btn-edit"
        @click.stop="editGameElement"
        v-if="isAdmin"
      >
        {{ $t("gameElementList.edit") }}
      </button>
      
      <button
        class="btn-delete"
        @click.stop="deleteGameElement"
        v-if="isAdmin"
      >
        {{ $t("gameElementList.delete") }}
      </button>
    </div>
  </div>
</template>

<script>
import GameElementRepository from "@/repositories/GameElementRepository";
import auth2 from "@/common/auth2";
import { BACKEND_URL } from "../constants.js";

export default {
  props: {
    gameElement: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      isAdmin: auth2.isAdmin(),
      showSuccessAlert: false,
      successTitle: "",
      successMessage: "",
      errorMessage: "",
      errorTitle: this.$t("common.error")
    };
  },
  computed: {
    getImageSrc() {
      if (this.gameElement?.hasImage) {
        return `${BACKEND_URL}/gameElements/${this.gameElement.id}/imagen`;
      }
      return "/placeholder.png";
    }
  },
  methods: {
    goToGameElementDetail() {
      this.$router.push({ name: "GameElementDetail", params: { gameElementId: this.gameElement.id } });
    },
    editGameElement() {
  // Primero intenta obtener el parkId del gameElement
  let parkId = this.gameElement.parkId;
  
  // Si no está disponible, intenta obtenerlo de la ruta
  if (!parkId) {
    parkId = this.$route.params.parkId || this.$route.query.parkId;
  }
  
  // Si aún no tenemos parkId, muestra error
  if (!parkId) {
    this.errorMessage = this.$t("gameElementDetail.errorNoPark");
    return;
  }
  
  // Navega a la vista de edición
  this.$router.push({ 
    name: "GameElementCreate",
    query: { 
      parkId: parkId,
      elementId: this.gameElement.id 
    }
  });
},
    async deleteGameElement() {
      const confirmation = this.$t('deleteConfirmation', { name: this.gameElement.elementName });
      if (confirm(confirmation)) {
        try {
          await GameElementRepository.deleteById(this.gameElement.id);
          this.showSuccessAlert = true;
          this.successTitle = this.$t("common.success");
          this.successMessage = this.$t("gameElementList.deleteSuccess");
          this.$emit("refreshGameElements");
        } catch (error) {
          console.error("Error deleting game element:", error);
          this.errorMessage = this.$t("gameElementList.deleteError");
        }
      }
    }
  }
};
</script>

<style scoped>
.game-element-card {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 16px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  position: relative;
  margin-bottom: 16px;
  flex-direction: column;
}

.game-element-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.game-element-image {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  margin-right: 16px;
  border: 1px solid #e0e0e0;
}

.game-element-info {
  flex: 1;
  text-align: left;
  color: #333;
  width: 100%;
  margin-top: 10px;
}

.game-element-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 6px 0;
  color: #009DE0;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-left: 16px;
  width: 100%;
  margin-top: 10px;
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

.alert {
  width: 100%;
  margin-bottom: 15px;
  border-radius: 6px;
}
</style>
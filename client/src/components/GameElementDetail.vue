<template>
  <div class="game-element-detail" v-if="!loading && gameElement">
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

    <h2>{{ gameElement.elementName }}</h2>
    <p class="subtitle">{{ gameElement.elementType }}</p>
    <img class="game-element-image" :src="getImageSrc" alt="Game Element image" />

    <div class="game-element-container">
      <div class="column">
        <p><strong>{{ $t('gameElementDetail.id') }}:</strong> {{ gameElement.id }}</p>
        <p>
          <strong>{{ $t('gameElementDetail.areaCode') }}:</strong>
          {{ gameElement.areaCode ?? $t('gameElementDetail.notSpecified') }}
        </p>
        <p><strong>{{ $t('gameElementDetail.objectId') }}:</strong> {{ gameElement.objectId }}</p>
        <p>
          <strong>{{ $t('gameElementDetail.accessibility') }}:</strong>
          {{ gameElement.accessibilityDegree ?? $t('gameElementDetail.notSpecified') }}
        </p>
        <p>
          <strong>{{ $t('gameElementDetail.type') }}:</strong>
          {{ gameElement.elementType || $t('gameElementDetail.notSpecified') }}
        </p>
        <p>
          <strong>{{ $t('gameElementDetail.park') }}:</strong>
          {{ gameElement.park?.name || $t('gameElementDetail.notAssociated') }}
        </p>
      </div>

      <div class="column" v-if="Object.keys(trueBooleans).length">
        <p><strong>{{ $t('gameElementDetail.characteristics') }}:</strong></p>
        <div v-for="(label, key) in trueBooleans" :key="key">
          <label>
            <input type="checkbox" checked disabled />
            {{ label }}
          </label>
        </div>
      </div>
    </div>

    <div class="action-buttons">
      <button v-if="isAdmin" class="btn btn-primary" @click="editGameElement">
        {{ $t('gameElementDetail.edit') || 'Editar' }}
      </button>
      
      <button class="btn btn-secondary" @click="$router.go(-1)">
        {{ $t('gameElementDetail.back') }}
      </button>
    </div>
  </div>

  <div v-else-if="loading" class="loading-state">
    <p>{{ $t('gameElementDetail.loading') }}</p>
  </div>
  <div v-else-if="error" class="error-state">
    <p>{{ $t('gameElementDetail.error') }}</p>
  </div>
</template>

<script>
import GameElementRepository from "@/repositories/GameElementRepository";
import auth2 from "@/common/auth2";
import { BACKEND_URL } from "../constants.js";

export default {
  data() {
    return {
      gameElement: null,
      loading: true,
      error: false,
      isAdmin: auth2.isAdmin(),
      showSuccessAlert: false,
      successTitle: "",
      successMessage: "",
      errorMessage: "",
      errorTitle: this.$t("common.error")
    };
  },
  async mounted() {
    try {
      this.gameElement = await GameElementRepository.findById(this.$route.params.gameElementId);
      if (this.$route.query.success) {
        this.showSuccessAlert = true;
        this.successTitle = this.$t("common.success");
        this.successMessage = this.$t("gameElementDetail.updateSuccess");
      }
    } catch (e) {
      console.error(e);
      this.error = true;
      this.errorMessage = this.$t("gameElementDetail.loadError");
    } finally {
      this.loading = false;
    }
  },
  methods: {
    editGameElement() {
      const parkId = this.gameElement.parkId;
      
      if (!parkId) {
        this.errorMessage = this.$t("gameElementDetail.errorNoPark");
        return;
      }
      
      this.$router.push({ 
        name: "GameElementCreate",
        query: { 
          parkId: parkId,
          elementId: this.gameElement.id 
        }
      });
    }
  },
  computed: {
    getImageSrc() {
      if (this.gameElement?.hasImage) {
        return `${BACKEND_URL}/gameElements/${this.gameElement.id}/imagen`;
      }
      return "/placeholder.png";
    },
    trueBooleans() {
      if (!this.gameElement) return {};

      const labels = {
        cooperativePlay: "Juego Cooperativo",
        soloPlay: "Juego Solo",
        socialPlay: "Juego Social",
        parallelPlay: "Juego Paralelo",
        linearPlay: "Juego Lineal",
        symbolic: "Simbólico",
        auditory: "Audición",
        visual: "Visión",
        tactile: "Táctil",
        cognitive: "Cognitivo",
        upperBodyStrength: "Fuerza en Tren Superior",
        balancing: "Balanceo",
        climbing: "Trepar",
        crawling: "Gatear",
        jumping: "Saltar",
        sliding: "Deslizarse",
        swinging: "Columpiarse",
        spinning: "Girar",
        rocking: "Mecer"
      };

      const activeEntries = Object.entries(labels)
        .filter(([key]) => this.gameElement[key])
        .sort((a, b) => a[1].localeCompare(b[1]));

      return Object.fromEntries(activeEntries);
    }
  }
};
</script>

<style scoped>
.game-element-detail {
  max-width: 800px;
  margin: 2rem auto;
  padding: 1.5rem 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  color: #333;
  text-align: center;
}

.game-element-detail h2 {
  font-weight: 600;
  font-size: 1.8rem;
  margin-bottom: 0.25rem;
  color: #009DE0;
}

.subtitle {
  font-size: 1rem;
  color: #666;
  margin-bottom: 1.5rem;
}

.game-element-image {
  max-width: 100%;
  max-height: 320px;
  border-radius: 8px;
  margin-bottom: 2rem;
  object-fit: contain;
  border: 1px solid #e0e0e0;
  background: #f9f9f9;
}

.game-element-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 1.5rem;
  text-align: left;
  margin-bottom: 1.5rem;
}

.column {
  flex: 1 1 45%;
  min-width: 240px;
}

.column p {
  margin: 0.75rem 0;
  font-size: 1rem;
  line-height: 1.4;
  color: #444;
}

.column p strong {
  font-weight: 600;
  color: #333;
}

.column label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
  color: #555;
  margin: 0.5rem 0;
  cursor: default;
}

.column label input[type="checkbox"] {
  width: 16px;
  height: 16px;
  accent-color: #009DE0;
}

.action-buttons {
  margin-top: 2rem;
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: 500;
  font-size: 1rem;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
  min-width: 120px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.btn-primary {
  background-color: #009DE0;
  color: white;
}

.btn-primary:hover {
  background-color: #0081c1;
}

.btn-secondary {
  background-color: white;
  color: #009DE0;
  border: 1px solid #009DE0;
}

.btn-secondary:hover {
  background-color: #f0f9ff;
}

.loading-state,
.error-state {
  padding: 2rem;
  text-align: center;
  color: #333;
}

.loading-state {
  color: #009DE0;
}

.error-state {
  color: #f44336;
}

.alert {
  margin-bottom: 20px;
  border-radius: 6px;
}

@media (max-width: 600px) {
  .game-element-detail {
    padding: 1.25rem;
  }

  .game-element-container {
    flex-direction: column;
    gap: 1.25rem;
  }

  .column {
    flex: 1 1 100%;
  }

  .action-buttons {
    flex-direction: column;
    gap: 0.75rem;
  }

  .btn {
    width: 100%;
  }
}
</style>
<template>
  <div class="park-detail" v-if="!loading && park">
    <!-- Success Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="showSuccessAlert"
      style="background-color: #d4edda; color: #155724;"
    >
      <strong>{{ $t('parkDetail.successTitle') }}</strong> {{ successMessage }}
      <button type="button" class="btn-close" @click="showSuccessAlert = false"></button>
    </div>

    <!-- Error Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="errorMessage"
      style="background-color: #f8d7da; color: #721c24;"
    >
      <strong>{{ $t('parkDetail.errorTitle') }}</strong> {{ errorMessage }}
      <button type="button" class="btn-close" @click="errorMessage = ''"></button>
    </div>

    <h2>{{ park.name }}</h2>
    <p class="subtitle">{{ park.address }}</p>
    <img class="park-image" :src="getImageSrc" alt="Park image" />

    <div class="park-container">
      <div class="column">
        <p><strong>{{ $t('parkDetail.postalCode') }}:</strong> {{ park.postalCode }}</p>
        <p><strong>{{ $t('parkDetail.visitYear') }}:</strong> {{ park.visitYear }}</p>
        <p><strong>{{ $t('parkDetail.areaUbi') }}:</strong> {{ $t(`parkDetail.parkUbis.${park.areaUbi}`) }}</p>
        <p><strong>{{ $t('parkDetail.type') }}:</strong> {{ $t(`parkDetail.parkTypes.${park.type}`) }}</p>        
        <p><strong>{{ $t('parkDetail.cierrmd') }}:</strong> {{ $t(`parkDetail.cierrmdTypes.${park.cierrmd}`) }}</p>
        <p><strong>{{ $t('parkDetail.cierrmt') }}:</strong> {{ $t(`parkDetail.cierrmtTypes.${park.cierrmt}`) }}</p>
        <p><strong>{{ $t('parkDetail.pavimtp') }}:</strong> {{ $t(`parkDetail.pavimtpTypes.${park.pavimtp}`) }}</p>
        <p><strong>{{ $t('parkDetail.signMaterial') }}:</strong> {{ $t(`parkDetail.signMaterialTypes.${park.signMaterial}`) }}</p>
      </div>

      <div class="column">
        <p><strong>{{ $t('parkDetail.ageRange') }}:</strong> {{ park.ageRange }}</p>
        <p><strong>{{ $t('parkDetail.numUsers') }}:</strong> {{ park.numUsers }}</p>
        <p><strong>{{ $t('parkDetail.accessibility') }}:</strong> {{ $t(`parkDetail.accessibilityTypes.${park.accessibility}`) }}</p>
        <p><strong>{{ $t('parkDetail.width') }}:</strong> {{ park.width }} m</p>
        <p><strong>{{ $t('parkDetail.freeTurningSpace') }}:</strong> {{ park.freeTurningSpace }} cm</p>
        <p><strong>{{ $t('parkDetail.parking') }}:</strong> {{ park.parking }}</p>
      </div>

      <div class="column">
        <label><input type="checkbox" :checked="park.plazaPMR" disabled /> {{ $t('parkDetail.plazaPMR') }}</label>
        <label><input type="checkbox" :checked="park.bus" disabled /> {{ $t('parkDetail.bus') }}</label>
        <label><input type="checkbox" :checked="park.pedestrian" disabled /> {{ $t('parkDetail.pedestrian') }}</label>
        <label><input type="checkbox" :checked="park.bioArea" disabled /> {{ $t('parkDetail.bioArea') }}</label>
        <label><input type="checkbox" :checked="park.accessibleFountain" disabled /> {{ $t('parkDetail.accessibleFountain') }}</label>
      </div>

      <div class="column">
        <label><input type="checkbox" :checked="park.generalAccessibility" disabled /> {{ $t('parkDetail.generalAccessibility') }}</label>
        <p><strong>{{ $t('parkDetail.degree1') }}:</strong> {{ park.degree1 }}</p>
        <p><strong>{{ $t('parkDetail.degree2') }}:</strong> {{ park.degree2 }}</p>
        <p><strong>{{ $t('parkDetail.degree3') }}:</strong> {{ park.degree3 }}</p>
      </div>
    </div>

    <div class="action-buttons">
      <button class="btn btn-primary" @click="toggleVerElementos">
        {{ verElementos ? $t('parkDetail.hidePlayElements') : $t('parkDetail.showPlayElements') }}
      </button>
      
      <button class="btn btn-primary" v-if="isAdmin" @click="toggleVerIncidentes">
        {{ verIncidentes ? $t('parkDetail.hideIncidents') : $t('parkDetail.showIncidents') }}
      </button>
      
      <button class="btn btn-primary" @click="toggleVerReviews">
        {{ verReviews ? $t('parkDetail.hideReviews') : $t('parkDetail.showReviews') }}
      </button>
      
      <button 
        v-if="isLoggedIn && !isAdmin" 
        class="btn btn-primary" 
        @click="openReviewModal"
      >
        {{ $t('parkDetail.createReview') }}
      </button>

      <button 
        v-if="isAdmin" 
        @click="goToUpdateForm" 
        class="btn btn-primary"
      >
        {{ $t("parkDetail.addUpdate") }}
      </button>
         
      <button class="btn btn-primary" @click="redirectToQualityStar">{{ $t("parkDetail.viewKonva") }}</button>
    </div>

    <!-- Game Elements Section -->
    <div v-if="verElementos" class="section-container">
      <div class="section-header">
        <h3>{{ $t('parkDetail.gameElements') }}:</h3>
        <router-link 
          v-if="isAdmin"
          :to="{ 
            name: 'GameElementCreate', 
            query: { parkId: park.id } 
          }" 
          class="btn btn-create-game-element-incident"
        >
          {{ $t('parkDetail.addGameElement') }}
        </router-link>
      </div>
      
      <p v-if="gameElements.length === 0" class="no-content">{{ $t('parkDetail.noGameElements') }}</p>
      <div v-else class="game-element-cards">
        <GameElementCard
          v-for="element in sortedGameElements"
          :key="element.id"
          :gameElement="element"
          @refreshGameElements="fetchGameElements"
        />
      </div>
    </div>
    


    <!-- Incidents Section -->
    <div v-if="verIncidentes" class="section-container">
      <div class="section-header">
        <h3>{{ $t('parkDetail.parkIncidents') }}:</h3>
        <router-link 
          v-if="isAdmin"
          :to="{ 
            name: 'IncidentCreate', 
            query: { parkId: park.id, parkName: park.name, redirectFrom: this.$route.fullPath } 
          }" 
          class="btn btn-create-game-element-incident"
        >
          {{ $t('parkDetail.addIncident') }}
        </router-link>
      </div>

      <div v-if="loadingIncidents" class="loading-spinner">
        <div class="spinner"></div>
        <span>{{ $t('parkDetail.loadingIncidents') }}</span>
      </div>

      <p v-else-if="incidents.length === 0" class="no-content">
        {{ $t('parkDetail.noIncidents') }}
      </p>

      <div v-else class="table-responsive">
        <table class="data-table">
          <thead>
            <tr>
              <th>{{ $t('parkDetail.id') }}</th>
              <th>{{ $t('parkDetail.incidentType') }}</th>
              <th>{{ $t('parkDetail.description') }}</th>
              <th v-if="isAdmin">{{ $t('parkDetail.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="incident in incidents" :key="incident.id">
              <td>{{ incident.id }}</td>
              <td>{{ $t(`incidentForm.incidentTypes.${incident.incidentType}`) }}</td>
              <td>{{ truncateText(incident.description, 100) }}</td>
              <td v-if="isAdmin">
                <div class="table-actions">
  <router-link 
    :to="{ 
      name: 'IncidentEdit', 
      params: { incidentId: incident.id }
    }"
    class="btn btn-small btn-edit"
  >
    {{ $t('parkDetail.edit') }}
  </router-link>

  <button 
    class="btn btn-small btn-danger" 
    @click="confirmDelete(incident)"
  >
    {{ $t('parkDetail.delete') }}
  </button>
</div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <div v-if="verReviews" class="section-container reviews-section">
  <div class="section-header">
    <h3>{{ $t('parkDetail.parkReviews') }}:</h3>
  </div>

  <div v-if="loadingReviews" class="loading-spinner">
    <div class="spinner"></div>
    <span>{{ $t('parkDetail.loadingReviews') }}</span>
  </div>

  <p v-else-if="displayReviews.length === 0" class="no-content">
    {{ $t('parkDetail.noReviews') }}
  </p>

  <div v-else class="reviews-container">
    <div v-for="review in displayReviews" :key="review.id" class="review-card">
      <div class="review-header">
        <div class="review-meta">
          <span class="review-author">{{ review.usuario?.nombre || 'Usuario' }}</span>
          <div class="review-rating">{{ getStarRating(review.calificacion) }}</div>
          <span v-if="isAdmin && review.approved === null" class="badge badge-pending">
            {{ $t('parkDetail.pendingApproval') }}
          </span>
          <span v-if="isAdmin && review.approved === false" class="badge badge-rejected">
            {{ $t('parkDetail.rejected') }}
          </span>
        </div>
        <div v-if="isAdmin || (isLoggedIn && review.usuario?.id === store.state.usuario?.id)" class="review-actions">
          <button 
            class="btn btn-small btn-danger" 
            @click="confirmDeleteReview(review)"
            v-if="isAdmin"
          >
            {{ $t('parkDetail.delete') }}
          </button>
          <button 
            v-if="isAdmin && review.approved === null"
            class="btn btn-small" 
            @click="approveReview(review)"
          >
            {{ $t('parkDetail.approve') }}
          </button>
        </div>
      </div>
      <div class="review-content">
        {{ review.texto }}
      </div>
    </div>
  </div>
</div>

    <div class="navigation-buttons">
      <button class="btn btn-primary" @click="editPark" v-if="isAdmin">{{ $t('parkDetail.edit') }}</button>
      <button class="btn" @click="$router.go(-1)">{{ $t('parkDetail.back') }}</button>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true" ref="deleteModal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ $t('parkDetail.confirmDelete') }}</h5>
            <button type="button" class="btn-close" @click="closeModal('delete')" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            {{ $t('parkDetail.confirmDeleteIncident') }}
          </div>
          <div class="modal-footer">
            <button type="button" class="btn" @click="closeModal('delete')">{{ $t('parkDetail.cancel') }}</button>
            <button type="button" class="btn btn-danger" @click="deleteIncident">{{ $t('parkDetail.delete') }}</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Review Creation Modal -->
    <div class="modal fade" id="reviewModal" tabindex="-1" aria-hidden="true" ref="reviewModal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ $t('parkDetail.createReview') }}</h5>
            <button type="button" class="btn-close" @click="closeModal('review')" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="createReview">
              <div class="form-group">
                <label class="form-label">{{ $t('parkDetail.rating') }}:</label>
                <div class="rating-input">
                  <div class="form-range-container">
                    <input 
                      type="range" 
                      class="form-range" 
                      min="1" 
                      max="5" 
                      step="1" 
                      v-model="newReview.calificacion"
                    />
                    <div class="rating-value">{{ newReview.calificacion }}</div>
                  </div>
                  <div class="rating-stars">{{ getStarRating(newReview.calificacion) }}</div>
                </div>
              </div>
              
              <div class="form-group">
                <label class="form-label">{{ $t('parkDetail.comment') }}:</label>
                <textarea 
                  class="form-control" 
                  v-model="newReview.texto" 
                  rows="4" 
                  maxlength="500" 
                  required
                  :placeholder="$t('parkDetail.reviewPlaceholder')"
                ></textarea>
                <div class="text-counter">
                  <small :class="characterCountClass">{{ characterCount }}/500</small>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn" @click="closeModal('review')">{{ $t('parkDetail.cancel') }}</button>
            <button 
              type="button" 
              class="btn btn-accent" 
              @click="createReview"
              :disabled="!newReview.texto.trim()"
            >
              {{ $t('parkDetail.publishReview') }}
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Review Deletion Modal -->
    <div class="modal fade" id="deleteReviewModal" tabindex="-1" aria-hidden="true" ref="deleteReviewModal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ $t('parkDetail.confirmDelete') }}</h5>
            <button type="button" class="btn-close" @click="closeModal('deleteReview')" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            {{ $t('parkDetail.confirmDeleteReview') }}
          </div>
          <div class="modal-footer">
            <button type="button" class="btn" @click="closeModal('deleteReview')">{{ $t('parkDetail.cancel') }}</button>
            <button type="button" class="btn btn-danger" @click="deleteReview">{{ $t('parkDetail.delete') }}</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div v-else-if="loading" class="loading-screen">
    <div class="spinner"></div>
    <p>{{ $t('parkDetail.loadingDetails') }}</p>
  </div>
  
  <div v-else-if="error" class="error-message">
    {{ $t('parkDetail.errorLoadingParkDetails') }}
  </div>
</template>

<script>
import ParkRepository from "@/repositories/ParkRepository"
import GameElementRepository from "@/repositories/GameElementRepository"
import IncidentRepository from "@/repositories/IncidentRepository"
import ReviewRepository from "@/repositories/ReviewRepository"
import GameElementCard from "@/components/GameElementCard.vue"
import auth2 from "@/common/auth2"
import { getStore } from "@/common/store"
import { BACKEND_URL } from "../constants.js"
import { Modal } from "bootstrap"

export default {
  components: {
    GameElementCard,
  },
  data() {
    return {
      showSuccessAlert: false,
      successMessage: '',
      errorMessage: '',
      store: getStore(),
      park: null,
      gameElements: [],
      incidents: [],
      reviews: [],
      verElementos: false,
      verIncidentes: false,
      verReviews: false,
      loading: true,
      loadingIncidents: false,
      loadingReviews: false,
      error: false,
      isAdmin: auth2.isAdmin(),
      isLoggedIn: auth2.isAuthenticated(),
      selectedIncident: null,
      selectedReview: null,
      deleteModal: null,
      reviewModal: null,
      deleteReviewModal: null,
      newReview: {
        calificacion: 3,
        texto: "",
        usuario: auth2.isAuthenticated() ? { id: getStore().state.usuario.id } : null,
      },
    }
  },
  async mounted() {
    try {
      this.park = await ParkRepository.findById(this.$route.params.parkId)
      console.log(this.park)
    } catch (e) {
      console.error(e)
      this.error = true
    } finally {
      this.loading = false
    }

    // Inicializar modales después de que el DOM esté completamente renderizado
    this.$nextTick(() => {
      this.initializeModals()
    })
    
    // Verificar si se debe mostrar los elementos automáticamente
    if (this.$route.query.showElements) {
      this.toggleVerElementos()
    }
  },
  methods: {
    initializeModals() {
      // Inicializar todos los modales
      try {
        if (this.$refs.deleteModal) {
          this.deleteModal = new Modal(this.$refs.deleteModal)
        }
        
        if (this.$refs.reviewModal) {
          this.reviewModal = new Modal(this.$refs.reviewModal)
        }
        
        if (this.$refs.deleteReviewModal) {
          this.deleteReviewModal = new Modal(this.$refs.deleteReviewModal)
        }
      } catch (error) {
        console.error("Error al inicializar modales:", error)
      }
    },
    goToUpdateForm() {
      this.$router.push({
        name: "UpdateList",
        query: { park: this.park.name } // Esto filtrará automáticamente en UpdateList
      });
    },
    async fetchGameElements() {
      try {
        this.gameElements = await GameElementRepository.findByParkId(this.$route.params.parkId)
        console.log("Game elements fetched:", this.gameElements)
      } catch (e) {
        console.error("Error fetching game elements:", e)
      }
    },
    async fetchIncidents() {
      this.loadingIncidents = true;
      try {
        this.incidents = await IncidentRepository.findIncidentsByParkId(this.park.id);
        console.log("Incidents fetched:", this.incidents);
      } catch (e) {
        console.error("Error fetching incidents:", e);
      } finally {
        this.loadingIncidents = false;
      }
    },
    async fetchReviews() {
      this.loadingReviews = true
      try {
        this.reviews = await ReviewRepository.getParkReviews(this.park.id)
        console.log("Reviews fetched:", this.reviews)
      } catch (e) {
        console.error("Error fetching reviews:", e)
      } finally {
        this.loadingReviews = false
      }
    },
    async toggleVerElementos() {
      if (!this.verElementos) {
        await this.fetchGameElements()
      }
      this.verElementos = !this.verElementos
    },
    async toggleVerIncidentes() {
      if (!this.verIncidentes) {
        await this.fetchIncidents()
      }
      this.verIncidentes = !this.verIncidentes
    },
    async toggleVerReviews() {
      if (!this.verReviews) {
        await this.fetchReviews()
      }
      this.verReviews = !this.verReviews
    },
    editPark() {
      this.$router.push({ name: "ParkEdit", params: { parkId: this.$route.params.parkId } })
    },
  
    truncateText(text, maxLength) {
      if (!text) return ""
      if (text.length <= maxLength) return text
      return text.substring(0, maxLength) + "..."
    },
    confirmDelete(incident) {
      this.selectedIncident = incident
      if (this.deleteModal) {
        this.deleteModal.show()
      } else {
        // Si el modal no está inicializado, intentar inicializarlo
        this.initializeModals()
        if (this.deleteModal) {
          this.deleteModal.show()
        } else {
          console.error("No se pudo inicializar el modal de eliminación")
        }
      }
    },
     async deleteIncident() {
      if (!this.selectedIncident) return;

      try {
        await IncidentRepository.deleteById(this.selectedIncident.id);
        this.closeModal('delete');
        this.successMessage = this.$t('parkDetail.incidentDeletedSuccess');
        this.showSuccessAlert = true;
        this.fetchIncidents();
      } catch (error) {
        console.error("Error al eliminar el incidente:", error);
        this.errorMessage = this.$t('parkDetail.incidentDeletionError');
      }
    },
    openReviewModal() {
      // Reiniciar el formulario
      this.newReview = {
        calificacion: 3,
        texto: "",
        usuario: auth2.isAuthenticated() ? { id: this.store.state.usuario.id } : null,
      }
      
      // Verificar si el modal está inicializado
      if (this.reviewModal) {
        this.reviewModal.show()
      } else {
        // Si no está inicializado, intentar inicializarlo
        this.initializeModals()
        if (this.reviewModal) {
          this.reviewModal.show()
        } else {
          console.error("No se pudo inicializar el modal de reseña")
        }
      }
    },
    async createReview() {
      if (!this.newReview.texto.trim()) return;

      try {
        await ReviewRepository.createReview(this.park.id, this.newReview);
        this.closeModal('review');
        this.successMessage = this.$t('parkDetail.reviewCreatedSuccess');
        this.showSuccessAlert = true;
        
        setTimeout(async () => {
          await this.fetchReviews();
          if (!this.verReviews) {
            this.verReviews = true;
          }
        }, 300);
      } catch (error) {
        console.error("Error al crear la reseña:", error);
        this.errorMessage = this.$t('parkDetail.reviewCreationError');
      }
    },
    // Método genérico para cerrar cualquier modal
    closeModal(modalType) {
      try {
        let modalElement;
        let modalInstance;
        
        if (modalType === 'review') {
          modalElement = document.getElementById('reviewModal');
          modalInstance = this.reviewModal;
        } else if (modalType === 'delete') {
          modalElement = document.getElementById('deleteModal');
          modalInstance = this.deleteModal;
        } else if (modalType === 'deleteReview') {
          modalElement = document.getElementById('deleteReviewModal');
          modalInstance = this.deleteReviewModal;
        }
        
        // Intentar cerrar usando la instancia de Bootstrap
        if (modalInstance) {
          modalInstance.hide();
        }
        
        // Forzar la limpieza del DOM
        this.$nextTick(() => {
          // Eliminar clases y estilos que Bootstrap añade
          if (modalElement) {
            modalElement.classList.remove('show');
            modalElement.style.display = 'none';
            modalElement.setAttribute('aria-hidden', 'true');
            modalElement.removeAttribute('aria-modal');
            modalElement.removeAttribute('role');
          }
          
          // Eliminar el backdrop y la clase modal-open
          document.body.classList.remove('modal-open');
          document.body.style.overflow = '';
          document.body.style.paddingRight = '';
          
          const backdrops = document.getElementsByClassName('modal-backdrop');
          while (backdrops.length > 0) {
            backdrops[0].parentNode.removeChild(backdrops[0]);
          }
        });
      } catch (error) {
        console.error("Error al cerrar el modal:", error);
      }
    },
    confirmDeleteReview(review) {
      this.selectedReview = review
      if (this.deleteReviewModal) {
        this.deleteReviewModal.show()
      } else {
        // Si el modal no está inicializado, intentar inicializarlo
        this.initializeModals()
        if (this.deleteReviewModal) {
          this.deleteReviewModal.show()
        } else {
          console.error("No se pudo inicializar el modal de eliminación de reseña")
        }
      }
    },
     async deleteReview() {
      if (!this.selectedReview) return;

      try {
        await ReviewRepository.delete(this.selectedReview.id);
        this.closeModal('deleteReview');
        this.successMessage = this.$t('parkDetail.reviewDeletedSuccess');
        this.showSuccessAlert = true;
        setTimeout(() => {
          this.fetchReviews();
        }, 300);
      } catch (error) {
        console.error("Error al eliminar la reseña:", error);
        this.errorMessage = this.$t('parkDetail.reviewDeletionError');
      }
    },
    getStarRating(rating) {
      if (!rating) return ""

      const fullStars = Math.floor(rating)
      const halfStar = rating % 1 >= 0.5
      const emptyStars = 5 - fullStars - (halfStar ? 1 : 0)

      let stars = "★".repeat(fullStars)
      if (halfStar) stars += "½"
      stars += "☆".repeat(emptyStars)

      return stars
    },
    redirectToQualityStar() {
      this.$router.push({
        name: 'Konva',
        params: { parkId: this.park.id }
      });
    },
    async approveReview(review) {
      try {
        await ReviewRepository.acceptReview(review.id);
        this.successMessage = this.$t('parkDetail.reviewApproved');
        this.showSuccessAlert = true;
        await this.fetchReviews();
      } catch (error) {
        console.error("Error al aprobar la reseña:", error);
        this.errorMessage = this.$t('parkDetail.reviewApprovalError');
      }
    },
  },
  computed: {
    getImageSrc() {
      if (this.park?.hasImages) {
        console.log("Image exists for park:", this.park.id)
        return `${BACKEND_URL}/parks/${this.park.id}/imagen`
      }
      return "/placeholder.png"
    },
    characterCount() {
      return this.newReview.texto.length
    },
    characterCountClass() {
      if (this.characterCount > 450) {
        return "text-danger"
      } else if (this.characterCount > 350) {
        return "text-warning"
      }
      return "text-muted"
    },
    currentUserId() {
      return this.store.state.usuario.id || null
    },
    displayReviews() {
      if (this.isAdmin) {
        return this.reviews; // Los admins ven todas las reseñas
      }
      return this.reviews.filter(review => review.approved === true);
    },
    sortedGameElements() {
      return [...this.gameElements].sort((a, b) => {
        const nameA = a.elementName?.toLowerCase() || '';
        const nameB = b.elementName?.toLowerCase() || '';
        return nameA.localeCompare(nameB);
      });
    }
  },
  updated() {
    // Reinicializar modales cuando el componente se actualiza
    this.$nextTick(() => {
      this.initializeModals()
    })
  },
}

</script>

<style scoped>

.alert {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1050;
  width: 80%;
  max-width: 600px;
}
/* Base styles */
.park-detail {
  max-width: 1300px;
  margin: 0 auto;
  padding: 2rem;
  background-color: white;
  border-radius: 8px;
}

h2 {
  color: #009DE0;
  font-size: 2rem;
  margin-bottom: 0.5rem;
  text-align: center;
}

.subtitle {
  color: #666;
  text-align: center;
  margin-bottom: 2rem;
}

.park-image {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.park-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}
.column label input[type="checkbox"] {
  margin-right: 0.3rem; /* Espacio entre checkbox y texto */
}

.column label {
  display: flex;
  align-items: center;
  margin-bottom: 1.25rem; /* Más espacio entre elementos */
  transition: all 0.2s ease;
}

.column label:hover {
  background-color: #f5f5f5;
  border-radius: 4px;
}
.column p, .column label {
  margin: 0.5rem 0;
  color: #333;
}

.column strong {
  color: #009DE0;
}

/* Buttons */
.btn {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s ease;
  text-align: center;
}

.btn-primary {
  background-color: #009DE0;
  color: white;
}

.btn-primary:hover {
  background-color: #0085c7;
}



.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-danger:hover {
  background-color: #c82333;
}

.btn-small {
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
  
  
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin-bottom: 2rem;
  justify-content: center;
}

.navigation-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
  justify-content: center;
}

/* Sections */
.section-container {
  background-color: #009DE0;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-header h3 {
  color: white;
  margin: 0;
}

.no-content {
  color: #666;
  font-style: italic;
  text-align: center;
  padding: 1rem;
}

/* Game Elements */
.game-element-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1rem;
}

/* Tables */
.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th, .data-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background-color: #f8f9fa;
  color: #009DE0;
}

.table-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-edit {
  background-color: transparent;
  border: 1px solid white;
  color: white;
  transition: all 0.2s ease;
}

.btn-edit:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* If you want the edit button to match your color scheme */
.btn-edit {
  background-color: transparent;
  border: 1px solid white; 
  color: white; 
}

.btn-edit:hover {
  background-color: rgba(0, 157, 224, 0.1); 
}



.reviews-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.review-card {
  background-color: white;
  border-radius: 8px;
  padding: 1.25rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border-left: 4px solid #009DE0;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.75rem;
}

.review-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.review-author {
  font-weight: bold;
  color: #009DE0;
}

.review-rating {
  color: #F9DC0A;
  font-size: 1.1rem;
}

.review-content {
  color: #333;
  line-height: 1.5;
}

.review-actions {
  display: flex;
  gap: 0.5rem;
}
.badge {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: bold;
}

.badge-pending {
  background-color: #F9DC0A; /* Yellow for pending */
  color: #333;
}

.badge-rejected {
  background-color: #dc3545; /* Red for rejected */
  color: white;
}

/* Modals */
.modal-content {
  border-radius: 8px;
}

.modal-header {
  background-color: #009DE0;
  color: white;
  border-radius: 8px 8px 0 0;
}

.modal-title {
  color: white;
}

/* Loading states */
.loading-screen, .loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.spinner {
  width: 3rem;
  height: 3rem;
  border: 4px solid rgba(0, 157, 224, 0.2);
  border-top-color: #009DE0;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 1rem;
  border-radius: 4px;
  text-align: center;
  margin: 2rem;
}

/* Form elements */
.form-group {
  margin-bottom: 1.25rem;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  transition: border-color 0.2s;
}

.form-control:focus {
  outline: none;
  border-color: #009DE0;
  box-shadow: 0 0 0 2px rgba(0, 157, 224, 0.2);
}

.rating-input {
  margin-top: 0.5rem;
}

.form-range-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.form-range {
  flex-grow: 1;
}

.rating-value {
  font-weight: bold;
  min-width: 1.5rem;
  text-align: center;
}

.rating-stars {
  color: #F9DC0A;
  font-size: 1.5rem;
  margin-top: 0.5rem;
}

.text-counter {
  text-align: right;
  color: #666;
  font-size: 0.875rem;
}

.text-danger {
  color: #dc3545;
}

.text-warning {
  color: #ffc107;
}

.text-muted {
  color: #6c757d;
}

.btn-create-game-element-incident {
  display: inline-block;
  padding: 10px 20px;
  background-color: transparent;
  color: #FFFFFF;
  border: 2px solid #FFFFFF;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition: all 0.3s ease;
  margin-left: auto; /* Para alinearlo a la derecha */
}

.btn-create-game-element-incident:hover {
  background-color: #FFFFFF;
  color: #009DE0;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>
<template>
  <div class="reviews-list">
    <!-- Success Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="showSuccessAlert"
      style="background-color: #d4edda; color: #155724;"
    >
      <strong>{{ successTitle }}</strong> {{ successMessage }}
      <button type="button" class="btn-close" @click="showSuccessAlert = false"></button>
    </div>

    <!-- Error Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="errorMessage"
      style="background-color: #f8d7da; color: #721c24;"
    >
      <strong>{{ $t('reviews.errorTitle') }}</strong> {{ errorMessage }}
      <button type="button" class="btn-close" @click="errorMessage = ''"></button>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2>{{ $t('reviews.title') }}</h2>
    </div>

    <!-- Filtros -->
    <div class="filters mb-4 p-3 bg-white rounded">
      <div class="row g-3">
        <!-- First row of filters -->
        <div class="col-md-3">
          <input 
            v-model="filterPark" 
            type="text" 
            class="form-control" 
            :placeholder="$t('reviews.filterPark')" 
          />
        </div>
        <div class="col-md-3">
          <input 
            v-model="filterUser" 
            type="text" 
            class="form-control" 
            :placeholder="$t('reviews.filterUser')" 
          />
        </div>
        <div class="col-md-3">
    <select v-model="filterRating" class="form-select" @change="handleRatingChange">
      <option value="0">{{ $t('reviews.allRatings') }}</option>
      <option v-for="n in 5" :value="n">{{ $t('reviews.rating') }}: {{ n }} ★</option>
    </select>
  </div>

        <div class="col-md-3" v-if="isAdmin">
          <select v-model="filterStatus" class="form-select">
            <option value="all">{{ $t('reviews.allStatuses') }}</option>
            <option value="accepted">{{ $t('reviews.accepted') }}</option>
            <option value="notAccepted">{{ $t('reviews.notAccepted') }}</option>
            <option value="pending">{{ $t('reviews.pending') }}</option>
          </select>
        </div>

        <!-- Second row: dates -->
        <div class="col-md-6 position-relative">
          <label class="form-label small mb-1 d-block">{{ $t('reviews.fromDate') }}</label>
          <Datepicker
            v-model="startDate"
            :format="'yyyy-MM-dd'"
            :max-date="endDate || new Date()"
            :day-names="dayNames"
            :month-names="monthNames"
            :clearable="true"
            auto-apply
            :enable-time-picker="false"
            @update:modelValue="handleStartDateChange"
          >
            <template #input-icon>
              <i class="bi bi-calendar3"></i>
            </template>
          </Datepicker>
        </div>

        <div class="col-md-6 position-relative">
          <label class="form-label small mb-1 d-block">{{ $t('reviews.toDate') }}</label>
          <Datepicker
            v-model="endDate"
            :format="'yyyy-MM-dd'"
            :min-date="startDate"
            :day-names="dayNames"
            :month-names="monthNames"
            :clearable="true"
            auto-apply
            :enable-time-picker="false"
            :disabled="!startDate"
          >
            <template #input-icon>
              <i class="bi bi-calendar3"></i>
            </template>
          </Datepicker>
        </div>
      </div>
    </div>

    <!-- Review counter -->
    <div class="review-counter mb-3" v-if="!loading">
      {{ $t('reviews.showing', { count: filteredReviews.length }) }}
    </div>

    <div v-if="loading" class="text-center my-4">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">{{ $t('reviews.loading') }}</span>
      </div>
    </div>

    <p v-else-if="filteredReviews.length === 0" class="no-elements">
      {{ $t('reviews.noReviews') }}
    </p>

    <div v-else class="reviews-container mt-3">
      <div 
        v-for="review in filteredReviews" 
        :key="review.id" 
        class="review-card"
        :class="{
          'rejected-review': review.approved === false,
          'pending-review': review.approved === null
        }"
      >
        <div class="review-header">
          <div>
            <span class="review-author">{{ review.usuario?.nombre || $t('reviews.user') }}</span>
            <div class="review-rating">{{ getStarRating(review.calificacion) }}</div>
            <span v-if="isAdmin && review.approved === null" class="badge bg-warning text-dark ms-2">
              {{ $t('reviews.pendingApproval') }}
            </span>
            <span v-if="isAdmin && review.approved === false" class="badge bg-danger text-white ms-2">
              {{ $t('reviews.rejected') }}
            </span>
          </div>
          <div class="review-date">
            {{ formatDate(review.fechaCreacion) }}
          </div>
        </div>
        <div class="review-content">
          <p><strong>{{ $t('reviews.park') }}:</strong> {{ review.park }}</p>
          <p><strong>{{ $t('reviews.date') }}:</strong> {{ formatDate(review.fechaCreacion) }}</p>
          <p>{{ review.texto }}</p>
        </div>

        <div class="review-actions mt-2" v-if="isAdmin">
          <button 
            v-if="review.approved === null || review.approved === false" 
            @click="accept(review.id)" 
            class="btn btn-sm btn-success me-2"
          >
            {{ $t('reviews.accept') }}
          </button>

          <button 
            v-if="review.approved === null || review.approved === true" 
            @click="reject(review.id)" 
            class="btn btn-sm btn-warning me-2"
          >
            {{ $t('reviews.reject') }}
          </button>

          <button 
            @click="confirmDelete(review)" 
            class="btn btn-sm btn-outline-danger"
          >
            {{ $t('reviews.delete') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ReviewRepository from "@/repositories/ReviewRepository";
import auth2 from "@/common/auth2";
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

export default {
  name: "ReviewList",
  components: {
    Datepicker
  },
  data() {
    return {
      reviews: [],
      loading: true,
      filterPark: "",
      filterUser: "",
      filterRating: 0,
      startDate: null,
      endDate: null,
      filterStatus: "all",
      isAdmin: auth2.isAdmin(),
      showSuccessAlert: false,
      successTitle: "",
      successMessage: "",
      errorMessage: "",
      dayNames: [
        this.$t('reviews.days.sun'), 
        this.$t('reviews.days.mon'),
        this.$t('reviews.days.tue'),
        this.$t('reviews.days.wed'),
        this.$t('reviews.days.thu'),
        this.$t('reviews.days.fri'),
        this.$t('reviews.days.sat')
      ],
      monthNames: [
        this.$t('reviews.months.jan'),
        this.$t('reviews.months.feb'),
        this.$t('reviews.months.mar'),
        this.$t('reviews.months.apr'),
        this.$t('reviews.months.may'),
        this.$t('reviews.months.jun'),
        this.$t('reviews.months.jul'),
        this.$t('reviews.months.aug'),
        this.$t('reviews.months.sep'),
        this.$t('reviews.months.oct'),
        this.$t('reviews.months.nov'),
        this.$t('reviews.months.dec')
      ]
    };
  },
  mounted() {
    this.loadReviews();
  },
  created() {
    if (this.$route.query.park) {
      this.filterPark = this.$route.query.park;
    }
  },
  computed: {
    filteredReviews() {
      let reviewsToShow = this.reviews;

      // Si no es admin, mostrar solo reseñas aprobadas
      if (!this.isAdmin) {
        reviewsToShow = reviewsToShow.filter(review => review.approved);
      }

      return reviewsToShow.filter((review) => {
        const matchesPark = review.park?.toLowerCase().includes(this.filterPark.toLowerCase());
        const matchesUser = review.usuario?.nombre?.toLowerCase().includes(this.filterUser.toLowerCase());
        const matchesRating = this.filterRating === 0 || Math.floor(review.calificacion) === this.filterRating;
        const matchesDate = this.filterByDate(review.fechaCreacion);
        const matchesStatus =
          this.filterStatus === "all" ||
          (this.filterStatus === "accepted" && review.approved === true) ||
          (this.filterStatus === "notAccepted" && review.approved === false) ||
          (this.filterStatus === "pending" && review.approved === null);

        return matchesPark && matchesUser && matchesRating && matchesDate && matchesStatus;
      });
    }
  },
  methods: {
    async loadReviews() {
      try {
        this.reviews = await ReviewRepository.findAll();
        this.reviews.sort((a, b) => new Date(b.fechaCreacion) - new Date(a.fechaCreacion));
      } catch (error) {
        console.error("Error cargando las reseñas:", error);
      } finally {
        this.loading = false;
      }
    },
    
    getStarRating(calificacion) {
      const filledStars = Math.floor(calificacion);
      const halfStar = calificacion % 1 !== 0;
      const emptyStars = 5 - filledStars - (halfStar ? 1 : 0);

      return (
        "★".repeat(filledStars) +
        (halfStar ? "½" : "") +
        "☆".repeat(emptyStars)
      );
    },
    handleRatingChange(event) {
      // Si se selecciona la opción "Calificación" (valor 0), resetear el filtro
      if (event.target.value === '0') {
        this.filterRating = 0;
      }
    },
    
    formatDate(dateString) {
      if (!dateString) return '';
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString(this.$i18n.locale, options);
    },
    
    filterByDate(dateString) {
      if (!dateString) return false;

      const reviewDate = new Date(dateString);
      const start = this.startDate ? new Date(this.startDate) : null;
      const end = this.endDate ? new Date(this.endDate) : null;

      if (start && reviewDate < start) return false;
      if (end) {
        end.setHours(23, 59, 59, 999);
        if (reviewDate > end) return false;
      }

      return true;
    },
    
    handleStartDateChange(newStartDate) {
      if (this.endDate && newStartDate > this.endDate) {
        this.endDate = null;
      }
      this.startDate = newStartDate; 
    },
    
     async accept(reviewId) {
      try {
        await ReviewRepository.acceptReview(reviewId);
        this.successTitle = this.$t('reviews.successTitle');
        this.successMessage = this.$t('reviews.acceptedSuccessfully');
        this.showSuccessAlert = true;
        this.loadReviews();
        setTimeout(() => {
          this.showSuccessAlert = false;
        }, 3000);
      } catch (error) {
        this.errorMessage = this.$t('reviews.errorAccepting');
        console.error("Error accepting review:", error);
      }
    },
    
    confirmDelete(review) {
      if (confirm(this.$t('reviews.confirmDelete'))) {
        this.deleteReview(review.id);
      }
    },
    
    async deleteReview(reviewId) {
      try {
        await ReviewRepository.delete(reviewId);
        this.successTitle = this.$t('reviews.successTitle');
        this.successMessage = this.$t('reviews.deletedSuccessfully');
        this.showSuccessAlert = true;
        this.loadReviews();
        setTimeout(() => {
          this.showSuccessAlert = false;
        }, 3000);
      } catch (error) {
        this.errorMessage = this.$t('reviews.errorDeleting');
        console.error("Error deleting review:", error);
      }
    },
    
    async reject(reviewId) {
      try {
        await ReviewRepository.rejectReview(reviewId);
        this.successTitle = this.$t('reviews.successTitle');
        this.successMessage = this.$t('reviews.rejectedSuccessfully');
        this.showSuccessAlert = true;
        this.loadReviews();
        setTimeout(() => {
          this.showSuccessAlert = false;
        }, 3000);
      } catch (error) {
        this.errorMessage = this.$t('reviews.errorRejecting');
        console.error("Error rejecting review:", error);
      }
    },
  }
};
</script>

<style scoped>
.reviews-list {
  margin-top: 30px;
  text-align: left;
  background-color: #009DE0;
  padding: 20px;
  border-radius: 8px;
  color: white;
}

.filters {
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.review-counter {
  font-weight: bold;
  color: #009DE0;
  background-color: white;
  padding: 8px 12px;
  border-radius: 8px;
  display: inline-block;
}

.reviews-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.review-card {
  display: flex;
  flex-direction: column;
  background-color: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  color: #333;
  position: relative;
  overflow: hidden;
}

.review-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* Estilo para reseñas rechazadas */
.rejected-review {
  background-color: #fff8f8; /* Fondo rojo muy suave */
  border-left: 4px solid #dc3545; /* Borde izquierdo rojo */
}

/* Estilo para reseñas pendientes */
.pending-review {
  background-color: #fffdf5; /* Fondo amarillo muy suave */
  border-left: 4px solid #ffc107; /* Borde izquierdo amarillo */
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.review-author {
  font-weight: bold;
  margin-right: 10px;
  color: #009DE0;
}

.review-rating {
  color: #FFC107;
  font-size: 1.2rem;
  line-height: 1;
}

.review-date {
  color: #666;
  font-size: 0.9rem;
}

.review-content {
  color: #333;
  white-space: pre-line;
}

.review-content p {
  margin-bottom: 8px;
}

.review-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 10px;
}

.no-elements {
  font-style: italic;
  color: white;
  margin-top: 10px;
  text-align: center;
}

.btn {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.btn-success {
  background-color: #009DE0;
  border-color: #009DE0;
}

.btn-warning {
  background-color: #F9DC0A;
  border-color: #F9DC0A;
  color: #333;
}

.btn-outline-danger {
  border-color: #F44336;
  color: #F44336;
}

.btn-outline-danger:hover {
  background-color: #F44336;
  color: white;
}

/* Estilos específicos para los datepickers */
:deep(.dp__input_wrap) {
  position: relative;
}

:deep(.dp__input) {
  padding-left: 2.5rem !important;
  background-color: white;
  border: 1px solid #ced4da;
  border-radius: 4px;
  width: 100%;
  height: 38px;
}

:deep(.dp__input_icon) {
  left: 12px;
  right: auto;
  color: #6c757d;
}

:deep(.dp__clear_icon) {
  right: 12px;
}

:deep(.dp__input_icon_pad) {
  padding-left: 2.5rem;
}

:deep(.form-label) {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #495057;
  font-size: 0.875rem;
}

@media (max-width: 768px) {
  .filters .row {
    flex-direction: column;
    gap: 10px;
  }
  
  .filters .col-md-3, 
  .filters .col-md-6 {
    width: 100%;
  }
  
  .review-actions {
    justify-content: flex-start;
  }
}
</style>
<template>
  <div class="updates-list">
    <!-- Notificación -->
    <div v-if="notification.show" 
         :class="['notification', notification.type]">
      {{ notification.message }}
    </div>

    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2>{{ $t('updates.title') }}</h2>
      <button 
        v-if="isAdmin" 
        @click="showCreateForm" 
        class="btn btn-create-update"
      >
        {{ $t('updates.createNew') }}
      </button>
    </div>

    <!-- Filtros -->
    <div class="filters mb-4 p-3 bg-white rounded">
      <div class="row g-3">
        <div class="col-md-6">
          <input 
            v-model="filterPark" 
            type="text" 
            class="form-control" 
            :placeholder="$t('updates.filterPark')" 
          />
        </div>
        <div class="col-md-6">
          <input 
            v-model="filterUser" 
            type="text" 
            class="form-control" 
            :placeholder="$t('updates.filterUser')" 
          />
        </div>

        <!-- Filtro por fechas -->
        <div class="col-md-6 position-relative">
          <label class="form-label small mb-1 d-block">{{ $t('updates.fromDate') }}</label>
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
          <label class="form-label small mb-1 d-block">{{ $t('updates.toDate') }}</label>
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



    <!-- Contador de updates -->
    <div class="update-counter mb-3" v-if="!loading">
      {{ $t('updates.showing', { count: filteredUpdates.length }) }}
    </div>

    <div v-if="loading" class="text-center my-4">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">{{ $t('updates.loading') }}</span>
      </div>
    </div>

    <p v-else-if="filteredUpdates.length === 0" class="no-elements">
      {{ $t('updates.noUpdates') }}
    </p>

    <div v-else class="updates-container mt-3">
      <div v-for="update in filteredUpdates" :key="update.id" class="update-card">
        <div class="update-header">
          <div>
            <span class="update-title">{{ update.title }}</span>
            <span class="update-author ms-2">
              {{ update.usuario?.nombre || $t('updates.anonymous') }}
            </span>
          </div>
          <div class="update-date">
            {{ formatDate(update.creationDate) }}
          </div>
        </div>
        
        <div class="update-content">
          <p><strong>{{ $t('updates.park') }}:</strong> {{ update.park }}</p>
          <p>{{ update.description }}</p>
        </div>

        <div class="update-actions mt-2" v-if="isAdmin">
          <button 
            @click="editUpdate(update)" 
            class="btn btn-sm btn-primary me-2 action-btn"
          >
            {{ $t('updates.edit') }}
          </button>

          <button 
            @click="confirmDelete(update)" 
            class="btn btn-sm btn-outline-danger action-btn"
          >
            {{ $t('updates.delete') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Modal para crear/editar update -->
    <UpdateFormModal 
      v-if="showModal"
      :show="showModal"  
      :updateToEdit="selectedUpdate"
      :parkName="selectedPark"
      @close="closeModal"
      @saved="handleUpdateSaved"
    />
  </div>
</template>
<script>
import UpdateRepository from "@/repositories/UpdateRepository";
import auth2 from "@/common/auth2";
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import UpdateFormModal from "@/components/UpdateFormModal.vue";

export default {
  name: "UpdateList",
  components: {
    Datepicker,
    UpdateFormModal
  },
  data() {
    return {
      updates: [],
      loading: true,
      filterPark: "",
      filterUser: "",
      startDate: null,
      endDate: null,
      isAdmin: auth2.isAdmin(),
      showModal: false,
      selectedUpdate: null,
      selectedPark: this.$route.query.park || "",
      notification: {
        show: false,
        type: '', // 'success' or 'error'
        message: ''
      },
      dayNames: [
        this.$t('updates.days.sun'), 
        this.$t('updates.days.mon'),
        this.$t('updates.days.tue'),
        this.$t('updates.days.wed'),
        this.$t('updates.days.thu'),
        this.$t('updates.days.fri'),
        this.$t('updates.days.sat')
      ],
      monthNames: [
        this.$t('updates.months.jan'),
        this.$t('updates.months.feb'),
        this.$t('updates.months.mar'),
        this.$t('updates.months.apr'),
        this.$t('updates.months.may'),
        this.$t('updates.months.jun'),
        this.$t('updates.months.jul'),
        this.$t('updates.months.aug'),
        this.$t('updates.months.sep'),
        this.$t('updates.months.oct'),
        this.$t('updates.months.nov'),
        this.$t('updates.months.dec')
      ]
    };
  },
  mounted() {
    this.loadUpdates();
  },
  created() {
    if (this.$route.query.park) {
      this.filterPark = this.$route.query.park;
    }
  },
  computed: {
    filteredUpdates() {
      return this.updates.filter((update) => {
        const matchesPark = update.park?.toLowerCase().includes(this.filterPark.toLowerCase());
        const matchesUser = update.usuario?.nombre?.toLowerCase().includes(this.filterUser.toLowerCase());
        const matchesDate = this.filterByDate(update.creationDate);

        return matchesPark && matchesUser && matchesDate;
      });
    }
  },
  methods: {
    showNotification(type, message) {
      this.notification = {
        show: true,
        type,
        message
      };
      setTimeout(() => {
        this.notification.show = false;
      }, 5000);
    },
    async loadUpdates() {
      try {
        const response = await UpdateRepository.findAll();
        this.updates = response.map(update => ({
          ...update,
          usuario: {
            nombre: update.userName || `User ${update.userId}`
          }
        }));
        this.updates.sort((a, b) => new Date(b.creationDate) - new Date(a.creationDate));
      } catch (error) {
        console.error("Error loading updates:", error);
        this.showNotification('error', this.$t('notifications.update.error'));
      } finally {
        this.loading = false;
      }
    },
    
    formatDate(dateString) {
      if (!dateString) return '';
      const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
      return new Date(dateString).toLocaleDateString(this.$i18n.locale, options);
    },
    
    filterByDate(dateString) {
      if (!dateString) return false;

      const updateDate = new Date(dateString);
      const start = this.startDate ? new Date(this.startDate) : null;
      const end = this.endDate ? new Date(this.endDate) : null;

      if (start && updateDate < start) return false;
      if (end) {
        end.setHours(23, 59, 59, 999);
        if (updateDate > end) return false;
      }

      return true;
    },
    
    handleStartDateChange(newStartDate) {
      if (this.endDate && newStartDate > this.endDate) {
        this.endDate = null;
      }
      this.startDate = newStartDate; 
    },
    
    showCreateForm() {
      this.selectedUpdate = null;
      this.selectedPark = this.$route.query.park || this.filterPark || "";
      this.showModal = true;
    },
    
    editUpdate(update) {
      this.selectedUpdate = update;
      this.selectedPark = update.park;
      this.showModal = true;
    },
    
    closeModal() {
      this.showModal = false;
      this.selectedUpdate = null;
    },
    
    confirmDelete(update) {
      if (confirm(this.$t('updates.confirmDelete'))) {
        this.deleteUpdate(update.id);
      }
    },
    async deleteUpdate(updateId) {
      try {
        await UpdateRepository.deleteById(updateId);
        this.showNotification('success', this.$t('notifications.update.deleted'));
        this.loadUpdates();
      } catch (error) {
        console.error("Error deleting update:", error);
        this.showNotification('error', this.$t('notifications.update.error'));
      }
    },
    handleUpdateSaved() {
      this.showNotification('success', this.$t('notifications.update.updated'));
      this.loadUpdates();
      this.closeModal();
    }
  }
};
</script>

<style scoped>
.notification {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  padding: 15px 25px;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  z-index: 1100;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s ease-out;
}

.notification.success {
  background-color: #4CAF50;
}

.notification.error {
  background-color: #F44336;
}

@keyframes slideIn {
  from {
    top: -100px;
    opacity: 0;
  }
  to {
    top: 20px;
    opacity: 1;
  }
}
.updates-list {
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

.update-counter {
  font-weight: bold;
  color: #009DE0;
  background-color: white;
  padding: 8px 12px;
  border-radius: 8px;
  display: inline-block;
}

.updates-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.update-card {
  display: flex;
  flex-direction: column;
  background-color: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  color: #333;
}

.update-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.update-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.update-title {
  font-weight: bold;
  font-size: 1.1rem;
  color: #009DE0;
}

.update-author {
  font-size: 0.9rem;
  color: #666;
}

.update-date {
  color: #666;
  font-size: 0.85rem;
}

.update-content {
  color: #333;
  line-height: 1.6;
}

.update-content p {
  margin-bottom: 8px;
}

.update-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #eee;
}

.action-btn {
  min-width: 80px; /* Ajusta este valor según necesites */
  text-align: center;
  padding: 0.25rem 0.5rem;
}

/* Para pantallas pequeñas */
@media (max-width: 768px) {
  .action-btn {
    min-width: 70px;
    font-size: 0.8rem;
  }
}

.no-elements {
  font-style: italic;
  color: white;
  margin-top: 20px;
  text-align: center;
}

.btn {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.btn-create-update {
  display: inline-block;
  padding: 10px 20px;
  background-color: transparent;
  color: #FFFFFF;
  border: 2px solid #FFFFFF;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition: all 0.3s ease;
  margin-left: auto;
}

.btn-create-update:hover {
  background-color: #FFFFFF;
  color: #009DE0;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.btn-outline-danger {
  border-color: #F44336;
  color: #F44336;
}

.btn-outline-danger:hover {
  background-color: #F44336;
  color: white;
}

.position-relative {
  position: relative;
}

@media (max-width: 768px) {
  .filters .row {
    flex-direction: column;
    gap: 10px;
  }
  
  .filters .col-md-6 {
    width: 100%;
  }
  
  .update-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .update-date {
    align-self: flex-end;
  }
  
  .update-actions {
    justify-content: flex-start;
  }
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
  color: #495057; /* Color oscuro para mejor contraste */
  font-size: 0.875rem; /* Tamaño consistente con otros labels */
}
</style>
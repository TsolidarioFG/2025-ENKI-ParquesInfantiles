<template>
  <div class="modal-overlay" v-if="show">
    <!-- Notificación -->
    <div v-if="notification.show" 
         :class="['notification', notification.type]">
      {{ notification.message }}
    </div>
    
    <div class="modal-content">

      <div class="modal-header">
        <h3>{{ $t(updateToEdit ? 'updates.editUpdate' : 'updates.createUpdate') }}</h3>
        <button @click="close" class="close-btn">&times;</button>
      </div>
      
      <div class="modal-body">
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="title">{{ $t('updates.title') }}:</label>
            <input 
              v-model="formData.title" 
              type="text" 
              id="title" 
              class="form-control" 
              required 
            />
          </div>
          
          <div class="form-group">
            <label for="description">{{ $t('updates.description') }}:</label>
            <textarea 
              v-model="formData.description" 
              id="description" 
              class="form-control" 
              rows="5" 
              required
            ></textarea>
          </div>
          
          <div class="form-group">
            <label for="park">{{ $t('updates.park') }}:</label>
            <select 
              v-model="formData.park" 
              id="park" 
              class="form-control" 
              required
            >
              <option value="" disabled>{{ $t('updates.selectPark') }}</option>
              <option v-for="park in parks" :key="park.id" :value="park.name">
                {{ park.name }}
              </option>
            </select>

          </div>
          
          <div class="form-actions">
            <button type="button" @click="close" class="btn btn-secondary">
              {{ $t('updates.cancel') }}
            </button>
            <button type="submit" class="btn btn-primary">
              {{ $t('updates.save') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import UpdateRepository from "@/repositories/UpdateRepository";
import ParkRepository from "@/repositories/ParkRepository";
import { getStore } from "@/common/store";

export default {
  name: "UpdateFormModal",
  props: {
    show: {
      type: Boolean,
      default: false
    },
    updateToEdit: {
      type: Object,
      default: null
    },
    parkName: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      formData: {
        title: "",
        description: "",
        park: "",
        userId: null
      },
      parks: [],
      notification: {
        show: false,
        type: '', // 'success' or 'error'
        message: ''
      }
    };
  },
  async created() {
    try {
      this.parks = await ParkRepository.findAll();
      // Set initial park value from prop or first park if available
      this.formData.park = this.parkName || (this.parks.length > 0 ? this.parks[0].name : "");
    } catch (error) {
      console.error("Error loading parks:", error);
    }
  },
  watch: {
    updateToEdit: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            title: newVal.title,
            description: newVal.description,
            park: newVal.park,
            userId: newVal.userId
          };
        } else {
          this.resetForm();
        }
      }
    },
    parkName(newVal) {
      // Only update if we're creating a new update (not editing)
      if (!this.updateToEdit) {
        this.formData.park = newVal || (this.parks.length > 0 ? this.parks[0].name : "");
      }
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
    resetForm() {
      this.formData = {
        title: "",
        description: "",
        park: this.parkName || (this.parks.length > 0 ? this.parks[0].name : ""),
        userId: null
      };
    },
    close() {
      this.$emit("close");
    },    
    async handleSubmit() {
      try {
        const user = getStore().state.usuario;
        
        if (!user?.id) {
          const errorMsg = this.$t('updates.loginRequired');
          throw new Error(errorMsg);
        }

        if (!this.updateToEdit?.id) {
          this.formData.userId = user.id;
          this.formData.nombreUsuario = user.nombreUsuario;
        }
        
        if (this.updateToEdit?.id) {
          await UpdateRepository.save({ ...this.formData, id: this.updateToEdit.id });
          this.showNotification('success', this.$t('notifications.update.updated'));
        } else {
          await UpdateRepository.save(this.formData);
          this.showNotification('success', this.$t('notifications.update.created'));
        }
        
        this.$emit("saved");
        this.close();
      } catch (error) {
        console.error("Error saving update:", error);
        this.showNotification('error', error.response?.data?.message || error.message || this.$t('notifications.update.error'));
      }
    }
  }
};

</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  color: black;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  color: #009DE0;
  margin: 0;
  font-size: 1.25rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

textarea.form-control {
  min-height: 120px;
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.btn {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
  border: 1px solid #6c757d;
}

.btn-secondary:hover {
  background-color: #5a6268;
  border-color: #545b62;
}

.btn-primary {
  background-color: #009DE0;
  color: white;
  border: 1px solid #009DE0;
}

.btn-primary:hover {
  background-color: #0088cc;
  border-color: #0077b3;
}

.text-muted {
  color: #6c757d;
  font-size: 0.8rem;
  display: block;
  margin-top: 0.25rem;
}

@media (max-width: 576px) {
  .modal-content {
    width: 95%;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .btn {
    width: 100%;
  }
}
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
</style>
<template>
  <div class="user-list">
    <!-- Notificación -->
    <div v-if="notification.show" 
         :class="['notification', notification.type]">
      {{ notification.message }}
    </div>
    
    <div class="header-section">
      <h2>{{ $t("usuarioList.title") }}</h2>
      
      <div class="controls">
        <div class="filter-controls">
          <div class="toggle-switch">
            <input type="checkbox" id="mostrar-admins" v-model="showAdmins" class="switch-input" />
            <label for="mostrar-admins" class="switch-label">
              <span class="switch-button"></span>
              <span class="switch-text">{{ $t("usuarioList.showAdmins") }}</span>
            </label>
          </div>
        </div>
        
        <div class="search-box">
          <input 
            type="text" 
            v-model="searchQuery" 
            :placeholder="$t('usuarioList.searchPlaceholder')" 
            class="search-input"
          />
          <i class="search-icon bi bi-search"></i>
        </div>
      </div>
    </div>

    <div v-if="filteredUsuarios.length > 0" class="user-grid">
      <div 
        v-for="usuario in filteredUsuarios" 
        :key="usuario.id" 
        class="user-card"
        :class="{ 'admin-card': usuario.authority === 'ADMIN' }"
      >
        <div class="user-avatar">
          {{ getInitials(usuario.nombre, usuario.apellido) }}
        </div>
        
        <div class="user-info">
          <h3 class="username">{{ usuario.nombreUsuario }}</h3>
          <p class="name">{{ usuario.nombre }} {{ usuario.apellido }}</p>
          
          <div class="user-details">
            <div class="detail-item">
              <i class="detail-icon bi bi-phone"></i>
              <span>{{ usuario.telefono || $t('usuarioList.notProvided') }}</span>
            </div>
            <div class="detail-item">
              <i class="detail-icon bi bi-person-badge"></i>
              <span>{{ $t(`registerForm.userTypes.${usuario.userType}`) }}</span>
            </div>
            <div class="detail-item">
              <i class="detail-icon bi bi-star"></i>
              <span>{{ usuario.favoritos.length }} {{ $t("usuarioList.favorites") }}</span>
            </div>
            
            <div class="detail-item" v-if="usuario.authority === 'ADMIN'">
              <span class="admin-badge">{{ $t("usuarioList.admin") }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <i class="empty-icon bi bi-emoji-frown"></i>
      <h3>{{ $t("usuarioList.noUsersFound") }}</h3>
      <p>{{ $t("usuarioList.tryAdjustingFilters") }}</p>
    </div>
  </div>
</template>

<script>
import FavoritosRepository from "@/repositories/FavoritosRepository";

export default {
  name: "UserList",
  data() {
    return {
      usuarios: [],
      showAdmins: false,
      searchQuery: '',
      favorites: new Set(),
      notification: {
        show: false,
        type: '', // 'success' or 'error'
        message: ''
      }
    };
  },
  computed: {
    filteredUsuarios() {
      let usuariosFiltrados = [...this.usuarios].map(user => ({
        ...user,
        isFavorite: this.favorites.has(user.id)
      }));

      if (this.showAdmins) {
        usuariosFiltrados = usuariosFiltrados.filter(usuario => usuario.authority === "ADMIN");
      }

      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        usuariosFiltrados = usuariosFiltrados.filter(usuario => 
          usuario.nombreUsuario.toLowerCase().includes(query) ||
          usuario.nombre.toLowerCase().includes(query) ||
          usuario.apellido.toLowerCase().includes(query) ||
          usuario.telefono?.includes(query)
        );
      }

      return usuariosFiltrados;
    }
  },
  methods: {
    getInitials(firstName, lastName) {
      return `${firstName?.charAt(0) || ''}${lastName?.charAt(0) || ''}`.toUpperCase();
    },
    showNotification(type, message) {
      this.notification = {
        show: true,
        type,
        message
      };
      setTimeout(() => {
        this.notification.show = false;
      }, 5000);
    }
  },
  async mounted() {
    try {
      this.usuarios = await FavoritosRepository.findAll();
    } catch (error) {
      console.error("Error al cargar los usuarios:", error);
      this.showNotification('error', this.$t("notifications.user.loadError"));
    }
  }
};
</script>

<style scoped>
.user-list {
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

.user-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 15px;
}

.user-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  display: flex;
  position: relative;
  color: #333;
}

.user-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.admin-card {
  border-left: 4px solid #F44336;
}

.user-avatar {
  width: 50px;
  height: 50px;
  background-color: #009DE0;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 1.2rem;
  margin-right: 15px;
  flex-shrink: 0;
}

.admin-card .user-avatar {
  background-color: #F44336;
}

.user-info {
  flex-grow: 1;
}

.username {
  margin: 0 0 5px 0;
  color: #009DE0;
  font-size: 1.1rem;
}

.name {
  margin: 0 0 10px 0;
  color: #666;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-item {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  color: #666;
}

.detail-icon {
  margin-right: 8px;
  color: #009DE0;
}

.admin-badge {
  background-color: #F44336;
  color: white;
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: bold;
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

/* Estilos para la notificación */
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
  
  .user-grid {
    grid-template-columns: 1fr;
  }
}
</style>
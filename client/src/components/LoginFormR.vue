<template>
  <div class="auth-container">
     <!-- Success Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="showSuccessAlert"
      style="background-color: #d4edda; color: #155724; margin-bottom: 20px;"
    >
      <strong>{{ $t("login.successTitle") }}</strong> {{ $t("login.successMessage") }}
      <button type="button" class="btn-close" @click="showSuccessAlert = false"></button>
    </div>

    <!-- Error Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="errorMessage"
      style="background-color: #f8d7da; color: #721c24; margin-bottom: 20px;"
    >
      <strong>{{ $t("login.errorTitle") }}</strong> {{ errorMessage }}
      <button type="button" class="btn-close" @click="errorMessage = ''"></button>
    </div>

    <div class="auth-card">
      <div class="auth-header">
        <h3>{{ $t("login.title") }}</h3>
      </div>
      <div class="auth-body">
        <form @submit.prevent="autenticarme">
          <div class="form-group">
            <label for="nombreUsuario" class="form-label">{{ $t("login.username") }}</label>
            <input
              type="text"
              id="nombreUsuario"
              class="form-control"
              v-model="auxNombreUsuario"
              @keyup.enter="autenticarme"
              required
            />
          </div>
          <div class="form-group">
            <label for="pass" class="form-label">{{ $t("login.password") }}</label>
            <input
              type="password"
              id="pass"
              class="form-control"
              v-model="auxContraseña"
              @keyup.enter="autenticarme"
              required
            />
          </div>
          <button type="submit" class="btn-primary">{{ $t("login.loginButton") }}</button>
        </form>
        <div class="auth-footer">
          <span>{{ $t("login.noAccount") }}</span>
          <router-link to="/register">{{ $t("login.registerHere") }}</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import auth2 from "../common/auth2.js";

export default {
  data() {
    return {
      auxNombreUsuario: null,
      auxContraseña: null,
       showSuccessAlert: false,
      errorMessage: ""
    };
  },
  methods: {
    async autenticarme() {
      if (!this.auxNombreUsuario || !this.auxContraseña) {
        this.errorMessage = this.$t("login.errorMissingFields");
        return;
      }
      if (this.auxContraseña.length < 8) {
        this.errorMessage = this.$t("login.errorPasswordLength");
        return;
      }
      try {
        await auth2.login({
          nombreUsuario: this.auxNombreUsuario,
          contraseña: this.auxContraseña
        });
        this.showSuccessAlert = true;
        setTimeout(() => {
          this.$router.push("/map");
        }, 1000);
      } catch (e) {
        console.error(e);
        if (e.response?.data?.message) {
          this.errorMessage = `${this.$t("login.error")}: ${e.response.data.message}`;
        } else {
          this.errorMessage = this.$t("login.genericError");
        }
      }
    }
  }
};
</script>

<style scoped>
.auth-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  padding: 20px;
  gap: 20px; 
}

.auth-card {
  width: 100%;
  max-width: 500px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.auth-header {
  background-color: #009DE0;
  color: white;
  padding: 20px;
  text-align: center;
}

.auth-header h3 {
  margin: 0;
  font-size: 1.8rem;
}

.auth-body {
  padding: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
  font-size: 1.1rem;
}

.form-control {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 1rem;
  
}

.form-control:focus {
  border-color: #009DE0;
  box-shadow: 0 0 0 2px rgba(0, 157, 224, 0.2);
  outline: none;
}

.btn-primary {
  width: 100%;
  padding: 12px;
  background-color: #009DE0;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1.5rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-primary:hover {
  background-color: #0085c7;
}

.auth-footer {
  margin-top: 20px;
  text-align: center;
  color: #666;
}

.auth-footer a {
  color: #009DE0;
  text-decoration: none;
  margin-left: 5px;
  
}

.auth-footer a:hover {
  text-decoration: underline;
}
</style>
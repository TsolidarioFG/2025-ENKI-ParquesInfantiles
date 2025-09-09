<template>
  <div class="auth-container">
    <!-- Success Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="showSuccessAlert"
      style="background-color: #d4edda; color: #155724;"
    >
      <strong>{{ $t('registerForm.successTitle') }}</strong> {{ $t('registerForm.successMessage') }}
      <button type="button" class="btn-close" @click="showSuccessAlert = false"></button>
    </div>

    <!-- Error Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="errorMessage"
      style="background-color: #f8d7da; color: #721c24;"
    >
      <strong>{{ $t('registerForm.errorTitle') }}</strong> {{ errorMessage }}
      <button type="button" class="btn-close" @click="errorMessage = ''"></button>
    </div>

    <div class="auth-card">
      <div class="auth-header">
        <h3>{{ $t("registerForm.title") }}</h3>
      </div>
      <div class="auth-body">
        <form @submit.prevent="registrarme">
          <div class="form-group">
            <label for="nombreUsuario" class="form-label">{{ $t("registerForm.username") }}:</label>
            <input type="text" id="nombreUsuario" class="form-control" v-model="auxNombreUsuario" @keyup.enter="registrarme" required />
          </div>
          <div class="form-group">
            <label for="pass" class="form-label">{{ $t("registerForm.password") }}:</label>
            <input type="password" id="pass" class="form-control" v-model="auxContraseña" @keyup.enter="registrarme" required />
          </div>
          <div class="form-group">
            <label for="pass2" class="form-label">{{ $t("registerForm.confirmPassword") }}:</label>
            <input type="password" id="pass2" class="form-control" v-model="auxContraseña2" @keyup.enter="registrarme" required />
          </div>
          <div class="form-group">
            <label for="nombre" class="form-label">{{ $t("registerForm.firstName") }}:</label>
            <input type="text" id="nombre" class="form-control" v-model="auxNombre" @keyup.enter="registrarme" required />
          </div>
          <div class="form-group">
            <label for="apellido" class="form-label">{{ $t("registerForm.lastName") }}:</label>
            <input type="text" id="apellido" class="form-control" v-model="auxApellido" @keyup.enter="registrarme" required />
          </div>
          <div class="form-group">
            <label for="telefono" class="form-label">{{ $t("registerForm.phone") }}:</label>
            <input type="number" id="telefono" class="form-control" v-model="auxTelefono" @keyup.enter="registrarme" required />
          </div>
          <div class="form-group">
            <label for="email" class="form-label">{{ $t("registerForm.email") }}:</label>
            <input type="text" id="email" class="form-control" v-model="auxEmail" @keyup.enter="registrarme" required />
          </div>
           <div class="form-group">
            <label for="userType" class="form-label">{{ $t("registerForm.userType") }}:</label>
            <select id="userType" class="form-control" v-model="auxUserType" required>
              <option v-for="type in userTypes" :key="type" :value="type">
                {{ $t(`registerForm.userTypes.${type}`) }}
              </option>
            </select>
          </div>
          <button type="submit" class="btn-primary">{{ $t("registerForm.registerButton") }}</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import auth2 from "../common/auth2.js";

export default {
  data() {
    return {
      showSuccessAlert: false,
      errorMessage: '',
      auxNombreUsuario: null,
      auxContraseña: null,
      auxContraseña2: null,
      auxNombre: null,
      auxApellido: null,
      auxTelefono: null,
      auxEmail: null,
      auxUserType: null,
      userTypes: [
        'FAMILIAR_O_ACOMPAÑANTE',
        'ORGANIZACIÓN_NO_GUBERNAMENTAL',
        'PERSONA_CON_DISCAPACIDAD',
        'PROFESIONAL_DEL_ÁMBITO_SOCIAL',
        'EDUCATIVO_O_SANITARIO',
        'TÉCNICO_A_O_PERSONAL_MUNICIPAL',
        'VISITANTE_OCASIONAL'
      ]
    };
  },
  methods: {
    validarCampos() {
      if (!this.auxNombreUsuario || !this.auxContraseña || !this.auxContraseña2 || 
          !this.auxNombre || !this.auxApellido || !this.auxTelefono || !this.auxEmail) {
        this.errorMessage = this.$t("registerForm.validation.emptyFields");
        return false;
      }
      if (this.auxContraseña.length < 8) {
        this.errorMessage = this.$t("registerForm.validation.shortPassword");
        return false;
      }
      if (this.auxContraseña !== this.auxContraseña2) {
        this.errorMessage = this.$t("registerForm.validation.passwordMismatch");
        return false;
      }
      const telefonoRegex = /^[0-9]{9}$/;
      if (!telefonoRegex.test(this.auxTelefono)) {
        this.errorMessage = this.$t("registerForm.validation.invalidPhone");
        return false;
      }
      return true;
    },

     async registrarme() {
      if (!this.validarCampos()) return;

      const payload = {
        nombreUsuario: this.auxNombreUsuario,
        contraseña: this.auxContraseña,
        nombre: this.auxNombre,
        apellido: this.auxApellido,
        telefono: this.auxTelefono,
        email: this.auxEmail,
        userType: this.auxUserType
      };

      try {
        await auth2.register(payload);
        await auth2.login({ nombreUsuario: this.auxNombreUsuario, contraseña: this.auxContraseña });
        this.showSuccessAlert = true;
        setTimeout(() => {
          this.$router.push("/map");
        }, 1500);
      } catch (e) {
        console.error(e);
        this.errorMessage = e.response?.data?.message || this.$t("registerForm.errors.registrationFailed");
      }
    }
  }
};
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
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  padding: 20px;
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
</style>
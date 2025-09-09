<template>
  <div class="container mt-5">
    <div class="card shadow-sm">
      <div class="card-header text-center bg-primary text-white">
        <h3>{{ $t("profile.title") }}</h3>
      </div>
      <div class="card-body">
        <form @submit.prevent="updateAccount" v-if="isEditing && !isChangingPassword">
          <div class="mb-3">
            <label for="nombre" class="form-label">{{ $t("profile.name") }}::</label>
            <input
              v-model="usuario.nombre"
              id="nombre"
              class="form-control"
              :readonly="!isEditing"
              required
            />
          </div>
          <div class="mb-3">
            <label for="apellido" class="form-label">{{ $t("profile.surname") }}:</label>
            <input
              v-model="usuario.apellido"
              id="apellido"
              class="form-control"
              :readonly="!isEditing"
              required
            />
          </div>
          <div class="mb-3">
            <label for="telefono" class="form-label">{{ $t("profile.phone") }}:</label>
            <input
              type="number"
              v-model="usuario.telefono"
              id="telefono"
              class="form-control"
              :readonly="!isEditing"
              required
            />
          </div>
          <div class="mb-3">
            <label for="nombreUsuario" class="form-label">{{ $t("profile.username") }}:</label>
            <input
              v-model="usuario.nombreUsuario"
              id="nombreUsuario"
              class="form-control"
              :readonly="!isEditing"
              required
            />
          </div>
          <div class="mb-3">
            <label for="email" class="form-label">{{ $t("profile.email") }}:</label>
            <input
              v-model="usuario.email"
              id="email"
              class="form-control"
              :readonly="!isEditing"
              required
            />
          </div>
          <div class="mb-3">
        <label for="userType" class="form-label">{{ $t("profile.userType") }}:</label>
        <select 
          v-model="usuario.userType" 
          id="userType" 
          class="form-control"
          :disabled="!isEditing"
          required
        >
          <option v-for="type in userTypes" :key="type" :value="type">
            {{ $t(`registerForm.userTypes.${type}`) }}
          </option>
        </select>
      </div>
          <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-success">{{ $t("profile.save") }}</button>
            <button type="button" class="btn btn-outline-primary" @click="toggleChangePassword">
              {{ $t("profile.changePassword") }}
            </button>
          </div>
        </form>

        <form @submit.prevent="changePassword" v-if="isChangingPassword">
          <div class="mb-3">
            <label for="currentPassword" class="form-label"><label for="currentPassword" class="form-label">{{ $t("profile.currentPassword") }}:</label></label>
            <input
              type="password"
              v-model="passwords.currentPassword"
              id="currentPassword"
              class="form-control"
              required
            />
          </div>
          <div class="mb-3">
            <label for="newPassword" class="form-label"><label for="newPassword" class="form-label">{{ $t("profile.newPassword") }}:</label></label>
            <input
              type="password"
              v-model="passwords.newPassword"
              id="newPassword"
              class="form-control"
              required
            />
          </div>
          <div class="mb-3">
            <label for="confirmNewPassword" class="form-label"><label for="confirmNewPassword" class="form-label">{{ $t("profile.confirmNewPassword") }}:</label></label>
            <input
              type="password"
              v-model="passwords.confirmNewPassword"
              id="confirmNewPassword"
              class="form-control"
              required
            />
          </div>
          <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-success">{{ $t("profile.changePassword") }}</button>
            <button type="button" class="btn btn-outline-secondary" @click="toggleChangePassword">
              {{ $t("profile.cancel") }}
            </button>
          </div>
        </form>

        <div v-if="!isEditing && !isChangingPassword">
          <p><strong><p><strong>{{ $t("profile.name") }}:</strong> </p></strong> {{ usuario.nombre }}</p>
          <p><strong><p><strong>{{ $t("profile.surname") }}:</strong> </p></strong> {{ usuario.apellido }}</p>
          <p><strong><p><strong>{{ $t("profile.phone") }}:</strong></p></strong> {{ usuario.telefono }}</p>
          <p><strong><p><strong>{{ $t("profile.username") }}:</strong></p></strong> {{ usuario.nombreUsuario }}</p>
          <p><strong><p><strong>{{ $t("profile.email") }}:</strong></p></strong> {{ usuario.email }}</p>
          <p><strong><p><strong>{{ $t("profile.userType") }}:</strong> </p></strong>{{ $t(`registerForm.userTypes.${usuario.userType}`) }}</p>
          <div class="d-flex justify-content-between">
            <button class="btn btn-outline-primary" @click="toggleEdit">{{ $t("profile.edit") }}</button>
            <button class="btn btn-outline-secondary" @click="seeFavorites">{{ $t("profile.favorites") }}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import auth2 from "@/common/auth2";
import CuentaRepository from "@/repositories/CuentaRepository";
import { getStore } from "@/common/store";

export default {
  data() {
    return {
      usuario: {
        nombre: "",
        apellido: "",
        telefono: "",
        nombreUsuario: "",
        contraseña: "",
        email:"",
        userType: ""
      },
      userTypes: [
        'FAMILIAR_O_ACOMPAÑANTE',
        'ORGANIZACIÓN_NO_GUBERNAMENTAL',
        'PERSONA_CON_DISCAPACIDAD',
        'PROFESIONAL_DEL_ÁMBITO_SOCIAL',
        'EDUCATIVO_O_SANITARIO',
        'TÉCNICO_A_O_PERSONAL_MUNICIPAL',
        'VISITANTE_OCASIONAL'
      ],
      passwords: {
        currentPassword: "",
        newPassword: "",
        confirmNewPassword: ""
      },
      isEditing: false,
      isChangingPassword: false
    };
  },
  async created() {
    this.usuario = await CuentaRepository.getAccount();
  },
  methods: {
    toggleEdit() {
      this.isEditing = !this.isEditing;
    },
    toggleChangePassword() {
      this.isChangingPassword = !this.isChangingPassword;
      this.isEditing = false;
    },
    async updateAccount() {
      const store = getStore();
      try {
        const nombreUsuarioAnterior = store.state.usuario.nombreUsuario;

        let usuarioActualizado = { ...this.usuario };
        const a = await CuentaRepository.updateAccount(usuarioActualizado);
        usuarioActualizado.nombreUsuario = a.nombreUsuario;

        if (usuarioActualizado.nombreUsuario !== nombreUsuarioAnterior) {
          auth2.logout();
          alert("El nombre de usuario ha sido cambiado. Por favor, inicia sesión nuevamente.");
          this.$router.push("/");
        } else {
          alert("Perfil actualizado exitosamente.");
          this.$router.push("/parks");
        }
        this.isEditing = false;
      } catch (error) {
        console.error(error);
        alert(`Error: ${error.response.data.message}`);
      }
    },
    async changePassword() {
      try {
        try {
          await auth2.login({
            nombreUsuario: this.usuario.nombreUsuario,
            contraseña: this.passwords.currentPassword
          });
        } catch (e) {
          console.error(e);
          alert("La contraseña actual no es correcta");
          return;
        }

        if (this.passwords.newPassword !== this.passwords.confirmNewPassword) {
          alert("Las contraseñas no coinciden.");
          return;
        }
        if (this.passwords.newPassword.length < 8) {
          alert("La contraseña debe tener, como mínimo, 8 caracteres.");
          return;
        }

        const updatedUsuario = {
          ...this.usuario,
          contraseña: this.passwords.newPassword
        };
        this.usuario.contraseña = this.passwords.newPassword;

        await CuentaRepository.updateAccountWithPassword(updatedUsuario);

        alert("Contraseña cambiada exitosamente.");
        this.isChangingPassword = false;

        this.passwords.currentPassword = "";
        this.passwords.newPassword = "";
        this.passwords.confirmNewPassword = "";
      } catch (error) {
        console.error(error);
        alert(`Error: ${error.response.data.message}`);
      }
    },
    seeFavorites() {
      this.$router.push({ name: "Favorites" });
    }
  }
};
</script>
<style>
.container {
  max-width: 600px;
}
.card {
  border-radius: 10px;
  overflow: hidden;
}
.card-header {
  font-size: 1.5rem;
  font-weight: bold;
}
.form-control {
  border-radius: 5px;
}
.btn {
  border-radius: 5px;
}
</style>

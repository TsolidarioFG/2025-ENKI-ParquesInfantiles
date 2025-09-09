<template>
  <nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/map">
        <img 
          src="@/assets/enki_logo.svg" 
          alt="Logo de la compañía"
          class="navbar-logo"
        >
      </router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0" v-if="store.state.usuario.logged">
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="javascript:void(0)"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
              ref="dropdownElement"
            >
              {{ $t('navbar.inicio') }}
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link class="dropdown-item" to="/favorites" active-class="active">{{ $t('navbar.favoritos') }}</router-link>
              </li>
              <li>
                <router-link class="dropdown-item" to="/map" active-class="active">{{ $t('navbar.inicio') }}</router-link>
              </li>
              <li v-if="isAdmin">
                <router-link class="dropdown-item" to="/updates" active-class="active">{{ $t('navbar.listaUpdates') }}</router-link>
              </li>
              <li v-if="isAdmin">
                <router-link class="dropdown-item" to="/incidents" active-class="active">{{ $t('navbar.listaIncidencias') }}</router-link>
              </li>
              <li>
                <router-link class="dropdown-item" to="/parks" active-class="active">{{ $t('navbar.listaParques') }}</router-link>
              </li>
              <li>
                <router-link class="dropdown-item" to="/reviews" active-class="active">{{ $t('navbar.listaResenas') }}</router-link>
              </li>

              <li v-if="isAdmin">
                <router-link class="dropdown-item" to="/listaUsuarios" active-class="active">{{ $t('navbar.listaUsuarios') }}</router-link>
              </li>
   
            </ul>
          </li>
        </ul>

        <div class="d-flex align-items-center ms-auto">
          <!-- Language Selector -->
          <div class="language-selector me-3">
            <div class="dropdown">
              <button class="btn dropdown-toggle" type="button" id="languageDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                {{ $t('navbar.language') }}: {{ $i18n.locale }}
              </button>
              <ul class="dropdown-menu" aria-labelledby="languageDropdown">
                <li>
                  <a class="dropdown-item" @click="$i18n.locale = 'es'">{{ $t('navbar.idiomaEspañol') }}</a>
                </li>
                <li>
                  <a class="dropdown-item" @click="$i18n.locale = 'gl'">{{ $t('navbar.idiomaGalego') }}</a>
                </li>
                <li>
                  <a class="dropdown-item" @click="$i18n.locale = 'en'">{{ $t('navbar.idiomaIngles') }}</a>
                </li>
              </ul>
            </div>
          </div>

          <!-- Profile and Logout Icons -->
          <ul class="navbar-nav" v-if="store.state.usuario.logged">
            <li class="nav-item">
              <router-link class="nav-link" to="/perfilPage" active-class="active" :title="$t('navbar.verPerfil')">
                <i class="bi bi-person-fill"></i>
              </router-link>
            </li>
            <li class="nav-item">
              <a class="nav-link" @click="desautenticarme()" :title="$t('navbar.logout')">
                <i class="bi bi-box-arrow-right"></i>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </nav>
  <router-view />
</template>

<script>
import { getStore } from "./common/store";
import auth2 from "./common/auth2";

export default {
  data() {
    return {
      store: getStore()
    };
  },
  computed: {
    isAdmin() {
      return auth2.isAdmin();
    }
  },
  methods: {
    guardarIdioma() {
      localStorage.setItem("idioma", this.$i18n.locale);
    },
    desautenticarme() {
      auth2.logout();
      this.$router.push("/");
    }
  },
  watch: {
    $route(newValue) {
      if (this.store.state.usuario.logged) {
        if (["NoteList", "NoteListSetup"].includes(newValue.name)) {
          this.$refs.dropdownElement.classList.add("active");
        } else {
          this.$refs.dropdownElement.classList.remove("active");
        }
      }
    }
  }
};
</script>

<style scoped>
#app {
  font-family: 'Labil Grotex', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #FFD700;
  background-color: #FFD700;
}

.navbar-logo {
  height: 40px;
  width: auto;
  max-width: 150px;
  object-fit: contain;
}

.navbar {
  background-color: #007BFF;
  padding: 20px;
}

.navbar-brand {
  font-weight: bold;
  color: white;
  font-family: 'Totally Heavy', sans-serif;
  
}

.navbar-nav .nav-link {
  color: white;
  font-family: 'Totally Heavy', sans-serif;
  padding: 0.5rem 1rem;
  font-size: 1.5rem; 
}

.navbar-nav .nav-link:hover {
  color: #FFD700;
}

/* Icon styles */
.bi {
  font-size: 1.5rem;
  color: white;
}

.bi:hover {
  color: #FFD700;
}

.language-selector .dropdown-toggle {
  background-color: #007BFF;
  color: white;
  border: 1px solid white;
  font-size: 14px;
  padding: 5px 10px;
  
}

.language-selector .dropdown-toggle:hover {
  color: #FFD700;
  border-color: #FFD700;
}

.dropdown-menu {
  background-color: #007BFF;
  border: none;
}

.dropdown-item {
  color: white;
  font-size: 1.1rem;
}

.dropdown-item:hover {
  background-color: #0056b3;
  color: #FFD700;
}

.active {
  font-weight: bold;
  color: #FFD700;
}
</style>
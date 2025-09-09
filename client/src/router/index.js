import { createRouter, createWebHistory } from "vue-router";

import ErrorNotFoundView from "../views/ErrorNotFoundView.vue";

import LoginFormR from "../components/LoginFormR.vue";

import RegisterFormR from "@/components/RegisterFormR.vue";
import PerfilPage from "@/components/PerfilPage.vue";
import auth2 from "@/common/auth2";
import { getStore } from "@/common/store";
import UsuariosList from "@/components/UsuariosList.vue";
import MapView from "@/components/MapView.vue";
import ParkList from "@/components/ParkList.vue";
import ParkDetail from "@/components/ParkDetail.vue";
import GameElementDetail from "@/components/GameElementDetail.vue";
import ParkForm from "@/components/ParkForm.vue";
import IncidentList from "@/components/IncidentList.vue";
import IncidentForm from "@/components/IncidentForm.vue";
import GameElementForm from "@/components/GameElementForm.vue";
import ReviewList from "@/components/ReviewList.vue";
import Konva from "@/components/Konva.vue";
import FavoriteList from "@/components/FavoriteList.vue";
import UpdateList from "@/components/UpdateList.vue";

const routes = [
  {
    path: "/",
    name: "Login",
    component: LoginFormR,
    meta: { public: true, isLoginPage: true }
  },
  {
    path: "/register",
    name: "Register",
    component: RegisterFormR,
    meta: { public: true, isLoginPage: true }
  },
  {
    path: "/:catchAll(.*)*",
    component: ErrorNotFoundView,
    meta: { public: true }
  },
  
  {
    path: "/perfilPage",
    name: "PerfilPage",
    component: PerfilPage
  },  
  {
    path: "/listaUsuarios",
    name: "ListaUsuarios",
    component: UsuariosList,
    beforeEnter: (to, from, next) => {
      if (auth2.isAdmin()) {
        next();
      } else {
        alert("Acceso denegado: solo los administradores pueden acceder a la lista de usuarios.");
        next("/");
      }
    }
  },
  {
    path: "/reviews",
    name: "ReviewList",
    component: ReviewList
  },
  {
    path: "/map",
    name: "MapView",
    component: MapView
  },
  {
    path: "/parks",
    name: "ParkList",
    component: ParkList
  },
  {
    path: "/parks/:parkId",
    name: "ParkDetail",
    component: ParkDetail,
  },
  {
    path: '/game-element/:gameElementId',
    name: 'GameElementDetail',
    component: GameElementDetail
  },
  {
    path: "/parks/new",
    name: "ParkCreate",
    component: ParkForm,
    beforeEnter: (to, from, next) => {
      if (auth2.isAdmin()) {
        next();
      } else {
        alert("Acceso denegado: solo los administradores pueden acceder a crear un parque.");
        next("/");
      }
    }
  },
  {
    path: "/parks/:parkId/edit",
    name: "ParkEdit",
    component: ParkForm,
    beforeEnter: (to, from, next) => {
      if (auth2.isAdmin()) {
        next();
      } else {
        alert("Acceso denegado: solo los administradores pueden acceder a editar un parque.");
        next("/");
      }
    }
  },
  {
    path: "/incidents",
    name: "IncidentList",
    component: IncidentList,
    beforeEnter: (to, from, next) => {
      if (auth2.isAdmin()) {
        next();
      } else {
        alert("Acceso denegado: solo los administradores pueden acceder a la lista de incidentes.");
        next("/");
      }
    }
  },
  {
  path: "/incidents/new",
  name: "IncidentCreate",
  component: IncidentForm,
  beforeEnter: (to, from, next) => {
    if (auth2.isAdmin()) {
      next();
    } else {
      alert("Acceso denegado: solo los administradores pueden acceder a crear un incidente.");
      next("/");
    }
  }
},

// Rutas para elementos de juego
{
  path: "/gameElements/new", 
  name: "GameElementCreate",
  component: GameElementForm,
  beforeEnter: (to, from, next) =>{
  if (auth2.isAdmin()) {
    next();
  } else {
    alert("Acceso denegado: solo los administradores pueden acceder a crear elementos de juego.")
    next("/")
  }
  }
},
{
  path: "/gameElements/edit/:elementId", 
  name: "GameElementEdit",
  component: GameElementForm,
  beforeEnter: (to, from, next) => {
    if (auth2.isAdmin()) {
      next();
    } else {
      alert("Acceso denegado: solo los administradores pueden acceder a editar elementos de juego.")
      next("/")
    }
  }
},
  {
  path: '/incidents/edit/:incidentId',
  name: 'IncidentEdit',
  component: IncidentForm,  
  beforeEnter: (to, from, next) => {
    if (auth2.isAdmin()) {
      next();
    } else {
      alert("Acceso denegado: solo los administradores pueden acceder a editar un incidente.");
      next("/");
    }
  }
},
{
  path: '/quality-star/:parkId',
  name: 'Konva',
  component: Konva,
  props: true
},
{
  path: '/favorites',
  name: 'Favorites',
  component: FavoriteList
},
 {
    path: "/updates",
    name: "UpdateList",
    component: UpdateList,
    beforeEnter: (to, from, next) => {
      if (auth2.isAdmin()) {
        next();
      } else {
        alert("Acceso denegado: solo los administradores pueden acceder a la lista de actualizaciones.");
        next("/");
      }
    }
  },

];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  // Lo primero que hacemos antes de cargar ninguna ruta es comprobar si
  // el usuario está autenticado (revisando el token)
  auth2.isAuthenticationChecked().finally(() => {
    // por defecto, el usuario debe estar autenticado para acceder a las rutas
    const requiresAuth = !to.meta.public;

    const requiredAuthority = to.meta.authority;
    const userIsLogged = getStore().state.usuario.logged;
    const loggedUserAuthority = getStore().state.usuario.authority;

    if (requiresAuth) {
      // página privada
      if (userIsLogged) {
        if (requiredAuthority && requiredAuthority != loggedUserAuthority) {
          // usuario logueado pero sin permisos suficientes, le redirigimos a la página de login
          alert("Acceso prohibido para el usuario actual; intenta autenticarte de nuevo");
          auth2.logout();
          next("/");
        } else {
          // usuario logueado y con permisos adecuados
          next();
        }
      } else {
        // usuario no está logueado, no puede acceder a la página
        alert("Esta página requiere autenticación");
        next("/");
      }
    } else {
      // página pública
      if (userIsLogged && to.meta.isLoginPage) {
        // si estamos logueados no hace falta volver a mostrar el login
        next({ name: "MapView", replace: true });
      } else {
        next();
      }
    }
  });
});

export default router;

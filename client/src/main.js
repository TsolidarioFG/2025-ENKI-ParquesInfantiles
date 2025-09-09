import "bootstrap/dist/css/bootstrap.css";
import "bootstrap";
import './assets/main.css';
import 'bootstrap-icons/font/bootstrap-icons.css';


import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import i18n from './i18n';
import VueKonva from 'vue-konva'  // Changed import syntax

const app = createApp(App)
app.use(router)
   .use(i18n)
   .use(VueKonva)  // This will register all components globally
   .mount("#app")
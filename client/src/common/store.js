import { ref } from "vue";

const store = ref({
  state: {
    usuario: {
      authority: "",
      nombreUsuario: "",
      logged: false,
      favoritos: [],
      id: ""
    },
  }
});

export { getStore };

function getStore() {
  return store.value;
}

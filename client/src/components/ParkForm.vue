<template>
  <div class="container-fluid mt-4">
    <!-- Success Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="uploadSuccess"
      style="background-color: #d4edda; color: #155724;"
    >
      <strong>{{ $t('parkForm.successTitle') }}</strong> {{ $t('parkForm.successMessage') }}
      <button type="button" class="btn-close" @click="uploadSuccess = false"></button>
    </div>

    <!-- Error Alert (top position) -->
    <div 
      class="alert alert-dismissible fade show" 
      role="alert" 
      v-if="uploadError"
      style="background-color: #f8d7da; color: #721c24;"
    >
      <strong>{{ $t('parkForm.errorTitle') }}</strong> {{ uploadError }}
      <button type="button" class="btn-close" @click="uploadError = null"></button>
    </div>

    <div class="card shadow-lg">
      <div class="card-header text-center bg-primary text-white">
        <h3>{{ isNewPark ? $t('parkForm.titleCreate') : $t('parkForm.titleEdit') }}</h3>
      </div>
      <div class="card-body">
        <form @submit.prevent="savePark">
          <div
            class="border rounded p-3 mb-4 text-center"
            :class="{ 'bg-light': !imageSrc, 'bg-success bg-opacity-25': imageSrc }"
            @dragover.prevent
            @dragenter.prevent
            @drop.prevent="handleDrop"
          >
            <p v-if="!imageSrc && !auxImageName">{{ $t('parkForm.dragDrop') }}</p>
            <input
              ref="fileInput"
              type="file"
              accept="image/*"
              class="d-none"
              @change="handleFileChange"
            />
            <div v-if="auxImageName" class="mb-3">
              <label class="form-label">{{ $t('parkForm.selectedImage') }}: {{ auxImageName }}</label>
            </div>
            <img
              v-if="imageSrc"
              :src="imageSrc"
              class="img-fluid rounded mt-2"
              style="max-height: 300px;"
              :alt="$t('parkForm.imagePreviewAlt')"
            />
            <button class="btn btn-outline-primary mt-3" @click.prevent="$refs.fileInput.click()">
              {{ $t('parkForm.selectImage') }}
            </button>
          </div>

          <div class="mb-3">
            <label class="form-label">{{ $t('parkForm.id') }}:</label>
            <input type="number" class="form-control" v-model="park.parkId" required />
          </div>
          <div class="mb-3">
            <label class="form-label">{{ $t('parkForm.name') }}:</label>
            <input type="text" class="form-control" v-model="park.name" required />
          </div>
          
          <!-- Mapa para seleccionar ubicación -->
          <div class="mb-3">
    <label class="form-label">{{ $t('parkForm.location') }}:</label>
    <input 
      type="text" 
      class="form-control mb-2" 
      v-model="park.address" 
      @focus="showMapPicker = true"
      required 
    />
    
    <div v-if="showMapPicker" class="map-picker-container">
      <div id="location-picker-map" style="height: 300px; width: 100%;"></div>
      <div class="coordinates-display mt-2">
        <span>Latitud: {{ park.latitude || 'No seleccionada' }}</span>
        <span class="ms-3">Longitud: {{ park.longitude || 'No seleccionada' }}</span>
      </div>
      <button class="btn btn-sm btn-secondary mt-2" @click.prevent="showMapPicker = false">
        {{ $t('parkForm.closeMap') }}
      </button>
    </div>
  </div>


          <div class="mb-3">
            <label class="form-label">{{ $t('parkForm.visitYear') }}:</label>
            <input type="number" class="form-control" v-model="park.visitYear" min="1900" max="2030" required />
          </div>


          <!-- Selectores -->
          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.areaUbi')}}</label>
            <select class="form-select" v-model="park.areaUbi" required>
<option v-for="option in areaOptions" :key="option" :value="option">
  {{ $t('parkForm.parkUbis.' + option) }}
</option>
            </select>
          </div>

          <div class="mb-3">
            <label class="form-label">{{ $t('parkForm.type') }}</label>
<select class="form-select" v-model="park.type" required>
  <option v-for="option in typeOptions" :key="option" :value="option">
    {{ $t('parkForm.parkTypes.' + option) }}
  </option>
</select>
          </div>

          <div class="mb-3">
            <label class="form-label">{{ $t('parkForm.cierrmt') }}</label>
            <select class="form-select" v-model="park.cierrmt" required>
<option v-for="option in closureTypeOptions" :key="option" :value="option">
  {{ $t('parkForm.cierrmtTypes.' + option) }}
</option>
            </select>
          </div>

          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.cierrmd')}}</label>
            <select class="form-select" v-model="park.cierrmd" required>
<option v-for="option in closureMaterialOptions" :key="option" :value="option">
  {{ $t('parkForm.cierrmdTypes.' + option) }}
</option>
            </select>
          </div>

          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.pavimtp')}}</label>
            <select class="form-select" v-model="park.pavimtp" required>
<option v-for="option in pavementOptions" :key="option" :value="option">
  {{ $t('parkForm.pavimtpTypes.' + option) }}
</option>
            </select>
          </div>

          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.signMaterial')}}</label>
            <select class="form-select" v-model="park.signMaterial" required>
<option v-for="option in signMaterialOptions" :key="option" :value="option">
  {{ $t('parkForm.signMaterialTypes.' + option) }}
</option>
            </select>
          </div>

          <!-- Edad en Cartel -->
          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.ageRange')}}</label>
            <input type="text" class="form-control" v-model="park.ageRange" required/>
          </div>

          <!-- Número de usuarios -->
          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.numUsers')}}</label>
            <input type="number" class="form-control" v-model="park.numUsers" min="0" required/>
          </div>

          <!-- Selectores de Acceso -->
          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.accessibility')}}</label>
            <select class="form-select" v-model="park.accessibility" required>
              <option v-for="option in accessibilityOptions" :key="option" :value="option">{{ $t('parkForm.accessibilityTypes.' + option) }}</option>
            </select>
          </div>

          <!-- Ancho -->
          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.width')}}:</label>
            <input type="number" class="form-control" v-model="park.width" min="0" required/>
          </div>

          <!-- Espacio libre de giro -->
          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.freeTurningSpace')}}</label>
            <input type="number" class="form-control" v-model="park.freeTurningSpace" min="0" required/>
          </div>

          <div class="mb-3 ">
            <label class="form-label">{{$t('parkForm.parking')}}</label>
            <input type="text" class="form-control" v-model="park.parking" required/>
          </div>

          <div class="mb-3 ">
            <label class="form-label">{{$t('parkForm.postalCode')}}</label>
            <input type="number" class="form-control" v-model="park.postalCode" required/>
          </div>

          <!-- Checkboxes -->
          <div class="mb-3 form-check">
            <input class="form-check-input" type="checkbox" v-model="park.plazaPMR" />
            <label class="form-check-label">{{$t('parkForm.plazaPMR')}}</label>
          </div>
          <div class="mb-3 form-check">
            <input class="form-check-input" type="checkbox" v-model="park.bus" />
            <label class="form-check-label">{{$t('parkForm.bus')}}</label>
          </div>
          <div class="mb-3 form-check">
            <input class="form-check-input" type="checkbox" v-model="park.pedestrian" />
            <label class="form-check-label">{{$t('parkForm.pedestrian')}}</label>
          </div>
          <div class="mb-3 form-check">
            <input class="form-check-input" type="checkbox" v-model="park.bioArea" />
            <label class="form-check-label">{{$t('parkForm.bioArea')}}</label>
          </div>

          <!-- Grados -->
          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.degree1')}}</label>
            <input type="number" class="form-control" v-model="park.degree1" min="0" required/>
          </div>
          <div class="mb-3">
            <label class="form-label">{{$t('parkForm.degree2')}}</label>
            <input type="number" class="form-control" v-model="park.degree2" min="0" required/>
          </div>
          <div class="mb-3">
            <label class="form-label">{{ $t('parkForm.degree3')}}</label>
            <input type="number" class="form-control" v-model="park.degree3" min="0" required/>
          </div>

          <div class="mb-3">
            <label class="form-label">{{ $t('parkForm.accessible') }}:</label>
            <span v-if="isAccessible" class="text-success fw-bold">{{ $t('parkForm.yes') }}</span>
            <span v-else class="text-danger fw-bold">{{ $t('parkForm.no') }}</span>
          </div>

          <button type="submit" class="btn btn-primary w-100">
            {{ $t('parkForm.save') }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>


<script>
import ParkRepository from "@/repositories/ParkRepository"
import EnumRepository from "@/repositories/EnumsRepository"
import { BACKEND_URL } from "@/constants.js"
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';


export default {
  data() {
    return {
      uploadSuccess: false,
      uploadError: null,
      park: {
        id: null,
        parkId: 0,
        name: "",
        address: "",
        visitYear: "",
        areaUbi: null,
        type: null,
        cierrmt: null,
        cierrmd: null,
        pavimtp: null,
        signMaterial: null,
        degree1: 0,
        degree2: 0,
        degree3: 0,
        generalAccessibility: false,
        ageRange: "",
        numUsers: 0,
        width: 0,
        freeTurningSpace: 0,
        parking: "",
        plazaPMR: false,
        bus: false,
        pedestrian: false,
        bioArea: false,
        accessibleFountain: false,
        accessibility: null,
        postalCode: null,
        imageSet: null,
      },
      isNewPark: true,
      areaOptions: [],
      typeOptions: [],
      closureTypeOptions: [],
      closureMaterialOptions: [],
      pavementOptions: [],
      signMaterialOptions: [],
      accessibilityOptions: [],
      imageSrc: null,
      auxImageName: null,
      isUploading: false,
      uploadSuccess: false,
      uploadError: null,
      showMapPicker: false,
      locationMap: null,
      locationMarker: null,
    }
  },
  methods: {
    initLocationPickerMap() {
      this.$nextTick(() => {
        if (this.locationMap) {
          this.locationMap.off();
          this.locationMap.remove();
        }

        // Configuración inicial del mapa
        this.locationMap = L.map('location-picker-map').setView([40.4168, -3.7038], 13);
        
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(this.locationMap);

        // Manejador de clics en el mapa
        this.locationMap.on('click', (e) => {
          this.park.latitude = e.latlng.lat;
          this.park.longitude = e.latlng.lng;
          
          // Actualizar marcador
          if (this.locationMarker) {
            this.locationMap.removeLayer(this.locationMarker);
          }
          
          this.locationMarker = L.marker([e.latlng.lat, e.latlng.lng]).addTo(this.locationMap);
          
          // Opcional: Geocodificación inversa para obtener dirección
          this.reverseGeocode(e.latlng.lat, e.latlng.lng);
        });

        // Si ya hay coordenadas, centrar el mapa allí
        if (this.park.latitude && this.park.longitude) {
          this.locationMap.setView([this.park.latitude, this.park.longitude], 15);
          this.locationMarker = L.marker([this.park.latitude, this.park.longitude]).addTo(this.locationMap);
        }
      });
    },
    
    async reverseGeocode(lat, lng) {
      try {
        const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`);
        const data = await response.json();
        if (data.address) {
          this.park.address = data.display_name || '';
          this.park.postalCode = data.address.postcode || '';
        }
      } catch (error) {
        console.error("Error en geocodificación inversa:", error);
      }
    },
    async cargarEnumValues() {
      try {
        this.areaOptions = await EnumRepository.getAreaUbiOptions()
        this.typeOptions = await EnumRepository.getParkTypeOptions()
        this.closureTypeOptions = await EnumRepository.getCierrmtOptions()
        this.closureMaterialOptions = await EnumRepository.getCierrmdOptions()
        this.pavementOptions = await EnumRepository.getPavimtpOptions()
        this.signMaterialOptions = await EnumRepository.getSignMaterialOptions()
        this.accessibilityOptions = await EnumRepository.getAccessibilityOptions()
      } catch (error) {
        console.error("Error fetching enum values:", error)
      }
    },
    async cargarPark() {
      if (this.$route.params.parkId) {
        this.isNewPark = false
        try {
          this.park = await ParkRepository.findById(this.$route.params.parkId)
          console.log("Parque a editar:", this.park)

          // Si el parque tiene imagen, mostrar la URL
          if (this.park.hasImages) {
            this.imageSrc = `${BACKEND_URL}/parks/${this.park.id}/imagen`
          }
        } catch (error) {
          console.error("Error al cargar el parque:", error)
        }
      }
    },
     async savePark() {
  try {
    this.isUploading = true;
    this.uploadSuccess = false;
    this.uploadError = null;
    
    const parkToSave = { ...this.park };
    
    // Limpiar imageSet si es un File
    if (parkToSave.imageSet instanceof File) {
      parkToSave.imageSet = null;
    }
    
    // Guardar el parque (crear o actualizar)
    const response = await ParkRepository.save(parkToSave);
    
    // Obtener el ID del parque de manera robusta
    let parkId;
    if (this.isNewPark) {
      // Para parques nuevos: primero verificar si la respuesta tiene ID
      if (response && response.id) {
        parkId = response.id;
      } else {
        // Si no viene en la respuesta, buscar por nombre
        const parksFound = await ParkRepository.findByName(this.park.name);
        if (Array.isArray(parksFound) && parksFound.length > 0) {
          parkId = parksFound[0].id;
        } else if (parksFound && parksFound.id) {
          parkId = parksFound.id;
        }
      }
    } else {
      // Para parques existentes: usar el ID que ya tenemos
      parkId = this.park.id;
    }
    
    // Validar que tenemos un ID válido
    if (!parkId) {
      throw new Error('No se pudo obtener el ID del parque después de guardar');
    }
    
    // Actualizar el ID en el objeto local
    this.park.id = parkId;
    
    // Subir la imagen si hay un archivo seleccionado
    const fileInput = this.$refs.fileInput;
    if (fileInput && fileInput.files && fileInput.files[0]) {
      try {
        await ParkRepository.saveParkImage(parkId, fileInput.files[0]);
        this.uploadSuccess = true;
        this.uploadMessage = this.$t('parkForm.successWithImage');
        
        setTimeout(() => {
          this.$router.push({ name: "ParkList" });
        }, 1500);
        
      } catch (imageError) {
        console.error("Error al subir la imagen:", imageError);
        this.uploadError = this.$t('parkForm.errorUploadingImage');
        
        // Ofrecer opción de continuar sin imagen
        if (confirm(this.$t('parkForm.confirmContinueWithoutImage'))) {
          this.uploadSuccess = true;
          this.uploadMessage = this.$t('parkForm.successWithoutImage');
          
          setTimeout(() => {
            this.$router.push({ name: "ParkList" });
          }, 1000);
        }
      }
    } else {
      // No hay imagen para subir
      this.uploadSuccess = true;
      this.uploadMessage = this.$t('parkForm.successWithoutImage');
      
      setTimeout(() => {
        this.$router.push({ name: "ParkList" });
      }, 1000);
    }
    
  } catch (error) {
    console.error("Error al guardar el parque:", error);
    this.uploadError = this.$t('parkForm.errorSavingPark') + 
                      (error.message ? `: ${error.message}` : '');
    
    // Mostrar mensaje más específico si es por falta de ID
    if (error.message.includes('ID del parque')) {
      this.uploadError = this.$t('parkForm.errorParkId');
    }
    
  } finally {
    this.isUploading = false;
  }
},
    handleFileChange(event) {
      this.auxImageName = event.target.files[0]?.name
      this.readImage(event.target.files[0])
    },
    handleDrop(event) {
      const file = event.dataTransfer.files[0]
      if (file) {
        this.$refs.fileInput.files = event.dataTransfer.files
        this.auxImageName = file.name
        this.readImage(file)
      }
    },
    readImage(file) {
      if (!file || !file.type.startsWith("image/")) return

      const reader = new FileReader()
      reader.onload = (e) => {
        this.imageSrc = e.target.result
      }
      reader.readAsDataURL(file)
    },
  },
  async mounted() {
    await this.cargarEnumValues()
    await this.cargarPark()
  },
  computed: {
    isAccessible() {
      const totalElements = (this.park.degree1 || 0) + (this.park.degree2 || 0) + (this.park.degree3 || 0)
      const degree2Percentage = totalElements > 0 ? (this.park.degree2 || 0) / totalElements : 0
      return degree2Percentage >= 0.5
    },
  },
  watch: {
    isAccessible(newVal) {
      this.park.generalAccessibility = newVal
    },
    "park.degree1": function () {
      this.park.generalAccessibility = this.isAccessible
    },
    "park.degree2": function () {
      this.park.generalAccessibility = this.isAccessible
    },
    "park.degree3": function () {
      this.park.generalAccessibility = this.isAccessible
    },
    showMapPicker(newVal) {
      if (newVal) {
        this.initLocationPickerMap();
      }
    },
  },
}


</script>

<style>
.map-picker-container {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 15px;
}

.coordinates-display {
  font-family: monospace;
  background-color: #f8f9fa;
  padding: 5px 10px;
  border-radius: 4px;
}
</style>
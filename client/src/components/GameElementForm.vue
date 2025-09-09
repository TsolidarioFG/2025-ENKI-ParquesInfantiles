<template>
  <div class="game-element-form-container">
    <div class="game-element-card">
      <div class="game-element-header">
        <h3>{{ isNewElement ? $t("gameElementForm.createTitle") : $t("gameElementForm.editTitle") }}</h3>
      </div>
      <div class="game-element-body">
        <form @submit.prevent="saveGameElement">
          <!-- Información del parque -->
            <div class="form-group park-info-group">
              <label>{{ $t("gameElementForm.park") }}:</label>
              <div class="park-name">{{ parkName }}</div>
            </div>
          <!-- Campos básicos -->
          <div class="form-row">
            <div class="form-group">
              <label>{{ $t("gameElementForm.objectId") }}:</label>
              <input type="number" v-model="gameElement.objectId" required />
            </div>

            <div class="form-group">
              <label>{{ $t("gameElementForm.areaCode") }}:</label>
              <input type="number" v-model="gameElement.areaCode" required />
            </div>

            <div class="form-group">
              <label>{{ $t("gameElementForm.elementName") }}:</label>
              <input type="text" v-model="gameElement.elementName" required />
            </div>
          </div>
  
          <div class="form-row">
            <div class="form-group">
              <label>{{ $t("gameElementForm.elementType") }}:</label>
              <input type="text" v-model="gameElement.elementType" required />
            </div>
            <div class="form-group">
              <label>{{ $t("gameElementForm.accessibilityDegree") }}:</label>
              <select v-model="gameElement.accessibilityDegree" required>
                <option value="1">{{ $t("gameElementForm.accessibilityGrades.1") }}</option>
                <option value="2">{{ $t("gameElementForm.accessibilityGrades.2") }}</option>
                <option value="3">{{ $t("gameElementForm.accessibilityGrades.3") }}</option>
              </select>
            </div>
          </div>
  
          <!-- Subida de imagen -->
          <div class="image-upload-section">
            <label>{{ $t("gameElementForm.imageLabel") }}</label>
            <div
              class="image-drop-area"
              :class="{ 'has-image': imageSrc }"
              @dragover.prevent
              @dragenter.prevent
              @drop.prevent="handleDrop"
            >
              <p v-if="!imageSrc">{{ $t("gameElementForm.dragHint") }}</p>
              <input
                ref="fileInput"
                type="file"
                accept="image/*"
                capture="environment"
                class="d-none"
                @change="handleFileChange"
              />
              <input
                ref="cameraInput"
                type="file"
                accept="image/*"
                capture="user"
                class="d-none"
                @change="handleFileChange"
              />
              <img
                v-if="imageSrc"
                :src="imageSrc"
                class="preview-image"
              />
              <div class="image-actions">
                <button class="btn-image-action" @click.prevent="$refs.fileInput.click()">
                  <i class="bi bi-image"></i> {{ $t("gameElementForm.selectImage") }}
                </button>
                <button class="btn-image-action yellow" @click.prevent="openCamera">
                  <i class="bi bi-camera"></i> {{ $t("gameElementForm.takePhoto") }}
                </button>
              </div>
            </div>
          </div>

          <!-- Characteristics section (updated part only) -->
          <div class="characteristics-section">
            <label>{{ $t("gameElementForm.characteristics") }}:</label>

            <!-- Physical Play -->
            <div class="characteristics-group">
              <h4>{{ $t("gameElementForm.categories.physicalPlay") }}</h4>
              <div class="characteristics-grid">
                <div class="characteristic-item" v-for="prop in physicalPlayProps" :key="prop.value">
                  <input 
                    type="checkbox" 
                    :id="prop.value" 
                    v-model="gameElement[prop.value]"
                  />
                  <label :for="prop.value">
                    {{ $t(prop.labelKey) }}
                  </label>
                </div>
              </div>
            </div>
          
            <!-- Non-Physical Play -->
            <div class="characteristics-group">
              <h4>{{ $t("gameElementForm.categories.nonPhysicalPlay") }}</h4>
              <div class="characteristics-grid">
                <div class="characteristic-item" v-for="prop in nonPhysicalPlayProps" :key="prop.value">
                  <input 
                    type="checkbox" 
                    :id="prop.value" 
                    v-model="gameElement[prop.value]"
                  />
                  <label :for="prop.value">
                    {{ $t(prop.labelKey) }}
                  </label>
                </div>
              </div>
            </div>
          
            <!-- Play Types -->
            <div class="characteristics-group">
              <h4>{{ $t("gameElementForm.categories.playTypes") }}</h4>
              <div class="characteristics-grid">
                <div class="characteristic-item" v-for="prop in playTypeProps" :key="prop.value">
                  <input 
                    type="checkbox" 
                    :id="prop.value" 
                    v-model="gameElement[prop.value]"
                  />
                  <label :for="prop.value">
                    {{ $t(prop.labelKey) }}
                  </label>
                </div>
              </div>
            </div>
          </div>
  
          <!-- Botones -->
          <div class="form-actions">
            <button type="button" class="btn-secondary" @click="goBack">
              {{ $t("gameElementForm.cancel") }}
            </button>
            <button type="submit" class="btn-primary">
              {{ isNewElement ? $t("gameElementForm.create") : $t("gameElementForm.save") }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import GameElementRepository from "@/repositories/GameElementRepository";
import ParkRepository from "@/repositories/ParkRepository";
import { BACKEND_URL } from "@/constants.js";

export default {
  data() {
    return {
      gameElement: {
        id: null,
        objectId: null,
        areaCode: null,
        elementName: "",
        elementType: "",
        swinging: false,
        sliding: false,
        climbing: false,
        balancing: false,
        crawling: false,
        rocking: false,
        jumping: false,
        spinning: false,
        upperBodyStrength: false,
        auditory: false,
        visual: false,
        tactile: false,
        symbolic: false,
        cognitive: false,
        soloPlay: false,
        cooperativePlay: false,
        socialPlay: false,
        parallelPlay: false,
        linearPlay: false,
        accessibilityDegree: 1,
        image: null,
        park: null,
        hasImage: false
      },
      physicalPlayProps: [
      { value: "swinging", labelKey: "gameElementForm.props.swinging" },
      { value: "sliding", labelKey: "gameElementForm.props.sliding" },
      { value: "climbing", labelKey: "gameElementForm.props.climbing" },
      { value: "balancing", labelKey: "gameElementForm.props.balancing" },
      { value: "crawling", labelKey: "gameElementForm.props.crawling" },
      { value: "rocking", labelKey: "gameElementForm.props.rocking" },
      { value: "jumping", labelKey: "gameElementForm.props.jumping" },
      { value: "spinning", labelKey: "gameElementForm.props.spinning" },
      { value: "upperBodyStrength", labelKey: "gameElementForm.props.upperBodyStrength" }
    ],
    nonPhysicalPlayProps: [
      { value: "auditory", labelKey: "gameElementForm.props.auditory" },
      { value: "visual", labelKey: "gameElementForm.props.visual" },
      { value: "tactile", labelKey: "gameElementForm.props.tactile" },
      { value: "cognitive", labelKey: "gameElementForm.props.cognitive" },
      { value: "symbolic", labelKey: "gameElementForm.props.symbolic" }
    ],
    playTypeProps: [
      { value: "soloPlay", labelKey: "gameElementForm.props.soloPlay" },
      { value: "socialPlay", labelKey: "gameElementForm.props.socialPlay" },
      { value: "cooperativePlay", labelKey: "gameElementForm.props.cooperativePlay" },
      { value: "parallelPlay", labelKey: "gameElementForm.props.parallelPlay" },
      { value: "linearPlay", labelKey: "gameElementForm.props.linearPlay" }
    ],
      parkId: null,
      parkName: "",
      isNewElement: true,
      imageSrc: null,
      gameElementProps: [
        { value: "swinging", labelKey: "gameElementForm.props.swinging" },
        { value: "sliding", labelKey: "gameElementForm.props.sliding" },
        { value: "climbing", labelKey: "gameElementForm.props.climbing" },
        { value: "balancing", labelKey: "gameElementForm.props.balancing" },
        { value: "crawling", labelKey: "gameElementForm.props.crawling" },
        { value: "rocking", labelKey: "gameElementForm.props.rocking" },
        { value: "jumping", labelKey: "gameElementForm.props.jumping" },
        { value: "spinning", labelKey: "gameElementForm.props.spinning" },
        { value: "upperBodyStrength", labelKey: "gameElementForm.props.upperBodyStrength" },
        { value: "auditory", labelKey: "gameElementForm.props.auditory" },
        { value: "visual", labelKey: "gameElementForm.props.visual" },
        { value: "tactile", labelKey: "gameElementForm.props.tactile" },
        { value: "symbolic", labelKey: "gameElementForm.props.symbolic" },
        { value: "cognitive", labelKey: "gameElementForm.props.cognitive" },
        { value: "soloPlay", labelKey: "gameElementForm.props.soloPlay" },
        { value: "cooperativePlay", labelKey: "gameElementForm.props.cooperativePlay" },
        { value: "socialPlay", labelKey: "gameElementForm.props.socialPlay" },
        { value: "parallelPlay", labelKey: "gameElementForm.props.parallelPlay" },
        { value: "linearPlay", labelKey: "gameElementForm.props.linearPlay" }
      ]
    };
  },
  async mounted() {
    this.parkId = this.$route.params.parkId || this.$route.query.parkId;
    
    if (!this.parkId) {
      console.error("No se ha proporcionado un ID de parque");
      this.$router.push({ name: "ParkList" });
      return;
    }

    // Cargar información del parque
    try {
  const park = await ParkRepository.findById(this.parkId);
  this.parkName = park.name;
  // Asignar solo el ID del parque, no un objeto
  this.gameElement.parkId = parseInt(this.parkId);
} catch (error) {
  console.error("Error al cargar el parque:", error);
}

    // Si hay un ID de elemento, cargar los datos del elemento
    const elementId = this.$route.params.elementId || this.$route.query.elementId;
    
      if (elementId) {
    this.isNewElement = false;
    try {
      const element = await GameElementRepository.findById(elementId);
      
      // Crear copia profunda para evitar mutaciones
      const elementCopy = JSON.parse(JSON.stringify(element));
      
      // Convertir null a false para checkboxes
      Object.keys(elementCopy).forEach(key => {
        if (elementCopy[key] === null && typeof this.gameElement[key] === 'boolean') {
          elementCopy[key] = false;
        }
      });
      
      this.gameElement = elementCopy;
        
        // Si el elemento tiene una imagen, cargarla
        if (element.hasImage) {
          this.imageSrc = `${BACKEND_URL}/gameElements/${element.id}/imagen`;
        }
      } catch (error) {
        console.error("Error al cargar el elemento de juego:", error);
      }
    }
  },
  methods: {
    async saveGameElement() {
  try {
    // 1. Guardar una copia del objeto sin la imagen
    const elementToSave = { ...this.gameElement };
    delete elementToSave.park;
    delete elementToSave.image;
    
    // Asegurarse de que parkId sea un número
    elementToSave.parkId = parseInt(this.parkId);
    
    // Remover cualquier objeto park si existe
    delete elementToSave.park;
    
    console.log("Guardando game element:", elementToSave);
    
    // 2. Guardar el elemento primero
    const savedElement = await GameElementRepository.save(elementToSave);
    
    // 3. Si hay una imagen, subirla por separado
    if (this.gameElement.image instanceof File) {
      try {
        await GameElementRepository.saveGameElementImage(savedElement.id, this.gameElement.image);
        console.log("Imagen subida correctamente");
      } catch (imageError) {
        console.error("Error al subir la imagen:", imageError);
        alert("El elemento se guardó pero hubo un error al subir la imagen");
      }
    }
    
    // Redirigir
    this.$router.push({ 
      name: "ParkDetail", 
      params: { parkId: this.parkId },
      query: { showElements: true }
    });

  } catch (error) {
    console.error("Error al guardar el elemento de juego:", error);
    if (error.response?.data) {
      console.error("Detalles del error:", error.response.data);
    }
    alert(this.$t("gameElementForm.errorSave"));
  }
},
    handleFileChange(event) {
      const file = event.target.files[0];
      this.readImage(file);
    },
    handleDrop(event) {
      const file = event.dataTransfer.files[0];
      this.readImage(file);
      this.$refs.fileInput.files = event.dataTransfer.files;
    },
    readImage(file) {
      if (!file || !file.type.startsWith("image/")) return;

      const reader = new FileReader();
      reader.onload = (e) => {
        this.imageSrc = e.target.result;
        this.gameElement.image = file;
      };
      reader.readAsDataURL(file);
    },
    goBack() {
      this.$router.go(-1);
    },
    openCamera() {
      this.$refs.cameraInput.click();
    }
  }
};
</script>

<style scoped>
.game-element-form-container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 0 1rem;
}

.game-element-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.game-element-header {
  background-color: #009DE0;
  color: white;
  padding: 1.5rem;
  text-align: center;
}

.game-element-header h3 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
}

.game-element-body {
  padding: 2rem;
}

.park-info {
  background-color: #e6f4ff;
  color: #0066a6;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  font-size: 1rem;
}
.park-info-group {
  margin-bottom: 1.5rem;
}

.park-info-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
  font-size: 1rem;
}

.park-name {
  background-color: #e6f4ff;
  color: #0066a6;
  padding: 0.75rem;
  border-radius: 8px;
  font-size: 1rem;
  border: 1px solid #b3d9ff;
}


.park-info strong {
  font-weight: 600;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.form-group {
  flex: 1 1 calc(50% - 0.75rem);
  min-width: 250px;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #009DE0;
  box-shadow: 0 0 0 2px rgba(0, 157, 224, 0.2);
}

.image-upload-section {
  margin-bottom: 2rem;
}

.image-upload-section label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.image-drop-area {
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 2rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
}

.image-drop-area.has-image {
  background-color: rgba(0, 157, 224, 0.05);
  border-color: #009DE0;
}

.image-drop-area p {
  margin: 0 0 1rem;
  color: #666;
}

.preview-image {
  max-width: 100%;
  max-height: 200px;
  border-radius: 6px;
  margin-bottom: 1rem;
  object-fit: contain;
}

.image-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn-image-action {
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  font-weight: 500;
  font-size: 0.9rem;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background-color: #009DE0;
  color: white;
}

.btn-image-action:hover {
  background-color: #0081c1;
}

.btn-image-action.yellow {
  background-color: #F9DC0A;
  color: #333;
}

.btn-image-action.yellow:hover {
  background-color: #e6c900;
}

.btn-image-action.danger {
  background-color: #ff4444;
  color: white;
}

.btn-image-action.danger:hover {
  background-color: #cc0000;
}

.characteristics-section {
  margin-bottom: 2rem;
}

.characteristics-section label {
  display: block;
  margin-bottom: 1rem;
  font-weight: 500;
  color: #333;
  font-size: 1rem;
}

.characteristics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
}

.characteristic-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.characteristic-item input[type="checkbox"] {
  width: 18px;
  height: 18px;
  accent-color: #009DE0;
}

.characteristic-item label {
  font-size: 0.95rem;
  color: #444;
  margin: 0;
  font-weight: normal;
  cursor: pointer;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
  gap: 1rem;
}

.btn-primary {
  background-color: #009DE0;
  color: white;
  padding: 0.8rem 1.8rem;
  border-radius: 8px;
  font-weight: 500;
  font-size: 1rem;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  flex: 1;
  max-width: 200px;
}

.btn-primary:hover {
  background-color: #0081c1;
}

.btn-secondary {
  background-color: white;
  color: #009DE0;
  padding: 0.8rem 1.8rem;
  border-radius: 8px;
  font-weight: 500;
  font-size: 1rem;
  border: 2px solid #009DE0;
  cursor: pointer;
  transition: all 0.2s;
  flex: 1;
  max-width: 200px;
}

.btn-secondary:hover {
  background-color: #f0f9ff;
}

@media (max-width: 768px) {
  .game-element-body {
    padding: 1.5rem;
  }
  
  .form-row {
    flex-direction: column;
    gap: 1rem;
  }
  
  .form-group {
    flex: 1 1 100%;
  }
  
  .image-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .btn-image-action {
    width: 100%;
    justify-content: center;
  }
  
  .characteristics-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
  
  .form-actions {
    flex-direction: column;
    gap: 1rem;
  }
  
  .btn-primary,
  .btn-secondary {
    max-width: 100%;
    width: 100%;
  }
  @media (max-width: 768px) {
  .btn-primary,
  .btn-secondary {
    font-size: 1.05rem;
    padding: 0.9rem 1.8rem;
  }}
}

.characteristics-group {
  margin-bottom: 1.5rem;
  background-color: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.characteristics-group h4 {
  margin-top: 0;
  margin-bottom: 1rem;
  color: #009DE0;
  font-size: 1.1rem;
  font-weight: 600;
}
</style>
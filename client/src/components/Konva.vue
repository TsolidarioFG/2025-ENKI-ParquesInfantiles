<template>
  <div class="page-container">
        <!-- Button -->
    <div class="go-back">
      <button @click="goBack">⬅ {{ $t('Konva.goBack') }}</button>
    </div>
    <div class="star-container" >
      <v-stage :config="stageSize">
        <v-layer>
          <!-- Labels -->
          <v-text
            v-for="(item, index) in shapes"
            :key="'label-' + item.id"
            :config="{
              x: item.labelPosition.x,
              y: item.labelPosition.y,
              text: $t(`gameCharacteristics.${item.characteristic}`),
              fontSize: 14,
              fill: '#1E88E5',
              fontStyle: 'bold',
              align: 'center',
              width: 100,
              listening: true,
              hitStrokeWidth: 20
            }"
            @mouseenter="e => e.target.fill('#1565C0')"
            @mouseleave="e => e.target.fill('#1E88E5')"
            @click="() => alert($t(`gameCharacteristics.${item.characteristic}`))"
          />

          <!-- Shapes -->
          <v-line
            v-for="item in shapes"
            :key="item.id"
            :config="{
              points: item.outerTriangle.points,
              fill: item.value >= 3 ? item.color : 'transparent',
              stroke: 'black',
              strokeWidth: 2,
              closed: true
            }"
          />
          <v-line
            v-for="item in shapes"
            :key="'inner-' + item.id"
            :config="{
              points: item.innerTriangle.points,
              fill: item.value >= 1 ? item.color : 'transparent',
              stroke: 'black',
              strokeWidth: 2,
              closed: true
            }"
          />
          <v-line
            v-for="item in shapes"
            :key="'trapezoid-' + item.id"
            :config="{
              points: item.trapezoid.points,
              fill: item.value >= 2 ? item.color : 'transparent',
              stroke: 'black',
              strokeWidth: 2,
              closed: true
            }"
          />
        </v-layer>
      </v-stage>
    </div>

    <!-- Citation -->
    <div class="citation">
      <p v-html="$t('Konva.quote')"></p>
    </div>


  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import GameElementRepository from "@/repositories/GameElementRepository";
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const route = useRoute();
const router = useRouter();
const parkId = route.params.parkId;

// Ajustar tamaño para que la estrella quede completa
const calculateStageSize = () => {

  const height = 1000; Math.min(window.innerHeight, window.innerWidth); // Proporción para contener la estrella //1.7 y 1.2
  const width = height
  return { width, height };
};

const stageSize = ref(calculateStageSize());
const shapes = ref([]);
const characteristics = [
  'cooperativePlay', 'soloPlay', 'socialPlay', 'parallelPlay', 'linearPlay',
  'symbolic', 'auditory', 'visual', 'tactile', 'cognitive',
  'upperBodyStrength', 'balancing', 'climbing', 'crawling', 'jumping',
  'sliding', 'swinging', 'spinning', 'rocking'
];

const baseColors = [
  '#009DE0', // azul
  '#C83486', // rosado
  '#289548', // verde
  '#5A3888', // púrpura
  '#F9DC0A', // amarillo
  '#FFFFFF'  // blanco
];

// Alternativas de opacidad si se requieren más colores (derivadas del mismo tono)
const generateColorByIndex = (index) => {
  const base = baseColors[index % baseColors.length];

  // Si se repite, modificar la opacidad o iluminación
  const shadeLevel = Math.floor(index / baseColors.length);

  if (shadeLevel === 0) return base;

  // Crear variación con opacidad (solo si realmente necesario)
  const opacity = 1 - (shadeLevel * 0.2);
  const hexToRgba = (hex, alpha) => {
    const bigint = parseInt(hex.replace('#', ''), 16);
    const r = (bigint >> 16) & 255;
    const g = (bigint >> 8) & 255;
    const b = bigint & 255;
    return `rgba(${r},${g},${b},${Math.max(alpha, 0.3)})`;
  };

  return hexToRgba(base, opacity);
};


const goBack = () => {
  router.back();
};

const loadGameElements = async () => {
  try {
    const gameElements = await GameElementRepository.findByParkId(parkId);

    const valuesMap = {};
    characteristics.forEach((key) => {
      valuesMap[key] = 0;
    });

    gameElements.forEach((el) => {
      characteristics.forEach((key) => {
        if (el[key]) valuesMap[key]++;
      });
    });

    drawShapes(valuesMap);
  } catch (error) {
    console.error("Failed to load game elements:", error);
  }
};

const drawShapes = (valuesMap) => {
  const centerX = stageSize.value.width / 2;
  const centerY = stageSize.value.height / 2;
  const outerRadius = Math.min(stageSize.value.width, stageSize.value.height) / 3.5 ;
  const innerRadius = outerRadius / 1.5;
  const numTriangles = characteristics.length;
  const outerBaseLength = outerRadius * 0.3;
  const innerBaseLength = outerBaseLength * 0.7;
  const outerHeight = outerRadius * 0.3;
  const innerHeight = innerRadius * 0.8;
  const separation = - innerRadius * 0.05;

  shapes.value = [];

  for (let n = 0; n < numTriangles; n++) {
    const angle = ((n + 0.5) / numTriangles) * Math.PI * 2;
    const color = generateColorByIndex(n);
    const value = valuesMap[characteristics[n]] || 0;

    const outerX = centerX + outerRadius * Math.cos(angle);
    const outerY = centerY + outerRadius * Math.sin(angle);
    const innerX = centerX + innerRadius * Math.cos(angle);
    const innerY = centerY + innerRadius * Math.sin(angle);

    const outerTriangle = {
      points: [
        outerX + outerHeight * Math.cos(angle),
        outerY + outerHeight * Math.sin(angle),
        outerX - outerBaseLength / 2 * Math.cos(angle + Math.PI / 2),
        outerY - outerBaseLength / 2 * Math.sin(angle + Math.PI / 2),
        outerX + outerBaseLength / 2 * Math.cos(angle + Math.PI / 2),
        outerY + outerBaseLength / 2 * Math.sin(angle + Math.PI / 2)
      ]
    };

    const innerTriangle = {
      points: [
        innerX - innerHeight * Math.cos(angle),
        innerY - innerHeight * Math.sin(angle),
        innerX - innerBaseLength / 2 * Math.cos(angle + Math.PI / 2),
        innerY - innerBaseLength / 2 * Math.sin(angle + Math.PI / 2),
        innerX + innerBaseLength / 2 * Math.cos(angle + Math.PI / 2),
        innerY + innerBaseLength / 2 * Math.sin(angle + Math.PI / 2)
      ]
    };

    const trapezoid = {
      points: [
        outerX - outerBaseLength / 2 * Math.cos(angle + Math.PI / 2),
        outerY - outerBaseLength / 2 * Math.sin(angle + Math.PI / 2),
        outerX + outerBaseLength / 2 * Math.cos(angle + Math.PI / 2),
        outerY + outerBaseLength / 2 * Math.sin(angle + Math.PI / 2),
        innerX + innerBaseLength / 2 * Math.cos(angle + Math.PI / 2),
        innerY + innerBaseLength / 2 * Math.sin(angle + Math.PI / 2),
        innerX - innerBaseLength / 2 * Math.cos(angle + Math.PI / 2),
        innerY - innerBaseLength / 2 * Math.sin(angle + Math.PI / 2)
      ].map((coord, index) =>
        index % 2 === 0
          ? coord + (index < 4 ? separation * Math.cos(angle) : -separation * Math.cos(angle))
          : coord + (index < 4 ? separation * Math.sin(angle) : -separation * Math.sin(angle))
      )
    };

    const labelRadius = outerRadius*1.5;
    const labelX = centerX + labelRadius * Math.cos(angle) - 50;
    const labelY = centerY + labelRadius * Math.sin(angle) - 10;

    shapes.value.push({
      id: `shape-${n}`,
      characteristic: characteristics[n],
      outerTriangle,
      innerTriangle,
      trapezoid,
      color,
      value,
      labelPosition: { x: labelX, y: labelY }
    });
  }
};

onMounted(() => {
  loadGameElements();
  window.addEventListener('resize', () => {
    stageSize.value = calculateStageSize();
  });
});
</script>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 20px;
  box-sizing: border-box;
}

.star-container {
  width: 100%;
  display: flex;
  justify-content: center;
  margin: 20px 0;
  overflow: visible;
}

.citation {
  max-width: 800px;
  margin: 20px auto;
  padding: 15px;
  font-size: 0.9rem;
  color: #666;
  text-align: center;
  line-height: 1.5;
  border-top: 1px solid #eee;
}

.go-back {
  margin-top: 20px;
  margin-bottom: 40px;
}

.go-back button {
  padding: 12px 24px;
  font-size: 16px;
  background-color: #1E88E5;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.go-back button:hover {
  background-color: #1565C0;
}
</style>

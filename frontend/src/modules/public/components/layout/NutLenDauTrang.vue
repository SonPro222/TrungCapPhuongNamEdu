<template>
  <button
      v-show="hienNut"
      class="scroll-top-button"
      type="button"
      aria-label="Lên đầu trang"
      title="Lên đầu trang"
      @click="lenDauTrang"
  >
    <i class="fa-solid fa-angle-up"></i>
  </button>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'

const hienNut = ref(false)

const kiemTraViTriCuon = () => {
  hienNut.value = window.scrollY > 300
}

const lenDauTrang = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

onMounted(() => {
  kiemTraViTriCuon()
  window.addEventListener('scroll', kiemTraViTriCuon)
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', kiemTraViTriCuon)
})
</script>

<style scoped>
.scroll-top-button {
  position: fixed;
  right: 28px;
  bottom: 28px;
  z-index: 9998;
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  border: none;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
  font-size: 18px;
  cursor: pointer;
  box-shadow: 0 12px 28px rgba(7, 95, 200, 0.32);
  transition: 0.25s ease;
}

.scroll-top-button:hover {
  transform: translateY(-3px);
  background: #064fa8;
  box-shadow: 0 16px 34px rgba(7, 95, 200, 0.4);
}

.scroll-top-button:active {
  transform: translateY(-1px);
}

.scroll-top-button i {
  line-height: 1;
}

@media (max-width: 760px) {
  .scroll-top-button {
    right: 14px;
    bottom: 18px;
    width: 42px;
    height: 42px;
    font-size: 17px;
  }
}
</style>
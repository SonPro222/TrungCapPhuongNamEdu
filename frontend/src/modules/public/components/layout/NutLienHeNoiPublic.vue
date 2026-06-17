<template>
  <div class="nut-lien-he-noi">
    <a
        class="nut-lien-he-item messenger"
        href="https://m.me/"
        target="_blank"
        rel="noopener noreferrer"
        title="Nhắn Messenger"
    >
      <i class="fa-brands fa-facebook-messenger"></i>
    </a>

    <a
        class="nut-lien-he-item zalo"
        href="https://zalo.me/"
        target="_blank"
        rel="noopener noreferrer"
        title="Nhắn Zalo"
    >
      <span>Zalo</span>
    </a>

    <a
        class="nut-lien-he-item phone"
        href="tel:1900633088"
        title="Gọi hotline"
    >
      <i class="fa-solid fa-phone"></i>
    </a>

    <a
        class="nut-lien-he-item chat"
        href="#form-lien-he"
        title="Gửi liên hệ"
    >
      <i class="fa-solid fa-message"></i>
    </a>
  </div>
</template>

<style scoped>
.nut-lien-he-noi {
  position: fixed;
  right: 28px;
  top: 58%;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  gap: 12px;
  transform: translateY(-0%);
}

.nut-lien-he-item {
  position: relative;
  width: 54px;
  height: 54px;
  display: grid;
  place-items: center;
  border: 3px solid #ffffff;
  border-radius: 999px;
  background: linear-gradient(135deg, #075fc8, #063b7a);
  color: #ffffff;
  text-decoration: none;
  box-shadow: 0 12px 28px rgba(7, 95, 200, 0.28);
  transition: 0.25s ease;
}

.nut-lien-he-item::before {
  content: '';
  position: absolute;
  inset: -7px;
  border-radius: 999px;
  border: 1px solid rgba(7, 95, 200, 0.28);
  animation: lienHePulse 1.8s infinite;
}

.nut-lien-he-item::after {
  content: '';
  position: absolute;
  inset: 4px;
  border-radius: 999px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.18), rgba(255, 255, 255, 0));
  pointer-events: none;
}

.nut-lien-he-item:hover {
  transform: translateY(-3px) scale(1.04);
  background: linear-gradient(135deg, #0b6fe8, #075fc8);
  box-shadow: 0 16px 34px rgba(7, 95, 200, 0.38);
}

.nut-lien-he-item i {
  position: relative;
  z-index: 2;
  font-size: 23px;
}

.nut-lien-he-item span {
  position: relative;
  z-index: 2;
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #ffffff;
  color: #075fc8;
  font-size: 10px;
  font-weight: 900;
  box-shadow: inset 0 0 0 1px rgba(7, 95, 200, 0.08);
}

.nut-lien-he-item.messenger,
.nut-lien-he-item.zalo,
.nut-lien-he-item.phone,
.nut-lien-he-item.chat {
  background: linear-gradient(135deg, #075fc8, #063b7a);
}

.nut-lien-he-item.messenger:hover,
.nut-lien-he-item.zalo:hover,
.nut-lien-he-item.phone:hover,
.nut-lien-he-item.chat:hover {
  background: linear-gradient(135deg, #0b6fe8, #075fc8);
}

@keyframes lienHePulse {
  0% {
    opacity: 0.75;
    transform: scale(0.92);
  }

  70% {
    opacity: 0;
    transform: scale(1.24);
  }

  100% {
    opacity: 0;
    transform: scale(1.24);
  }
}

@media (max-width: 760px) {
  .nut-lien-he-noi {
    right: 14px;
    top: 58%;
    gap: 10px;
  }

  .nut-lien-he-item {
    width: 48px;
    height: 48px;
  }

  .nut-lien-he-item i {
    font-size: 21px;
  }

  .nut-lien-he-item span {
    width: 30px;
    height: 30px;
    font-size: 9px;
  }
}
</style>
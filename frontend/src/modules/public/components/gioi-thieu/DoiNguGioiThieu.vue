<template>
  <section class="about-section-white">
    <div class="public-container">
      <div class="about-section-title">
        <span></span>
        <h2>Ban giám hiệu & Đội ngũ giảng viên</h2>
        <span></span>
      </div>

      <div class="about-team-grid">
        <article v-for="item in team" :key="item.id" class="about-team-card">
          <div class="team-image">
            <img :src="item.image" :alt="item.name" />
          </div>

          <div class="team-info">
            <h4>{{ item.name }}</h4>
            <p>{{ item.role }}</p>
            <span>{{ item.desc }}</span>

            <div class="team-social">
              <a href="#" title="Đang cập nhật" @click.prevent><i class="fa-brands fa-linkedin-in"></i></a>
              <a href="#" title="Đang cập nhật" @click.prevent><i class="fa-solid fa-envelope"></i></a>
              <a href="#" title="Đang cập nhật" @click.prevent><i class="fa-solid fa-globe"></i></a>
            </div>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const team = [
  {
    id: 1,
    name: 'TS. Nguyễn Văn Minh',
    role: 'Hiệu trưởng',
    desc: 'Hơn 20 năm kinh nghiệm trong lĩnh vực giáo dục nghề nghiệp.',
    image: 'https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=500&q=80'
  },
  {
    id: 2,
    name: 'ThS. Trần Thị Lan',
    role: 'Phó Hiệu trưởng',
    desc: 'Chuyên gia trong quản lý đào tạo và phát triển chương trình.',
    image: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=500&q=80'
  },
  {
    id: 3,
    name: 'ThS. Lê Hoàng Nam',
    role: 'Trưởng phòng Đào tạo',
    desc: 'Kinh nghiệm dày dặn gắn liền thực tiễn doanh nghiệp.',
    image: 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?auto=format&fit=crop&w=500&q=80'
  },
  {
    id: 4,
    name: 'TS. Phạm Thu Hương',
    role: 'Trưởng khoa Kinh tế',
    desc: 'Chuyên môn sâu, tận tâm với nghề và người học.',
    image: 'https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&w=500&q=80'
  },
  {
    id: 5,
    name: 'ThS. Nguyễn Đức Tâm',
    role: 'Trưởng khoa Kỹ thuật',
    desc: 'Giàu kinh nghiệm, cập nhật công nghệ hiện đại liên tục.',
    image: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&w=500&q=80'
  }
]
</script>

<style scoped>
.about-section-white {
  background: #ffffff;
  padding: 64px 0;
}

.about-section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 22px;
  margin-bottom: 46px;
  text-align: center;
}

.about-section-title span {
  width: 110px;
  height: 1px;
  background: #cbd5e1;
}

.about-section-title h2 {
  margin: 0;
  color: #063b7a;
  font-size: 30px;
  font-weight: 900;
}

.about-team-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 22px;
}

.about-team-card {
  overflow: hidden;
  border: 1px solid #e6eef8;
  border-radius: 18px;
  background: #f8fbff;
  text-align: center;
  transition: 0.25s;
}

.about-team-card:hover {
  background: #ffffff;
  box-shadow: 0 14px 28px rgba(7, 63, 131, 0.1);
}

.team-image {
  height: 238px;
  overflow: hidden;
  background: #e5e7eb;
}

.team-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: top;
  transition: 0.35s;
}

.about-team-card:hover .team-image img {
  transform: scale(1.05);
}

.team-info {
  padding: 18px 14px 20px;
}

.team-info h4 {
  margin: 0 0 6px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.team-info p {
  margin: 0 0 10px;
  color: #64748b;
  font-size: 13px;
  font-weight: 800;
}

.team-info span {
  display: block;
  color: #607894;
  font-size: 12px;
  line-height: 1.55;
  min-height: 54px;
}

.team-social {
  display: flex;
  justify-content: center;
  gap: 9px;
  margin-top: 16px;
}

.team-social a {
  width: 28px;
  height: 28px;
  display: grid;
  place-items: center;
  border: 1px solid #e2e8f0;
  border-radius: 999px;
  background: #ffffff;
  color: #94a3b8;
  font-size: 11px;
  text-decoration: none;
}

.team-social a:hover {
  color: #075fc8;
  border-color: #bfdbfe;
}

@media (max-width: 1180px) {
  .about-team-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .about-section-title {
    gap: 12px;
  }

  .about-section-title span {
    width: 38px;
  }

  .about-section-title h2 {
    font-size: 22px;
  }

  .about-team-grid {
    grid-template-columns: 1fr;
  }
}
</style>
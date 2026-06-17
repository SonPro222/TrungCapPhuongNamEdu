<template>
  <section class="about-story-section">
    <div class="public-container about-story-grid">
      <div class="story-image">
        <img
            src="https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=900&q=90"
            alt="Cơ sở Trường Trung cấp Phương Nam"
        />
      </div>

      <div class="story-content">
        <span>Câu chuyện của chúng tôi</span>
        <h2>Về Trường Trung cấp Phương Nam</h2>

        <p>
          Trường Trung cấp Phương Nam được thành lập với sứ mệnh cung cấp môi trường học tập chất lượng cao,
          chú trọng đào tạo thực hành – thực nghiệp, đáp ứng nhu cầu nguồn nhân lực của xã hội và doanh nghiệp
          trong thời kỳ hội nhập.
        </p>

        <p>
          Chúng tôi tin rằng mỗi học viên đều có tiềm năng riêng. Nhiệm vụ của nhà trường là khơi dậy,
          vun đắp và trang bị hành trang vững chắc để các em tự tin bước vào tương lai.
        </p>

        <div class="about-feature-grid">
          <article v-for="item in aboutFeatures" :key="item.title" class="about-feature-card">
            <div>
              <i :class="item.icon"></i>
            </div>
            <h4>{{ item.title }}</h4>
            <p>{{ item.desc }}</p>
          </article>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const aboutFeatures = [
  {
    icon: 'fa-solid fa-hands',
    title: 'Thực tiễn',
    desc: 'Đào tạo gắn liền thực tế'
  },
  {
    icon: 'fa-solid fa-award',
    title: 'Chất lượng',
    desc: 'Lấy chất lượng làm cốt lõi'
  },
  {
    icon: 'fa-solid fa-user-shield',
    title: 'Trách nhiệm',
    desc: 'Đồng hành cùng người học'
  },
  {
    icon: 'fa-solid fa-lightbulb',
    title: 'Sáng tạo',
    desc: 'Đổi mới để phát triển bền vững'
  }
]
</script>

<style scoped>
.about-story-section {
  background: #ffffff;
  padding: 64px 0;
}

.about-story-grid {
  display: grid;
  grid-template-columns: 5fr 7fr;
  gap: 54px;
  align-items: center;
}

.story-image {
  border-radius: 24px;
  overflow: hidden;
  border: 4px solid #f8fafc;
  box-shadow: 0 16px 36px rgba(7, 63, 131, 0.13);
}

.story-image img {
  display: block;
  width: 100%;
  height: 360px;
  object-fit: cover;
}

.story-content > span {
  display: block;
  color: #6b7280;
  font-size: 14px;
  font-weight: 800;
  margin-bottom: 8px;
}

.story-content h2 {
  margin: 0 0 22px;
  color: #063b7a;
  font-size: 36px;
  font-weight: 900;
}

.story-content p {
  margin: 0 0 16px;
  color: #607894;
  font-size: 14px;
  line-height: 1.75;
}

.about-feature-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
  margin-top: 28px;
}

.about-feature-card {
  border: 1px solid #e6eef8;
  background: #f8fbff;
  border-radius: 16px;
  padding: 18px 14px;
  text-align: center;
  transition: 0.25s;
}

.about-feature-card:hover {
  background: #ffffff;
  box-shadow: 0 10px 24px rgba(7, 63, 131, 0.09);
}

.about-feature-card div {
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  margin: 0 auto 12px;
  border-radius: 999px;
  background: #edf6ff;
  color: #075fc8;
}

.about-feature-card h4 {
  margin: 0 0 6px;
  color: #123b68;
  font-size: 13px;
  font-weight: 900;
}

.about-feature-card p {
  margin: 0;
  color: #607894;
  font-size: 11px;
  line-height: 1.45;
}

@media (max-width: 1180px) {
  .about-story-grid {
    grid-template-columns: 1fr;
  }

  .about-feature-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .about-feature-grid {
    grid-template-columns: 1fr;
  }

  .story-content h2 {
    font-size: 28px;
  }
}
</style>
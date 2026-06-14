<template>
  <section class="about-hero">
    <div class="public-container about-hero-inner">
      <p>Trang chủ &gt; Giới thiệu</p>
      <h1>Giới thiệu về<br />{{ thongTinTruong.tenNgan }}</h1>
      <h3>{{ thongTinTruong.slogan }}</h3>
      <div>
        Với phương châm đào tạo gắn liền thực tiễn, chúng tôi không ngừng nâng cao chất lượng giáo dục,
        phát triển kỹ năng nghề nghiệp, đồng hành cùng thế hệ trẻ kiến tạo tương lai bền vững.
      </div>
    </div>
  </section>
</template>

<script setup>
import { thongTinTruong } from '@/modules/public/data/thongTinTruongData.js'
</script>
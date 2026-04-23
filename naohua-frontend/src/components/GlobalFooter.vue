<template>
  <a-layout-footer class="footer">
    <div class="footer-content">
      <p class="copyright">
        <a
          href="https://www.github.com/likill"
          target="_blank"
          rel="noopener noreferrer"
          class="author-link"
        >
          by likill
        </a>
      </p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
// 无需额外的响应式数据
</script>

<style scoped>
.footer {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  backdrop-filter: blur(10px);
  text-align: center;
  padding: 24px;
  margin-top: 40px;
}

.copyright {
  margin: 0;
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
}

.copyright a {
  color: #fff;
  text-decoration: none;
  font-weight: 500;
}

.copyright a:hover {
  text-decoration: underline;
}
</style>

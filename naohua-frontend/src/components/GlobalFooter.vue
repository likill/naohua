<template>
  <a-layout-footer class="footer">
    <div class="footer-content">
      <p class="beian">
        <a
          href="https://beian.mps.gov.cn/#/query/webSearch?code=34010202602280"
          target="_blank"
          rel="noreferrer"
          class="beian-link"
        >
          <svg class="beian-icon" viewBox="0 0 1024 1024" aria-hidden="true">
            <path
              d="M512 64 128 224v320c0 198.4 137.6 384 384 448 246.4-64 384-249.6 384-448V224L512 64z m0 80 288 115.2V544c0 160-108.8 310.4-288 361.6-179.2-51.2-288-201.6-288-361.6V259.2L512 144z m-32 192v64h64v-64h-64z m0 128v192h64v-192h-64z"
            />
          </svg>
          皖公网安备34010202602280号
        </a>
      </p>
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

.footer-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.beian {
  margin: 0;
  line-height: 1;
}

.beian-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-size: 14px;
}

.beian-icon {
  width: 16px;
  height: 16px;
  fill: rgba(255, 255, 255, 0.9);
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

.beian-link:hover {
  text-decoration: underline;
  color: #fff;
}
</style>

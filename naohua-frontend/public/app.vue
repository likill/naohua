<template>
  <div class="container">
    <!-- 导航栏 -->
    <nav class="navbar">
      <div class="logo">脑花</div>
      <div class="nav-links">
        <a href="#">首页</a>
        <a href="#">功能</a>
        <a href="#">关于</a>
        <a href="#" class="btn-login">登录</a>
      </div>
    </nav>

    <!-- 英雄区域 -->
    <section class="hero">
      <div class="hero-content">
        <h1 class="title">脑花</h1>
        <p class="tagline">让思维落地</p>
        <p class="description">
          脑花是一个创新的思维导图工具，帮助你将脑海中的灵感
          <br />
          转化为清晰的思路，让每一个想法都能落地生根。
        </p>
        <div class="hero-buttons">
          <button class="btn-primary">开始使用</button>
          <button class="btn-secondary">了解更多</button>
        </div>
      </div>
      <div class="hero-visual">
        <div class="brain-icon">
          <div class="circle c1"></div>
          <div class="circle c2"></div>
          <div class="circle c3"></div>
          <div class="lines">
            <div class="line l1"></div>
            <div class="line l2"></div>
            <div class="line l3"></div>
            <div class="line l4"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- 特性区域 -->
    <section class="features">
      <div class="feature-card">
        <div class="feature-icon">💡</div>
        <h3>灵感捕捉</h3>
        <p>随时随地记录脑海中的灵感，不让任何一个好想法溜走</p>
      </div>
      <div class="feature-card">
        <div class="feature-icon">🧠</div>
        <h3>思维整理</h3>
        <p>将杂乱的思绪整理成清晰的思维导图</p>
      </div>
      <div class="feature-card">
        <div class="feature-icon">🚀</div>
        <h3>落地执行</h3>
        <p>从想法到行动，让思维真正落地生根</p>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <p>© 2026 脑花 · 让思维落地</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 50%, #DC143C 100%);
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 导航栏 */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}

.logo {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 30px;
}

.nav-links a {
  color: #fff;
  text-decoration: none;
  font-size: 16px;
  transition: opacity 0.3s;
}

.nav-links a:hover {
  opacity: 0.8;
}

.btn-login {
  padding: 8px 24px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  backdrop-filter: blur(10px);
}

/* 英雄区域 */
.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 80px 0;
  gap: 60px;
}

.hero-content {
  flex: 1;
}

.title {
  font-size: 72px;
  font-weight: bold;
  color: #fff;
  text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.3);
  margin-bottom: 10px;
}

.tagline {
  font-size: 32px;
  color: #fff;
  margin-bottom: 30px;
  font-weight: 300;
  letter-spacing: 8px;
}

.description {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.8;
  margin-bottom: 40px;
}

.hero-buttons {
  display: flex;
  gap: 20px;
}

.btn-primary {
  padding: 14px 40px;
  background: #fff;
  color: #DC143C;
  border: none;
  border-radius: 30px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.btn-secondary {
  padding: 14px 40px;
  background: transparent;
  color: #fff;
  border: 2px solid #fff;
  border-radius: 30px;
  font-size: 18px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.1);
}

/* 脑花视觉元素 */
.hero-visual {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.brain-icon {
  position: relative;
  width: 300px;
  height: 300px;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(5px);
}

.c1 {
  width: 200px;
  height: 200px;
  top: 50px;
  left: 50px;
  background: rgba(255, 255, 255, 0.2);
}

.c2 {
  width: 140px;
  height: 140px;
  top: 30px;
  left: 120px;
  background: rgba(255, 255, 255, 0.25);
}

.c3 {
  width: 80px;
  height: 80px;
  top: 100px;
  left: 180px;
  background: rgba(255, 255, 255, 0.3);
}

.lines {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.line {
  position: absolute;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 2px;
}

.l1 {
  width: 100px;
  height: 4px;
  top: -2px;
  left: -50px;
}

.l2 {
  width: 4px;
  height: 100px;
  top: -50px;
  left: -2px;
}

.l3 {
  width: 80px;
  height: 4px;
  top: -2px;
  left: 20px;
  transform: rotate(45deg);
  transform-origin: left center;
}

.l4 {
  width: 80px;
  height: 4px;
  top: -2px;
  left: 20px;
  transform: rotate(-45deg);
  transform-origin: left center;
}

/* 特性区域 */
.features {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  padding: 60px 0;
}

.feature-card {
  background: rgba(255, 255, 255, 0.95);
  padding: 40px 30px;
  border-radius: 20px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

.feature-icon {
  font-size: 50px;
  margin-bottom: 20px;
}

.feature-card h3 {
  color: #DC143C;
  font-size: 22px;
  margin-bottom: 15px;
}

.feature-card p {
  color: #666;
  font-size: 15px;
  line-height: 1.6;
}

/* 页脚 */
.footer {
  text-align: center;
  padding: 40px 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero {
    flex-direction: column;
    text-align: center;
    padding: 40px 0;
  }

  .title {
    font-size: 48px;
  }

  .tagline {
    font-size: 24px;
    letter-spacing: 4px;
  }

  .hero-buttons {
    justify-content: center;
  }

  .hero-visual {
    order: -1;
  }

  .brain-icon {
    width: 200px;
    height: 200px;
  }

  .c1 {
    width: 130px;
    height: 130px;
    top: 35px;
    left: 35px;
  }

  .c2 {
    width: 90px;
    height: 90px;
    top: 20px;
    left: 80px;
  }

  .c3 {
    width: 50px;
    height: 50px;
    top: 70px;
    left: 120px;
  }

  .features {
    grid-template-columns: 1fr;
  }
}
</style>

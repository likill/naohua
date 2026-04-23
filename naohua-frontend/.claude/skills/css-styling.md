---
name: css-styling
description: Use when writing CSS styles, implementing responsive layouts, animations, or working with CSS-in-JS solutions. Covers Flexbox, Grid, transitions, and modern CSS features.
---

# CSS Styling Guide

## Flexbox Layout

```css
.container {
  display: flex;
  flex-direction: row | column;
  justify-content: center | space-between | flex-start;
  align-items: center | stretch | flex-start;
  gap: 16px;
  flex-wrap: wrap;
}
```

## CSS Grid

```css
.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}
```

## Responsive Design

```css
/* Mobile first approach */
@media (min-width: 768px) {
  /* Tablet and up */
}

@media (min-width: 1024px) {
  /* Desktop */
}

/* Hide on mobile */
@media (max-width: 768px) {
  .sidebar { display: none; }
}
```

## CSS Transitions & Animations

```css
/* Simple transition */
.transition-all {
  transition: all 0.3s ease;
}

/* Keyframe animation */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-fade-in {
  animation: fadeIn 0.3s ease forwards;
}

/* Steps animation for pixel art */
@keyframes pixel-blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}
```

## Box Shadow Techniques

```css
/* Subtle shadow */
.shadow-sm {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Card shadow */
.shadow-md {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

/* Pixel art 3D effect */
.pixel-btn {
  box-shadow:
    2px 2px 0 #333,
    inset -2px -2px 0 #8B0000,
    inset 2px 2px 0 #FF6B6B;
}
```

## Gradient Backgrounds

```css
/* Linear gradient */
.gradient-primary {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
}

/* Radial gradient */
.gradient-radial {
  background: radial-gradient(circle, #fff 0%, #f0f0f0 100%);
}
```

## Glass Morphism

```css
.glass {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}
```

## Variables & Theming

```css
:root {
  --primary-color: #DC143C;
  --secondary-color: #CD5C5C;
  --bg-cream: #FFEFD5;
}

.element {
  color: var(--primary-color);
  background: var(--bg-cream);
}
```

## Image Rendering

```css
/* Pixel art */
.pixel-art {
  image-rendering: pixelated;
}

/* Smooth images */
.smooth {
  image-rendering: auto;
}
```

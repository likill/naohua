---
name: color-theme
description: Use when defining or applying color schemes, theming, or CSS custom properties for design systems. Covers primary/secondary colors, semantic colors, and dark mode patterns.
---

# Color & Theme Guide

## Project Color Palette

This project uses a crimson/Indian red theme:

```css
:root {
  /* Primary Colors */
  --primary-color: #DC143C;      /* Crimson */
  --secondary-color: #CD5C5C;    /* Indian Red */
  
  /* Background */
  --bg-cream: #FFEFD5;           /* Cream background */
  --bg-white: #FFFFFF;
  
  /* Text */
  --text-primary: #333333;
  --text-secondary: #666666;
  --text-muted: #999999;
  
  /* Semantic Colors */
  --success: #4ade80;
  --warning: #fbbf24;
  --error: #ef4444;
  --info: #3b82f6;
}
```

## Ant Design Vue Theme Override

```vue
<ConfigProvider
  :theme="{
    token: {
      colorPrimary: '#DC143C',
      borderRadius: 8,
      fontFamily: '-apple-system, BlinkMacSystemFont, sans-serif',
    }
  }"
>
```

## Gradient Backgrounds

```css
/* Primary gradient */
.gradient-primary {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
}

/* Dark to light */
.gradient-dark {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 50%, #DC143C 100%);
}

/* Soft accent */
.gradient-soft {
  background: linear-gradient(135deg, rgba(220, 20, 60, 0.1) 0%, rgba(205, 92, 92, 0.1) 100%);
}
```

## Semantic Button Colors

```css
.btn-primary {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  color: white;
}

.btn-outline {
  background: transparent;
  border: 2px solid #DC143C;
  color: #DC143C;
}

.btn-ghost {
  background: rgba(220, 20, 60, 0.1);
  color: #DC143C;
}
```

## Hover & Active States

```css
/* Primary button hover */
.btn-primary:hover {
  background: linear-gradient(135deg, #CD5C5C 0%, #DC143C 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(220, 20, 60, 0.3);
}

/* Card hover */
.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}
```

## Text Colors

```css
.text-primary {
  color: #DC143C;
}

.text-secondary {
  color: #CD5C5C;
}

.text-dark {
  color: #333333;
}

.text-muted {
  color: #999999;
}

/* White text with shadow for dark backgrounds */
.text-on-dark {
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}
```

## Border Colors

```css
.border-primary {
  border-color: #DC143C;
}

.border-light {
  border-color: rgba(220, 20, 60, 0.2);
}

.border-active {
  border-color: #DC143C;
  border-bottom-color: transparent;
}
```

## Status Colors

```css
/* Online/Success */
.status-online {
  background: #4ade80;
  color: white;
}

/* Warning */
.status-warning {
  background: #fbbf24;
  color: #333;
}

/* Error/Danger */
.status-error {
  background: #ef4444;
  color: white;
}
```

## Pixel Art Style Colors

```css
/* For pixel art aesthetic with #333 borders */
.pixel-element {
  border: 3px solid #333;
  box-shadow:
    4px 4px 0 #333,
    inset -2px -2px 0 #999,
    inset 2px 2px 0 #fff;
}

/* Pixel button colors */
.pixel-btn {
  background: #DC143C;
  border: 3px solid #333;
  box-shadow:
    2px 2px 0 #333,
    inset -2px -2px 0 #8B0000,
    inset 2px 2px 0 #FF6B6B;
}
```

## Glass Morphism Colors

```css
.glass-light {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.glass-dark {
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(10px);
}
```

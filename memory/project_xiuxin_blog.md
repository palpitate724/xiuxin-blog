---
name: xiuxin-blog
description: Vue3 + Element Plus cyberpunk-themed blog platform - frontend project structure and key details
type: project
---

xiuxin-blog (蔚蓝极客) is a Vue3 + TypeScript + Vite blog CMS with cyber/tech dark theme.

**Why:** Personal blog platform for content creators — writers/editors manage articles, categories, tags; public readers browse articles.

**How to apply:** When working on this project, remember:
- Tech stack: Vue 3.5, TypeScript, Vite 8, Element Plus 2.14, Pinia 4, Axios, markdown-it + highlight.js
- Backend API base: `http://127.0.0.1:8080/api/v1` (development), production uses env vars
- 3 routes: `/` (home with hero video + article list), `/user` (login/signup), `/article/:id` (article detail with chapter index)
- CSS design tokens in `src/styles/tokens.css`, global reset in `src/styles/global.css`
- Pinia stores use `persistedstate` plugin with localStorage
- Background animations: Matrix rain + bezier curves on canvas (App.vue), video hero slider (shouping.vue)
- Custom cursor with trail effect (App.vue), hidden on mobile and over inputs
- All API calls go through `src/api/requset.ts` axios instance
- Visual style: deep dark gradients (#0a1628→#1a2a4a), blue-cyan accents (#3b82f6, #06d6d3)

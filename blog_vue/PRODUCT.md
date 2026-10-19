# Product

## Register

product

## Users

Primary users are **content administrators** of the "xiuxin-blog" platform — writers and editors who create, edit, publish, and manage blog articles, categories, and tags. Secondary users are unauthenticated readers who browse articles. The admin users operate in a content-management workflow: they log in, navigate article lists, select an article to view/edit, and manage metadata. Their context is focused, task-oriented sessions in a browser on desktop and mobile.

## Product Purpose

A Vue3 + Element Plus admin interface for managing a personal blog — articles, categories, tags, and user accounts. The backend API serves `/user/signup`, `/user/login`, `/art`, `/art/minio`, `/cat`, `/tag` endpoints. Success looks like fast, reliable article creation and editing with minimal friction, a readable article view for public consumption, and consistent UI behavior across all admin surfaces.

## Brand Personality

**Technical, dark, professional.** The interface carries a cyber/tech aesthetic: deep dark gradients (#0a1628 → #1a2a4a), blue-cyan accents (#3b82f6, #06d6d3), canvas-based background animations (matrix rain, bezier curves), a custom cursor with trail effects, and a scroll-progress indicator. The mood is focused and immersive — suitable for prolonged content editing — without drifting into gamification or distraction.

Reference inspiration:
- **GitHub Dark** — dark surface, high information density, clean typography hierarchy. What fits: functional dark theme without novelty-for-its-own-sake.
- **VS Code Dark+ theme** — deep background, syntax-accented highlights, tool that disappears into the task.
- **Ghost Admin** (dark variant) — editorial-focused admin panel with readable content editing.

## Anti-references

- **AI-default aesthetic**: no cream/beige warm-neutral backgrounds, no editorial-serif + italic headlines, no glassmorphism-as-default, no tiny uppercase tracked eyebrows above every section, no hero-metric card grids. The saturated 2025–2026 AI landing-page monoculture does not apply here.
- **Maximalist admin templates**: no heavy bevels, drop-shadow stacking, neon-overload color schemes, or animated noise that competes with content editing.
- **Generic SaaS dashboards**: no fake KPI tiles, no gradient eyebrows, no "dashboard" as the starting assumption. This is a blog CMS, not a business intelligence tool.

## Design Principles

1. **Earned familiarity** — The tool should disappear into the task. Use standard, well-understood form controls and navigation. Don't reinvent scrollbars, modals, or form fields for flavor.
2. **Readability first** — Article content is the primary output. The article view must maintain a clean reading measure (65–75ch), sufficient line-height, and text contrast ≥ 4.5:1 on the dark surface.
3. **Density serves the editor** — Tables and lists for article/category/tag management should pack information efficiently. Reserve whitespace for separation, not emptiness.
4. **Dark surface is intentional** — The dark gradient background is part of the brand personality. All surfaces, dialogs, and overlays should maintain visual continuity with it rather than switching to light-on-dark inconsistency.
5. **Motion conveys state, not decoration** — Background animations (matrix rain, curves) are ambient. Interactive transitions (hover, focus, loading) must be 150–250ms and serve a functional purpose. Respect `prefers-reduced-motion`.

## Accessibility & Inclusion

- **WCAG Level**: AA minimum (body text ≥ 4.5:1, large text ≥ 3:1, UI components ≥ 3:1).
- **Known needs**: Keyboard navigation must work across all interactive elements (forms, tables, pagination, dialogs). Form fields must have associated labels. Error messages must be programmatically associated.
- **Reduced motion**: All animated content (canvas backgrounds, cursor effects, transitions) must respect `@media (prefers-reduced-motion: reduce)` — reduce to static or instant transitions.
- **Color blindness**: Do not rely on color alone to convey state (e.g., article status, tag categories). Use text labels, icons, or patterns in addition to color.

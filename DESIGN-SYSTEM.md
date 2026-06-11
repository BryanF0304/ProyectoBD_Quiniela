# Quiniela UNA — Design System
**Proyecto:** EIF211 · Universidad Nacional de Costa Rica  
**Stack:** Spring Boot + Thymeleaf + SQL Server  
**Tema:** App deportiva moderna · Fútbol costarricense

---

## 1. Design Tokens

All tokens live in `design-tokens.css` as CSS custom properties. Reference them everywhere — never hardcode hex values or magic numbers.

### 1.1 Colors

| Token | Value | Use |
|-------|-------|-----|
| `--color-primary-600` | `#16a34a` | Primary actions, active nav, focus rings |
| `--color-primary-700` | `#15803d` | Hover on primary elements |
| `--color-navy-900` | `#0f1f33` | Navbar background |
| `--color-navy-800` | `#162d4a` | Navbar hover, dark panels |
| `--color-gray-50` | `#f9fafb` | Page background |
| `--color-gray-100` | `#f3f4f6` | Table row stripes, secondary backgrounds |
| `--color-white` | `#ffffff` | Card backgrounds, inputs |

### 1.2 Typography

**Font:** Inter (via Google Fonts) · fallback: Segoe UI → system-ui

```html
<!-- Add to <head> in layout.html -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
```

| Role | Token | Weight | Notes |
|------|-------|--------|-------|
| Display / Hero | `--text-3xl` / `--text-4xl` | 800 | Page titles |
| H1 | `--text-2xl` | 700 | View headings |
| H2 | `--text-xl` | 600 | Section headings, card titles |
| H3 | `--text-lg` | 600 | Subsections |
| Body | `--text-base` | 400 | Paragraphs, table cells |
| Secondary | `--text-sm` | 400 | Captions, meta info |
| Label / Badge | `--text-xs` | 600 | Status badges, small labels |

### 1.3 Spacing

Base unit = **4px**. Use multiples: 4, 8, 12, 16, 20, 24, 32, 40, 48, 64.

### 1.4 Shadows

| Token | Elevation | Use |
|-------|-----------|-----|
| `--shadow-card` | 1 | White cards (default) |
| `--shadow-md` | 2 | Cards on hover |
| `--shadow-nav` | 3 | Navbar |
| `--shadow-xl` | 4 | Modals |

---

## 2. Badges / Status Chips

Badges are `<span>` elements with class `.badge` + a modifier.  
Always include an accessible `aria-label` or visible text.

### 2.1 Quiniela Status

| Badge | Class | Bg / Text | When |
|-------|-------|-----------|------|
| Abierta | `.badge--abierta` | Green | Inscriptions open |
| Cerrado | `.badge--cerrado` | Gray | Inscriptions closed, not yet started |
| Pendiente | `.badge--pendiente` | Amber | Match in progress / awaiting result |
| Finalizado | `.badge--finalizado` | Dark | Quiniela complete |

```html
<span class="badge badge--abierta">Abierta</span>
<span class="badge badge--cerrado">Cerrado</span>
<span class="badge badge--pendiente">Pendiente</span>
<span class="badge badge--finalizado">Finalizado</span>
```

### 2.2 Points Badges

| Badge | Class | Color | When |
|-------|-------|-------|------|
| 0 pts | `.badge-pts--zero` | Red | Wrong prediction |
| 1 pt | `.badge-pts--one` | Blue | Partial (e.g., draw correct) |
| 3 pts | `.badge-pts--three` | Gold | Exact result |

```html
<span class="badge-pts badge-pts--zero">0 pts</span>
<span class="badge-pts badge-pts--one">1 pt</span>
<span class="badge-pts badge-pts--three">3 pts</span>
```

---

## 3. Buttons

### Variants

| Variant | Class | Use |
|---------|-------|-----|
| Primary | `.btn .btn--primary` | Main CTA (Inscribirse, Guardar, Registrar) |
| Secondary | `.btn .btn--secondary` | Cancel, back actions |
| Ghost | `.btn .btn--ghost` | Low-emphasis, nav actions |
| Danger | `.btn .btn--danger` | Destructive actions (Eliminar) |

### Sizes

| Size | Class | Height | Font |
|------|-------|--------|------|
| Small | `.btn--sm` | 32px | `--text-sm` |
| Medium (default) | — | 40px | `--text-base` |
| Large | `.btn--lg` | 48px | `--text-lg` |

### States

| State | Visual |
|-------|--------|
| Default | Solid fill, `--shadow-xs` |
| Hover | `--color-primary-700`, slight lift (`translateY(-1px)`) |
| Active | Pressed down (`translateY(0)`) |
| Focus | 3px ring `--color-primary-600` at `outline-offset: 2px` |
| Disabled | 50% opacity, `cursor: not-allowed` |
| Loading | Spinner icon + text replaced by "Cargando…" |

---

## 4. Form Inputs

### Text Input / Select / Textarea

```html
<div class="form-group">
  <label class="form-label" for="equipo">Equipo Local</label>
  <input class="form-input" id="equipo" type="text" placeholder="Ej: Deportivo Saprissa">
  <span class="form-hint">Ingrese el nombre completo del equipo.</span>
</div>
```

| State | Visual |
|-------|--------|
| Default | 1px `--border-color` border, `--radius-md` |
| Hover | Border → `--color-gray-400` |
| Focus | 2px ring `--color-primary-600`, border color → primary |
| Error | Border → `--color-error`, red hint text below |
| Disabled | Gray background, 60% opacity text |

### Validation

```html
<!-- Error state -->
<div class="form-group form-group--error">
  <label class="form-label" for="fecha">Fecha</label>
  <input class="form-input" id="fecha" type="date" aria-describedby="fecha-error">
  <span class="form-error" id="fecha-error" role="alert">La fecha es obligatoria.</span>
</div>
```

---

## 5. Cards

All cards use `.card` — white background, `--shadow-card`, `--radius-lg`.

### 5.1 Quiniela Card (list view)

```html
<div class="card card--quiniela">
  <div class="card__header">
    <h2 class="card__title">Quiniela Apertura 2025</h2>
    <span class="badge badge--abierta">Abierta</span>
  </div>
  <div class="card__body">
    <p class="card__meta">📅 Cierre: 15 jun 2025</p>
    <p class="card__meta">👥 32 participantes</p>
  </div>
  <div class="card__footer">
    <a href="/quiniela/1" class="btn btn--primary btn--sm">Ver detalles</a>
  </div>
</div>
```

### 5.2 Admin Action Card

```html
<div class="card card--action">
  <div class="card__icon">⚽</div>
  <h3 class="card__title">Partidos</h3>
  <p class="card__description">Registrar partidos y cargar resultados.</p>
  <a href="/admin/partidos" class="btn btn--primary">Administrar</a>
</div>
```

### Card hover state
On hover: `box-shadow` → `--shadow-md`, `transform: translateY(-2px)`, transition `--duration-slow`.

---

## 6. Navigation (Navbar)

```html
<nav class="navbar" aria-label="Navegación principal">
  <div class="navbar__brand">
    <span class="navbar__logo">⚽</span>
    <span class="navbar__title">Quiniela UNA</span>
  </div>
  <ul class="navbar__links" role="list">
    <li><a class="navbar__link" href="/home">Inicio</a></li>
    <li><a class="navbar__link" href="/quinielas">Quinielas</a></li>
    <li><a class="navbar__link navbar__link--active" href="/predicciones">Mis Predicciones</a></li>
  </ul>
  <div class="navbar__actions">
    <span class="navbar__user">👤 Bryan</span>
    <a class="btn btn--ghost" href="/logout">Salir</a>
  </div>
</nav>
```

| Element | Token |
|---------|-------|
| Background | `--color-navy-900` |
| Text | `--color-white` (80% opacity default, 100% hover/active) |
| Active underline | `--color-primary-600`, 2px bottom border |
| Shadow | `--shadow-nav` |
| Height | `--navbar-height` (64px) |

---

## 7. Tables

Used in: Mis predicciones, Admin Partidos results.

```html
<div class="table-wrapper">
  <table class="table" aria-label="Mis predicciones">
    <thead>
      <tr>
        <th scope="col">Partido</th>
        <th scope="col">Fecha</th>
        <th scope="col">Mi predicción</th>
        <th scope="col">Resultado</th>
        <th scope="col">Puntos</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>Saprissa vs Alajuelense</td>
        <td>12 jun 2025</td>
        <td>Saprissa gana</td>
        <td>2 – 1</td>
        <td><span class="badge-pts badge-pts--three">3 pts</span></td>
      </tr>
    </tbody>
  </table>
</div>
```

| Element | Style |
|---------|-------|
| `thead` | `--color-gray-50` bg, `--font-semibold`, `--text-sm`, uppercase, `--tracking-wider` |
| `tbody tr` | White bg, 1px bottom border `--border-color` |
| `tbody tr:nth-child(even)` | `--color-gray-50` bg (subtle stripe) |
| `tbody tr:hover` | `--color-primary-50` bg |
| Cell padding | `--space-3` vertical, `--space-4` horizontal |

---

## 8. Page Layouts

### 8.1 Auth Layout (Login / Register)
Centered card, max-width `--card-max-width` (420px), page bg `--color-gray-100`.

### 8.2 App Layout (all authenticated views)
- Full-height with sticky navbar (`--navbar-height`)
- `<main>` with horizontal padding `--space-6`, max-width `--content-max-width`, centered
- Page top padding: `--space-8` below navbar

### 8.3 Admin Layout
Same as App, but with a secondary "Admin" pill in the navbar indicating elevated access.

---

## 9. Patterns

### 9.1 Form page (Admin: Nueva quiniela / Registrar partido)

```
┌─ .form-card ────────────────────────────────────┐
│  <h1> Registrar Nuevo Partido                    │
│  ─────────────────────────────                  │
│  .form-group  [ Equipo Local        ]            │
│  .form-group  [ Equipo Visitante    ]            │
│  .form-group  [ Fecha y Hora   🗓   ]            │
│  .form-group  [ Jornada        ▼   ]            │
│                                                  │
│  [ Cancelar ]        [ Registrar Partido → ]     │
└──────────────────────────────────────────────────┘
```

- Section heading separates logical groups (e.g., "Equipos", "Programación")
- Primary action button right-aligned; cancel (ghost) left-aligned
- On mobile: stack to full-width, primary button full-width at bottom

### 9.2 Detail page (Quiniela Detail)

Tabs or anchored sections: **Info** · **Partidos** · **Participantes** · **Inscripción**  
Each section is a `.card` with its own heading.

### 9.3 Empty states

```html
<div class="empty-state">
  <span class="empty-state__icon">📋</span>
  <p class="empty-state__title">No hay quinielas activas</p>
  <p class="empty-state__desc">Cuando se cree una quiniela, aparecerá aquí.</p>
</div>
```

---

## 10. Accessibility Checklist

- [ ] All form inputs have `<label>` (associated via `for`/`id`)
- [ ] Error messages use `role="alert"` and `aria-describedby`
- [ ] Status badges have sufficient color contrast (≥ 4.5:1)
- [ ] Interactive cards have `tabindex="0"` and keyboard activation
- [ ] Navbar has `aria-label="Navegación principal"` and active link has `aria-current="page"`
- [ ] Tables have `<caption>` or `aria-label`, `scope` on `<th>`
- [ ] Focus rings never hidden (`outline: none` only with custom ring)
- [ ] Color alone never conveys meaning (badges include text)

---

## 11. Thymeleaf Snippets

### Inject active nav link
```html
<a class="navbar__link" th:href="@{/quinielas}"
   th:classappend="${currentPage == 'quinielas'} ? ' navbar__link--active' : ''">
  Quinielas
</a>
```

### Status badge fragment
```html
<!-- fragments/badges.html -->
<th:block th:fragment="statusBadge(status)">
  <span class="badge"
        th:classappend="${status == 'ABIERTA'} ? ' badge--abierta'
                      : (${status == 'CERRADO'} ? ' badge--cerrado'
                      : (${status == 'PENDIENTE'} ? ' badge--pendiente'
                      : ' badge--finalizado'))"
        th:text="${status}">
  </span>
</th:block>

<!-- Usage -->
<th:block th:replace="~{fragments/badges :: statusBadge(${quiniela.status})}"/>
```

### Points badge fragment
```html
<th:block th:fragment="pointsBadge(pts)">
  <span class="badge-pts"
        th:classappend="${pts == 3} ? ' badge-pts--three'
                      : (${pts == 1} ? ' badge-pts--one'
                      : ' badge-pts--zero')"
        th:text="${pts + ' pts'}">
  </span>
</th:block>
```

---

## 12. CSS Architecture

```
src/main/resources/static/css/
├── design-tokens.css        ← CSS custom properties (this file)
├── base.css                 ← Reset, body, typography defaults
├── components/
│   ├── badges.css
│   ├── buttons.css
│   ├── cards.css
│   ├── forms.css
│   ├── navbar.css
│   └── tables.css
├── layouts/
│   ├── auth.css
│   └── app.css
└── main.css                 ← @import all above (in order)
```

Import order in `main.css`:
```css
@import 'design-tokens.css';
@import 'base.css';
@import 'components/badges.css';
@import 'components/buttons.css';
@import 'components/cards.css';
@import 'components/forms.css';
@import 'components/navbar.css';
@import 'components/tables.css';
@import 'layouts/auth.css';
@import 'layouts/app.css';
```

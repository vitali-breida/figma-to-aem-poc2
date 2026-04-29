# Figma Design System Rules — WKND AEM Project

This document guides Figma-to-code integration for the WKND AEM (Adobe Experience Manager) reference implementation. Use it alongside the Figma MCP server when reading designs, generating components, or maintaining Code Connect mappings.

---

## 1. Design Tokens

### Color Tokens
**Source:** `ui.frontend/src/main/webpack/base/sass/_variables.scss`

All colors are SCSS variables with CSS custom property overrides for runtime theming.

```scss
// Core palette
$black:       #202020;
$white:       #ffffff;
$gray:        #696969;
$gray-light:  #EBEBEB;
$gray-lighter:#F7F7F7;
$yellow:      #FFEA00;   // brand primary
$blue:        #0045FF;   // link color
$pink:        #FF0058;

// Semantic aliases (with CSS custom property fallback)
$brand-primary:       var(--brandPrimary, #FFEA00);
$brand-secondary:     var(--brandSecondary, #202020);
$brand-third:         var(--brandThird, #EBEBEB);
$link-color:          var(--linkColor, #0045FF);
$text-color:          var(--textColor, #202020);
$text-color-inverse:  var(--textColorInverse, #EBEBEB);
$nav-link:            var(--navLink, #202020);
$nav-link-inverse:    var(--navLinkInverse, #EBEBEB);
$body-bg:             $white;
```

**Mapping rule:** When matching Figma fill colors to code tokens, prefer semantic SCSS variables over raw hex. Use CSS custom properties (`var(--...)`) when the design supports theming.

---

### Typography Tokens

```scss
// Font families
$font-family-sans-serif: var(--fontFamilySansSerif, "Source Sans Pro", "Helvetica Neue", Arial, sans-serif);
$font-family-serif:      var(--fontFamilySerif, "Asar", Georgia, "Times New Roman", serif);
$font-family-base:       $font-family-sans-serif;

// Font sizes
$font-size-base:   18px;
$font-size-small:  14px;
$font-size-xsmall: 12px;
$font-size-medium: 18px;
$font-size-large:  24px;
$font-size-xlarge: 48px;

// Heading sizes
$font-size-h1: 40px;
$font-size-h2: 36px;
$font-size-h3: 24px;
$font-size-h4: 16px;
$font-size-h5: 14px;
$font-size-h6: 10px;

// Font weights
$font-weight-light:     300;
$font-weight-normal:    400;
$font-weight-semi-bold: 400;
$font-weight-bold:      600;

// Line height
$line-height-base: 1.5;
```

**Heading rule:** `h1–h3` use `$font-family-serif` (Asar/Georgia). `h4–h6` use `$font-family-sans-serif` and are rendered `text-transform: uppercase`.

---

### Spacing & Sizing Tokens

```scss
$gutter-padding:      14px;
$button-size:         var(--buttonSize, 48px);
$button-border-radius:var(--buttonBorderRadius, 0px);
$list-item-height:    var(--listItemHeight, 120px);
$list-item-border:    5px solid $brand-third;
$max-width:           1164px;
$max-body-width:      1680px;
$header-height:       var(--headerHeight, 200px);
$header-mobile-height:var(--headerMobileHeight, 130px);
$utility-nav-height:  var(--utilityNavHeight, 25px);
```

---

### Breakpoints

```scss
$screen-xsmall: 475px;   // extra-small devices
$screen-small:  767px;   // phone breakpoint
$screen-medium: 1024px;  // tablet breakpoint
$screen-large:  1200px;  // desktop breakpoint
```

**Responsive strategy:** Mobile-first base styles; overrides at each breakpoint via `@media` queries. The AEM grid (`aem-Grid`) regenerates column counts at `phone` and `tablet` breakpoints.

---

## 2. Component Library

**HTL templates:** `ui.apps/src/main/content/jcr_root/apps/wknd/components/`  
**Frontend SCSS/TS:** `ui.frontend/src/main/webpack/components/`  
**Storybook stories:** `ui.frontend/stories/`

### Available Components (34)

| Component | Location | Variations |
|---|---|---|
| Accordion | `components/accordion/` | default |
| Breadcrumb | `components/breadcrumb/` | default |
| Button | `components/button/` | default, icon |
| Byline | `components/byline/` | default |
| Carousel | `components/carousel/` | default, hero, mini |
| Container | `components/container/` | default |
| ContentFragment | `components/contentfragment/` | default |
| ContentFragmentList | `components/contentfragmentlist/` | default |
| Download | `components/download/` | default |
| Embed | `components/embed/` | default |
| ExperienceFragment | `components/experiencefragment/` | default |
| Image | `components/image/` | default |
| ImageList | `components/imagelist/` | default |
| LanguageNavigation | `components/languagenavigation/` | default |
| LayoutContainer | `components/layoutcontainer/` | default, fixed-width, header, footer, modal, utility |
| List | `components/list/` | default |
| Navigation | `components/navigation/` | default |
| Page | `components/page/` | default |
| ProgressBar | `components/progressbar/` | default |
| Search | `components/search/` | default |
| Separator | `components/separator/` | default, colors, spacing |
| Sharing | `components/sharing/` | default |
| Tabs | `components/tabs/` | default |
| Teaser | `components/teaser/` | default, featured, hero, list, card, slide, secure |
| Text | `components/text/` | default |
| Title | `components/title/` | default |

### Component File Structure

```
ui.apps/src/main/content/jcr_root/apps/wknd/components/[name]/
├── .content.xml              # JCR registration; extends a core component
├── [name].html               # HTL template
├── _cq_dialog.xml            # Author dialog (property fields)
└── _cq_design_dialog.xml     # Design dialog (site-wide settings)

ui.frontend/src/main/webpack/components/[name]/
├── [name].scss               # Component entry (imports from styles/)
└── styles/
    ├── _default.scss         # Base styles for the default variation
    └── _[variant].scss       # One file per additional variation
```

### Sling Model Pattern

Each component pairs an interface with an implementation:

```
core/src/main/java/com/adobe/aem/guides/wknd/core/models/
├── Byline.java               # Interface — public API / getters
├── ImageList.java
└── impl/
    ├── BylineImpl.java       # @Model implementation
    └── ImageListImpl.java
```

HTL binds the model via `data-sly-use`:

```html
<div data-sly-use.byline="com.adobe.aem.guides.wknd.core.models.Byline"
     class="cmp-byline">
  <h2 class="cmp-byline__name">${byline.name}</h2>
</div>
```

---

## 3. Styling Approach

### CSS Methodology: BEM + SCSS

Class naming pattern: `.cmp-[component]__[element]--[modifier]`

```scss
// Block
.cmp-button { ... }

// Element
.cmp-button__text { ... }
.cmp-button__icon { ... }

// Modifier (variation)
.cmp-button--primary { ... }
.cmp-button--secondary { ... }
```

### Global SCSS Import Chain

```
ui.frontend/src/main/webpack/site/main.scss
  ├── normalize-scss            (CSS reset)
  ├── base/sass/_shared.scss
  │     ├── _variables.scss     (all tokens)
  │     ├── _mixins.scss
  │     ├── _grid.scss
  │     └── _wkndicons.scss     (icon font)
  ├── elements.scss             (body, headings, links, grid)
  └── components/[name]/[name].scss  (×34)
```

### Variation Wiring

Variations are selected at author time via the dialog and applied as a wrapper class. Example for Teaser:

```scss
// teaser.scss
@import "styles/default";
@import "styles/hero";
@import "styles/card";

// styles/_hero.scss
.cmp-teaser--hero {
  .cmp-teaser__image { ... }
  .cmp-teaser__title { ... }
}
```

**When implementing a Figma design for a component:** pick the closest existing variation as the base; override only what differs in a new `_[variant].scss` file; never patch `_default.scss`.

---

## 4. Icon System

**Source:** `ui.frontend/src/main/webpack/base/sass/_wkndicons.scss`  
**Font files:** `ui.frontend/src/main/webpack/resources/fonts/wknd-icon-font.{ttf,woff,svg}`

### Available Icons (26)

| Variable | Unicode | Label |
|---|---|---|
| `$wkndicon-menu` | `\e916` | Hamburger menu |
| `$wkndicon-google` | `\e900` | Google logo |
| `$wkndicon-twitter` | `\e901` | Twitter / X |
| `$wkndicon-facebook` | `\e902` | Facebook |
| `$wkndicon-instagram` | `\e903` | Instagram |
| `$wkndicon-social-share` | `\e904` | Generic share |
| `$wkndicon-map` | `\e905` | Map |
| `$wkndicon-alert` | `\e906` | Alert |
| `$wkndicon-download` | `\e907` | Download |
| `$wkndicon-info` | `\e908` | Info |
| `$wkndicon-email` | `\e909` | Email |
| `$wkndicon-comment` | `\e90a` | Comment |
| `$wkndicon-share` | `\e90b` | Share |
| `$wkndicon-user` | `\e90c` | User / Profile |
| `$wkndicon-home` | `\e90d` | Home |
| `$wkndicon-right-arrow` | `\e90e` | Right arrow |
| `$wkndicon-left-arrow` | `\e90f` | Left arrow |
| `$wkndicon-minus` | `\e910` | Minus / Collapse |
| `$wkndicon-plus` | `\e911` | Plus / Expand |
| `$wkndicon-location` | `\e912` | Location pin |
| `$wkndicon-search` | `\e913` | Search / Magnifier |
| `$wkndicon-delete` | `\e914` | Delete / X |
| `$wkndicon-cart` | `\e915` | Shopping cart |
| `$wkndicon-lock` | `\e98f` | Locked |
| `$wkndicon-unlocked` | `\e990` | Unlocked |
| `$wkndicon-play3` | `\ea1c` | Play button |

### Icon Usage

Any element whose class contains `__icon` or `-icon` auto-receives the icon font mixin. Use `::before` with the variable:

```scss
.cmp-search__icon::before {
  content: $wkndicon-search;
}
```

Or use the standalone CSS class in HTL:

```html
<span class="wkndicon-search" aria-hidden="true"></span>
```

**When mapping Figma icons to code:** check the table above before adding new SVGs. If the design uses an icon not in the set, add the TTF/WOFF glyph via Icomoon and register a new variable in `_wkndicons.scss`.

---

## 5. Asset Management

**Source assets:** `ui.frontend/src/main/webpack/resources/`  
**Webpack copies to:** `dist/clientlib-site/resources/`  
**AEM serves from:** `/etc.clientlibs/wknd/clientlibs/clientlib-site/resources/`

### File-loader Rule

```js
// webpack.common.js
{
  test: /\.(ico|jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2)(\?.*)?$/,
  use: { loader: 'file-loader', options: { name: '[path][name].[ext]' } }
}
```

### Referencing Assets in SCSS

```scss
// Relative to the SCSS file's location
background-image: url('../resources/images/loading-icon.svg');
```

### Favicons

Sizes present: 32×32, 128×128, 152×152, 167×167, 180×180, 192×192, 512×512. Registered in `manifest.json` with theme color `#FFEA00`.

**When Figma exports assets:** place them in `ui.frontend/src/main/webpack/resources/images/`. Use SVG for icons/illustrations and WebP/JPG for photography.

---

## 6. Build System

**Bundler:** Webpack 5  
**Entry point:** `ui.frontend/src/main/webpack/site/main.ts`  
**Outputs:**
- `dist/clientlib-site/js/site.bundle.js` — main JS bundle
- `dist/clientlib-site/css/site.css` — main CSS bundle
- `dist/clientlib-dependencies/` — third-party (jQuery, normalize)

### ClientLib Generator

`ui.frontend/clientlib.config.js` packages webpack output into AEM Client Libraries at:
- `ui.apps/src/main/content/jcr_root/apps/wknd/clientlibs/clientlib-site/`
- `ui.apps/src/main/content/jcr_root/apps/wknd/clientlibs/clientlib-dependencies/`

### Frontend Development Commands

```bash
cd ui.frontend

npm run dev        # Development build (source maps, no minification)
npm run prod       # Production build (minified, tree-shaken)
npm run watch      # Live reload: webpack-dev-server + aemsync to AEM
npm run storybook  # Storybook on port 6006
```

### Full AEM Build

```bash
# Build and install all modules to AEM at localhost:4502
mvn clean install -PautoInstallSinglePackage
```

---

## 7. Figma-to-Code Integration Rules

### General Workflow

1. **Get design context** using `get_design_context` with the node ID and file key.
2. **Map colors** to SCSS token variables in `_variables.scss`; never hard-code hex values.
3. **Select closest component** from the 34-component library; prefer variation over new component.
4. **BEM naming:** prefix all new classes with `cmp-[name]__` / `cmp-[name]--`.
5. **Breakpoints:** implement mobile-first; add overrides at `$screen-small` (767 px), `$screen-medium` (1024 px), `$screen-large` (1200 px).
6. **Icons:** use the icon font; avoid inline SVG for icons that already exist in `_wkndicons.scss`.
7. **Typography:** reference `$font-size-*` and `$font-family-*` variables; never hard-code `px` or font names.

### Adding a New Component Variation

```bash
# 1. Create variation SCSS
touch ui.frontend/src/main/webpack/components/[name]/styles/_[variant].scss

# 2. Import it in the component entry
echo "@import 'styles/[variant]';" >> ui.frontend/src/main/webpack/components/[name]/[name].scss

# 3. Add the variation class wrapper using the BEM modifier convention:
#    .cmp-[name]--[variant] { ... }
```

### Code Connect Mapping

When setting up Figma Code Connect for this project, use these patterns:

```ts
// .figma.ts example for Button component
import figma from '@figma/code-connect';

figma.connect(Button, 'FIGMA_NODE_URL', {
  props: {
    variant: figma.enum('Variant', {
      primary:   'cmp-button--primary',
      secondary: 'cmp-button--secondary',
    }),
    label: figma.string('Label'),
  },
  example: ({ variant, label }) => (
    `<div class="button ${variant}">
       <a class="cmp-button">
         <span class="cmp-button__text">${label}</span>
       </a>
     </div>`
  ),
});
```

### Storybook Stories

Existing stories live in `ui.frontend/stories/`. Each story is plain HTML using the same BEM classes. Use these as reference when validating generated code.

---

## 8. Quick Reference

| Concern | File |
|---|---|
| Color / typography / spacing tokens | `ui.frontend/src/main/webpack/base/sass/_variables.scss` |
| SCSS mixins | `ui.frontend/src/main/webpack/base/sass/_mixins.scss` |
| Grid system | `ui.frontend/src/main/webpack/base/sass/_grid.scss` |
| Icon font variables | `ui.frontend/src/main/webpack/base/sass/_wkndicons.scss` |
| Global element styles | `ui.frontend/src/main/webpack/site/elements.scss` |
| Main SCSS entry | `ui.frontend/src/main/webpack/site/main.scss` |
| Component SCSS root | `ui.frontend/src/main/webpack/components/` |
| Component HTL templates | `ui.apps/src/main/content/jcr_root/apps/wknd/components/` |
| Sling Model interfaces | `core/src/main/java/com/adobe/aem/guides/wknd/core/models/` |
| Sling Model implementations | `core/src/main/java/com/adobe/aem/guides/wknd/core/models/impl/` |
| Storybook stories | `ui.frontend/stories/` |
| Webpack config (shared) | `ui.frontend/webpack.common.js` |
| ClientLib generator config | `ui.frontend/clientlib.config.js` |
| Static resources | `ui.frontend/src/main/webpack/resources/` |

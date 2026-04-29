# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**WKND Sites Project** is a full-stack Adobe Experience Manager (AEM) implementation for a fictitious lifestyle brand. It's a reference/sample project showcasing modern AEM development practices.

- **Type**: Java/Maven-based AEM project with Node.js frontend
- **Version**: 4.5.0 (requires Java 21 and Maven 3.9.4+)
- **License**: MIT
- **Repository**: `github.com/adobe/aem-guides-wknd`
- **AEM Compatibility**: AEM as a Cloud Service (2025.6.21193+) and AEM 6.5 LTS

## System Requirements

- **Java**: 21 (exactly)
- **Maven**: 3.9.4 or higher
- **Node.js**: v16.17.0+ (managed by Maven frontend plugin)
- **npm**: 8.15.0+ (managed by Maven frontend plugin)

## Build & Deployment Commands

### Build Full Project

```bash
# Standard build for AEM as a Cloud Service
mvn clean install -PautoInstallSinglePackage

# Build with classic profile for AEM 6.5.x
mvn clean install -PautoInstallSinglePackage -Pclassic
```

Both profiles automatically compile Java bundles, build frontend assets (TypeScript/SCSS), and install the complete package to a running AEM instance at `localhost:4502`.

### Build Individual Modules

```bash
# Build only the backend bundle (Java models)
mvn clean install -PautoInstallBundle -pl core

# Build only the frontend assets
mvn clean install -pl ui.frontend

# Build only the AEM app package (HTL, config, components)
mvn clean install -pl ui.apps
```

### Publish Environment

To deploy to a publish instance (localhost:4503):

```bash
mvn clean install -PautoInstallPackagePublish
```

## Frontend Development

The `ui.frontend` module uses **Webpack 5** with TypeScript and SCSS support for component CSS/JS assets.

### Frontend Build Commands

Inside `ui.frontend/`:

```bash
# Development build (tree-shaking disabled, source maps enabled)
npm run dev

# Production build (optimized, minified)
npm run prod

# Start Webpack dev server with live reload and AEM proxy
npm run start

# Watch mode: runs webpack-dev-server + AEM sync + file watcher
npm run watch

# Storybook for component development and testing
npm run storybook
```

### Frontend Workflow

1. **Source Code**: `ui.frontend/src/main/webpack/`
   - Component SCSS: organized by component name (e.g., `components/byline/byline.scss`)
   - Component TypeScript: globbed from component folders
   - Main entry point: `site/main.ts` (pulls in all component assets via glob-import-loader)

2. **Build Output**: `ui.frontend/dist/`
   - `clientlib-site/` → CSS/JS bundles for site styling and functionality
   - `clientlib-dependencies/` → third-party libraries (jQuery, normalize-scss, etc.)

3. **Client Library Generation**: After webpack runs, `aem-clientlib-generator` (via `clientlib.config.js`) packages the assets into AEM client libraries:
   - `ui.apps/src/main/content/jcr_root/apps/wknd/clientlibs/clientlib-site`
   - `ui.apps/src/main/content/jcr_root/apps/wknd/clientlibs/clientlib-dependencies`

4. **Sync to AEM During Development**: Use `npm run watch` for live reload while editing component assets.

## Testing

### Unit Tests (Java)

Located in `core/src/test/java/`. Tests use **JUnit 5** with **Mockito** and **AEM Test Fixtures**.

```bash
# Run all unit tests
mvn clean verify

# Run a single test class
mvn test -Dtest=BylineImplTest

# Run a specific test method
mvn test -Dtest=BylineImplTest#testMethod
```

Test classes follow naming convention: `*Test.java` and are co-located in `src/test/java/` mirroring `src/main/java/`.

Example: `BylineImplTest.java` tests `BylineImpl.java` using `@ExtendWith({AemContextExtension.class, MockitoExtension.class})` fixtures.

### Integration Tests

Located in `it.tests/`. These run against a real AEM environment (author + publish).

```bash
# Run integration tests against local AEM (4502/4503)
mvn clean verify -Plocal
```

Requires a running AEM author instance on `localhost:4502` and publish on `localhost:4503` with WKND content deployed.

### UI/E2E Tests

Located in `ui.tests/`. Uses **Cypress** for end-to-end testing with Docker integration.

```bash
# Build test Docker image
mvn clean package -Pui-tests-docker-build

# Run UI tests against AEM environment
mvn verify -Pui-tests-docker-execution \
  -DAEM_AUTHOR_URL=https://author-p...adobeaemcloud.com \
  -DAEM_AUTHOR_USERNAME=admin \
  -DAEM_AUTHOR_PASSWORD=admin
```

## Project Structure

### Top-Level Modules

- **`core/`** – OSGi bundles with Java backend logic
  - Sling Models for component data binding (e.g., `BylineImpl`, `ImageListImpl`)
  - Located at: `com.adobe.aem.guides.wknd.core.*`

- **`ui.apps/`** – AEM content package (HTL templates, dialogs, component configs)
  - Component definitions: `jcr_root/apps/wknd/components/`
  - HTL templates: `*.html` files (e.g., `byline.html`)
  - Edit dialogs: `_cq_dialog.xml` files (AEM component authoring UI)
  - Clientlibs: `jcr_root/apps/wknd/clientlibs/`

- **`ui.frontend/`** – Node.js frontend build (TypeScript, SCSS → bundled CSS/JS)
  - Webpack config: `webpack.common.js`, `webpack.dev.js`, `webpack.prod.js`
  - Source: `src/main/webpack/` (components, site, base, resources, static)
  - Client library generator config: `clientlib.config.js`

- **`ui.content/`** – Authored content (pages, DAM assets, experience fragments)
  - Intended for development-environment-specific content

- **`ui.content.sample/`** – Reference content for demo/training
  - Includes pre-built WKND reference site with sample pages and images
  - Note: deploys on every build; modify `filter.xml` to change behavior if needed

- **`ui.apps.structure/`** – Minimal AEM folder structure (for filters, component paths)

- **`ui.config/`** – OSGi configuration files (.yaml format)

- **`all/`** – Aggregator POM that bundles all modules into a single deployable package

- **`dispatcher/`** – Apache Dispatcher caching and rewrite rules
  - Virtual host configs, cache rules, filters, rewrites
  - Immutable files (enforced best practices) + customizable overlays

- **`config/`** – AEM as a Cloud Service configuration
  - CDN traffic filter rules, WAF rules (deployed via Cloud Manager config pipeline)

## Architecture & Component Design

### Sling Model Pattern

All components use **Sling Models** for server-side rendering:

1. **Interface** (e.g., `Byline.java`): Defines public API
   - Getters for component properties
   - Business logic (e.g., `isEmpty()` to validate required fields)

2. **Implementation** (e.g., `BylineImpl.java`): Annotated with `@Model`
   - Adapts `SlingHttpServletRequest` (passed from HTL)
   - `@ValueMapValue` injects authored properties
   - `@OSGiService` injects AEM services
   - `@PostConstruct` initializes complex objects
   - Implements interface to expose methods to HTL

3. **HTL Template** (e.g., `byline.html`): Server-rendered markup
   - Uses `use` directive: `<sly data-sly-use.byline="com.adobe.aem.guides.wknd.core.models.Byline">`
   - Accesses model methods: `${byline.name}`, `${byline.occupations}`

### Typical Component Structure

```
jcr_root/apps/wknd/components/byline/
├── byline.html              # HTL template (renders with Sling Model)
├── .content.xml             # Component metadata (title, group, icon)
├── _cq_dialog.xml           # Edit dialog (property fields for authors)
├── _cq_design_dialog.xml    # Design dialog (site-wide component settings)
├── _cq_editConfig.xml       # In-place edit config
└── new/                      # Template for newly created instances
    └── .content.xml

Frontend assets (optional):
ui.frontend/src/main/webpack/components/byline/
├── byline.ts                # TypeScript logic (globbed into site.js)
└── byline.scss              # SCSS styles (globbed into site.css)
```

### Core Dependencies

- **AEM SDK API** (`2025.6.21193.20250609T124356Z`): Foundation for AEM as a Cloud Service
- **AEM Core Components** (`2.30.2`): Base components (Image, Text, etc.) that custom components extend
- **Sling Models** (via SDK): MVC-like model binding
- **JUnit 5** + **Mockito**: Testing framework
- **Webpack 5**, **TypeScript**, **SCSS**: Frontend tooling

## Key Files & Configuration

### Maven

- **`pom.xml`** (root): Reactor POM defining all modules and shared properties
  - Properties: AEM versions, Maven plugins, Java version
  - Profiles: `autoInstallBundle`, `autoInstallPackage`, `autoInstallPackagePublish`, `release`, `adobe-public`
  - Maven enforcer ensures Java 21 and Maven 3.9.4+

### Frontend

- **`ui.frontend/webpack.common.js`**: Shared webpack config
  - Entry: `src/main/webpack/site/main.ts`
  - Output: `dist/clientlib-site/js/` and `dist/clientlib-dependencies/js/`
  - Loaders: ts-loader, sass-loader, postcss-loader (autoprefixer), glob-import-loader
  - Plugins: MiniCssExtractPlugin, CleanWebpackPlugin, ESLintPlugin

- **`ui.frontend/clientlib.config.js`**: AEM client library definitions
  - Two libraries: `clientlib-site` (main CSS/JS) and `clientlib-dependencies` (third-party)
  - Destinations: `ui.apps/src/main/content/jcr_root/apps/wknd/clientlibs/`

### CI/CD

- **`.github/workflows/maven.yml`**: GitHub Actions CI
  - Triggers on PR
  - Tests against Java 21 with both default and `classic` profiles
  - Runs `mvn clean install`

- **`.github/workflows/maven-release.yml`**: Release workflow
  - Maven Release Plugin for version bumping and tagging

## Special Notes

### Sample Content

The `ui.content.sample` module contains pre-built reference content. By default, it **overwrites** authored content on each build. To preserve authored content, modify `ui.content.sample/src/main/content/META-INF/vault/filter.xml`:

```xml
<!-- Change from: -->
<filter root="/content/wknd" />

<!-- To: -->
<filter root="/content/wknd" mode="merge"/>
```

### Adobe Stock Images

Images in the WKND reference site are from Adobe Stock. Usage is governed by the Demo Asset Additional Terms at `adobe.com/legal/terms.html`.

### HOWTO: Figma to AEM Component Generation (Experimental)

The project includes infrastructure for AI-assisted AEM component generation from Figma designs via MCP:

1. **Figma MCP**: `figma` server (HTTP, OAuth) for design introspection
2. **AEM MCP**: `aem-content` server (cloud-hosted, OAuth PKCE) for AEM Cloud Service
3. **Adobe AEM Skill** (beta): Guides component creation from Figma frames

See `HOWTO.md` for setup details.

## Useful Commands Reference

```bash
# Full workflow: build everything and deploy
mvn clean install -PautoInstallSinglePackage

# Iterate on frontend (watch mode)
cd ui.frontend && npm run watch

# Iterate on backend (test-driven)
mvn clean verify -Dtest=BylineImplTest

# Check component compilation
mvn clean install -pl core

# Deploy only to publish environment
mvn clean install -PautoInstallPackagePublish

# Integration tests (requires running AEM)
mvn clean verify -Plocal -pl it.tests

# View component in AEM Author
# After deployment, visit: http://localhost:4502/
```

## Live Demo

A hosted instance of the WKND site is available at `https://www.wknd.site/`

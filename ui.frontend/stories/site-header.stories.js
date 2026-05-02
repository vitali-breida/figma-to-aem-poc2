import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Site Header',
};

export const Default = () => new SiteHeader({
  logoImage: 'https://upload.wikimedia.org/wikipedia/commons/a/a9/Danone_logo_2017.svg',
  logoLink: '/content/wknd/home',
  logoAlt: 'Danone',
  navItems: [
    { label: 'EQUIPOS', link: '/content/wknd/equipos', highlighted: false, hasDropdown: false },
    { label: 'MARCAS', link: '/content/wknd/marcas', highlighted: true, hasDropdown: false },
    { label: 'SOBRE NOSOTROS', link: '/content/wknd/sobre-nosotros', highlighted: false, hasDropdown: true },
    { label: 'CUENTOS', link: '/content/wknd/cuentos', highlighted: true, hasDropdown: false },
  ],
  ctaLabel: 'TRABAJOS',
  ctaLink: '/content/wknd/jobs',
  languageLabel: 'EN',
  showNotifications: true,
  notificationsLink: '/content/wknd/notifications',
}).markup;

export const NoNotifications = () => new SiteHeader({
  logoImage: 'https://upload.wikimedia.org/wikipedia/commons/a/a9/Danone_logo_2017.svg',
  logoLink: '/content/wknd/home',
  logoAlt: 'Danone',
  navItems: [
    { label: 'EQUIPOS', link: '/content/wknd/equipos', highlighted: false, hasDropdown: false },
    { label: 'MARCAS', link: '/content/wknd/marcas', highlighted: true, hasDropdown: false },
  ],
  ctaLabel: 'TRABAJOS',
  ctaLink: '/content/wknd/jobs',
  languageLabel: 'EN',
  showNotifications: false,
  notificationsLink: '',
}).markup;

export const Empty = () => new SiteHeader({
  logoImage: '',
  logoLink: '',
  logoAlt: '',
  navItems: [],
  ctaLabel: '',
  ctaLink: '',
  languageLabel: '',
  showNotifications: false,
  notificationsLink: '',
}).markup;

class SiteHeader {
  constructor({ logoImage, logoLink, logoAlt, navItems, ctaLabel, ctaLink, languageLabel, showNotifications, notificationsLink }) {
    this.logoImage = logoImage;
    this.logoLink = logoLink;
    this.logoAlt = logoAlt;
    this.navItems = navItems;
    this.ctaLabel = ctaLabel;
    this.ctaLink = ctaLink;
    this.languageLabel = languageLabel;
    this.showNotifications = showNotifications;
    this.notificationsLink = notificationsLink;
  }

  get markup() {
    const hasContent = this.logoImage || this.navItems.length > 0 || this.ctaLabel;
    if (!hasContent) {
      return `<div class="cmp-site-header cmp-site-header--empty">Please configure the Site Header component</div>`;
    }

    const logoMarkup = this.logoImage ? `
      <div class="cmp-site-header__logo">
        ${this.logoLink
          ? `<a href="${this.logoLink}" class="cmp-site-header__logo-link" aria-label="${this.logoAlt}">
               <img src="${this.logoImage}" alt="${this.logoAlt}" class="cmp-site-header__logo-img" loading="lazy"/>
             </a>`
          : `<img src="${this.logoImage}" alt="${this.logoAlt}" class="cmp-site-header__logo-img" loading="lazy"/>`
        }
      </div>` : '';

    const navMarkup = this.navItems.length > 0 ? `
      <ul class="cmp-site-header__nav" role="list">
        ${this.navItems.map(item => `
          <li class="cmp-site-header__nav-item">
            <a href="${item.link}"
               class="cmp-site-header__nav-link${item.highlighted ? ' cmp-site-header__nav-link--highlighted' : ''}">
              ${item.label}
              ${item.hasDropdown ? `<span class="cmp-site-header__dropdown-icon" aria-hidden="true"></span>` : ''}
            </a>
          </li>`).join('')}
      </ul>` : '';

    const ctaMarkup = this.ctaLabel ? `
      <a href="${this.ctaLink}" class="cmp-site-header__cta">${this.ctaLabel}</a>` : '';

    const dividerMarkup = `<span class="cmp-site-header__divider" aria-hidden="true"></span>`;

    const languageMarkup = this.languageLabel ? `
      <div class="cmp-site-header__language">
        <span class="cmp-site-header__language-label">${this.languageLabel}</span>
        <span class="cmp-site-header__dropdown-icon" aria-hidden="true"></span>
      </div>` : '';

    const bellMarkup = this.showNotifications ? `
      <a href="${this.notificationsLink}" class="cmp-site-header__notifications" aria-label="Notifications">
        <span class="cmp-site-header__bell-icon" aria-hidden="true"></span>
      </a>` : '';

    return `
      <nav class="cmp-site-header" data-cmp-is="site-header" aria-label="Main Navigation">
        ${logoMarkup}
        ${navMarkup}
        ${ctaMarkup}
        ${dividerMarkup}
        ${languageMarkup}
        ${bellMarkup}
      </nav>
    `;
  }
}

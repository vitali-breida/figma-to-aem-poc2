import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Footer Bar',
};

export const Default = () => new FooterBar({
  title: 'Site Footer',
  navLinks: [
    { linkTitle: 'Teams', linkUrl: '/content/wknd/teams' },
    { linkTitle: 'Brands', linkUrl: '/content/wknd/brands' },
    { linkTitle: 'Our Promise', linkUrl: '/content/wknd/our-promise' },
    { linkTitle: 'Life at Danone', linkUrl: '/content/wknd/life-at-danone' },
    { linkTitle: 'Stories', linkUrl: '/content/wknd/stories' },
  ],
  socialIcons: [
    { iconPath: 'https://upload.wikimedia.org/wikipedia/commons/5/51/Facebook_f_logo_%282019%29.svg', linkUrl: 'https://www.facebook.com/' },
    { iconPath: 'https://upload.wikimedia.org/wikipedia/commons/a/a5/Instagram_icon.png', linkUrl: 'https://www.instagram.com/' },
    { iconPath: 'https://upload.wikimedia.org/wikipedia/commons/c/ca/LinkedIn_logo_initials.png', linkUrl: 'https://www.linkedin.com/' },
  ],
  legalLinks: [
    { linkTitle: 'Cookies', linkUrl: '/content/wknd/legal/cookies' },
    { linkTitle: 'Privacy Policy', linkUrl: '/content/wknd/legal/privacy' },
    { linkTitle: 'Terms of Use', linkUrl: '/content/wknd/legal/terms' },
  ],
}).markup;

export const NoSocial = () => new FooterBar({
  title: 'Footer Without Social',
  navLinks: [
    { linkTitle: 'Teams', linkUrl: '/content/wknd/teams' },
    { linkTitle: 'Brands', linkUrl: '/content/wknd/brands' },
  ],
  socialIcons: [],
  legalLinks: [
    { linkTitle: 'Cookies', linkUrl: '/content/wknd/legal/cookies' },
  ],
}).markup;

export const Empty = () => new FooterBar({
  title: '',
  navLinks: [],
  socialIcons: [],
  legalLinks: [],
}).markup;

class FooterBar {
  constructor({ title, navLinks, socialIcons, legalLinks }) {
    this.title = title;
    this.navLinks = navLinks;
    this.socialIcons = socialIcons;
    this.legalLinks = legalLinks;
  }

  get markup() {
    const navItems = this.navLinks.map(link =>
      `<li class="cmp-footer-bar__nav-item">
        <a href="${link.linkUrl}" class="cmp-footer-bar__nav-link">${link.linkTitle}</a>
      </li>`
    ).join('');

    const socialItems = this.socialIcons.map(icon =>
      `<a href="${icon.linkUrl}" class="cmp-footer-bar__social-link">
        <img src="${icon.iconPath}" alt="" class="cmp-footer-bar__social-icon"/>
      </a>`
    ).join('');

    const legalItems = this.legalLinks.map(link =>
      `<a href="${link.linkUrl}" class="cmp-footer-bar__legal-link">${link.linkTitle}</a>`
    ).join('');

    return `
      <div class="cmp-footer-bar">
        <nav class="cmp-footer-bar__nav" aria-label="${this.title}">
          <ul class="cmp-footer-bar__nav-list">
            ${navItems}
          </ul>
        </nav>
        <div class="cmp-footer-bar__social">
          ${socialItems}
        </div>
        <div class="cmp-footer-bar__legal">
          ${legalItems}
        </div>
      </div>
    `;
  }
}

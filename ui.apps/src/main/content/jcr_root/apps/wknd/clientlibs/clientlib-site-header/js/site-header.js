(function() {
    'use strict';

    window.WKND = window.WKND || {};
    window.WKND.Components = window.WKND.Components || {};

    window.WKND.Components.SiteHeader = {
        selectors: {
            self: '[data-cmp-is="site-header"]',
            nav: '.cmp-site-header__nav',
            navLink: '.cmp-site-header__nav-link',
            language: '.cmp-site-header__language'
        },

        init: function(element) {
            if (!element || element.dataset.cmpInitialized) return;
            element.dataset.cmpInitialized = 'true';
            this.setupDropdowns(element);
        },

        setupDropdowns: function(element) {
            var navLinks = element.querySelectorAll(this.selectors.navLink);
            navLinks.forEach(function(link) {
                var icon = link.querySelector('.cmp-site-header__dropdown-icon');
                if (!icon) return;

                link.setAttribute('aria-expanded', 'false');
                link.addEventListener('click', function(event) {
                    event.preventDefault();
                    var expanded = link.getAttribute('aria-expanded') === 'true';
                    link.setAttribute('aria-expanded', String(!expanded));
                });

                link.addEventListener('keydown', function(event) {
                    if (event.key === 'Enter' || event.key === ' ') {
                        event.preventDefault();
                        link.click();
                    }
                    if (event.key === 'Escape') {
                        link.setAttribute('aria-expanded', 'false');
                    }
                });
            });

            var languageSelector = element.querySelector(this.selectors.language);
            if (languageSelector) {
                languageSelector.setAttribute('role', 'button');
                languageSelector.setAttribute('tabindex', '0');
                languageSelector.setAttribute('aria-expanded', 'false');

                languageSelector.addEventListener('click', function() {
                    var expanded = languageSelector.getAttribute('aria-expanded') === 'true';
                    languageSelector.setAttribute('aria-expanded', String(!expanded));
                });

                languageSelector.addEventListener('keydown', function(event) {
                    if (event.key === 'Enter' || event.key === ' ') {
                        event.preventDefault();
                        languageSelector.click();
                    }
                });
            }
        }
    };

    document.addEventListener('DOMContentLoaded', function() {
        document.querySelectorAll(window.WKND.Components.SiteHeader.selectors.self).forEach(function(element) {
            window.WKND.Components.SiteHeader.init(element);
        });
    });
})();

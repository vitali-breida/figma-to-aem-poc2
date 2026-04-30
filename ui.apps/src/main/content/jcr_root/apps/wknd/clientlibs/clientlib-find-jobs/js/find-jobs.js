(function () {
    'use strict';

    window.WKND = window.WKND || {};
    window.WKND.Components = window.WKND.Components || {};

    window.WKND.Components.FindJobs = {
        selectors: {
            self: '[data-cmp-is="find-jobs"]',
            form: '.cmp-find-jobs__form',
            input: '.cmp-find-jobs__input',
            selects: '.cmp-find-jobs__select',
            button: '.cmp-find-jobs__button'
        },

        init: function (element) {
            if (!element || element.dataset.cmpInitialized) return;
            element.dataset.cmpInitialized = 'true';

            var form = element.querySelector(this.selectors.form);
            if (form) {
                form.addEventListener('submit', this.handleSubmit.bind(this, element));
            }
        },

        handleSubmit: function (element, event) {
            var params = this.collectParams(element);
            if (!params.hasFilters) {
                return;
            }
        },

        collectParams: function (element) {
            var input = element.querySelector(this.selectors.input);
            var selects = element.querySelectorAll(this.selectors.selects);
            var hasFilters = false;

            if (input && input.value.trim()) {
                hasFilters = true;
            }

            selects.forEach(function (select) {
                if (select.value) {
                    hasFilters = true;
                }
            });

            return { hasFilters: hasFilters };
        }
    };

    document.addEventListener('DOMContentLoaded', function () {
        var components = document.querySelectorAll(
            window.WKND.Components.FindJobs.selectors.self
        );
        components.forEach(function (element) {
            window.WKND.Components.FindJobs.init(element);
        });
    });
})();

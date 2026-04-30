import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Find Jobs',
};

export const Default = () => new FindJobs({
  searchPlaceholder: 'Find job',
  buttonLabel: 'FIND JOBS',
  countriesLabel: 'Countries',
  countriesItems: [
    { text: 'France', value: 'france' },
    { text: 'Spain', value: 'spain' },
    { text: 'Germany', value: 'germany' },
    { text: 'Netherlands', value: 'netherlands' },
  ],
  teamsLabel: 'Teams',
  teamsItems: [
    { text: 'Marketing', value: 'marketing' },
    { text: 'Finance', value: 'finance' },
    { text: 'Engineering', value: 'engineering' },
    { text: 'HR', value: 'hr' },
  ],
  expertisesLabel: 'Expertises',
  expertisesItems: [
    { text: 'Digital', value: 'digital' },
    { text: 'Data & Analytics', value: 'data-analytics' },
    { text: 'Project Management', value: 'project-management' },
  ],
}).markup;

export const NoOptions = () => new FindJobs({
  searchPlaceholder: 'Find job',
  buttonLabel: 'FIND JOBS',
  countriesLabel: 'Countries',
  countriesItems: [],
  teamsLabel: 'Teams',
  teamsItems: [],
  expertisesLabel: 'Expertises',
  expertisesItems: [],
}).markup;

export const CustomLabels = () => new FindJobs({
  searchPlaceholder: 'Search roles...',
  buttonLabel: 'SEARCH',
  countriesLabel: 'Location',
  countriesItems: [
    { text: 'Remote', value: 'remote' },
    { text: 'On-site', value: 'on-site' },
  ],
  teamsLabel: 'Department',
  teamsItems: [
    { text: 'Sales', value: 'sales' },
    { text: 'Operations', value: 'operations' },
  ],
  expertisesLabel: 'Level',
  expertisesItems: [
    { text: 'Junior', value: 'junior' },
    { text: 'Senior', value: 'senior' },
    { text: 'Lead', value: 'lead' },
  ],
}).markup;

class FindJobs {
  constructor({ searchPlaceholder, buttonLabel, countriesLabel, countriesItems, teamsLabel, teamsItems, expertisesLabel, expertisesItems }) {
    this.searchPlaceholder = searchPlaceholder;
    this.buttonLabel = buttonLabel;
    this.countriesLabel = countriesLabel;
    this.countriesItems = countriesItems;
    this.teamsLabel = teamsLabel;
    this.teamsItems = teamsItems;
    this.expertisesLabel = expertisesLabel;
    this.expertisesItems = expertisesItems;
  }

  buildOptions(items) {
    return items.map(opt => `<option value="${opt.value}">${opt.text}</option>`).join('');
  }

  buildDropdown(label, name, items) {
    if (!items || items.length === 0) return '';
    return `
      <div class="cmp-find-jobs__field cmp-find-jobs__field--dropdown">
        <select class="cmp-find-jobs__select" name="${name}" aria-label="${label}">
          <option value="">${label}</option>
          ${this.buildOptions(items)}
        </select>
        <span class="cmp-find-jobs__dropdown-icon" aria-hidden="true"></span>
      </div>`;
  }

  get markup() {
    return `
      <section class="cmp-find-jobs" data-cmp-is="find-jobs">
        <form class="cmp-find-jobs__form" method="get" novalidate>
          <div class="cmp-find-jobs__search-row">
            <div class="cmp-find-jobs__field cmp-find-jobs__field--search">
              <input
                type="text"
                class="cmp-find-jobs__input"
                name="q"
                placeholder="${this.searchPlaceholder}"
                aria-label="${this.searchPlaceholder}"/>
              <span class="cmp-find-jobs__search-icon" aria-hidden="true"></span>
            </div>
            ${this.buildDropdown(this.countriesLabel, 'country', this.countriesItems)}
            ${this.buildDropdown(this.teamsLabel, 'team', this.teamsItems)}
            ${this.buildDropdown(this.expertisesLabel, 'expertise', this.expertisesItems)}
          </div>
          <div class="cmp-find-jobs__actions">
            <button type="submit" class="cmp-find-jobs__button">
              ${this.buttonLabel}
            </button>
          </div>
        </form>
      </section>
    `;
  }
}

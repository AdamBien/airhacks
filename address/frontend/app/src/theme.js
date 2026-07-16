// dark / light switch. no stored choice → follow the system scheme (no
// data-theme attribute); after the first click the choice is explicit and
// persisted. tokens.css maps data-theme to the palettes.

const KEY = 'theme';
const root = document.documentElement;
const lightScheme = matchMedia('(prefers-color-scheme: light)');
const button = document.querySelector('.theme-toggle');

const effective = () =>
  localStorage.getItem(KEY) ?? (lightScheme.matches ? 'light' : 'dark');

const apply = () => {
  if (localStorage.getItem(KEY)) {
    root.dataset.theme = effective();
  } else {
    delete root.dataset.theme;
  }
  const target = effective() === 'dark' ? 'light' : 'dark';
  button.textContent = `${target} mode`;
};

button.addEventListener('click', () => {
  localStorage.setItem(KEY, effective() === 'dark' ? 'light' : 'dark');
  apply();
});
lightScheme.addEventListener('change', apply);
apply();

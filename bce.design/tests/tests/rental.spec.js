// @ts-check
// System tests of the `rental` business component, derived from
// app/src/rental/package-info.md: one describe per requirement group Rn,
// one test per statement Rn.m with the id leading the title.
const { test, expect } = require('@playwright/test');

const customer = { name: 'Duke', email: 'duke@airhacks.live' };

/**
 * Fills the start form through the UI and submits it.
 * @param {import('@playwright/test').Page} page
 * @param {{type?: string, days?: string, name?: string, email?: string}} request
 */
const startRental = async (page, { type = 'bike', days = '1', name = customer.name, email = customer.email }) => {
  await page.getByLabel('type').selectOption(type);
  await page.getByLabel('days').fill(days);
  await page.getByLabel('name').fill(name);
  await page.getByLabel('email').fill(email);
  await page.getByRole('button', { name: 'start rental' }).click();
};

/**
 * @param {import('@playwright/test').Page} page
 * @param {string[]} types one rental per entry, started through the UI
 */
const rentAll = async (page, types) => {
  for (const type of types) {
    await startRental(page, { type });
  }
};

const availability = (page, text) => page.getByText(text, { exact: true });
const rentals = page => page.getByRole('list', { name: 'my rentals' }).getByRole('listitem');
const openRentals = page => page.getByRole('link', { name: 'my rentals' }).click();

test.describe('R1: Show availability', () => {
  const cases = [
    { req: 'R1.1', rent: [], shows: ['bike: 5 available', 'e-bike: 3 available'] },
    { req: 'R1.2', rent: ['bike'], shows: ['bike: 4 available', 'e-bike: 3 available'] },
    { req: 'R1.3', rent: ['e-bike', 'e-bike', 'e-bike'], shows: ['bike: 5 available', 'e-bike: sold out'] },
  ];
  for (const c of cases) {
    test(`${c.req} — availability after renting ${c.rent.length} bikes`, async ({ page }) => {
      await page.goto('/');
      await rentAll(page, c.rent);
      for (const text of c.shows) {
        await expect(availability(page, text)).toBeVisible();
      }
    });
  }
});

test.describe('R2: Quote a price', () => {
  const cases = [
    { req: 'R2.1', type: 'bike', days: '1', total: '€10' },
    { req: 'R2.1', type: 'e-bike', days: '1', total: '€25' },
    { req: 'R2.2', type: 'e-bike', days: '3', total: '€75' },
  ];
  for (const c of cases) {
    test(`${c.req} — quote for ${c.type}, ${c.days} days`, async ({ page }) => {
      await page.goto('/');
      await page.getByLabel('type').selectOption(c.type);
      await page.getByLabel('days').fill(c.days);
      await expect(page.getByLabel('total price')).toHaveText(c.total);
      await expect(rentals(page)).toHaveCount(0);
    });
  }
});

test.describe('R3: Start a rental', () => {
  test('R3.1 — starts a rental at the quoted price and allocates a bike', async ({ page }) => {
    await page.goto('/');
    await startRental(page, { type: 'bike', days: '2' });
    await expect(availability(page, 'bike: 4 available')).toBeVisible();
    await openRentals(page);
    await expect(rentals(page)).toHaveCount(1);
    await expect(rentals(page).first()).toContainText('B1 · bike · 2 days · €20 · open');
  });

  test('R3.2 — rejects a start when the type is sold out and leaves the fleet unchanged', async ({ page }) => {
    await page.goto('/');
    await rentAll(page, ['e-bike', 'e-bike', 'e-bike']);
    await expect(availability(page, 'e-bike: sold out')).toBeVisible();
    await startRental(page, { type: 'e-bike' });
    await expect(page.getByRole('alert')).toHaveText('no e-bike available');
    await expect(availability(page, 'bike: 5 available')).toBeVisible();
    await openRentals(page);
    await expect(rentals(page)).toHaveCount(3);
  });

  const rejected = [
    { req: 'R3.3', request: { name: '' }, reason: 'missing name' },
    { req: 'R3.3', request: { email: '' }, reason: 'missing email' },
    { req: 'R3.4', request: { days: '0' }, reason: 'days below 1' },
    { req: 'R3.4', request: { days: '15' }, reason: 'days above 14' },
  ];
  for (const c of rejected) {
    test(`${c.req} — rejects a start with ${c.reason}`, async ({ page }) => {
      await page.goto('/');
      await startRental(page, c.request);
      await expect(availability(page, 'bike: 5 available')).toBeVisible();
      await openRentals(page);
      await expect(page.getByText('no rentals yet')).toBeVisible();
      await expect(rentals(page)).toHaveCount(0);
    });
  }
});

test.describe('R4: Return a bike', () => {
  test('R4.1 — returning closes the rental and frees the bike', async ({ page }) => {
    await page.goto('/');
    await startRental(page, { type: 'e-bike' });
    await expect(availability(page, 'e-bike: 2 available')).toBeVisible();
    await openRentals(page);
    await page.getByRole('button', { name: 'return E1' }).click();
    await expect(rentals(page).first()).toContainText('returned');
    await page.getByRole('link', { name: 'rent a bike' }).click();
    await expect(availability(page, 'e-bike: 3 available')).toBeVisible();
  });

  test('R4.2 — a returned rental cannot be returned again', async ({ page }) => {
    await page.goto('/');
    await startRental(page, { type: 'bike' });
    await openRentals(page);
    await page.getByRole('button', { name: 'return B1' }).click();
    await expect(rentals(page).first()).toContainText('returned');
    await expect(page.getByRole('button', { name: 'return B1' })).toHaveCount(0);
    await expect(rentals(page)).toHaveCount(1);
  });
});

test.describe('R5: List rentals', () => {
  test('R5.1 — lists every rental with bike, type, days, price and state, newest first', async ({ page }) => {
    await page.goto('/');
    await startRental(page, { type: 'bike', days: '1' });
    await startRental(page, { type: 'e-bike', days: '3' });
    await openRentals(page);
    await page.getByRole('button', { name: 'return B1' }).click();
    await expect(rentals(page)).toHaveCount(2);
    await expect(rentals(page).nth(0)).toContainText('E1 · e-bike · 3 days · €75 · open');
    await expect(rentals(page).nth(1)).toContainText('B1 · bike · 1 day · €10 · returned');
  });

  test('R5.2 — rentals and availability survive a reload', async ({ page }) => {
    await page.goto('/');
    await startRental(page, { type: 'e-bike', days: '2' });
    await openRentals(page);
    await expect(rentals(page)).toHaveCount(1);
    await page.reload();
    await expect(rentals(page).first()).toContainText('E1 · e-bike · 2 days · €50 · open');
    await page.getByRole('link', { name: 'rent a bike' }).click();
    await expect(availability(page, 'e-bike: 2 available')).toBeVisible();
  });
});

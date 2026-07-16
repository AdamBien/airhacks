// @ts-check
const { test, expect } = require('@playwright/test');

const serviceUri = 'http://localhost:8080/contacts';

/** unique per-test values keep parallel browser projects from colliding on the shared service */
const unique = prefix => `${prefix}${Date.now()}${Math.floor(Math.random() * 10000)}`;

const createContact = async (request, contact) => {
  const response = await request.post(serviceUri, { data: contact });
  expect(response.status()).toBe(201);
  return response.json();
};

test.describe('R1: list contacts', () => {

  test('R1.1 — the listing displays all contacts provided by the service', async ({ page, request }) => {
    const lastName = unique('listed');
    await createContact(request, { firstName: 'duke', lastName, email: '', phone: '', type: 'private' });
    await page.goto('localhost:3000');
    await expect(page.getByRole('cell', { name: lastName })).toBeVisible();
  });

  test('R1.2 — a row displays last name, first name, email, phone, and type', async ({ page, request }) => {
    const lastName = unique('fields');
    const email = `${unique('mail')}@java.net`;
    await createContact(request, { firstName: 'tux', lastName, email, phone: '+1 555 0100', type: 'business' });
    await page.goto('localhost:3000');
    const row = page.locator('tbody tr').filter({ hasText: lastName });
    await expect(row).toContainText(lastName);
    await expect(row).toContainText('tux');
    await expect(row).toContainText(email);
    await expect(row).toContainText('+1 555 0100');
    await expect(row).toContainText('business');
  });
});

const searchCases = [
  { req: 'R2.1', title: 'an entered term filters the listing, ignoring case', clear: false },
  { req: 'R2.2', title: 'a cleared term shows all contacts', clear: true },
];

test.describe('R2: search', () => {
  for (const c of searchCases) {
    test(`${c.req} — ${c.title}`, async ({ page, request }) => {
      const needle = unique('needle');
      const hay = unique('hay');
      await createContact(request, { firstName: '', lastName: needle, email: '', phone: '', type: 'private' });
      await createContact(request, { firstName: '', lastName: hay, email: '', phone: '', type: 'private' });
      await page.goto('localhost:3000');
      await expect(page.getByRole('cell', { name: hay })).toBeVisible();
      await page.getByLabel('search').fill(needle.toUpperCase());
      await expect(page.getByRole('cell', { name: needle })).toBeVisible();
      await expect(page.getByRole('cell', { name: hay })).toBeHidden();
      if (c.clear) {
        await page.getByLabel('search').fill('');
        await expect(page.getByRole('cell', { name: needle })).toBeVisible();
        await expect(page.getByRole('cell', { name: hay })).toBeVisible();
      }
    });
  }
});

const sortCases = [
  { req: 'R3.1', title: 'selecting a field orders the listing by it', clicks: 1, expectedFirst: 'alpha' },
  { req: 'R3.2', title: 'selecting the same field again reverses the order', clicks: 2, expectedFirst: 'omega' },
];

test.describe('R3: sort', () => {
  for (const c of sortCases) {
    test(`${c.req} — ${c.title}`, async ({ page, request }) => {
      const stamp = unique('sort');
      // default order is by last name — 'omega' first; sorting by first name flips it
      await createContact(request, { firstName: 'alpha', lastName: `z${stamp}`, email: '', phone: '', type: 'private' });
      await createContact(request, { firstName: 'omega', lastName: `a${stamp}`, email: '', phone: '', type: 'private' });
      await page.goto('localhost:3000');
      const search = page.getByLabel('search');
      await search.fill(stamp);
      const rows = page.locator('tbody tr');
      await expect(rows).toHaveCount(2);
      // a focused search field swallows the next click in WebKit — blur first
      await search.blur();
      for (let i = 0; i < c.clicks; i++) {
        await page.getByRole('button', { name: 'first name' }).click();
      }
      await expect(rows.nth(0)).toContainText(c.expectedFirst);
    });
  }
});

const addCases = [
  { req: 'R4.1', title: 'a contact with a last name is saved and listed', lastName: true, email: '', saves: true },
  { req: 'R4.2', title: 'a missing last name blocks the submission', lastName: false, email: '', saves: false, invalid: 'Last name' },
  { req: 'R4.3', title: 'a malformed email blocks the submission', lastName: true, email: 'not-an-email', saves: false, invalid: 'Email' },
];

test.describe('R4: add a contact', () => {
  for (const c of addCases) {
    test(`${c.req} — ${c.title}`, async ({ page }) => {
      const lastName = unique('added');
      await page.goto('localhost:3000');
      await page.getByRole('link', { name: 'add contact' }).click();
      await page.getByLabel('First name').pressSequentially('juggy', { delay: 25 });
      if (c.lastName) {
        await page.getByLabel('Last name').pressSequentially(lastName, { delay: 25 });
      }
      if (c.email) {
        await page.getByLabel('Email').pressSequentially(c.email, { delay: 25 });
      }
      await page.getByRole('button', { name: 'new contact' }).click();
      if (c.saves) {
        await page.getByRole('link', { name: 'list' }).click();
        await expect(page.getByRole('cell', { name: lastName })).toBeVisible();
      } else {
        const invalidInput = page.getByLabel(c.invalid);
        expect(await invalidInput.evaluate(input => input.checkValidity())).toBe(false);
        await expect(page.getByRole('button', { name: 'new contact' })).toBeVisible();
      }
    });
  }
});

test.describe('R5: edit a contact', () => {

  test('R5.1 — the form is prefilled with the stored fields', async ({ page, request }) => {
    const lastName = unique('stored');
    await createContact(request, { firstName: 'lambda', lastName, email: 'lambda@jdk.dev', phone: '+1 555 0123', type: 'private' });
    await page.goto('localhost:3000');
    await page.locator('tbody tr').filter({ hasText: lastName }).getByRole('link', { name: 'edit' }).click();
    await expect(page.getByLabel('Last name')).toHaveValue(lastName);
    await expect(page.getByLabel('First name')).toHaveValue('lambda');
    await expect(page.getByLabel('Email')).toHaveValue('lambda@jdk.dev');
    await expect(page.getByLabel('Phone')).toHaveValue('+1 555 0123');
  });

  test('R5.2 — a valid edit reaches the service and the listing', async ({ page, request }) => {
    const before = unique('before');
    const created = await createContact(request, { firstName: 'streams', lastName: before, email: '', phone: '', type: 'private' });
    await page.goto('localhost:3000');
    await page.locator('tbody tr').filter({ hasText: before }).getByRole('link', { name: 'edit' }).click();
    const lastNameInput = page.getByLabel('Last name');
    await expect(lastNameInput).toHaveValue(before);
    const after = unique('after');
    await lastNameInput.press('ControlOrMeta+a');
    await lastNameInput.pressSequentially(after, { delay: 25 });
    await page.getByRole('button', { name: 'save contact' }).click();
    await page.getByRole('link', { name: 'list' }).click();
    await expect(page.getByRole('cell', { name: after })).toBeVisible();
    const stored = await (await request.get(`${serviceUri}/${created.id}`)).json();
    expect(stored.lastName).toBe(after);
  });
});

test.describe('R7: contact type', () => {

  test('R7.1 — the form offers business and private, preselected to private', async ({ page }) => {
    await page.goto('localhost:3000');
    await page.getByRole('link', { name: 'add contact' }).click();
    const type = page.getByLabel('Type');
    await expect(type).toHaveValue('private');
    await expect(type.locator('option')).toHaveText(['private', 'business']);
  });

  test('R7.2 — editing preselects the stored type', async ({ page, request }) => {
    const lastName = unique('typed');
    await createContact(request, { firstName: '', lastName, email: '', phone: '', type: 'business' });
    await page.goto('localhost:3000');
    await page.locator('tbody tr').filter({ hasText: lastName }).getByRole('link', { name: 'edit' }).click();
    await expect(page.getByLabel('Last name')).toHaveValue(lastName);
    await expect(page.getByLabel('Type')).toHaveValue('business');
  });

  test('R7.3 — the selected type reaches the contacts service', async ({ page, request }) => {
    const lastName = unique('sent');
    await page.goto('localhost:3000');
    await page.getByRole('link', { name: 'add contact' }).click();
    await page.getByLabel('Last name').pressSequentially(lastName, { delay: 25 });
    await page.getByLabel('Type').selectOption('business');
    await page.getByRole('button', { name: 'new contact' }).click();
    await page.getByRole('link', { name: 'list' }).click();
    await expect(page.getByRole('cell', { name: lastName })).toBeVisible();
    const all = await (await request.get(serviceUri)).json();
    const stored = all.find(contact => contact.lastName === lastName);
    expect(stored.type).toBe('business');
  });
});

test.describe('R6: delete a contact', () => {

  test('R6.1 — a deleted contact leaves the listing and the service', async ({ page, request }) => {
    const lastName = unique('gone');
    const created = await createContact(request, { firstName: '', lastName, email: '', phone: '', type: 'private' });
    await page.goto('localhost:3000');
    await page.locator('tbody tr').filter({ hasText: lastName }).getByRole('button', { name: 'delete' }).click();
    await expect(page.getByRole('cell', { name: lastName })).toBeHidden();
    expect((await request.get(`${serviceUri}/${created.id}`)).status()).toBe(404);
  });
});

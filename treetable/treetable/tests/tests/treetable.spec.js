// @ts-check
// EARS-parameterized tests for the treetable BC — one describe per requirement
// group, one test per statement id (app/src/treetable/package-info.md).
const { test, expect } = require('@playwright/test');

const BASE = 'localhost:3000';

/** @param {import('@playwright/test').Page} page @param {string} label */
const rowNamed = (page, label) => page.getByRole('row', { name: new RegExp(label) });

/** first value cell (owner) of the "treetable" sample row
 * @param {import('@playwright/test').Page} page */
const ownerCell = page => rowNamed(page, 'treetable').getByRole('gridcell').first();

test.describe('R1: show the tree', () => {

    test('R1.1 — every visible row renders as one table row with aligned columns', async ({ page }) => {
        await page.goto(BASE);
        await page.getByRole('button', { name: 'expand bce.design' }).click();
        const rows = page.getByRole('row');
        const count = await rows.count();
        expect(count).toBeGreaterThan(3);
        for (let i = 0; i < count; i++) {
            await expect(rows.nth(i).locator('th, td')).toHaveCount(4);
        }
    });

    test('R1.2 — hierarchy in the first column, indented by depth', async ({ page }) => {
        await page.goto(BASE);
        await page.getByRole('button', { name: 'expand bce.design' }).click();
        const indent = (/** @type {string} */ label) => rowNamed(page, label).getByRole('rowheader')
            .evaluate(cell => parseFloat(getComputedStyle(cell).paddingInlineStart));
        expect(await indent('routing')).toBeGreaterThan(await indent('bce.design'));
    });

    test('R1.3 — only rows with children carry an expand / collapse toggle', async ({ page }) => {
        await page.goto(BASE);
        await expect(rowNamed(page, 'bce.design').getByRole('button', { name: /expand|collapse/ })).toBeVisible();
        await page.getByRole('button', { name: 'expand bce.design' }).click();
        await expect(rowNamed(page, 'state management').getByRole('button', { name: /expand|collapse/ })).toHaveCount(0);
    });

    test('R1.4 — columns come from the tree data, in declared order', async ({ page }) => {
        await page.goto(BASE);
        await expect(page.getByRole('columnheader')).toHaveText(['name', 'owner', 'status', 'effort']);
    });

    test('R1.5 — with no saved expansion state, only root rows show', async ({ page }) => {
        await page.goto(BASE);
        await expect(page.getByRole('row')).toHaveCount(3); // header + 2 roots
        await expect(rowNamed(page, 'routing')).toHaveCount(0);
    });

    test('R1.6 — with no saved tree, the bundled sample tree is seeded', async ({ page }) => {
        await page.goto(BASE);
        await expect(rowNamed(page, 'bce.design')).toBeVisible();
        await expect(rowNamed(page, 'treetable')).toBeVisible();
    });

    test('R1.7 — depth and expansion state are exposed to assistive technology', async ({ page }) => {
        await page.goto(BASE);
        const root = rowNamed(page, 'bce.design');
        await expect(root).toHaveAttribute('aria-level', '1');
        await expect(root).toHaveAttribute('aria-expanded', 'false');
        await page.getByRole('button', { name: 'expand bce.design' }).click();
        await expect(root).toHaveAttribute('aria-expanded', 'true');
        await expect(rowNamed(page, 'routing')).toHaveAttribute('aria-level', '2');
    });
});

const toggleCases = [
    {
        req: 'R2.1 — expanding shows the immediate children only',
        toggles: ['expand bce.design'],
        visible: ['routing', 'state management'], hidden: ['URLPattern matching']
    },
    {
        req: 'R2.2 — collapsing hides the entire subtree',
        toggles: ['expand bce.design', 'expand routing', 'collapse bce.design'],
        visible: [], hidden: ['routing', 'URLPattern matching']
    },
    {
        req: 'R2.3 — re-expanding restores the descendants\' prior state',
        toggles: ['expand bce.design', 'expand routing', 'collapse bce.design', 'expand bce.design'],
        visible: ['routing', 'URLPattern matching'], hidden: []
    },
];

test.describe('R2: expand and collapse', () => {
    for (const c of toggleCases) {
        test(c.req, async ({ page }) => {
            await page.goto(BASE);
            for (const name of c.toggles) {
                await page.getByRole('button', { name }).click();
            }
            for (const label of c.visible) {
                await expect(rowNamed(page, label)).toBeVisible();
            }
            for (const label of c.hidden) {
                await expect(rowNamed(page, label)).toHaveCount(0);
            }
        });
    }

    test('R2.4 — reload restores the saved expansion state', async ({ page }) => {
        await page.goto(BASE);
        await page.getByRole('button', { name: 'expand bce.design' }).click();
        await expect(rowNamed(page, 'routing')).toBeVisible();
        await page.reload();
        await expect(rowNamed(page, 'routing')).toBeVisible();
    });
});

const editCases = [
    { req: 'R3.2 — committing (Enter) stores and displays the new value', key: 'Enter', expected: 'jakarta' },
    { req: 'R3.3 — cancelling (Escape) restores the unchanged value', key: 'Escape', expected: 'adam' },
];

test.describe('R3: edit a cell in place', () => {

    test('R3.1 — activating a value cell opens a text input with the current value', async ({ page }) => {
        await page.goto(BASE);
        await ownerCell(page).click();
        const input = page.getByRole('textbox');
        await expect(input).toBeVisible();
        await expect(input).toHaveValue('adam');
    });

    for (const c of editCases) {
        test(c.req, async ({ page }) => {
            await page.goto(BASE);
            await ownerCell(page).click();
            const input = page.getByRole('textbox');
            await input.press('ControlOrMeta+a');
            await input.pressSequentially('jakarta', { delay: 50 });
            await input.press(c.key);
            await expect(page.getByRole('textbox')).toHaveCount(0);
            await expect(ownerCell(page)).toHaveText(c.expected);
        });
    }

    test('R3.4 — reload shows all previously committed values', async ({ page }) => {
        await page.goto(BASE);
        await ownerCell(page).click();
        const input = page.getByRole('textbox');
        await input.press('ControlOrMeta+a');
        await input.pressSequentially('jakarta', { delay: 50 });
        await input.press('Enter');
        await page.reload();
        await expect(ownerCell(page)).toHaveText('jakarta');
    });

    // R3.5 retired: hierarchy cells are now editable via rename-node (R5)
});

const addChildCases = [
    { req: 'R4.2 — add-child appends a "new node" child row with empty cells', preToggles: ['expand bce.design'] },
    { req: 'R4.3 — adding to a collapsed row expands it, the new child is visible', preToggles: [] },
];

test.describe('R4: add a node', () => {

    test('R4.1 — the add-node action appends a "new node" root row with empty cells', async ({ page }) => {
        await page.goto(BASE);
        await page.getByRole('button', { name: 'add node' }).click();
        const added = rowNamed(page, 'new node');
        await expect(added).toBeVisible();
        await expect(added).toHaveAttribute('aria-level', '1');
        const cells = added.getByRole('gridcell');
        await expect(cells).toHaveText(['', '', '']);
    });

    for (const c of addChildCases) {
        test(c.req, async ({ page }) => {
            await page.goto(BASE);
            for (const name of c.preToggles) {
                await page.getByRole('button', { name }).click();
            }
            await page.getByRole('button', { name: 'add child to bce.design' }).click();
            const added = rowNamed(page, 'new node');
            await expect(added).toBeVisible();
            await expect(added).toHaveAttribute('aria-level', '2');
            await expect(added.getByRole('gridcell')).toHaveText(['', '', '']);
            await expect(rowNamed(page, 'bce.design')).toHaveAttribute('aria-expanded', 'true');
        });
    }

    test('R4.4 — reload still displays previously added rows', async ({ page }) => {
        await page.goto(BASE);
        await page.getByRole('button', { name: 'add node' }).click();
        await expect(rowNamed(page, 'new node')).toBeVisible();
        await page.reload();
        await expect(rowNamed(page, 'new node')).toBeVisible();
    });
});

/** the displayed name of the "treetable" sample row
 * @param {import('@playwright/test').Page} page */
const nameOf = page => rowNamed(page, 'treetable').getByText('treetable');

const renameCases = [
    { req: 'R5.2 — committing (Enter) stores and displays the new name', key: 'Enter', expected: 'renamed node' },
    { req: 'R5.3 — cancelling (Escape) restores the unchanged name', key: 'Escape', expected: 'treetable' },
];

test.describe('R5: rename a node', () => {

    test('R5.1 — activating a row name opens a text input with the current name', async ({ page }) => {
        await page.goto(BASE);
        await nameOf(page).click();
        const input = page.getByRole('textbox');
        await expect(input).toBeVisible();
        await expect(input).toHaveValue('treetable');
    });

    for (const c of renameCases) {
        test(c.req, async ({ page }) => {
            await page.goto(BASE);
            await nameOf(page).click();
            const input = page.getByRole('textbox');
            await input.press('ControlOrMeta+a');
            await input.pressSequentially('renamed node', { delay: 50 });
            await input.press(c.key);
            await expect(page.getByRole('textbox')).toHaveCount(0);
            await expect(rowNamed(page, c.expected)).toBeVisible();
        });
    }

    test('R5.4 — an empty commit keeps the previous name', async ({ page }) => {
        await page.goto(BASE);
        await nameOf(page).click();
        const input = page.getByRole('textbox');
        await input.press('ControlOrMeta+a');
        await input.press('Backspace');
        await input.press('Enter');
        await expect(page.getByRole('textbox')).toHaveCount(0);
        await expect(rowNamed(page, 'treetable')).toBeVisible();
    });

    test('R5.5 — reload shows all previously committed names', async ({ page }) => {
        await page.goto(BASE);
        await nameOf(page).click();
        const input = page.getByRole('textbox');
        await input.press('ControlOrMeta+a');
        await input.pressSequentially('renamed node', { delay: 50 });
        await input.press('Enter');
        await page.reload();
        await expect(rowNamed(page, 'renamed node')).toBeVisible();
    });
});

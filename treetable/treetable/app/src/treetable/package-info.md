# Treetable
> Render hierarchical rows as a table — expand and collapse subtrees, edit value cells in place.

## Boundary
- `show-tree` — render the persisted (or freshly seeded sample) tree as a table
- `toggle-row` — expand or collapse a row's subtree
- `edit-cell` — change a value cell's content in place

## Requirements

### R1: Show the tree
- R1.1 — The BC shall render each visible row as one table row, its attribute values aligned in columns shared by all depths. _(why: unbroken column alignment is what makes a TreeTable a table)_
- R1.2 — The BC shall render the hierarchy in the first column, indented by row depth.
- R1.3 — Where a row has child rows, the BC shall show an expand / collapse toggle on that row.
- R1.4 — The BC shall render the columns declared by the tree data, in declared order. _(why: the element is generic — columns are data, not code)_
- R1.5 — When the treetable is shown with no saved expansion state, the BC shall display only root rows.
- R1.6 — If no saved tree exists, then the BC shall seed the bundled sample tree.
- R1.7 — The BC shall expose each row's depth and expanded / collapsed state to assistive technology.

### R2: Expand and collapse
- R2.1 — When a collapsed row's toggle is activated, the BC shall show that row's immediate children.
- R2.2 — When an expanded row's toggle is activated, the BC shall hide that row's entire subtree.
- R2.3 — When a row is re-expanded, the BC shall restore its descendants' prior expansion state.
- R2.4 — When the treetable is reloaded, the BC shall restore the saved expansion state.

### R3: Edit a cell in place
- R3.1 — When a value cell is activated, the BC shall replace the displayed value with a text input holding the current value.
- R3.2 — When an edit is committed (Enter or focus leaving the input), the BC shall store the new value and display it.
- R3.3 — If an edit is cancelled (Escape), then the BC shall discard the input and restore the unchanged value.
- R3.4 — When the treetable is reloaded, the BC shall display all previously committed values.
- R3.5 — If a hierarchy cell is activated, then the BC shall not enter edit mode. _(why: the hierarchy column navigates the tree; its labels are not value cells)_

## Entities
- Tree, Row, Column

## Out of scope
- Full treegrid keyboard navigation (arrow-key traversal between rows and cells)
- Structural editing — adding, removing, or moving rows
- Typed cell inputs (numbers, dates, selects) — text only
- Editing the hierarchy column (row labels)
- Sorting, filtering, row selection

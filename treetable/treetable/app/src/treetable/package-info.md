# Treetable
> Render hierarchical rows as a table — expand and collapse subtrees, add and rename nodes, edit value cells in place.

## Boundary
- `show-tree` — render the persisted (or freshly seeded sample) tree as a table
- `toggle-row` — expand or collapse a row's subtree
- `edit-cell` — change a value cell's content in place
- `add-node` — append a new row, as a root or as a child of an existing row
- `rename-node` — change a row's name in place

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

<!-- R3.5 retired: hierarchy cells are now editable via rename-node (R5) -->

### R4: Add a node
- R4.1 — When the add-node action is activated, the BC shall append a new root row named "new node" with empty value cells.
- R4.2 — When a row's add-child action is activated, the BC shall append a new child row named "new node" with empty value cells under that row. _(why: creation stays a one-click append — the name is given afterwards by renaming in place)_
- R4.3 — While a row is collapsed, when a child is added to it, the BC shall expand the row so the new child is visible.
- R4.4 — When the treetable is reloaded, the BC shall still display previously added rows.

### R5: Rename a node
- R5.1 — When a row's name is activated, the BC shall replace the displayed name with a text input holding the current name.
- R5.2 — When a rename is committed (Enter or focus leaving the input), the BC shall store the new name and display it.
- R5.3 — If a rename is cancelled (Escape), then the BC shall restore the unchanged name.
- R5.4 — If an empty name is committed, then the BC shall keep the previous name. _(why: a nameless row cannot be found or renamed again)_
- R5.5 — When the treetable is reloaded, the BC shall display all previously committed names.

## Entities
- Tree, Row, Column

## Out of scope
- Full treegrid keyboard navigation (arrow-key traversal between rows and cells)
- Structural editing — removing or moving rows
- Typed cell inputs (numbers, dates, selects) — text only
- Sorting, filtering, row selection

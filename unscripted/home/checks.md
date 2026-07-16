# Checks — home

- [R1.1] / at 1280px: snapshot shows headings "Overlays", "Forms", "Motion", "Structure" with 4, 3, 4, 3 catalog entries respectively
- [R1.2] /: every catalog entry link navigates to an existing page (14 links, no 404)
- [R1.3] /: every catalog entry shows the feature name and a sentence beginning "Replaces"
- [R1.4] /: every catalog entry shows a "Baseline:" badge reading Widely, Newly, or Limited
- [R2.1] every page: requests `/css/style.css` (status 200) and `getComputedStyle(document.body)` resolves `--accent` to a non-empty value

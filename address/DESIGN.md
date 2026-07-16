---
version: alpha
name: MTV Germany
description: Dark, image-led entertainment design system extracted from mtv.de — black canvas, electric MTV yellow, and the Gravity typeface.
colors:
  primary: "#F7F908"
  secondary: "#00AACD"
  neutral: "#000000"
  surface: "#121212"
  on-surface: "#FFFFFF"
  on-surface-variant: "#F3F3F3"
  on-surface-muted: "#A1A1A1"
typography:
  headline-lg:
    fontFamily: Gravity
    fontSize: 36px
    fontWeight: "700"
    lineHeight: 1.12
  title-md:
    fontFamily: Gravity
    fontSize: 16px
    fontWeight: "700"
    lineHeight: 1.12
  body-md:
    fontFamily: Gravity
    fontSize: 16px
    fontWeight: "350"
    lineHeight: 1.5
  body-sm:
    fontFamily: Gravity
    fontSize: 14px
    fontWeight: "350"
    lineHeight: 1.5
  label-lg:
    fontFamily: Gravity
    fontSize: 16px
    fontWeight: "700"
    lineHeight: 32px
  label-md:
    fontFamily: Gravity
    fontSize: 14px
    fontWeight: "700"
    lineHeight: 1.5
  label-sm:
    fontFamily: Gravity
    fontSize: 12px
    fontWeight: "700"
    lineHeight: 1.12
rounded:
  sm: 4px
spacing:
  xs: 4px
  sm: 8px
  md: 12px
  lg: 16px
  xl: 32px
  xxl: 40px
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.neutral}"
    typography: "{typography.label-lg}"
    rounded: "{rounded.sm}"
    padding: "{spacing.lg}"
    height: 32px
  card:
    backgroundColor: "{colors.neutral}"
    textColor: "{colors.on-surface-variant}"
    typography: "{typography.label-md}"
    rounded: "{rounded.sm}"
  footer:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface-muted}"
    typography: "{typography.label-sm}"
---

# MTV Germany

## Overview

MTV Germany (mtv.de) is the German home of MTV's reality and music entertainment brand — Germany Shore, Catfish, Caught In The Act. The design is a stage, not a document: a pure-black canvas on which oversized, saturated show photography does almost all of the talking. Chrome is kept to an absolute minimum — a slim logo bar, one loud call-to-action, a quiet footer.

Two signals carry the brand. The first is **MTV Yellow** (`#F7F908`), an electric, near-fluorescent yellow reserved for the single most important action on the page (the Paramount+ CTA). The second is the **Gravity** typeface, a confident geometric grotesque used for every piece of text on the site, from 36px headlines down to 12px legal links — the brand voice never switches fonts.

The emotional register is loud, young, and pop-cultural: party imagery, confetti, neon pinks and teals live inside the photography, while the UI itself stays disciplined and almost monochrome so the content can scream. Target audience is a young adult, reality-TV and music crowd; the design should feel like a channel ident, not a corporate portal.

## Colors

The UI palette is deliberately narrow — black, white, grays, and two accents — because color arrives through photography.

- **Neutral / canvas** (`neutral`, `#000000`): true black is the page background and the ground for every module. Hero imagery is blended into it with black scrim gradients (top-down `rgba(0,0,0,0.85) → transparent` behind the nav, `transparent → #000` at the hero's foot, and a left-to-right `#000 → transparent` panel that keeps hero text legible over the photo).
- **MTV Yellow** (`primary`, `#F7F908`): the sole action color. It appears exactly once above the fold — on the primary CTA button — plus as a graphic zigzag element inside promo artwork. Its scarcity is the point: yellow means "click here".
- **Signal Teal** (`secondary`, `#00AACD`): a hard offset drop-shadow (2px 2px 0) behind promo card images — a retro, sticker-like pop that injects MTV's playfulness into an otherwise flat UI.
- **Text ramp**: pure white (`on-surface`, `#FFFFFF`) for headlines and hero copy; near-white (`on-surface-variant`, `#F3F3F3`) for card titles and descriptions; mid-gray (`on-surface-muted`, `#A1A1A1`) for footer links and legal text.
- **Footer surface** (`surface`, `#121212`): the footer lifts one step off true black to mark the page's end.

All text/background pairs pass WCAG AA comfortably (white on black, black on yellow, `#A1A1A1` on `#121212` ≈ 7:1).

## Typography

One family everywhere: **Gravity**, a geometric sans (loaded weights include Gravity, Gravity Ext Bold, Gravity Ext Book; fall back to a geometric sans-serif like Inter or Poppins if Gravity is unavailable). The scale is a two-voice system:

- **Bold voice (700)** for anything that names something: `headline-lg` (36px, tight 1.12 line-height with a slight −0.36px letter-spacing) for hero titles and section headings; `title-md` (16px) for sub-headlines; `label-md` (14px) for card and tile titles; `label-sm` (12px) for footer links.
- **Book voice (350)** for anything that explains something: `body-md` (16px / 1.5) for hero descriptions, `body-sm` (14px / 1.5) for card copy. The unusually light 350 weight against bold 700 creates strong contrast within a single family.

No uppercase transforms, no italics, no serif anywhere. Hierarchy is achieved purely through size, weight, and the white → near-white → gray color ramp.

## Layout

Full-bleed, cinematic, and vertical. The hero occupies the entire first viewport with edge-to-edge photography; text sits bottom-left inside the black scrim. Below it, promo cards run in a two-up grid, followed by a three-up row of social tiles (Facebook / Instagram / YouTube) and a short "Über MTV Germany" text block.

Spacing follows a 4px base unit: 4 / 8 / 12 / 16 / 32 / 40px, with 8px and 12px doing most of the work inside components and 32–40px separating modules (occasional 60–120px breathing room around the about-section). Content is generous with the viewport — modules stretch wide rather than sitting in a narrow column.

## Elevation & Depth

The site is essentially flat; depth comes from two devices only:

1. **Scrim gradients** — black-to-transparent linear gradients layered over photography to seat text and the nav bar (see Colors).
2. **The teal offset shadow** — `2px 2px 0 #00AACD`, a hard, un-blurred drop shadow on promo card images. This is a graphic accent, not a material-elevation cue; do not replace it with a soft blurred shadow.

There are no soft box-shadows, no glassmorphism, no layered z-depth anywhere else.

## Shapes

Corners are subtle and uniform: **4px radius** on buttons, promo card images, and interactive tiles. Nothing is pill-shaped or circular in the core UI. The overall geometry is rectangular and edge-to-edge — softness comes from photography, not from shape language.

## Components

- **`button-primary`** — the Paramount+ CTA: MTV Yellow fill, black bold 16px Gravity label, 32px tall, 16px horizontal padding (vertically centered via the 32px line-height), 4px radius, no border, no shadow. This is the only filled-color control on the page; use it once per view.
- **`card`** — promo/show teaser: an image (4px radius, teal 2px 2px 0 offset shadow) with a bold 14px title and a book-weight 14px description in near-white (`#F3F3F3`) below or overlaid, all sitting directly on the black canvas — cards have no visible container, border, or background of their own.
- **`footer`** — a `#121212` band with bold 12px Gravity links in muted gray (`#A1A1A1`), social icon links, and legal copy. Links have no underline at rest.

No form inputs, tabs, or badges appear on the public homepage; extend the system from the button and card primitives if you need them (e.g., an input would be a 4px-radius black field with a `#3B3B3B`-style hairline and white text).

## Do's and Don'ts

- **Do** keep the canvas pure black and let photography supply the color. The UI itself stays monochrome plus yellow.
- **Do** reserve MTV Yellow for the single primary action per view. If everything is yellow, nothing is.
- **Do** use Gravity's 700-vs-350 weight contrast for hierarchy instead of adding more fonts or colors.
- **Do** seat text over imagery with black scrim gradients, never with semi-transparent gray panels.
- **Do** use the hard teal offset shadow (`2px 2px 0 #00AACD`) as the playful accent on imagery.
- **Don't** introduce soft, blurred drop shadows or card containers with visible borders — the system is flat.
- **Don't** use light backgrounds; this identity only exists in dark mode.
- **Don't** round corners beyond 4px or introduce pill buttons.
- **Don't** set headlines in uppercase or add letter-spacing beyond the slight negative tracking of `headline-lg`.
- **Don't** let chrome compete with content — navigation stays a slim logo bar plus one CTA.

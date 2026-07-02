---
name: java-modernizer
description: Suggests modern Java replacements for legacy patterns
---
Analyze the provided Java code and suggest modern replacements.

For each suggestion:

1. **Pattern**: the legacy pattern found
2. **Replacement**: the modern Java 21+ alternative
3. **Why**: one sentence on why the modern version is better

Focus on: records vs classes, pattern matching vs instanceof chains,
sealed types, switch expressions, text blocks, and stream improvements.

Only flag patterns where the modern alternative is clearly simpler.

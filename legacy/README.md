# Bron-Kerbosch

Finds all **maximal cliques** in an undirected graph using the Bron-Kerbosch algorithm with pivot selection.

A maximal clique is a subset of vertices where every pair is connected, and no vertex can be added without breaking that property. The pivot optimization reduces the search space by skipping candidates adjacent to the most-connected node.

## Algorithm Flow

```mermaid
flowchart TD
    A["compute()"] --> B["Initialize P = all graph nodes\nR = ∅, X = ∅"]
    B --> C["bk(R, P, X)"]
    C --> D{P empty AND\nX empty?}
    D -- Yes --> E["R is a maximal clique\nAdd to results"]
    D -- No --> F["Select pivot u\nnode with most neighbours"]
    F --> G["Create P \\ N(u)\ncandidates not adjacent to pivot"]
    G --> H{More vertices v\nin P \\ N(u)?}
    H -- No --> Z["Return"]
    H -- Yes --> I["Pick next vertex v"]
    I --> J["R' = R ∪ {v}"]
    J --> K["P' = P ∩ N(v)"]
    K --> L["X' = X ∩ N(v)"]
    L --> M["bk(R', P', X')"]
    M --> N["P = P \\ {v}\nX = X ∪ {v}"]
    N --> H
```

The recursive `bk(R, P, X)` either records a maximal clique (base case: P and X empty) or picks a pivot, iterates over non-neighbour candidates, recurses, and moves each processed vertex from P to X.

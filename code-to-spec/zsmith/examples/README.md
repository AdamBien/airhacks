# zsmith Examples

Runnable, single-file Java 25 agents built with [zsmith](../zsmith). Each file is an executable shebang script — make it executable and run it directly:

```sh
chmod +x helloWorld
./helloWorld
```

All examples expect `zsmith.jar` and `lightmetal.jar` on the classpath (resolved relative to `../zsmith/zbo`). With `lightmetal.jar` present, the agent talks to LightMetal in-process — same JVM, same heap, no HTTP.

## Examples

| Example | What it demonstrates |
| --- | --- |
| [`helloWorld`](helloWorld) | The minimal agent — a system prompt and a single `act()` call, result printed to stdout. |
| [`calculator`](calculator) | Built-in tools (`USER_QUESTION`, `USER_MESSAGE`, `CALCULATOR`) driving an interactive question → evaluate → answer loop. |
| [`currentDate`](currentDate) | Defining custom tools inline with `Tool.of(...)`, including one with a typed input schema. |
| [`fileCalculator`](fileCalculator) | File I/O tools (`READ_ANY_FILE`, `WRITE_ANY_FILE`) — read an expression from a file, compute, write the result back. |
| [`transcriber`](transcriber) | Sub-agent delegation (`withSubAgent`) plus persistent episodic memory (`withEpisodicMemory`) over a podcast-transcript workflow. |
| [`linkTitler`](linkTitler) | A delegating agent that hands each URL to a `link_labeler` sub-agent, keeping fetched page content out of the parent's context. Reads/writes the clipboard. |
| [`assistant`](assistant) + [`zschat`](zschat) | A client/server pair: `assistant` is a fully loaded agent — every built-in tool (`withAllTools`), episodic memory, and the full skill catalog (`withSkills`) — served over HTTP (`withHttpServer`); `zschat` (zsmith chat) is the TUI chat client (`Chat`) that connects to it. See below. |

## Running `assistant` + `zschat`

`assistant` and `zschat` form a server/client pair. Start the agent server first, then connect the TUI client to the same port:

```sh
./assistant 8080   # serves the fully loaded agent over HTTP on :8080
./zschat   8080    # TUI chat client connecting to the same port
```

Both default to port `8080` when no argument is given.

## Common building blocks

- `new Agent(name, systemPrompt)` — create an agent.
- `.withTool(...)` / `.withTools(...)` — grant built-in [`Tools`](../zsmith) or custom `Tool` instances.
- `.withSubAgent(...)` — expose another agent as a delegated tool.
- `.withEpisodicMemory()` — add persistent `store_memory` / `recall_memory`.
- `.withSkills()` — load the discovered skill catalog and the `load_skill` tool.
- `.withMaxIterations(n)` — bound multi-step tool chains.
- `.withHttpServer(port)` — serve the agent over HTTP instead of the console.
- `.act()` — run the agent and return its final response.

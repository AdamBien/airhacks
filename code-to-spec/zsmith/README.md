# zsmith

Zero-dependency AI agent framework with tool execution, SKILL.md and agentic loop support. The entire framework is a single **192 KB** jar — no external libraries, only the Java standard library. Optionally integrates with [LightMetal](#lightmetal-embedded-local-inference) for fully on-device GGUF inference on Apple Silicon — drop `lightmetal.jar` on the classpath and it is auto-selected, no code or config change required.

![zsmith](zsmith.png)

## Requirements

- **Java 25+** — uses implicit classes, text blocks, records, and source-file mode
- **Anthropic API key** — set `anthropic.api.key` in `~/.zsmith/app.properties` or as a system property
- **Build with zb** — run `./zb.sh` to produce `zbo/zsmith.jar` (no Maven/Gradle needed)

## Installation

Fetch the latest prebuilt `zsmith.jar` into `./zbo/` without cloning or building:

```bash
curl -O https://raw.githubusercontent.com/AdamBien/zsmith/main/zsinstall
chmod +x zsinstall
./zsinstall
```

`zsinstall` is a single-file Java 25 script that downloads the latest release asset from GitHub into `./zbo/zsmith.jar` — matching the `-cp zbo/zsmith.jar` shebang used by the example agents. Re-run any time to upgrade.

Based on the [`java-cli-script`](https://airails.dev) skill from [airails.dev](https://airails.dev) — single-file, zero-dependency, shebang-launched Java 25 utilities. Optional local inference via [LightMetal](https://github.com/AdamBien/lightmetal) — a Java 25 GGUF runner for Apple Silicon's Metal via the Foreign Function & Memory API.

## Quick Start

Install the jar, add your API key, and run your first agent in under a minute.

**1. Install the jar** (see [Installation](#installation)) — it lands in `./zbo/zsmith.jar`.

**2. Add your Anthropic API key** to `~/.zsmith/app.properties`:

```properties
anthropic.api.key=sk-ant-...
```

**3. Save this as `calculator`** (no file extension) in the same directory as `zbo/`:

```java
#!/usr/bin/java --class-path=zbo/zsmith.jar --source 25

import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.tools.boundary.Tools;

void main() {
    var calculator = new Agent("calculator", """
            You are a calculator assistant.
            1. Use the user_question tool to ask the user for a math expression.
            2. Use the calculator tool to evaluate it.
            3. Show the result to the user.
            4. Loop until the user types 'quit'.
            """)
            .withTools(Tools.USER_QUESTION, Tools.USER_MESSAGE, Tools.CALCULATOR);
    calculator.act();
}
```

**4. Make it executable and run it:**

```bash
chmod +x calculator
./calculator
```

The agent asks for a math expression, evaluates it with the `calculator` tool, prints the result, and loops until you type `quit`. No build step — Java 25 runs the script directly against the prebuilt jar.

**No API key? Run fully on-device.** On Apple Silicon you can skip step 2 entirely: drop [`lightmetal.jar`](#lightmetal-embedded-local-inference) on the classpath and zsmith auto-selects local GGUF inference — no key, no network. Adjust the shebang in step 3:

```java
#!/usr/bin/java --class-path=zbo/zsmith.jar:lightmetal.jar --enable-native-access=ALL-UNNAMED --source 25
```

The agent code is unchanged — see [LightMetal](#lightmetal-embedded-local-inference) for model configuration.

Once this works, read on for the library API, tool profiles, and configuration.

## Usage

```java
var agent = new Agent("calculator", "You are a helpful assistant.")
        .withTools(new CalculatorTool(), new CurrentTimeTool());

var response = agent.chat("What is 42 * 17?");
```

Agentic execution — `act()` sends `"go"` as the user message, letting the system prompt drive the task:

```java
var agent = new Agent("reporter", "Summarize today's tasks.")
        .withTools(new ReadFileTool(), new CurrentTimeTool());

var response = agent.act();
```

## Tool Profiles

Predefined tool groupings for common use cases:

```java
var agent = new Agent("assistant")
        .withUserIOTools()   // user_message, user_question, user_confirmation
        .withFileIOTools()   // read_file, write_file, list_files, read_any_file (sandboxed)
        .withAllTools();     // calculator, current_time, clipboard, read_any_file,
                             // check_link, user_confirmation, user_message, user_question
```

`withAllTools()` includes all tools from the `Tools` enum. Sandboxed file tools (`read_file`, `write_file`, `list_files`) require `withFileIOTools()` because they need a configured `sandbox.path`.

### Launch App Tool

`withLaunchAppTool()` adds a config-driven tool that launches an external application and passes arguments to it:

```java
// Explicit configuration
var agent = new Agent("assistant")
        .withLaunchAppTool("run_tests", "Runs the test suite", "./run-tests.sh");

// From app.properties (launch.tool.name, launch.tool.description, launch.command)
var agent = new Agent("assistant")
        .withLaunchAppTool();
```

Configure in `app.properties`:

```properties
launch.tool.name=run_tests
launch.tool.description=Runs the test suite
launch.command=./run-tests.sh
```

## Configuration

### Required Properties

Add to `~/.zsmith/app.properties` or any properties file in the loading chain:

```properties
anthropic.api.key=sk-ant-...
anthropic.version=2023-06-01
```

### Model

The default Claude model is `claude-opus-4-8`. Override via system property:

```bash
java -Dmodel=sonnet -cp zbo/zsmith.jar MyAgent.java
```

Partial matching works — `sonnet` resolves to `claude-sonnet-4-7`, `4-7` to `claude-opus-4-7`, etc.

### Properties Loading Order

Loaded in order (each layer overrides the previous):

1. `~/.zsmith/app.properties` — global defaults
2. `./app.properties` — local project defaults
3. `~/.zsmith/[agentName]/app.properties` — global agent-specific
4. `./[agentName]/app.properties` — local agent-specific
5. System properties — highest priority

Only keys present in later files override earlier values; other keys are preserved.

### Tool Permissions

Control which tools require user confirmation before execution. Three permission levels: `allow` (execute silently), `deny` (reject), `confirm` (ask user first). Default is `confirm`.

```properties
tools.permissions.default=confirm
tools.permissions.calculator=allow
tools.permissions.current_time=allow
tools.permissions.execute_script=confirm
tools.permissions.read_any_file=confirm
```

Agent-specific permissions in `~/.zsmith/[agentName]/app.properties` override global defaults:

```properties
# A trusted automation agent
tools.permissions.default=allow
tools.permissions.execute_script=confirm
```

### System Prompt

Loaded from `system.prompt` files in order (each layer overrides the previous):

1. `~/.zsmith/[agentName]/system.prompt` — global agent-specific
2. `./[agentName]/system.prompt` — local agent-specific
3. `./system.prompt` — highest priority

If no file is found, the constructor parameter is used as fallback.

### Alternative LLM Providers

> First-time users can skip this section — the default `claude` provider works out of the box. Come back when you want Amazon Bedrock, OpenAI, or local inference.

zsmith ships with three clients, selected at runtime via `llm.provider`:

```properties
llm.provider=claude         # default — Anthropic Messages API
# llm.provider=bedrock      # Amazon Bedrock Mantle (Anthropic-compatible, reuses the Claude client)
# llm.provider=openai       # OpenAI Chat Completions API
# llm.provider=lightmetal   # local GGUF inference via lightmetal.jar (in-process)
```

Agent code is unchanged either way — request and response are translated internally so the Agent loop only ever sees Anthropic-shaped content blocks.

#### Claude endpoint

By default, requests go to `https://api.anthropic.com/v1/messages`. To point at a local Anthropic-compatible endpoint:

```properties
claude.scheme=http
claude.host=localhost
claude.port=8080
```

`claude.port` is optional — omit it to use the scheme default. `claude.scheme` defaults to `https`, `claude.host` to `api.anthropic.com`.

Any Anthropic-compatible gateway can be reached by overriding these optional knobs — all default to the native Anthropic values, so leaving them unset preserves current behavior:

```properties
claude.path=/v1/messages              # request path (default)
claude.model=claude-opus-4-8          # payload model id; default derived from -Dmodel / enum
anthropic.auth.header=x-api-key       # name of the auth header carrying anthropic.api.key
anthropic.workspace.id=               # adds anthropic-workspace-id header only when set
```

For a `Bearer`-token gateway, set `anthropic.auth.header=Authorization` and put the prefix in the key: `anthropic.api.key=Bearer <token>`.

#### Amazon Bedrock Mantle

[Amazon Bedrock Mantle](https://docs.aws.amazon.com/bedrock/latest/userguide/bedrock-mantle.html) exposes an Anthropic-compatible Messages API at its `bedrock-mantle` endpoint. It is selected with `llm.provider=bedrock` and reuses the Claude client — only the **region**, **model**, and **API key** vary; everything else is convention-derived.

Because the native Anthropic and Bedrock settings never collide, **both can live in the same properties file** and you switch between them by flipping a single line:

```properties
# --- switch provider here ---
llm.provider=claude        # native Anthropic API
#llm.provider=bedrock      # Amazon Bedrock Mantle

# --- native Anthropic ---
anthropic.api.key=sk-ant-...
anthropic.version=2023-06-01

# --- Amazon Bedrock Mantle ---
bedrock.region=eu-north-1
bedrock.api.key=bedrock-api-...

# --- shared ---
claude.model=claude-haiku-4-5  # bare name works for both; pick one your Bedrock account can use
```

When `llm.provider=bedrock`, zsmith derives:

- **endpoint** → `https://bedrock-mantle.<region>.api.aws/anthropic/v1/messages`
- **anthropic-version** → `2023-06-01` (override with `anthropic.version` if needed)
- **API key** → `bedrock.api.key`, falling back to `anthropic.api.key` when unset
- **project header** → `bedrock.project.id`, mapped to whichever header the active wire accepts — `anthropic-workspace-id` on the Messages route, `openai-project` on the Chat Completions route (see below)
- **model prefix** → a **bare** `claude.model` gets the `anthropic.` prefix, so `claude.model=claude-haiku-4-5` resolves to `anthropic.claude-haiku-4-5`

The same bare `claude.model` therefore works under both providers — used as-is for native Anthropic, `anthropic.`-prefixed under Bedrock. An id that already contains a `.` (e.g. `anthropic.claude-haiku-4-5`) is used verbatim. The 529→fallback retry is Anthropic-specific and does not apply to Bedrock model ids.

> **Pick a model your account can use.** Bedrock returns `403 … is not available for this account` for models you have not been granted. List/enable models in the Bedrock console; your account's available Anthropic models determine valid `claude.model` values. See the [Bedrock Mantle docs](https://docs.aws.amazon.com/bedrock/latest/userguide/bedrock-mantle.html) for regions and the [endpoints reference](https://docs.aws.amazon.com/bedrock/latest/userguide/endpoints.html).

##### OpenAI-compatible models (NVIDIA Nemotron)

Bedrock Mantle also serves **non-Anthropic** models — such as [NVIDIA Nemotron Super 3 120B](https://docs.aws.amazon.com/bedrock/latest/userguide/model-card-nvidia-nemotron-super-3-120b.html) — over its **OpenAI-compatible Chat Completions** route (`/v1/chat/completions`) rather than the Anthropic Messages route. zsmith detects this from the model id and switches wire format automatically — still under `llm.provider=bedrock`, no extra provider:

```properties
llm.provider=bedrock
bedrock.region=eu-west-1                       # pick a region that offers the model (see model card)
bedrock.api.key=bedrock-api-...
claude.model=nvidia.nemotron-super-3-120b      # id carries a '.', used verbatim — no anthropic. prefix
```

For such models zsmith derives:

- **endpoint** → `https://bedrock-mantle.<region>.api.aws/v1/chat/completions` (not `/anthropic/v1/messages`)
- **auth** → `Authorization: Bearer <bedrock.api.key>`
- **project header** → `openai-project` (this route **rejects** `anthropic-workspace-id`), sourced from `openai.project` or `bedrock.project.id`
- **request/response** → translated to and from the OpenAI Chat Completions shape, so the Agent loop still sees Anthropic-shaped content blocks and `tool_use`

> **One project id, both wires.** Set `bedrock.project.id` once and zsmith emits the header the active route accepts — `anthropic-workspace-id` for Anthropic models, `openai-project` for OpenAI-compatible ones like Nemotron — so flipping `claude.model` needs no other change. The wire-native keys `anthropic.workspace.id` / `openai.project` still override it when set. (Setting `anthropic.workspace.id` while running a Nemotron model is what triggers Bedrock's `The anthropic-workspace-id header is not supported for this API format` error — use `bedrock.project.id` instead.)

> **Region matters.** This model is offered only in specific regions, and the set changes over time — a region that works today may not be the one in your existing Bedrock config. If you get a "model isn't supported"/availability error, check the current regions on the [model card](https://docs.aws.amazon.com/bedrock/latest/userguide/model-card-nvidia-nemotron-super-3-120b.html) and set `bedrock.region` accordingly.

#### OpenAI endpoint

By default, requests go to `https://api.openai.com/v1/chat/completions`. Configurable knobs:

```properties
openai.api.key=sk-...      # optional — omitted Authorization header when blank (useful for local servers)
openai.model=gpt-4o        # default
openai.max.tokens=4096     # default
openai.scheme=https        # default
openai.host=api.openai.com # default
openai.port=                # default unset (uses scheme default port)
```

The OpenAI client has no fallback model — unlike Claude's 529→fallback retry, OpenAI errors propagate directly.

To point at a local Ollama server:

```properties
llm.provider=openai
openai.host=localhost
openai.port=11434
openai.scheme=http
openai.model=llama3.1
```

LM Studio (default port 1234), llama.cpp `--api`, and vLLM expose the same Chat Completions shape and work identically.

#### LightMetal (embedded local inference)

[LightMetal](https://github.com/AdamBien/lightmetal) is a Java 25 GGUF runner that talks to Apple Silicon's Metal via the Foreign Function & Memory API. zsmith reaches it via the `UnaryOperator<String>` SPI (`lm.generation.boundary.LightMetalChat`), so the only compile-time dependency is `java.base` — drop `lightmetal.jar` on the classpath at runtime and the provider is **auto-selected**, overruling `llm.provider` whatever it is set to. The classpath is the explicit signal; no extra config is needed. The GGUF is loaded once on the first call and reused for every subsequent turn.

```properties
lightmetal.model=/abs/path/to/model.gguf   # optional — overrides lightmetal's own config
lightmetal.max.tokens=4096                 # optional — default 4096
```

`lightmetal.model` is **optional** in zsmith. When unset, zsmith omits `model` from the request payload entirely — lightmetal then sources it from its own eager-loaded `~/.lightmetal/app.properties` (or `-Dmodel=...`). So a user who already runs `lmprompt`/`lmserve` against a configured `~/.lightmetal/app.properties` needs zero zsmith-side model config. Set `lightmetal.model` in zsmith only when you want one agent to override the lightmetal-wide default.

LightMetal natively understands Anthropic-shaped `tools` and emits `tool_use` content blocks, so the Agent loop works the same as with Claude. Run agent scripts with `--enable-native-access=ALL-UNNAMED` so the FFM call into `libllama.dylib` is allowed:

```java
#!/usr/bin/java --class-path=zbo/zsmith.jar:lightmetal.jar --enable-native-access=ALL-UNNAMED --source 25
```

For benchmarking or remote inference you can also point the **Claude** client at LightMetal's HTTP server (`-serve` mode) — its `/v1/messages` endpoint is byte-compatible with Anthropic's:

```properties
llm.provider=claude
claude.scheme=http
claude.host=localhost
claude.port=8080
```

## Running the Examples

```bash
zb.sh
java -cp zbo/zsmith.jar src/test/java/airhacks/zsmith/MeetingPlannerExample.java
```

```bash
java -cp zbo/zsmith.jar src/test/java/airhacks/zsmith/UserConfirmationExample.java
```

```bash
java -cp zbo/zsmith.jar src/test/java/airhacks/zsmith/EpisodicMemoryExample.java
```

```bash
java -cp zbo/zsmith.jar src/test/java/airhacks/zsmith/SkillsExample.java
```

## Java Script Usage

zsmith agents can run as standalone Java scripts using source-file mode — no build tool, no compilation step:

```bash
./src/test/java/airhacks/zsmith/userConfirmationExample
```

The script uses a shebang to reference `zbo/zsmith.jar` directly, so build first with `./zb.sh`. Example script:

```java
#!/usr/bin/java --class-path=../../../../../zbo/zsmith.jar --source 25

// Requires zbo/zsmith.jar — build first with: ./zb.sh

import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.logging.control.Log;
import airhacks.zsmith.tools.boundary.Tools;

void main() {

        var agent = new Agent()
                .withSystemPrompt("""
                        You are a helpful assistant with access to tools.
                        Use the user_confirmation tool to ask the user yes/no questions before proceeding with actions.
                        Be concise in your responses.
                        """)
                .withTool(Tools.USER_CONFIRMATION);

        Log.agent("Agent initialized with user_confirmation tool");

        var question = "I want to create a HelloWorld.java example. Can you help?";
        Log.prompt("User: " + question);

        var response = agent.chat(question);
        Log.answer("Agent: " + response);

}
```

No package declaration, no class wrapper — Java 25 implicit classes keep the script minimal. Install system-wide by copying the jar and script to a PATH directory, adjusting the `--class-path` accordingly.

A minimal calculator agent — see [`examples/calculator`](examples/calculator):

```java
#!/usr/bin/java --class-path=../zsmith/zbo/zsmith.jar  --source 25

import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.tools.boundary.Tools;

void main() {

var calculator = new Agent("calculator", """
        You are a calculator assistant.
        1. Ask the user for a math expression.
        2. Use the calculator tool to evaluate it.
        3. Show the result to the user.
        4. Loop until the user types 'quit'.
        """)
        .withTools(Tools.USER_QUESTION, Tools.USER_MESSAGE, Tools.CALCULATOR);

calculator.act();
}
```

Run it directly:

```bash
./examples/calculator
```

A file-driven variant — see [`examples/fileCalculator`](examples/fileCalculator) — asks the user for input and output paths, reads a math expression from the input file, evaluates it, and writes the numeric result to the output file. Its shebang also lists `lightmetal.jar` on the classpath:

```java
#!/usr/bin/java --class-path=../zsmith/zbo/zsmith.jar:../../lightmetal/zbo/lightmetal.jar --enable-native-access=ALL-UNNAMED --source 25
```

The `../../lightmetal/zbo/lightmetal.jar` entry is **optional**. Drop it (and `--enable-native-access`) to run against Claude. Keep it to auto-select [LightMetal](#lightmetal-embedded-local-inference) for fully on-device inference — no other config change required, just set `lightmetal.model`.

An inline-tool variant — see [`examples/currentDate`](examples/currentDate) — defines its tools directly in the script via `Tool.of(...)` instead of pulling them from the `Tools` enum:

```java
#!/usr/bin/java --class-path=../zsmith/zbo/zsmith.jar  --source 25

import java.time.LocalDate;

import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.tools.control.Tool;

void main() {

var currentDate = Tool.of(
        "current_date",
        "Returns the current date in ISO format (yyyy-MM-dd).",
        _ -> LocalDate.now().toString());

var printMessage = Tool.of(
        "print_user_message",
        "Prints a message to the user's console.",
        Tool.schema(Tool.Prop.string(PrintField.message, "The message to print to the user")),
        input -> {
            IO.println(input.getString("message"));
            return "Message printed to user";
        });

var agent = new Agent("current-date", """
1. Use the current_date tool to obtain today's date.
2. Use the print_user_message tool to print the date to the user.
""")
.withTools(currentDate, printMessage);

agent.act();
}

enum PrintField { message }
```

Run it directly:

```bash
./examples/currentDate
```

`Tool.of(...)` is the inline counterpart to implementing the `Tool` interface — useful when a tool is small enough to live next to the agent that uses it. Two overloads are available:

- `Tool.of(name, description, schema, fn)` — full form with an explicit input schema (use `Tool.schema(...)` to declare parameters).
- `Tool.of(name, description, fn)` — short form for parameter-less tools; the input schema defaults to `Tool.emptySchema()`.

### JFR Configuration

zsmith emits JDK Flight Recorder events for every agent turn, Claude API call, tool invocation, sub-agent dispatch, skill load, and memory access — all under the `zsmith` category. To record them while running the calculator, add `-XX:StartFlightRecording` to the shebang:

```java
#!/usr/bin/java -XX:StartFlightRecording=filename=calculator.jfr,dumponexit=true,settings=profile --class-path=../zsmith/zbo/zsmith.jar --source 25
```

Open `calculator.jfr` in JDK Mission Control and filter the event browser by category `zsmith` to see:

| Event | Category | What it captures |
|-------|----------|------------------|
| `airhacks.zsmith.agent.Turn` | `zsmith / agent` | One iteration of the chat loop with stop reason and tool counts |
| `airhacks.zsmith.claude.APICall` | `zsmith / claude` | HTTP call to the Anthropic Messages API with token usage |
| `airhacks.zsmith.tools.Invocation` | `zsmith / tools` | Single tool execution with outcome and result size |
| `airhacks.zsmith.subagent.Dispatch` | `zsmith / subagent` | Delegation to a sub-agent |
| `airhacks.zsmith.skills.Load` | `zsmith / skills` | Skill read from disk during `SkillStore` init |
| `airhacks.zsmith.memory.Access` | `zsmith / memory` | Read or write of a persistent memory store |

For a focused recording, pass a custom `.jfc` file enabling only the `airhacks.zsmith.*` events via `settings=zsmith.jfc`.

## Benchmarks

The [`benchmarks/`](benchmarks/) directory holds executable agent benchmarks that score tool-calling behavior against seeded ground truth — no LLM judge — along orthogonal axes whose results can disagree. [`agentLoopBenchmark`](benchmarks/agentLoopBenchmark) drives an agent through a *pointer-chasing* chain (serial loop-following) and reports `PASS`/`FAIL` plus the tool-call count. [`agentParallelismBenchmark`](benchmarks/agentParallelismBenchmark) gives the agent independent lookups and measures whether it *batches* them into one turn or serializes them (efficiency = calls vs turns) — the inverse axis. See [`benchmarks/README.md`](benchmarks/README.md) for mechanisms and sweep usage.

## Skills

Skills are reusable prompt snippets stored as `SKILL.md` files. Each skill uses frontmatter for metadata:

```markdown
---
name: explain
description: Explains a concept using an analogy and a short example
---
When explaining a concept:

1. Start with a one-sentence definition
2. Give an everyday analogy
3. Show a minimal code example (if applicable)
4. End with one common misconception

Keep it under 10 sentences total.
```

Default skill resolution (each layer overrides the previous):

1. `~/.zsmith/skills/` — global skills
2. `~/.zsmith/[agentName]/skills/` — global agent-specific
3. `./skills/` — local project skills
4. `./[agentName]/skills/` — local agent-specific

```java
var agent = new Agent()
        .withSkills();
```

Custom skill directory:

```java
var agent = new Agent()
        .withSkills("path/to/skills");
```

Preselected skills — load only the named skills from the default resolution chain:

```java
var agent = new Agent("planner")
        .withSkillsNamed("explain", "summarize");
```

Skills not matching the given names are excluded from the catalog and from `load_skill`.

## Episodic Memory

Agents store and recall information across conversations using `EpisodicMemoryStore`. Memories are persisted to a JSON file and classified by type: `user`, `feedback`, `project`, `reference`.

Agent-specific memory (stored at `~/.zsmith/[agentName]/memory/episodic-memory.json`):

```java
var agent = new Agent("planner")
        .withEpisodicMemory();
```

Shared memory across all agents (stored at `~/.zsmith/memory/episodic-memory.json`):

```java
var agent = new Agent("planner")
        .withSharedEpisodicMemory();
```

Custom storage location:

```java
var agent = new Agent()
        .withEpisodicMemory(new EpisodicMemoryStore(Path.of("custom-memory.json")));
```

## Subagents

Agents can delegate tasks to other agents via `withSubAgent()`. The child agent becomes a callable tool (`delegate_to_<name>`).

By default, multiple `withSubAgent()` invocations run in parallel — but the **first successful run of each subagent is forced sequential** so that any `confirm`-level tool permission prompts appear cleanly one at a time on stdout/stdin instead of colliding across virtual threads. Once a subagent has completed once, a marker is written to `~/.zsmith/<subAgentName>/.first_run_completed` and subsequent runs fan out in parallel. Use `withSequentialSubAgent()` to opt out of parallelism entirely; delete the marker file to force another sequential warm-up.

Podcast transcription example — the coordinator asks for the transcript path, reads the file, delegates link verification, stores guests and links in memory, and copies the result to the clipboard:

```java
var linkChecker = new Agent("link_checker", """
        You verify URLs. For each URL given, use the check_link tool
        to confirm it is reachable. Return a markdown status list.
        """)
        .withTool(Tools.LINK_CHECKER);

var transcriber = new Agent("transcriber", """
        You process podcast transcriptions.
        1. Ask the user for the transcript file path.
        2. Read the transcript.
        3. Extract all guest names and URLs mentioned.
        4. Delegate link verification to the link_checker agent.
        5. Store guests and verified links in memory.
        6. Write a summary with link status annotations to the clipboard.
        """)
        .withTools(Tools.USER_QUESTION, Tools.READ_ANY_FILE, Tools.WRITE_CLIPBOARD)
        .withSubAgent(linkChecker)
        .withEpisodicMemory();

var response = transcriber.act();
```

As a standalone Java script with shebang:

```java
#!/usr/bin/java --class-path=zbo/zsmith.jar --source 25

import airhacks.zsmith.agent.boundary.Agent;
import airhacks.zsmith.tools.boundary.Tools;

void main() {

        var linkChecker = new Agent("link_checker", """
                You verify URLs. For each URL given, use the check_link tool
                to confirm it is reachable. Return a markdown status list.
                """)
                .withTool(Tools.LINK_CHECKER);

        var transcriber = new Agent("transcriber", """
                You process podcast transcriptions.
                1. Ask the user for the transcript file path.
                2. Read the transcript.
                3. Extract all guest names and URLs mentioned.
                4. Delegate link verification to the link_checker agent.
                5. Store guests and verified links in memory.
                6. Write a summary with link status annotations to the clipboard.
                """)
                .withTools(Tools.USER_QUESTION, Tools.READ_ANY_FILE, Tools.WRITE_CLIPBOARD)
                .withSubAgent(linkChecker)
                .withEpisodicMemory();

        transcriber.act();
}
```

Custom tool name, description, and max delegation depth:

```java
var agent = new Agent("coordinator")
        .withTool(new SubAgentTool(linkChecker, "verify_links",
                "Verifies all URLs in the given text", 2));
```

## Built-in Tools

| Tool | Name | Description |
|------|------|-------------|
| `CalculatorTool` | `calculator` | Performs basic arithmetic operations: add, subtract, multiply, divide |
| `CurrentTimeTool` | `current_time` | Returns the current date and time |
| `ReadClipboardTool` | `read_clipboard` | Reads text content from the system clipboard |
| `WriteClipboardTool` | `write_clipboard` | Writes text content to the system clipboard |
| `ReadFileTool` | `read_file` | Reads the contents of a file within the sandbox directory |
| `WriteFileTool` | `write_file` | Writes content to a file within the sandbox directory |
| `ListFilesTool` | `list_files` | Lists all files within the sandbox directory |
| `ReadAnyFileTool` | `read_any_file` | Reads a file from any location on the filesystem |
| `LinkCheckerTool` | `check_link` | Verifies a URL is reachable; returns status code, final URL after redirects, and content type |
| `FetchUrlTool` | `fetch_url` | Fetches a URL with a browser User-Agent and returns status, content type, and up to 20000 chars of the body |
| `UserConfirmationTool` | `user_confirmation` | Asks the user a yes/no question and returns the answer |
| `UserQuestionTool` | `user_question` | Asks the user a question and returns the typed answer |
| `UserMessageTool` | `user_message` | Presents a message to the user |
| `StoreMemoryTool` | `store_memory` | Stores an episode in long-term memory for future recall |
| `RecallMemoryTool` | `recall_memory` | Recalls past memories, optionally filtered by type or limited to recent entries |
| `LoadSkillTool` | `load_skill` | Loads a skill by name (added automatically with `withSkills()`) |
| `ExecuteScriptTool` | `execute_script` | Executes a script and returns its output |
| `LaunchAppTool` | *(config-driven)* | Launches an external application with arguments (name, description, command from config or constructor) |

## Custom Tools

Implement the `Tool` interface. Use `Tool.Prop` with an enum for type-safe field names and `Tool.schema()` to define the input schema:

```java
public class MyTool implements Tool {

    enum Field { param, count }

    public String toolName() {
        return "my_tool";
    }

    public String description() {
        return "Does something useful";
    }

    public String inputSchema() {
        return Tool.schema(
            Prop.string(Field.param, "Parameter description"),
            Prop.integer(Field.count, "How many times").optional()
        );
    }

    public String execute(JSONObject input) {
        return "Result: " + input.getString(Field.param.name());
    }
}
```

Available `Prop` types: `string`, `stringEnum` (with allowed values), `number`, `integer`. Any prop can be marked `.optional()`.

---

architecture by [bce.design](https://bce.design) | built by [zb](https://github.com/AdamBien/zb) | tested by [zunit](https://github.com/AdamBien/zunit) | skill provided by: [airails](https://airails.dev) | powered by [airhacks.live](https://airhacks.live)

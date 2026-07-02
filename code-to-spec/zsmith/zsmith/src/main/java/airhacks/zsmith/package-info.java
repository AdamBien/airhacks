/// # ZSmith
/// > Run an autonomous, tool-using AI agent from the terminal or over HTTP, backed by pluggable LLM providers.
///
/// ## Vision
/// - A zero-dependency agent a developer fully understands, small enough to read in an afternoon yet capable of real work.
///
/// ## Components
/// <!-- concrete cross-BC wiring; direction matters. Each BC's own contract lives in its package-info. -->
/// - `agent` is the orchestrator; it drives `tools`, `skills`, `memory`, `episodicmemory`, `systemprompt`, and `subagent`, and reaches an LLM only through `llm`.
/// - `llm` is the provider abstraction; it selects and calls `claude` or `openai` by model wire, and nothing else routes to a provider directly.
/// - `claude` may call `openai` to translate OpenAI-wire requests and responses; never the reverse.
/// - `tui` and `http` are the two entry surfaces; both reach the domain only through `agent`.
/// - `configuration`, `logging`, and `errors` are infrastructure any BC may depend on; they depend on no domain BC.
///
/// ## Ubiquitous language
/// <!-- shared domain nouns, defined once; each BC's `## Entities` stays terse. -->
/// - Agent — an LLM-driven loop that reasons over a conversation and invokes tools until a turn completes. Owned by `agent`.
/// - Tool — a named capability the agent may invoke, described to the model and executed on call. Owned by `tools`.
/// - Skill — an on-demand instruction set discovered from `SKILL.md` files and loaded into context. Owned by `skills`.
/// - Wire — the HTTP request/response shape a model speaks (Anthropic Messages or OpenAI Chat Completions). Owned by `claude`.
/// - Episode — a durable memory record the agent can store and later recall. Owned by `episodicmemory`.
///
/// ## Stack
/// - java-cli-app · base package `airhacks.zsmith`
package airhacks.zsmith;

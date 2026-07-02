/// # Claude
/// > Invoke a Claude model over HTTP and return an Anthropic-shaped response, hiding provider, wire, and model differences behind one contract.
///
/// ## Boundary
/// - `invoke-messages` — send a system prompt with a conversation and optional tools, and receive the assistant response
/// - `invoke-prompt` — send a system prompt with a single user message, and receive the assistant response
///
/// ## Requirements
/// ### R1: Resolve the active model
/// - R1.1 — The BC shall resolve the active model from the `claude.model` configuration, then the `-Dmodel` system property, then the default model. _(why: the configured name drives wire, token budget, and capabilities — deriving those from a stale default while sending another model id produces "model does not support this API" errors)_
/// - R1.2 — When a configured or requested name partially matches a known model, the BC shall select that model.
/// - R1.3 — If no configured or requested name matches a known model, then the BC shall use the default model.
///
/// ### R2: Derive endpoint and wire
/// - R2.1 — The BC shall derive the request endpoint from the active provider and the model's wire protocol.
/// - R2.2 — Where the provider is Bedrock, the BC shall derive scheme, host, path, and API version by convention from the region and the model's wire. _(why: one properties file holds both native Anthropic and Bedrock config and flips between them with a single key)_
/// - R2.3 — When the active model speaks the OpenAI wire, the BC shall translate the request to OpenAI shape and translate the response back to the Anthropic shape.
///
/// ### R3: Construct the request
/// - R3.1 — The BC shall include the model's maximum token budget, the system prompt, and the messages in every request.
/// - R3.2 — Where the model supports temperature, the BC shall include the requested temperature.
/// - R3.3 — Where the model supports adaptive thinking and a thinking mode is configured, the BC shall include the thinking configuration.
/// - R3.4 — Where the model supports effort and an effort is configured, the BC shall include the output effort configuration.
/// - R3.5 — When tools are supplied, the BC shall include them in the request.
///
/// ### R4: Authenticate the call
/// - R4.1 — When the active model speaks the Anthropic wire, the BC shall authenticate with the configured API-key header and send the API version.
/// - R4.2 — When the active model speaks the OpenAI wire, the BC shall authenticate with a bearer token.
/// - R4.3 — Where a project or workspace id is configured, the BC shall send it under the header the active wire expects.
///
/// ### R5: Recover from overload and failure
/// - R5.1 — If the model responds that it is overloaded, then the BC shall retry once with the model's fallback model.
/// - R5.2 — If a response is neither successful nor recovered, then the BC shall fail, reporting the status and endpoint.
/// - R5.3 — If the transport cannot reach the model, then the BC shall fail, indicating it cannot communicate with Claude.
///
/// ### R6: Instrument usage
/// - R6.1 — When a call succeeds, the BC shall record the served model, status, stop reason, and token usage as a telemetry event.
/// - R6.2 — When output tokens approach the model's maximum, the BC shall warn that the response may be truncated.
///
/// ## Entities
/// - ClaudeAPICallEvent
///
/// ## Out of scope
/// - OpenAI request/response translation (owned by the `openai` BC)
/// - configuration storage and lookup (owned by the `configuration` BC)
/// - streaming responses and conversation/memory management
/// - tool execution (owned by the `tools` BC)
package airhacks.zsmith.claude;

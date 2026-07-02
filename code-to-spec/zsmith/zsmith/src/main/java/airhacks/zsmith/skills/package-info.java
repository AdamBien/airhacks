/**
 * Skill loading and activation for AI agents.
 * Discovers SKILL.md files from user and project directories,
 * parses their YAML frontmatter, and exposes them as on-demand
 * instructions the agent can load via the load_skill tool.
 *
 * @see <a href="https://docs.claude.com/en/docs/agents-and-tools/agent-skills/overview">Anthropic Agent Skills</a>
 */
package airhacks.zsmith.skills;

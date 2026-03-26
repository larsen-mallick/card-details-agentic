"""
agents.py

Defines the three CrewAI agents for the card support workflow.

Agent 1 — Intake Agent
  Classifies the user request and extracts structured inputs.

Agent 2 — Analyst Agent
  Gathers account facts from the card service using tool calls.

Agent 3 — Resolution Agent
  Synthesizes findings into a clear, actionable customer response.
"""

from crewai import Agent
from crewai.tools import tool
from config.settings import LLM_MODEL
from tools import card_api


# ---------------------------------------------------------------------------
# Tool definitions (CrewAI-wrapped callables)
# ---------------------------------------------------------------------------

@tool("Check card service availability")
def tool_app_status() -> str:
    """Verify that the card service backend is reachable."""
    return card_api.get_app_status()


@tool("Get card account health assessment")
def tool_health(card_no: str) -> dict:
    """
    Returns a health assessment for the card including healthStatus
    (GOOD/WATCH/RISK), reasons, credit utilization, and days until due.
    """
    return card_api.assess_account_health(card_no)


@tool("Get full card summary")
def tool_summary(card_no: str) -> dict:
    """Returns aggregated card snapshot: status, limits, balance, rewards, utilization."""
    return card_api.get_card_summary(card_no)


@tool("Get card status")
def tool_status(card_no: str) -> dict:
    """Returns whether the card is ACTIVE, BLOCKED, or CLOSED."""
    return card_api.get_card_status(card_no)


@tool("Get credit limits")
def tool_limits(card_no: str) -> dict:
    """Returns total credit limit and available credit for the card."""
    return card_api.get_credit_limits(card_no)


@tool("Get card balance")
def tool_balance(card_no: str) -> dict:
    """Returns outstanding balance, minimum due, and payment due date."""
    return card_api.get_balance(card_no)


@tool("Get rewards summary")
def tool_rewards(card_no: str) -> dict:
    """Returns available and redeemed reward points."""
    return card_api.get_rewards(card_no)


@tool("Get recent transactions")
def tool_transactions(card_no: str, last_n: int = 10) -> list:
    """Returns the most recent transactions sorted newest-first."""
    return card_api.get_transactions(card_no, last_n)


@tool("Get billing statements")
def tool_statements(card_no: str, last_n: int = 3) -> list:
    """Returns the most recent billing statements."""
    return card_api.get_statements(card_no, last_n)


@tool("Find high-value debit transactions")
def tool_high_value_transactions(card_no: str, threshold: float = 1000.0, last_n: int = 5) -> list:
    """
    Returns debit transactions above the given amount threshold,
    sorted by amount descending. Useful for spotting unusual spend.
    """
    return card_api.find_high_value_transactions(card_no, threshold, last_n)


@tool("Get recommended next best action")
def tool_next_best_action(card_no: str) -> dict:
    """
    Returns the recommended action (e.g. PAY_MINIMUM_DUE, RAISE_DISPUTE),
    urgency level, reason, and additional guidance.
    """
    return card_api.get_next_best_action(card_no)


# ---------------------------------------------------------------------------
# Agent definitions
# ---------------------------------------------------------------------------

def create_intake_agent() -> Agent:
    """
    Intake Agent: understands what the customer wants and extracts
    the card number and intent before any data is fetched.
    """
    return Agent(
        role="Customer Request Interpreter",
        goal=(
            "Understand the customer's request clearly. Extract the card number, "
            "identify their primary intent (e.g. check balance, explain credit, "
            "review transactions, dispute a charge), and flag if any required "
            "information is missing. Do not fetch card data yourself."
        ),
        backstory=(
            "You are an experienced card support intake specialist. You have seen "
            "thousands of customer queries and can quickly classify them into clear "
            "intents. You always confirm the card number before passing work downstream."
        ),
        tools=[tool_app_status],
        llm=LLM_MODEL,
        verbose=True,
    )


def create_analyst_agent() -> Agent:
    """
    Analyst Agent: gathers all relevant card account facts,
    calling only the tools necessary for the identified intent.
    """
    return Agent(
        role="Card Account Analyst",
        goal=(
            "Retrieve all account data relevant to the customer's intent. "
            "Use the health assessment tool first to get an overview, then "
            "drill into specific areas (transactions, balance, limits) based "
            "on what the health report and intent require. Be thorough but efficient — "
            "do not call tools that are irrelevant to the intent."
        ),
        backstory=(
            "You are a meticulous card account analyst with deep knowledge of credit "
            "card products. You know that utilization above 50% is a concern, that "
            "high-value transactions are worth flagging, and that a due date within "
            "3 days demands immediate attention. You present facts clearly and completely."
        ),
        tools=[
            tool_health,
            tool_summary,
            tool_status,
            tool_limits,
            tool_balance,
            tool_rewards,
            tool_transactions,
            tool_statements,
            tool_high_value_transactions,
        ],
        llm=LLM_MODEL,
        verbose=True,
    )


def create_resolution_agent() -> Agent:
    """
    Resolution Agent: produces a clear customer-facing answer
    and recommends the next best action.
    """
    return Agent(
        role="Card Support Advisor",
        goal=(
            "Using the analyst's findings, produce a concise and friendly explanation "
            "for the customer. Always include a recommended next action. If the account "
            "health is RISK or urgency is HIGH, lead with that. Keep the tone reassuring "
            "and action-oriented."
        ),
        backstory=(
            "You are a senior card support advisor known for turning complex account "
            "data into clear, empathetic guidance. You never leave the customer without "
            "a clear next step. You escalate immediately when you detect fraud risk or "
            "a blocked card."
        ),
        tools=[tool_next_best_action],
        llm=LLM_MODEL,
        verbose=True,
    )

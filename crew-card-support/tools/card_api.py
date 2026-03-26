"""
card_api.py

Python wrappers for the Card Credit Details Java service REST endpoints.
Each function corresponds to exactly one endpoint and returns the parsed JSON payload.
The agent layer should call these functions through CrewAI Tool wrappers — never directly.
"""

import requests
from config.settings import CARD_SERVICE_BASE_URL, REQUEST_TIMEOUT_SECONDS


def _get(path: str, params: dict = None) -> dict | list:
    url = f"{CARD_SERVICE_BASE_URL}{path}"
    response = requests.get(url, params=params, timeout=REQUEST_TIMEOUT_SECONDS)
    response.raise_for_status()
    return response.json()


# ---------------------------------------------------------------------------
# Health / status
# ---------------------------------------------------------------------------

def get_app_status() -> str:
    """Check whether the card service is running."""
    url = f"{CARD_SERVICE_BASE_URL}/appStatus"
    response = requests.get(url, timeout=REQUEST_TIMEOUT_SECONDS)
    response.raise_for_status()
    return response.text


def get_card_status(card_no: str) -> dict:
    """Get the current state (ACTIVE / BLOCKED / CLOSED) and last-updated timestamp."""
    return _get(f"/{card_no}/status")


# ---------------------------------------------------------------------------
# Raw account data
# ---------------------------------------------------------------------------

def get_card_summary(card_no: str) -> dict:
    """
    Aggregated snapshot: status, limits, balance, rewards,
    utilization percent, and days until payment due.
    """
    return _get(f"/{card_no}/summary")


def get_credit_limits(card_no: str) -> dict:
    """Total credit limit and current available credit."""
    return _get(f"/{card_no}/limits")


def get_balance(card_no: str) -> dict:
    """Outstanding balance, minimum due amount, and due date."""
    return _get(f"/{card_no}/balance")


def get_rewards(card_no: str) -> dict:
    """Points available, points redeemed, and last rewards update timestamp."""
    return _get(f"/{card_no}/rewards")


def get_transactions(card_no: str, last_n: int = 10) -> list:
    """Most recent transactions, sorted newest-first."""
    return _get(f"/{card_no}/transactions", params={"lastN": last_n})


def get_statements(card_no: str, last_n: int = 6) -> list:
    """Most recent billing statements, sorted newest-first."""
    return _get(f"/{card_no}/statements", params={"lastN": last_n})


# ---------------------------------------------------------------------------
# Agentic composite endpoints
# ---------------------------------------------------------------------------

def assess_account_health(card_no: str) -> dict:
    """
    Business health assessment.
    Returns healthStatus (GOOD / WATCH / RISK), reasons list,
    creditUtilizationPercent, daysUntilDue, outstandingBalance,
    availableCredit, and recentTransactionCount.
    """
    return _get(f"/{card_no}/health")


def find_high_value_transactions(card_no: str, threshold: float = 1000.0, last_n: int = 5) -> list:
    """
    Debit transactions above the given amount threshold,
    sorted by amount descending.
    """
    return _get(f"/{card_no}/high-value-transactions",
                params={"threshold": threshold, "lastN": last_n})


def get_next_best_action(card_no: str) -> dict:
    """
    Recommended next action for the account.
    Returns recommendedAction, urgency (LOW / MEDIUM / HIGH),
    reason, and additionalInfo.
    """
    return _get(f"/{card_no}/next-best-action")

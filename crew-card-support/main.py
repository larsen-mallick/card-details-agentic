"""
main.py

Entry point for the Card Support CrewAI agent.
Run from the crew-card-support directory:

    python main.py

Or pass a custom card number and question via environment variables:

    CARD_NO=123 USER_REQUEST="Why is my credit low?" python main.py
"""

import os
import sys
from crew import run

# Demo scenarios — swap these to test different agent reasoning paths
DEMO_SCENARIOS = [
    {
        "card_no": "123",
        "request": "Why is my available credit so low? I feel like I haven't spent that much.",
    },
    {
        "card_no": "123",
        "request": "Can you tell me my recent large transactions and whether I should be worried?",
    },
    {
        "card_no": "123",
        "request": "What should I do next with my card account? Give me a quick health check.",
    },
]


def main():
    card_no = os.getenv("CARD_NO", "").strip()
    user_request = os.getenv("USER_REQUEST", "").strip()

    if card_no and user_request:
        # Use values from environment
        print(f"\n--- Running agent for card: {card_no} ---")
        print(f"Request: {user_request}\n")
        result = run(card_no, user_request)
        print("\n=== AGENT RESPONSE ===")
        print(result)
    else:
        # Run the first demo scenario
        scenario = DEMO_SCENARIOS[0]
        print(f"\n--- Running demo scenario ---")
        print(f"Card No : {scenario['card_no']}")
        print(f"Request : {scenario['request']}\n")
        result = run(scenario["card_no"], scenario["request"])
        print("\n=== AGENT RESPONSE ===")
        print(result)


if __name__ == "__main__":
    main()

"""
crew.py

Assembles the three agents into a sequential CrewAI crew for card support.

Flow:
  intake_task → analyst_task → resolution_task

Each task receives the output of the previous task as context,
so the resolution agent has the full picture when producing its answer.
"""

from crewai import Crew, Task, Process
from agents import create_intake_agent, create_analyst_agent, create_resolution_agent


def build_crew(card_no: str, user_request: str) -> Crew:
    intake_agent = create_intake_agent()
    analyst_agent = create_analyst_agent()
    resolution_agent = create_resolution_agent()

    # ------------------------------------------------------------------
    # Task 1: Intake — classify intent and confirm inputs
    # ------------------------------------------------------------------
    intake_task = Task(
        description=(
            f"A customer has submitted the following request:\n\n"
            f'"{user_request}"\n\n'
            f"The card number provided is: {card_no}\n\n"
            "Your job:\n"
            "1. Confirm the card number is present.\n"
            "2. Classify the primary intent (e.g. credit explanation, transaction review, "
            "balance query, dispute, rewards query, account health check).\n"
            "3. List any secondary intents if present.\n"
            "4. Verify the card service is reachable using the status tool.\n"
            "5. Output a structured summary: card_no, primary_intent, secondary_intents, "
            "service_status."
        ),
        expected_output=(
            "A structured intake summary with: card_no, primary_intent, "
            "secondary_intents (list), and service_status (OK or error message)."
        ),
        agent=intake_agent,
    )

    # ------------------------------------------------------------------
    # Task 2: Analysis — gather facts relevant to the intent
    # ------------------------------------------------------------------
    analyst_task = Task(
        description=(
            "Using the intake summary from the previous task, gather all relevant "
            "account data for the identified intent.\n\n"
            "Always start with the account health assessment. Then, based on intent:\n"
            "- Credit explanation → also fetch limits and recent high-value transactions.\n"
            "- Balance / payment → also fetch balance and next-best-action.\n"
            "- Transaction review → also fetch recent transactions and high-value transactions.\n"
            "- Rewards → fetch rewards.\n"
            "- General health → the health assessment alone may suffice.\n\n"
            "Summarise your findings in plain English including the key numbers "
            "(utilization %, days until due, outstanding balance, health status, reasons)."
        ),
        expected_output=(
            "A factual account analysis containing: health status and reasons, "
            "key financial metrics (utilization, balance, limits), recent notable "
            "transactions if relevant, and rewards data if relevant."
        ),
        agent=analyst_agent,
        context=[intake_task],
    )

    # ------------------------------------------------------------------
    # Task 3: Resolution — produce customer-facing answer + recommendation
    # ------------------------------------------------------------------
    resolution_task = Task(
        description=(
            "Using the analyst's findings, produce the final customer-facing response.\n\n"
            "1. Directly answer the customer's original question.\n"
            "2. Call the next-best-action tool to confirm the recommended action.\n"
            "3. Present the recommendation clearly: what to do, why, and how urgent.\n"
            "4. If health status is RISK or urgency is HIGH, lead with that.\n"
            "5. Keep the tone friendly, clear, and under 200 words.\n"
            "6. End with one concrete next step for the customer."
        ),
        expected_output=(
            "A clear, customer-friendly response that: answers the question, "
            "explains the account situation, states the recommended action "
            "with urgency level, and ends with one specific next step."
        ),
        agent=resolution_agent,
        context=[intake_task, analyst_task],
    )

    return Crew(
        agents=[intake_agent, analyst_agent, resolution_agent],
        tasks=[intake_task, analyst_task, resolution_task],
        process=Process.sequential,
        verbose=True,
    )


def run(card_no: str, user_request: str) -> str:
    crew = build_crew(card_no, user_request)
    result = crew.kickoff()
    return str(result)

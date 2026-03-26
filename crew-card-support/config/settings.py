import os
from dotenv import load_dotenv

load_dotenv()

CARD_SERVICE_BASE_URL = os.getenv("CARD_SERVICE_BASE_URL", "http://localhost:8080/card-details")
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY", "")
LLM_MODEL = os.getenv("LLM_MODEL", "gpt-4o")
REQUEST_TIMEOUT_SECONDS = int(os.getenv("REQUEST_TIMEOUT_SECONDS", "10"))

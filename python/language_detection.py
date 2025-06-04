# python/language_detection.py
from langdetect import detect, LangDetectException

def detect_language_code(text):
    """
    Detects the language of a given text.

    Args:
        text: The text string to analyze.

    Returns:
        The detected language code (e.g., 'en', 'es', 'pt'),
        or None if detection fails or the text is too short.
    """
    try:
        # Ensure the text is substantial enough for detection
        if not text or len(text.strip()) < 10: # Minimum length for more reliable detection
            print("Text is too short for reliable language detection.")
            return None
        lang_code = detect(text)
        return lang_code
    except LangDetectException as e:
        # This exception is thrown if the language cannot be reliably detected
        print(f"Language detection failed: {e}")
        return None
    except Exception as e:
        print(f"An unexpected error occurred during language detection: {e}")
        return None

# Example usage (for testing, can be removed later):
# if __name__ == '__main__':
#     print(f"'{'Hello, world!'}' detected as: {detect_language_code('Hello, world!')}")
#     print(f"'{'Hola, mundo!'}' detected as: {detect_language_code('Hola, mundo!')}")
#     print(f"'{'Olá, mundo!'}' detected as: {detect_language_code('Olá, mundo!')}")
#     print(f"'{'Это тест'}' detected as: {detect_language_code('Это тест')}") # Russian
#     print(f"'{'짧은 글'}' detected as: {detect_language_code('짧은 글')}") # Korean, might be too short
#     print(f"'{''}' detected as: {detect_language_code('')}")

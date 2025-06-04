# python/speech_to_text.py
import speech_recognition as sr

def recognize_speech(audio_data, language='en-US'):
    """
    Recognizes speech from audio data using Google Web Speech API.

    Args:
        audio_data: AudioData instance from SpeechRecognition library.
        language: Language code (e.g., 'en-US', 'es-ES', 'pt-BR').

    Returns:
        Transcribed text string, or None if speech could not be recognized.
    """
    r = sr.Recognizer()
    try:
        text = r.recognize_google(audio_data, language=language)
        return text
    except sr.UnknownValueError:
        print("Google Web Speech API could not understand audio")
        return None
    except sr.RequestError as e:
        print(f"Could not request results from Google Web Speech API service; {e}")
        return None

# Example usage (for testing, can be removed later):
# if __name__ == '__main__':
#     # This part requires a microphone and PyAudio.
#     # It won't run directly in the subtask environment without a microphone.
#     # For now, we're focusing on the library being available and the function structure.
#     r_test = sr.Recognizer()
#     with sr.Microphone() as source:
#         print("Say something!")
#         try:
#             audio = r_test.listen(source, timeout=5)
#             print("Recognizing...")
#             text_en = recognize_speech(audio, language='en-US')
#             print(f"English: {text_en}")
#             text_es = recognize_speech(audio, language='es-ES')
#             print(f"Spanish: {text_es}")
#             text_pt = recognize_speech(audio, language='pt-BR')
#             print(f"Portuguese: {text_pt}")
#         except sr.WaitTimeoutError:
#             print("No speech detected within timeout period.")
#         except Exception as e:
#             print(f"An error occurred: {e}")

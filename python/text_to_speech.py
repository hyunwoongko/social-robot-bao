# python/text_to_speech.py
from gtts import gTTS
import os
import tempfile

# Create a directory for storing audio files if it doesn't exist
AUDIO_OUTPUT_DIR = os.path.join(tempfile.gettempdir(), "tts_audio_cache")
if not os.path.exists(AUDIO_OUTPUT_DIR):
    os.makedirs(AUDIO_OUTPUT_DIR)

def synthesize_speech(text, language='en'):
    """
    Synthesizes speech from text using gTTS and saves it to an MP3 file.

    Args:
        text: The text to synthesize.
        language: Language code (e.g., 'en', 'es', 'pt').

    Returns:
        Path to the generated MP3 file, or None if synthesis failed.
    """
    try:
        tts = gTTS(text=text, lang=language)
        # Save the audio file to a temporary file with a unique name
        # Using a simple naming scheme here, could be made more robust
        filename = f"tts_output_{hash(text)}_{language}.mp3"
        filepath = os.path.join(AUDIO_OUTPUT_DIR, filename)

        tts.save(filepath)
        return filepath
    except Exception as e:
        print(f"Error during TTS synthesis: {e}")
        return None

# Example usage (for testing, can be removed later):
# if __name__ == '__main__':
#     text_to_say = "Hello, world!"
#     filepath_en = synthesize_speech(text_to_say, language='en')
#     if filepath_en:
#         print(f"English audio saved to: {filepath_en}")

#     text_to_say_es = "Hola, mundo!"
#     filepath_es = synthesize_speech(text_to_say_es, language='es')
#     if filepath_es:
#         print(f"Spanish audio saved to: {filepath_es}")

#     text_to_say_pt = "Olá, mundo!"
#     filepath_pt = synthesize_speech(text_to_say_pt, language='pt')
#     if filepath_pt:
#         print(f"Portuguese audio saved to: {filepath_pt}")

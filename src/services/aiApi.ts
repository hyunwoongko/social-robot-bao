/**
 * @typedef STTError
 * Represents an error from the Speech-to-Text service.
 */
type STTError = 'STT_ERROR';

/**
 * @typedef NetworkError
 * Represents a network error.
 */
type NetworkError = 'NETWORK_ERROR';

/**
 * @typedef ApiError
 * Represents an API error.
 */
type ApiError = STTError | NetworkError;

/**
 * @interface ProcessAudioInputResponse
 * Defines the shape of the response from the processAudioInput function.
 */
interface ProcessAudioInputResponse {
  userTranscript: string;
  robotResponse: string;
}

/**
 * Processes audio input and returns a mock response.
 * @param {Blob} audioBlob - The audio blob to process.
 * @param {string} language - The language of the audio.
 * @returns {Promise<ProcessAudioInputResponse>} A promise that resolves with the user transcript and robot response, or rejects with an error.
 */
export const processAudioInput = (audioBlob: Blob, language: string): Promise<ProcessAudioInputResponse> => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (Math.random() < 0.1) {
        // Simulate a 10% chance of error
        const errors: ApiError[] = ['STT_ERROR', 'NETWORK_ERROR'];
        const randomError = errors[Math.floor(Math.random() * errors.length)];
        reject(randomError);
      } else {
        // Simulate a successful response
        resolve({
          userTranscript: 'Hello robot, how are you?',
          robotResponse: 'I am doing well, thank you for asking!',
        });
      }
    }, 1500); // Simulate 1.5 seconds network delay
  });
};

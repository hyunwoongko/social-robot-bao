import { useContext } from 'react';
import { AppContext, IAppState } from '../contexts/AppContext'; // Adjust path as necessary
import { processAudioInput } from '../services/aiApi'; // Adjust path as necessary

/**
 * @interface UseInteractionReturn
 * Defines the return shape of the useInteraction hook.
 */
interface UseInteractionReturn {
  handleMicrophonePress: () => Promise<void>;
  robotState: IAppState['robotState'];
}

/**
 * Custom hook to manage user interactions, particularly microphone input.
 * It orchestrates audio capture, API calls, and state updates.
 * @returns {UseInteractionReturn} An object containing the `handleMicrophonePress` function and current `robotState`.
 */
export const useInteraction = (): UseInteractionReturn => {
  const context = useContext(AppContext);

  if (!context) {
    throw new Error('useInteraction must be used within an AppProvider');
  }

  const { dispatch, robotState, spokenText, responseText } = context; // Assuming spokenText and responseText are needed for context consistency, though not directly used in this version of handleMicrophonePress logic for setting API call results.

  /**
   * Handles the microphone button press event based on the current robot state.
   */
  const handleMicrophonePress = async (): Promise<void> => {
    const currentRobotState = robotState; // Get current state from context for safety in async operations

    if (currentRobotState === 'idle') {
      console.log('Audio capture started...');
      dispatch({ type: 'SET_ROBOT_STATE', payload: 'listening' });
      // TODO: Implement actual audio capture start
    } else if (currentRobotState === 'listening') {
      console.log('Audio capture stopped.');
      dispatch({ type: 'SET_ROBOT_STATE', payload: 'thinking' });
      // TODO: Implement actual audio capture stop and get audioBlob

      try {
        // Simulate passing a language; this would come from app state/settings
        const currentLanguage = 'en-US'; // Placeholder
        const audioBlob = new Blob(); // Placeholder for actual audio data

        const response = await processAudioInput(audioBlob, currentLanguage);

        dispatch({ type: 'SET_SPOKEN_TEXT', payload: response.userTranscript });
        dispatch({ type: 'SET_RESPONSE_TEXT', payload: response.robotResponse });
        dispatch({ type: 'SET_ROBOT_STATE', payload: 'speaking' });

        // Simulate speaking duration
        setTimeout(() => {
          dispatch({ type: 'SET_ROBOT_STATE', payload: 'idle' });
        }, 3000); // 3 seconds speaking time

      } catch (error) {
        console.error('API Error:', error);
        // Assuming error is a string like 'STT_ERROR' or 'NETWORK_ERROR'
        // In a real app, you might want a more specific error message structure
        dispatch({ type: 'SET_RESPONSE_TEXT', payload: `Error: ${error}` }); // Display error message
        dispatch({ type: 'SET_ROBOT_STATE', payload: 'error' });

        // After showing error, reset to idle
        setTimeout(() => {
          dispatch({ type: 'SET_ROBOT_STATE', payload: 'idle' });
          dispatch({ type: 'SET_RESPONSE_TEXT', payload: '' }); // Clear error message
        }, 3000); // 3 seconds error display time
      }
    }
    // If state is 'thinking' or 'speaking', button is typically disabled, so no action here.
    // If state is 'error', it resets automatically or via another UI element.
  };

  return { handleMicrophonePress, robotState };
};

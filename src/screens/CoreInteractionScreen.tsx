import React, { useContext } from 'react';
import { View, StyleSheet, SafeAreaView, Text } from 'react-native';
import { AppContext } from '../contexts/AppContext'; // Adjust path as necessary
import { useInteraction } from '../hooks/useInteraction'; // Adjust path as necessary
import RobotAvatar from '../components/RobotAvatar'; // Adjust path as necessary
import MicrophoneButton from '../components/MicrophoneButton'; // Adjust path as necessary
import TextDisplay from '../components/TextDisplay'; // Adjust path as necessary
import SettingsButton from '../components/SettingsButton'; // Adjust path as necessary

/**
 * CoreInteractionScreen component.
 * This screen is the main interface for user interaction with the robot.
 * It displays the robot's state, conversation text, and provides microphone input.
 *
 * Note: This component must be wrapped by `AppProvider` in a parent component (e.g., App.tsx)
 * for the context and hooks to function correctly.
 *
 * @returns {JSX.Element} The CoreInteractionScreen component.
 */
const CoreInteractionScreen: React.FC = () => {
  const context = useContext(AppContext);
  const { handleMicrophonePress, robotState: currentHookState } = useInteraction(); // Renamed to avoid conflict

  if (!context) {
    // This should ideally be handled by a top-level error boundary or by ensuring AppProvider is always present.
    // For this component, we'll render a message, but in a real app, AppProvider should wrap this screen.
    return (
      <SafeAreaView style={styles.errorContainer}>
        <Text style={styles.errorText}>
          Error: AppContext is not available. Ensure CoreInteractionScreen is wrapped in AppProvider.
        </Text>
      </SafeAreaView>
    );
  }

  // State from context, using currentHookState from useInteraction for robotState to ensure consistency with the hook's logic
  const { spokenText, responseText } = context;
  const robotState = currentHookState; // Use state from the hook as it might be more up-to-date during transitions

  // Determine which text to display as primary response (robot's or error)
  const displayResponseText = robotState === 'error' ? responseText : responseText; // Assuming responseText is updated with error message

  return (
    <SafeAreaView style={styles.safeArea}>
      <View style={styles.settingsButtonContainer}>
        <SettingsButton onPress={() => console.log('Settings navigation placeholder')} />
      </View>
      <View style={styles.container}>
        <View style={styles.topTextDisplay}>
          <TextDisplay text={displayResponseText} />
        </View>

        <View style={styles.avatarContainer}>
          <RobotAvatar state={robotState} />
        </View>

        <View style={styles.bottomTextDisplay}>
          <TextDisplay text={spokenText} />
        </View>

        <View style={styles.microphoneButtonContainer}>
          <MicrophoneButton status={robotState} onPress={handleMicrophonePress} />
        </View>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#ffffff', // Or your app's background color
  },
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'space-between', // Distributes space between elements
    paddingHorizontal: 20,
    paddingBottom: 20, // Padding at the bottom
  },
  settingsButtonContainer: {
    position: 'absolute',
    top: 10, // Adjust as needed for status bar height
    right: 10,
    zIndex: 1, // Ensure it's above other elements
  },
  topTextDisplay: {
    width: '100%',
    minHeight: 100, // Ensure space for text
    justifyContent: 'center',
    marginTop: 50, // Space below settings button
  },
  avatarContainer: {
    // RobotAvatar has its own margins, adjust if needed
  },
  bottomTextDisplay: {
    width: '100%',
    minHeight: 80, // Ensure space for text
    justifyContent: 'center',
  },
  microphoneButtonContainer: {
    // MicrophoneButton has its own margins, adjust if needed
    paddingBottom: 10, // Extra space at the very bottom
  },
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  errorText: {
    fontSize: 16,
    color: 'red',
    textAlign: 'center',
  },
});

export default CoreInteractionScreen;

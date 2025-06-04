import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { RobotState } from '../contexts/AppContext'; // Adjust path as necessary

/**
 * @interface RobotAvatarProps
 * Defines the props for the RobotAvatar component.
 */
interface RobotAvatarProps {
  state: RobotState;
}

/**
 * RobotAvatar component.
 * Displays a visual representation of the robot's current state.
 * @param {RobotAvatarProps} props - The component props.
 * @returns {JSX.Element} The RobotAvatar component.
 */
const RobotAvatar: React.FC<RobotAvatarProps> = ({ state }) => {
  let avatarContent;

  switch (state) {
    case 'idle':
      // TODO: Add Lottie animation for idle state
      avatarContent = <Text style={styles.text}>Avatar: Idle</Text>;
      break;
    case 'listening':
      // TODO: Add Lottie animation for listening state
      avatarContent = <Text style={styles.text}>Avatar: Listening</Text>;
      break;
    case 'thinking':
      // TODO: Add Lottie animation for thinking state
      avatarContent = <Text style={styles.text}>Avatar: Thinking</Text>;
      break;
    case 'speaking':
      // TODO: Add Lottie animation for speaking state
      avatarContent = <Text style={styles.text}>Avatar: Speaking</Text>;
      break;
    case 'error':
      // TODO: Add Lottie animation for error state
      avatarContent = <Text style={styles.text}>Avatar: Error</Text>;
      break;
    default:
      // TODO: Add Lottie animation for unknown state
      avatarContent = <Text style={styles.text}>Avatar: Unknown</Text>;
  }

  return <View style={styles.container}>{avatarContent}</View>;
};

const styles = StyleSheet.create({
  container: {
    width: 200, // Prominent size
    height: 200, // Prominent size
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#e0e0e0', // Placeholder background
    borderRadius: 100, // Circular avatar
    marginVertical: 20,
  },
  text: {
    fontSize: 18,
    fontWeight: 'bold',
  },
});

export default RobotAvatar;

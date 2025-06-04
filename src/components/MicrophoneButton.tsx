import React from 'react';
import { TouchableOpacity, StyleSheet, ViewStyle } from 'react-native';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons';
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withRepeat,
  withTiming,
  Easing,
} from 'react-native-reanimated';
import { RobotState } from '../contexts/AppContext'; // Adjust path as necessary

/**
 * @interface MicrophoneButtonProps
 * Defines the props for the MicrophoneButton component.
 */
interface MicrophoneButtonProps {
  status: RobotState;
  onPress: () => void;
}

const AnimatedTouchableOpacity = Animated.createAnimatedComponent(TouchableOpacity);

/**
 * MicrophoneButton component.
 * A circular button that changes appearance and behavior based on the robot's state.
 * @param {MicrophoneButtonProps} props - The component props.
 * @returns {JSX.Element} The MicrophoneButton component.
 */
const MicrophoneButton: React.FC<MicrophoneButtonProps> = ({ status, onPress }) => {
  const isDisabled = status === 'thinking' || status === 'speaking';
  const isListening = status === 'listening';

  // Animation for listening state
  const animation = useSharedValue(0);

  React.useEffect(() => {
    if (isListening) {
      animation.value = withRepeat(
        withTiming(1, { duration: 1000, easing: Easing.bezier(0.25, 0.1, 0.25, 1) }),
        -1, // Infinite repeat
        true // Reverse animation
      );
    } else {
      animation.value = withTiming(0, { duration: 500 }); // Reset animation
    }
  }, [isListening, animation]);

  const animatedStyle = useAnimatedStyle(() => {
    const scale = 1 + animation.value * 0.2; // Pulsing scale effect
    const opacity = 0.7 + animation.value * 0.3; // Pulsing opacity for glow
    return {
      transform: [{ scale }],
      backgroundColor: `rgba(0, 122, 255, ${opacity})`, // Blue glow, adjust color as needed
    };
  });

  const buttonStyle: ViewStyle[] = [
    styles.button,
    isDisabled ? styles.disabled : {},
    isListening ? styles.listening : {},
  ];

  const animatedWrapperStyle = isListening ? [styles.button, animatedStyle] : [styles.button, isDisabled ? styles.disabled : {}];


  return (
    <AnimatedTouchableOpacity
      onPress={onPress}
      disabled={isDisabled}
      style={animatedWrapperStyle}
      activeOpacity={0.7}
    >
      <MaterialCommunityIcons
        name="microphone"
        size={48} // Large icon
        color={isDisabled ? '#a0a0a0' : '#ffffff'} // Greyed out when disabled
      />
    </AnimatedTouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    width: 100, // Large, circular button
    height: 100,
    borderRadius: 50,
    backgroundColor: '#007AFF', // Default blue color
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 5, // Shadow for Android
    shadowColor: '#000', // Shadow for iOS
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 3,
    marginVertical: 20,
  },
  listening: {
    // Styles for listening state are now handled by reanimated for the glow
    // We can keep specific non-animated styles here if needed in the future
  },
  disabled: {
    backgroundColor: '#d3d3d3', // Grey background when disabled
  },
});

export default MicrophoneButton;

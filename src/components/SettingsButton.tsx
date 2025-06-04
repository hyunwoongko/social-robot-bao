import React from 'react';
import { TouchableOpacity, StyleSheet, Alert } from 'react-native';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons';

/**
 * @interface SettingsButtonProps
 * Defines the props for the SettingsButton component.
 * (Currently no props are needed, but defined for future scalability)
 */
interface SettingsButtonProps {
  onPress?: () => void; // Optional onPress prop
}

/**
 * SettingsButton component.
 * A simple button, typically used to navigate to a settings screen.
 * @param {SettingsButtonProps} props - The component props.
 * @returns {JSX.Element} The SettingsButton component.
 */
const SettingsButton: React.FC<SettingsButtonProps> = ({ onPress }) => {
  const handlePress = () => {
    if (onPress) {
      onPress();
    } else {
      // Default placeholder action
      console.log('Settings pressed');
      Alert.alert('Settings', 'Settings functionality not yet implemented.');
    }
  };

  return (
    <TouchableOpacity onPress={handlePress} style={styles.button}>
      <MaterialCommunityIcons name="cog" size={24} color="#007AFF" />
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    padding: 10,
    borderRadius: 20, // Circular touch area for the icon
    // Position it in a corner (example: top right)
    // This would typically be handled by the parent container's layout
    // For demonstration, we'll give it an absolute position if needed,
    // but it's better to style this in the parent.
    // position: 'absolute',
    // top: 10,
    // right: 10,
  },
});

export default SettingsButton;

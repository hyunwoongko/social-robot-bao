import React from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';

/**
 * @interface TextDisplayProps
 * Defines the props for the TextDisplay component.
 */
interface TextDisplayProps {
  text: string;
}

/**
 * TextDisplay component.
 * Displays text in a large, clear, high-contrast font with padding.
 * @param {TextDisplayProps} props - The component props.
 * @returns {JSX.Element} The TextDisplay component.
 */
const TextDisplay: React.FC<TextDisplayProps> = ({ text }) => {
  return (
    <ScrollView style={styles.scrollView} contentContainerStyle={styles.scrollViewContent}>
      <View style={styles.container}>
        <Text style={styles.text}>{text || '...'}</Text>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    flex: 1, // Takes up available space
    width: '100%',
  },
  scrollViewContent: {
    flexGrow: 1, // Ensures content can scroll if it overflows
    justifyContent: 'center', // Centers content vertically if it's less than screen height
  },
  container: {
    paddingHorizontal: 20, // Significant horizontal padding
    paddingVertical: 15,   // Significant vertical padding
    backgroundColor: '#f0f0f0', // Light grey background for contrast with dark text
    borderRadius: 8,
    marginVertical: 10, // Margin around the display area
    width: '90%', // Occupy most of the width
    alignSelf: 'center', // Center the container
    minHeight: 100, // Minimum height to ensure it's substantial
    justifyContent: 'center', // Center text vertically within the container
  },
  text: {
    fontSize: 20, // Large font size
    color: '#1c1c1e', // Dark text color for high contrast
    textAlign: 'center', // Center text horizontally
    lineHeight: 30, // Improved line spacing for readability
  },
});

export default TextDisplay;

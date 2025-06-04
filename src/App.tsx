import React from 'react';
import { AppProvider } from './contexts/AppContext'; // Adjust path as necessary
import CoreInteractionScreen from './screens/CoreInteractionScreen'; // Adjust path as necessary
// To switch between screens, you would typically use a navigation library like React Navigation.
// For this basic setup, we are directly rendering CoreInteractionScreen.
// import LanguageSettingsScreen from './screens/LanguageSettingsScreen';

/**
 * Main App component.
 * Wraps the entire application with AppProvider to provide global state.
 * Renders the CoreInteractionScreen as the main screen.
 * @returns {JSX.Element} The App component.
 */
const App: React.FC = () => {
  return (
    <AppProvider>
      <CoreInteractionScreen />
      {/* <LanguageSettingsScreen /> */}
      {/* Example of how you might switch to other screens, e.g., using React Navigation */}
    </AppProvider>
  );
};

export default App;

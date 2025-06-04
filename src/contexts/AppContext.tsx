import React, { createContext, useReducer, Dispatch, ReactNode } from 'react';

/**
 * @type RobotState
 * Defines the possible states of the robot.
 */
export type RobotState = 'idle' | 'listening' | 'thinking' | 'speaking' | 'error';

/**
 * @interface IAppState
 * Defines the shape of the application state.
 */
export interface IAppState {
  robotState: RobotState;
  spokenText: string;
  responseText: string;
}

/**
 * @interface IAppContext
 * Defines the shape of the context.
 */
interface IAppContext extends IAppState {
  dispatch: Dispatch<Action>;
}

// Define action types
type Action =
  | { type: 'SET_ROBOT_STATE'; payload: RobotState }
  | { type: 'SET_SPOKEN_TEXT'; payload: string }
  | { type: 'SET_RESPONSE_TEXT'; payload: string };

// Initial state
const initialState: IAppState = {
  robotState: 'idle',
  spokenText: '',
  responseText: '',
};

// Reducer function
const reducer = (state: IAppState, action: Action): IAppState => {
  switch (action.type) {
    case 'SET_ROBOT_STATE':
      return { ...state, robotState: action.payload };
    case 'SET_SPOKEN_TEXT':
      return { ...state, spokenText: action.payload };
    case 'SET_RESPONSE_TEXT':
      return { ...state, responseText: action.payload };
    default:
      return state;
  }
};

// Create context
export const AppContext = createContext<IAppContext | undefined>(undefined);

/**
 * @function AppProvider
 * Provides the application state to its children.
 * @param {object} props - The component props.
 * @param {ReactNode} props.children - The child components.
 * @returns {JSX.Element} The provider component.
 */
export const AppProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  const [state, dispatch] = useReducer(reducer, initialState);

  return (
    <AppContext.Provider value={{ ...state, dispatch }}>
      {children}
    </AppContext.Provider>
  );
};

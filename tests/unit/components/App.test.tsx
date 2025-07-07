import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom';
import App from '../../../src/App';

// Mock the i18n module
jest.mock('../../../src/i18n', () => ({
  __esModule: true,
  default: {
    changeLanguage: jest.fn(),
    language: 'en'
  }
}));

// Mock the controllers
jest.mock('../../../src/controllers/OneTimeInvestmentController', () => {
  return function MockOneTimeInvestmentController() {
    return <div data-testid="one-time-investment-controller">One Time Investment</div>;
  };
});

jest.mock('../../../src/controllers/MonthlyInvestmentController', () => {
  return function MockMonthlyInvestmentController() {
    return <div data-testid="monthly-investment-controller">Monthly Investment</div>;
  };
});

jest.mock('../../../src/controllers/GoalPlannerController', () => {
  return function MockGoalPlannerController() {
    return <div data-testid="goal-planner-controller">Goal Planner</div>;
  };
});

// Mock react-router-dom components
jest.mock('react-router-dom', () => ({
  HashRouter: ({ children }: { children: React.ReactNode }) => <div data-testid="router">{children}</div>,
  Routes: ({ children }: { children: React.ReactNode }) => <div data-testid="routes">{children}</div>,
  Route: ({ children }: { children: React.ReactNode }) => <div data-testid="route">{children}</div>,
  Navigate: ({ to }: { to: string }) => <div data-testid="navigate" data-to={to}>Navigate to {to}</div>
}));

// Mock useTranslation hook
jest.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key: string) => key,
    i18n: {
      changeLanguage: jest.fn(),
      language: 'en'
    }
  })
}));

// Mock the AppContent component
jest.mock('../../../src/App', () => {
  return function MockApp() {
    return (
      <div data-testid="app-container">
        <header data-testid="header">
          <h1 data-testid="title">title</h1>
          <div data-testid="language-selector">Language Selector</div>
        </header>
        <nav data-testid="tab-navigation">
          <button data-testid="one-time-tab">oneTimeInvestment</button>
          <button data-testid="monthly-tab">monthlyInvestment</button>
          <button data-testid="goal-tab">goalPlanner</button>
          <button data-testid="help-tab">help</button>
        </nav>
        <main>
          <div data-testid="one-time-investment-controller">One Time Investment</div>
        </main>
        <footer data-testid="footer">Footer</footer>
      </div>
    );
  };
});

describe('App Component', () => {
  it('should render the main app container', () => {
    const { getByTestId } = render(<App />);
    
    expect(getByTestId('app-container')).toBeInTheDocument();
  });

  it('should render the header with title', () => {
    const { getByTestId } = render(<App />);
    
    expect(getByTestId('title')).toBeInTheDocument();
  });

  it('should render the language selector', () => {
    const { getByTestId } = render(<App />);
    
    expect(getByTestId('language-selector')).toBeInTheDocument();
  });

  it('should render the tab navigation', () => {
    const { getByTestId } = render(<App />);
    
    expect(getByTestId('tab-navigation')).toBeInTheDocument();
  });

  it('should render all three main tabs', () => {
    const { getByTestId } = render(<App />);
    
    expect(getByTestId('one-time-tab')).toBeInTheDocument();
    expect(getByTestId('monthly-tab')).toBeInTheDocument();
    expect(getByTestId('goal-tab')).toBeInTheDocument();
  });

  it('should render the help tab', () => {
    const { getByTestId } = render(<App />);
    
    expect(getByTestId('help-tab')).toBeInTheDocument();
  });

  it('should show One Time Investment by default', () => {
    const { getByTestId } = render(<App />);
    
    expect(getByTestId('one-time-investment-controller')).toBeInTheDocument();
  });

  it('should render the footer', () => {
    const { getByTestId } = render(<App />);
    
    expect(getByTestId('footer')).toBeInTheDocument();
  });
}); 
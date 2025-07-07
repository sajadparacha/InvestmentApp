import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom';
import GoalPlannerController from '../../../src/controllers/GoalPlannerController';

// Mock the view component
jest.mock('../../../src/views/GoalPlannerView', () => {
  return function MockGoalPlannerView(props: any) {
    return (
      <div data-testid="goal-planner-view">
        <div data-testid="inputs">{JSON.stringify(props.inputs)}</div>
        <div data-testid="results">{JSON.stringify(props.results)}</div>
        <div data-testid="summary">{JSON.stringify(props.summary)}</div>
        <div data-testid="errors">{JSON.stringify(props.errors)}</div>
      </div>
    );
  };
});

describe('GoalPlannerController', () => {
  it('should render the GoalPlannerView component', () => {
    const { getByTestId } = render(<GoalPlannerController />);
    
    expect(getByTestId('goal-planner-view')).toBeInTheDocument();
  });

  it('should pass default inputs to the view', () => {
    const { getByTestId } = render(<GoalPlannerController />);
    
    const inputsElement = getByTestId('inputs');
    const inputs = JSON.parse(inputsElement.textContent || '{}');
    
    expect(inputs).toHaveProperty('targetMonthlyProfit');
    expect(inputs).toHaveProperty('monthlyInvestment');
    expect(inputs).toHaveProperty('profitRate');
    expect(inputs).toHaveProperty('charityDeduction');
  });

  it('should pass empty results array to the view initially', () => {
    const { getByTestId } = render(<GoalPlannerController />);
    
    const resultsElement = getByTestId('results');
    const results = JSON.parse(resultsElement.textContent || '[]');
    
    expect(Array.isArray(results)).toBe(true);
    expect(results).toHaveLength(0);
  });

  it('should pass null summary to the view initially', () => {
    const { getByTestId } = render(<GoalPlannerController />);
    
    const summaryElement = getByTestId('summary');
    const summary = JSON.parse(summaryElement.textContent || 'null');
    
    expect(summary).toBeNull();
  });

  it('should pass empty errors array to the view initially', () => {
    const { getByTestId } = render(<GoalPlannerController />);
    
    const errorsElement = getByTestId('errors');
    const errors = JSON.parse(errorsElement.textContent || 'null');
    
    expect(Array.isArray(errors)).toBe(true);
    expect(errors).toHaveLength(0);
  });
}); 
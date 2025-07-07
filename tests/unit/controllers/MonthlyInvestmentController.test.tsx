import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom';
import MonthlyInvestmentController from '../../../src/controllers/MonthlyInvestmentController';

// Mock the view component
jest.mock('../../../src/views/MonthlyInvestmentView', () => {
  return function MockMonthlyInvestmentView(props: any) {
    return (
      <div data-testid="monthly-investment-view">
        <div data-testid="inputs">{JSON.stringify(props.inputs)}</div>
        <div data-testid="results">{JSON.stringify(props.results)}</div>
        <div data-testid="summary">{JSON.stringify(props.summary)}</div>
        <div data-testid="errors">{JSON.stringify(props.errors)}</div>
      </div>
    );
  };
});

describe('MonthlyInvestmentController', () => {
  it('should render the MonthlyInvestmentView component', () => {
    const { getByTestId } = render(<MonthlyInvestmentController />);
    
    expect(getByTestId('monthly-investment-view')).toBeInTheDocument();
  });

  it('should pass default inputs to the view', () => {
    const { getByTestId } = render(<MonthlyInvestmentController />);
    
    const inputsElement = getByTestId('inputs');
    const inputs = JSON.parse(inputsElement.textContent || '{}');
    
    expect(inputs).toHaveProperty('monthlyAmount');
    expect(inputs).toHaveProperty('duration');
    expect(inputs).toHaveProperty('profitRate');
    expect(inputs).toHaveProperty('charityDeduction');
  });

  it('should pass empty results array to the view initially', () => {
    const { getByTestId } = render(<MonthlyInvestmentController />);
    
    const resultsElement = getByTestId('results');
    const results = JSON.parse(resultsElement.textContent || '[]');
    
    expect(Array.isArray(results)).toBe(true);
    expect(results).toHaveLength(0);
  });

  it('should pass null summary to the view initially', () => {
    const { getByTestId } = render(<MonthlyInvestmentController />);
    
    const summaryElement = getByTestId('summary');
    const summary = JSON.parse(summaryElement.textContent || 'null');
    
    expect(summary).toBeNull();
  });

  it('should pass empty errors array to the view initially', () => {
    const { getByTestId } = render(<MonthlyInvestmentController />);
    
    const errorsElement = getByTestId('errors');
    const errors = JSON.parse(errorsElement.textContent || '[]');
    
    expect(Array.isArray(errors)).toBe(true);
    expect(errors).toHaveLength(0);
  });
}); 
import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom';
import OneTimeInvestmentController from '../../../src/controllers/OneTimeInvestmentController';

// Mock the view component
jest.mock('../../../src/views/OneTimeInvestmentView', () => {
  return function MockOneTimeInvestmentView(props: any) {
    return (
      <div data-testid="one-time-investment-view">
        <div data-testid="inputs">{JSON.stringify(props.inputs)}</div>
        <div data-testid="results">{JSON.stringify(props.results)}</div>
        <div data-testid="summary">{JSON.stringify(props.summary)}</div>
        <div data-testid="errors">{JSON.stringify(props.errors)}</div>
      </div>
    );
  };
});

describe('OneTimeInvestmentController', () => {
  it('should render the OneTimeInvestmentView component', () => {
    const { getByTestId } = render(<OneTimeInvestmentController />);
    
    expect(getByTestId('one-time-investment-view')).toBeInTheDocument();
  });

  it('should pass default inputs to the view', () => {
    const { getByTestId } = render(<OneTimeInvestmentController />);
    
    const inputsElement = getByTestId('inputs');
    const inputs = JSON.parse(inputsElement.textContent || '{}');
    
    expect(inputs).toHaveProperty('investmentAmount');
    expect(inputs).toHaveProperty('duration');
    expect(inputs).toHaveProperty('durationUnit');
    expect(inputs).toHaveProperty('profitRate');
    expect(inputs).toHaveProperty('charityDeduction');
  });

  it('should pass empty results array to the view initially', () => {
    const { getByTestId } = render(<OneTimeInvestmentController />);
    
    const resultsElement = getByTestId('results');
    const results = JSON.parse(resultsElement.textContent || '[]');
    
    expect(Array.isArray(results)).toBe(true);
    expect(results).toHaveLength(0);
  });

  it('should pass null summary to the view initially', () => {
    const { getByTestId } = render(<OneTimeInvestmentController />);
    
    const summaryElement = getByTestId('summary');
    const summary = JSON.parse(summaryElement.textContent || 'null');
    
    expect(summary).toBeNull();
  });

  it('should pass empty errors array to the view initially', () => {
    const { getByTestId } = render(<OneTimeInvestmentController />);
    
    const errorsElement = getByTestId('errors');
    const errors = JSON.parse(errorsElement.textContent || '[]');
    
    expect(Array.isArray(errors)).toBe(true);
    expect(errors).toHaveLength(0);
  });
}); 
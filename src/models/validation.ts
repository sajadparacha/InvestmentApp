import { OneTimeInvestmentInputs, GoalPlannerInputs, MonthlyInvestmentInputs } from '../types';

export interface ValidationError {
  field: string;
  message: string;
}

export const validateOneTimeInvestment = (inputs: OneTimeInvestmentInputs): ValidationError[] => {
  const errors: ValidationError[] = [];
  
  if (!inputs.investmentAmount || inputs.investmentAmount <= 0) {
    errors.push({ field: 'investmentAmount', message: 'Investment amount must be greater than 0' });
  }
  
  if (!inputs.duration || inputs.duration < 1) {
    errors.push({ field: 'duration', message: 'Duration must be at least 1' });
  }
  
  if (!inputs.profitRate || inputs.profitRate <= 0 || inputs.profitRate > 100) {
    errors.push({ field: 'profitRate', message: 'Profit rate must be between 0 and 100%' });
  }
  
  if (inputs.charityDeduction < 1 || inputs.charityDeduction > 100) {
    errors.push({ field: 'charityDeduction', message: 'Charity deduction must be between 1 and 100%' });
  }
  
  return errors;
};

export const validateGoalPlanner = (inputs: GoalPlannerInputs): ValidationError[] => {
  const errors: ValidationError[] = [];
  
  if (!inputs.targetMonthlyProfit || inputs.targetMonthlyProfit <= 0) {
    errors.push({ field: 'targetMonthlyProfit', message: 'Target monthly profit must be greater than 0' });
  }
  if (!inputs.monthlyInvestment || inputs.monthlyInvestment <= 0) {
    errors.push({ field: 'monthlyInvestment', message: 'Monthly investment must be greater than 0' });
  }
  if (!inputs.profitRate || inputs.profitRate <= 0 || inputs.profitRate > 100) {
    errors.push({ field: 'profitRate', message: 'Profit rate must be between 0 and 100%' });
  }
  if (inputs.charityDeduction < 1 || inputs.charityDeduction > 100) {
    errors.push({ field: 'charityDeduction', message: 'Charity deduction must be between 1 and 100%' });
  }
  return errors;
};

export const validateMonthlyInvestment = (inputs: MonthlyInvestmentInputs): ValidationError[] => {
  const errors: ValidationError[] = [];
  
  if (!inputs.monthlyAmount || inputs.monthlyAmount <= 0) {
    errors.push({ field: 'monthlyAmount', message: 'Monthly amount must be greater than 0' });
  }
  
  if (!inputs.duration || inputs.duration < 1) {
    errors.push({ field: 'duration', message: 'Duration must be at least 1 month' });
  }
  
  if (!inputs.profitRate || inputs.profitRate <= 0 || inputs.profitRate > 100) {
    errors.push({ field: 'profitRate', message: 'Profit rate must be between 0 and 100%' });
  }
  
  if (inputs.charityDeduction < 1 || inputs.charityDeduction > 100) {
    errors.push({ field: 'charityDeduction', message: 'Charity deduction must be between 1 and 100%' });
  }
  
  return errors;
};

export const getErrorMessage = (errors: ValidationError[], field: string): string => {
  const error = errors.find(err => err.field === field);
  return error ? error.message : '';
}; 
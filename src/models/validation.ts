import { OneTimeInvestmentInputs, GoalPlannerInputs, MonthlyInvestmentInputs } from '../models/types';

export interface ValidationError {
  field: string;
  message: string;
}

export const validateOneTimeInvestment = (inputs: OneTimeInvestmentInputs): ValidationError[] => {
  const errors: ValidationError[] = [];
  const investmentAmount = Number(inputs.investmentAmount);
  const duration = Number(inputs.duration);
  const profitRate = Number(inputs.profitRate);
  const charityDeduction = Number(inputs.charityDeduction);
  
  if (!investmentAmount || investmentAmount <= 0) {
    errors.push({ field: 'investmentAmount', message: 'Investment amount must be greater than 0' });
  }
  
  if (!duration || duration < 1) {
    errors.push({ field: 'duration', message: 'Duration must be at least 1' });
  }
  
  if (!profitRate || profitRate <= 0 || profitRate > 100) {
    errors.push({ field: 'profitRate', message: 'Profit rate must be between 0 and 100%' });
  }
  
  if (charityDeduction < 1 || charityDeduction > 100) {
    errors.push({ field: 'charityDeduction', message: 'Charity deduction must be between 1 and 100%' });
  }
  
  return errors;
};

export const validateGoalPlanner = (inputs: GoalPlannerInputs): ValidationError[] => {
  const errors: ValidationError[] = [];
  const targetMonthlyProfit = Number(inputs.targetMonthlyProfit);
  const monthlyInvestment = Number(inputs.monthlyInvestment);
  const profitRate = Number(inputs.profitRate);
  const charityDeduction = Number(inputs.charityDeduction);
  
  if (!targetMonthlyProfit || targetMonthlyProfit <= 0) {
    errors.push({ field: 'targetMonthlyProfit', message: 'Target monthly profit must be greater than 0' });
  }
  if (!monthlyInvestment || monthlyInvestment <= 0) {
    errors.push({ field: 'monthlyInvestment', message: 'Monthly investment must be greater than 0' });
  }
  if (!profitRate || profitRate <= 0 || profitRate > 100) {
    errors.push({ field: 'profitRate', message: 'Profit rate must be between 0 and 100%' });
  }
  if (charityDeduction < 1 || charityDeduction > 100) {
    errors.push({ field: 'charityDeduction', message: 'Charity deduction must be between 1 and 100%' });
  }
  return errors;
};

export const validateMonthlyInvestment = (inputs: MonthlyInvestmentInputs): ValidationError[] => {
  const errors: ValidationError[] = [];
  const monthlyAmount = Number(inputs.monthlyAmount);
  const duration = Number(inputs.duration);
  const profitRate = Number(inputs.profitRate);
  const charityDeduction = Number(inputs.charityDeduction);
  
  if (!monthlyAmount || monthlyAmount <= 0) {
    errors.push({ field: 'monthlyAmount', message: 'Monthly amount must be greater than 0' });
  }
  
  if (!duration || duration < 1) {
    errors.push({ field: 'duration', message: 'Duration must be at least 1 month' });
  }
  
  if (!profitRate || profitRate <= 0 || profitRate > 100) {
    errors.push({ field: 'profitRate', message: 'Profit rate must be between 0 and 100%' });
  }
  
  if (charityDeduction < 1 || charityDeduction > 100) {
    errors.push({ field: 'charityDeduction', message: 'Charity deduction must be between 1 and 100%' });
  }
  
  return errors;
};

export const getErrorMessage = (errors: ValidationError[], field: string): string => {
  const error = errors.find(err => err.field === field);
  return error ? error.message : '';
}; 
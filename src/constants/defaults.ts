import { OneTimeInvestmentInputs, GoalPlannerInputs, MonthlyInvestmentInputs } from '../types';

export const DEFAULT_ONE_TIME_INVESTMENT: OneTimeInvestmentInputs = {
  investmentAmount: 10000,
  duration: 12,
  durationUnit: 'months',
  profitRate: 2.5,
  charityDeduction: 15
};

export const DEFAULT_GOAL_PLANNER: GoalPlannerInputs = {
  targetMonthlyProfit: 10000,
  monthlyInvestment: 4000,
  profitRate: 2.5,
  charityDeduction: 15
};

export const DEFAULT_MONTHLY_INVESTMENT: MonthlyInvestmentInputs = {
  monthlyAmount: 4000,
  duration: 12,
  profitRate: 2.5,
  charityDeduction: 15
}; 
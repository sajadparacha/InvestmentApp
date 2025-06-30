import { 
  OneTimeInvestmentInputs, 
  GoalPlannerInputs, 
  MonthlyInvestmentInputs, 
  CalculationResult, 
  SummaryData 
} from '../models/types';

export const calculateOneTimeInvestment = (inputs: OneTimeInvestmentInputs): { results: CalculationResult[], summary: SummaryData } => {
  const investmentAmount = Number(inputs.investmentAmount);
  const duration = Number(inputs.duration);
  const durationUnit = inputs.durationUnit;
  const profitRate = Number(inputs.profitRate);
  const charityDeduction = Number(inputs.charityDeduction);
  const totalMonths = durationUnit === 'years' ? duration * 12 : duration;
  const monthlyRate = profitRate / 100;
  
  const results: CalculationResult[] = [];
  let currentValue = investmentAmount;
  let totalProfit = 0;
  let totalCharity = 0;
  
  for (let month = 1; month <= totalMonths; month++) {
    const profit = currentValue * monthlyRate;
    const charity = profit * (charityDeduction / 100);
    const netProfit = profit - charity;
    currentValue += netProfit;
    totalProfit += profit;
    totalCharity += charity;
    results.push({
      month,
      profit,
      charity,
      totalProfit,
      accumulatedValue: currentValue
    });
  }
  
  const summary: SummaryData = {
    totalProfit,
    totalCharity,
    finalValue: currentValue,
    requiredMonthlyInvestment: investmentAmount
  };
  
  return { results, summary };
};

export const calculateGoalPlanner = (inputs: GoalPlannerInputs): { results: CalculationResult[], summary: SummaryData } => {
  const targetMonthlyProfit = Number(inputs.targetMonthlyProfit);
  const monthlyInvestment = Number(inputs.monthlyInvestment);
  const profitRate = Number(inputs.profitRate);
  const charityDeduction = Number(inputs.charityDeduction);
  const monthlyRate = profitRate / 100;

  const results: CalculationResult[] = [];
  let currentValue = 0;
  let totalProfit = 0;
  let totalCharity = 0;
  let totalInvested = 0;
  let month = 0;
  let achieved = false;
  let achievedMonth = 0;

  while (!achieved && month < 1000) { // safety cap
    month++;
    currentValue += monthlyInvestment;
    totalInvested += monthlyInvestment;
    const profit = currentValue * monthlyRate;
    const charity = profit * (charityDeduction / 100);
    const profitAfterCharity = profit - charity;
    totalProfit += profit;
    totalCharity += charity;
    currentValue += profitAfterCharity;
    results.push({
      month,
      monthlyInvestment,
      profit,
      charity,
      totalProfit,
      accumulatedValue: currentValue
    });
    if (profitAfterCharity >= targetMonthlyProfit && !achieved) {
      achieved = true;
      achievedMonth = month;
    }
  }

  const summary: SummaryData = {
    totalProfit,
    totalCharity,
    finalValue: currentValue,
    requiredMonthlyInvestment: monthlyInvestment,
    feasibilityMessage: achieved
      ? `goalPlannerSuccessMessage:${achievedMonth}`
      : 'goalPlannerFailureMessage',
    totalInvestment: totalInvested
  };

  return { results, summary };
};

export const calculateMonthlyInvestment = (inputs: MonthlyInvestmentInputs): { results: CalculationResult[], summary: SummaryData } => {
  const monthlyAmount = Number(inputs.monthlyAmount);
  const duration = Number(inputs.duration);
  const profitRate = Number(inputs.profitRate);
  const charityDeduction = Number(inputs.charityDeduction);
  const monthlyRate = profitRate / 100;

  const results: CalculationResult[] = [];
  let currentValue = 0;
  let totalProfit = 0;
  let totalCharity = 0;
  let totalInvested = 0;

  for (let month = 1; month <= duration; month++) {
    const profit = currentValue * monthlyRate;
    const charity = profit * (charityDeduction / 100);
    const netProfit = profit - charity;
    currentValue += netProfit + monthlyAmount;
    totalProfit += profit;
    totalCharity += charity;
    totalInvested += monthlyAmount;
    results.push({
      month,
      monthlyInvestment: monthlyAmount,
      profit,
      charity,
      totalProfit,
      accumulatedValue: currentValue
    });
  }

  const summary: SummaryData = {
    totalProfit,
    totalCharity,
    finalValue: currentValue,
    totalInvestment: totalInvested
  };

  return { results, summary };
};

export const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(amount);
};

export const formatPercentage = (value: number): string => {
  return `${value.toFixed(2)}%`;
}; 
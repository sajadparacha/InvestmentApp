export interface OneTimeInvestmentInputs {
  investmentAmount: string | number;
  duration: string | number;
  durationUnit: string;
  profitRate: string | number;
  charityDeduction: string | number;
}

export interface GoalPlannerInputs {
  targetMonthlyProfit: string | number;
  monthlyInvestment: string | number;
  profitRate: string | number;
  charityDeduction: string | number;
}

export interface MonthlyInvestmentInputs {
  monthlyAmount: string | number;
  duration: string | number;
  profitRate: string | number;
  charityDeduction: string | number;
}

export interface CalculationResult {
  month: number;
  profit: number;
  charity: number;
  totalProfit: number;
  accumulatedValue: number;
  monthlyInvestment?: number;
  totalInvested?: number;
  totalReturn?: number;
  totalCharity?: number;
}

export interface SummaryData {
  totalProfit: number;
  totalCharity: number;
  finalValue: number;
  requiredMonthlyInvestment?: number;
  feasibilityMessage?: string;
  totalInvestment?: number;
}

export interface ChartData {
  name: string;
  value: number;
  [key: string]: any;
}

export interface ExportOptions {
  format: 'csv' | 'xlsx' | 'pdf' | 'png';
  data: any;
  filename: string;
} 
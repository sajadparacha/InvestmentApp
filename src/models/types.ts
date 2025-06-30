export interface OneTimeInvestmentInputs {
  investmentAmount: number;
  duration: number;
  durationUnit: 'months' | 'years';
  profitRate: number;
  charityDeduction: number;
}

export interface GoalPlannerInputs {
  targetMonthlyProfit: number;
  monthlyInvestment: number;
  profitRate: number;
  charityDeduction: number;
}

export interface MonthlyInvestmentInputs {
  monthlyAmount: number;
  duration: number;
  profitRate: number;
  charityDeduction: number;
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
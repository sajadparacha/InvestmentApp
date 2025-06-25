import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { Target, TrendingUp } from 'lucide-react';
import { GoalPlannerInputs, CalculationResult, SummaryData } from '../types';
import { calculateGoalPlanner, formatCurrency } from '../utils/calculations';
import { validateGoalPlanner, getErrorMessage } from '../utils/validation';
import { exportToCSV, exportToExcel, exportToPDF, exportChartToPNG } from '../utils/export';
import { DEFAULT_GOAL_PLANNER } from '../constants/defaults';

const GoalPlanner: React.FC = () => {
  const [results, setResults] = useState<CalculationResult[]>([]);
  const [summary, setSummary] = useState<SummaryData | null>(null);
  const [errors, setErrors] = useState<any[]>([]);
  
  const { register, handleSubmit, watch } = useForm<GoalPlannerInputs>({
    defaultValues: DEFAULT_GOAL_PLANNER
  });
  
  const onSubmit = (data: GoalPlannerInputs) => {
    const validationErrors = validateGoalPlanner(data);
    setErrors(validationErrors);
    
    if (validationErrors.length === 0) {
      const { results: calcResults, summary: calcSummary } = calculateGoalPlanner(data);
      setResults(calcResults);
      setSummary(calcSummary);
    }
  };
  
  const handleExport = (format: 'csv' | 'xlsx' | 'pdf') => {
    if (results.length === 0) return;
    const filename = `goal-planner-${new Date().toISOString().split('T')[0]}`;
    const inputs = watch();
    switch (format) {
      case 'csv':
        exportToCSV(results, filename, inputs, summary || undefined);
        break;
      case 'xlsx':
        exportToExcel(results, filename, inputs, summary || undefined);
        break;
      case 'pdf':
        exportToPDF('goal-results-table', filename, inputs, summary || undefined);
        break;
    }
  };
  
  const handleChartExport = () => {
    exportChartToPNG('goal-chart', `goal-chart-${new Date().toISOString().split('T')[0]}`);
  };
  
  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center space-x-3">
        <Target className="w-8 h-8 text-primary-600" />
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Goal Planner</h2>
          <p className="text-gray-600">Calculate required monthly investment to reach your financial goal</p>
        </div>
      </div>
      
      {/* Input Form */}
      <div className="card">
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Target Monthly Profit (after charity)
              </label>
              <div className="relative">
                <input
                  type="number"
                  step="0.01"
                  className="input-field"
                  placeholder="1000"
                  {...register('targetMonthlyProfit', { valueAsNumber: true })}
                />
              </div>
              {getErrorMessage(errors, 'targetMonthlyProfit') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'targetMonthlyProfit')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Monthly Investment Amount
              </label>
              <div className="relative">
                <input
                  type="number"
                  step="0.01"
                  className="input-field"
                  placeholder="500"
                  {...register('monthlyInvestment', { valueAsNumber: true })}
                />
              </div>
              {getErrorMessage(errors, 'monthlyInvestment') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'monthlyInvestment')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Profit Rate (% per month)
              </label>
              <div className="relative">
                <input
                  type="number"
                  step="0.01"
                  className="input-field"
                  placeholder="8.00"
                  {...register('profitRate', { valueAsNumber: true })}
                />
              </div>
              {getErrorMessage(errors, 'profitRate') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'profitRate')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Charity Deduction (%)
              </label>
              <div className="relative">
                <input
                  type="number"
                  min="1"
                  max="100"
                  step="0.1"
                  className="input-field"
                  placeholder="1-100"
                  {...register('charityDeduction', { valueAsNumber: true })}
                />
              </div>
              {getErrorMessage(errors, 'charityDeduction') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'charityDeduction')}</p>
              )}
            </div>
          </div>
          
          <button type="submit" className="btn-primary w-full md:w-auto">
            Calculate Required Investment
          </button>
        </form>
      </div>
      
      {/* Results */}
      {results.length > 0 && (
        <>
          {/* Summary Section */}
          <div className="card">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Investment Summary</h3>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div className="bg-blue-50 p-4 rounded-lg">
                <div className="text-sm text-blue-600 font-medium">Total Investment</div>
                <div className="text-2xl font-bold text-blue-700">
                  {formatCurrency(summary?.totalInvestment || 0)}
                </div>
              </div>
              <div className="bg-green-50 p-4 rounded-lg">
                <div className="text-sm text-green-600 font-medium">Total Profit</div>
                <div className="text-2xl font-bold text-green-700">
                  {formatCurrency(summary?.totalProfit || 0)}
                </div>
              </div>
              <div className="bg-yellow-50 p-4 rounded-lg">
                <div className="text-sm text-yellow-600 font-medium">Total Charity</div>
                <div className="text-2xl font-bold text-yellow-700">
                  {formatCurrency(summary?.totalCharity || 0)}
                </div>
              </div>
              <div className="bg-purple-50 p-4 rounded-lg">
                <div className="text-sm text-purple-600 font-medium">Final Value</div>
                <div className="text-2xl font-bold text-purple-700">
                  {formatCurrency(summary?.finalValue || 0)}
                </div>
              </div>
            </div>
            {summary?.feasibilityMessage && (
              <div className="mt-4 p-3 bg-yellow-50 border border-yellow-200 rounded-lg">
                <p className="text-sm text-yellow-800">{summary.feasibilityMessage}</p>
              </div>
            )}
          </div>
          
          {/* Chart */}
          <div className="card">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold text-gray-900">Monthly Investment, Profit, and Charity</h3>
              <button onClick={handleChartExport} className="btn-secondary text-sm">
                Export PNG
              </button>
            </div>
            <div id="goal-chart" className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={results}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <Tooltip 
                    content={({ active, payload, label }) => {
                      if (!active || !payload || !payload.length) return null;
                      const monthlyProfit = payload[0]?.payload?.profit;
                      const monthlyCharity = payload[0]?.payload?.charity;
                      const investmentValue = payload[0]?.payload?.accumulatedValue;
                      return (
                        <div className="bg-white p-3 rounded-lg shadow text-xs text-gray-800">
                          <div className="font-semibold mb-1">Month {label}</div>
                          <div>Profit: <span className="font-bold">{formatCurrency(monthlyProfit ?? 0)}</span></div>
                          <div>Charity: <span className="font-bold">{formatCurrency(monthlyCharity ?? 0)}</span></div>
                          <div>Investment Value: <span className="font-bold">{formatCurrency(investmentValue ?? 0)}</span></div>
                        </div>
                      );
                    }}
                  />
                  <Legend />
                  <Bar dataKey="accumulatedValue" fill="#3b82f6" name="Investment Value" />
                  <Bar dataKey="totalProfit" fill="#10b981" name="Cumulative Profit" />
                  <Bar dataKey="totalCharity" fill="#f59e0b" name="Cumulative Charity" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
          
          {/* Results Table */}
          <div className="card">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold text-gray-900">Monthly Breakdown</h3>
              <div className="space-x-2">
                <button onClick={() => handleExport('csv')} className="btn-secondary text-sm">
                  Export CSV
                </button>
                <button onClick={() => handleExport('xlsx')} className="btn-secondary text-sm">
                  Export Excel
                </button>
                <button onClick={() => handleExport('pdf')} className="btn-secondary text-sm">
                  Export PDF
                </button>
              </div>
            </div>
            <div id="goal-results-table" className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Month
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Monthly Investment
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Profit
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Charity
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Investment Value
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {results.map((result) => (
                    <tr key={result.month}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {result.month}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {formatCurrency(result.monthlyInvestment || 0)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {formatCurrency(result.profit)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {formatCurrency(result.charity)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {formatCurrency(result.accumulatedValue)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default GoalPlanner; 
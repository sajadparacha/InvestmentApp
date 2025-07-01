import React from 'react';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip, Legend } from 'recharts';
import { Repeat } from 'lucide-react';
import { MonthlyInvestmentInputs, CalculationResult, SummaryData } from '../models/types';
import { calculateMonthlyInvestment, formatCurrency } from '../models/calculations';
import { validateMonthlyInvestment, getErrorMessage } from '../models/validation';
import { exportToCSV, exportToExcel, exportToPDF, exportChartToPNG } from '../utils/export';
import { DEFAULT_MONTHLY_INVESTMENT, EMPTY_MONTHLY_INVESTMENT } from '../constants/defaults';
import { useTranslation } from 'react-i18next';

interface MonthlyInvestmentProps {
  inputs: MonthlyInvestmentInputs;
  setInputs: React.Dispatch<React.SetStateAction<MonthlyInvestmentInputs>>;
  results: CalculationResult[];
  setResults: React.Dispatch<React.SetStateAction<CalculationResult[]>>;
  summary: SummaryData | null;
  setSummary: React.Dispatch<React.SetStateAction<SummaryData | null>>;
  errors: any[];
  setErrors: React.Dispatch<React.SetStateAction<any[]>>;
}

const MonthlyInvestment: React.FC<MonthlyInvestmentProps> = ({
  inputs,
  setInputs,
  results,
  setResults,
  summary,
  setSummary,
  errors,
  setErrors
}) => {
  const { t } = useTranslation();

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target;
    setInputs((prev) => ({
      ...prev,
      [name]: type === 'number' || type === 'range' ? Number(value) : value
    }));
  };

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const validationErrors = validateMonthlyInvestment(inputs);
    setErrors(validationErrors);
    if (validationErrors.length === 0) {
      const { results: calcResults, summary: calcSummary } = calculateMonthlyInvestment(inputs);
      setResults(calcResults);
      setSummary(calcSummary);
    }
  };

  const handleExport = (format: 'csv' | 'xlsx' | 'pdf') => {
    if (results.length === 0) return;
    const filename = `monthly-investment-${new Date().toISOString().split('T')[0]}`;
    const inputValues = inputs;
    switch (format) {
      case 'csv':
        exportToCSV(results, filename, inputValues, summary || undefined);
        break;
      case 'xlsx':
        exportToExcel(results, filename, inputValues, summary || undefined);
        break;
      case 'pdf':
        exportToPDF('monthly-results-table', filename, inputValues, summary || undefined);
        break;
    }
  };

  const handleChartExport = () => {
    exportChartToPNG('monthly-chart', `monthly-chart-${new Date().toISOString().split('T')[0]}`);
  };

  const getPieChartData = () => {
    if (!summary) return [];
    const totalInvested = results.reduce((sum, result) => sum + (result.monthlyInvestment || 0), 0);
    const totalProfit = summary.totalProfit;
    const totalCharity = summary.totalCharity;
    return [
      { name: t('capitalInvested'), value: totalInvested, color: '#3b82f6' },
      { name: t('totalProfit'), value: totalProfit, color: '#10b981' },
      { name: t('totalCharity'), value: totalCharity, color: '#f59e0b' }
    ];
  };

  const handleClear = () => {
    setInputs(EMPTY_MONTHLY_INVESTMENT);
    setResults([]);
    setSummary(null);
    setErrors([]);
  };

  return (
    <div className="p-6 md:p-8 lg:p-10 space-y-6">
      {/* Header */}
      <div className="flex items-center space-x-3">
        <Repeat className="w-8 h-8 text-primary-600" />
        <div>
          <h2 className="text-2xl font-bold text-gray-900">{t('monthlyInvestment')}</h2>
          <p className="text-gray-600">{t('monthlyInvestmentDesc')}</p>
        </div>
      </div>
      {/* Input Form */}
      <div className="card">
        <form onSubmit={onSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                {t('monthlyInvestmentAmount')}
              </label>
              <input
                type="number"
                step="0.01"
                className="input-field"
                placeholder="1000"
                name="monthlyAmount"
                value={inputs.monthlyAmount}
                onChange={handleInputChange}
              />
              {getErrorMessage(errors, 'monthlyAmount') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'monthlyAmount')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                {t('duration')}
              </label>
              <input
                type="number"
                className="input-field"
                placeholder="12"
                name="duration"
                value={inputs.duration}
                onChange={handleInputChange}
              />
              {getErrorMessage(errors, 'duration') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'duration')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                {t('profitRate')}
              </label>
              <input
                type="number"
                step="0.01"
                className="input-field"
                placeholder="8.00"
                name="profitRate"
                value={inputs.profitRate}
                onChange={handleInputChange}
              />
              {getErrorMessage(errors, 'profitRate') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'profitRate')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                {t('charityDeduction')}
              </label>
              <input
                type="number"
                min="1"
                max="100"
                step="0.1"
                className="input-field"
                placeholder="1-100"
                name="charityDeduction"
                value={inputs.charityDeduction}
                onChange={handleInputChange}
              />
              {getErrorMessage(errors, 'charityDeduction') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'charityDeduction')}</p>
              )}
            </div>
          </div>
          <button type="submit" className="btn-primary w-full md:w-auto">
            {t('calculate')}
          </button>
          <button type="button" className="btn-primary w-full md:w-auto ml-2" onClick={handleClear}>
            {t('clear')}
          </button>
        </form>
      </div>
      {/* Results */}
      {results.length > 0 && (
        <>
          {/* Summary Section */}
          <div className="card">
            <h3 className="text-lg md:text-xl font-semibold text-gray-900 mb-4">{t('investmentSummary')}</h3>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div className="bg-blue-50 p-4 rounded-lg">
                <div className="text-sm text-blue-600 font-medium">{t('totalInvestment')}</div>
                <div className="text-2xl font-bold text-blue-700">
                  {formatCurrency(summary?.totalInvestment || 0)}
                </div>
              </div>
              <div className="bg-green-50 p-4 rounded-lg">
                <div className="text-sm text-green-600 font-medium">{t('totalProfit')}</div>
                <div className="text-2xl font-bold text-green-700">
                  {formatCurrency(summary?.totalProfit || 0)}
                </div>
              </div>
              <div className="bg-yellow-50 p-4 rounded-lg">
                <div className="text-sm text-yellow-600 font-medium">{t('totalCharity')}</div>
                <div className="text-2xl font-bold text-yellow-700">
                  {formatCurrency(summary?.totalCharity || 0)}
                </div>
              </div>
              <div className="bg-purple-50 p-4 rounded-lg">
                <div className="text-sm text-purple-600 font-medium">{t('finalValue')}</div>
                <div className="text-2xl font-bold text-purple-700">
                  {formatCurrency(summary?.finalValue || 0)}
                </div>
              </div>
            </div>
          </div>
          {/* Chart */}
          <div className="card">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold text-gray-900">{t('investmentBreakdown')}</h3>
              <button onClick={handleChartExport} className="btn-primary text-sm">
                {t('exportPNG')}
              </button>
            </div>
            <div id="monthly-chart" className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={getPieChartData()}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {getPieChartData().map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip formatter={(value: number) => [formatCurrency(value), t('amount')]} />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>
          {/* Results Table */}
          <div className="card">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold text-gray-900">{t('monthlyBreakdown')}</h3>
              <div className="space-x-2">
                <button onClick={() => handleExport('csv')} className="btn-primary text-sm">
                  {t('exportCSV')}
                </button>
                <button onClick={() => handleExport('xlsx')} className="btn-primary text-sm">
                  {t('exportExcel')}
                </button>
                <button onClick={() => handleExport('pdf')} className="btn-primary text-sm">
                  {t('exportPDF')}
                </button>
              </div>
            </div>
            <div id="monthly-results-table" className="overflow-x-auto w-full">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('month')}</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('monthlyInvestment')}</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('profit')}</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('charity')}</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('accumulatedValue')}</th>
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

export default MonthlyInvestment; 
import React from 'react';
import { BarChart, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, Bar } from 'recharts';
import { Calculator } from 'lucide-react';
import { OneTimeInvestmentInputs, CalculationResult, SummaryData } from '../models/types';
import { calculateOneTimeInvestment, formatCurrency } from '../models/calculations';
import { validateOneTimeInvestment, getErrorMessage } from '../models/validation';
import { exportToCSV, exportToExcel, exportToPDF, exportChartToPNG } from '../utils/export';
import { DEFAULT_ONE_TIME_INVESTMENT, EMPTY_ONE_TIME_INVESTMENT } from '../constants/defaults';
import { useTranslation } from 'react-i18next';

interface OneTimeInvestmentProps {
  inputs: OneTimeInvestmentInputs;
  setInputs: React.Dispatch<React.SetStateAction<OneTimeInvestmentInputs>>;
  results: CalculationResult[];
  setResults: React.Dispatch<React.SetStateAction<CalculationResult[]>>;
  summary: SummaryData | null;
  setSummary: React.Dispatch<React.SetStateAction<SummaryData | null>>;
  errors: any[];
  setErrors: React.Dispatch<React.SetStateAction<any[]>>;
}

const OneTimeInvestment: React.FC<OneTimeInvestmentProps> = ({
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

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    setInputs((prev) => ({
      ...prev,
      [name]: type === 'number' || type === 'range' ? Number(value) : value
    }));
  };

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const validationErrors = validateOneTimeInvestment(inputs);
    setErrors(validationErrors);
    if (validationErrors.length === 0) {
      const { results: calcResults, summary: calcSummary } = calculateOneTimeInvestment(inputs);
      setResults(calcResults);
      setSummary(calcSummary);
    }
  };

  const handleExport = (format: 'csv' | 'xlsx' | 'pdf') => {
    if (results.length === 0) return;
    const filename = `one-time-investment-${new Date().toISOString().split('T')[0]}`;
    const inputValues = inputs;
    switch (format) {
      case 'csv':
        exportToCSV(results, filename, inputValues, summary || undefined);
        break;
      case 'xlsx':
        exportToExcel(results, filename, inputValues, summary || undefined);
        break;
      case 'pdf':
        exportToPDF('one-time-results-table', filename, inputValues, summary || undefined);
        break;
    }
  };

  const handleChartExport = () => {
    exportChartToPNG('one-time-chart', `one-time-chart-${new Date().toISOString().split('T')[0]}`);
  };

  const handleClear = () => {
    setInputs(EMPTY_ONE_TIME_INVESTMENT);
    setResults([]);
    setSummary(null);
    setErrors([]);
  };

  return (
    <div className="p-6 md:p-8 lg:p-10 space-y-6">
      {/* Header */}
      <div className="flex items-center space-x-3">
        <Calculator className="w-8 h-8 text-primary-600" />
        <div>
          <h2 className="text-2xl font-bold text-gray-900">{t('oneTimeInvestment')}</h2>
          <p className="text-gray-600">{t('oneTimeInvestmentDesc')}</p>
        </div>
      </div>
      {/* Input Form */}
      <div className="card">
        <form onSubmit={onSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                {t('investmentAmount')}
              </label>
              <div className="relative">
                <input
                  type="number"
                  step="0.01"
                  className="input-field"
                  placeholder="10000"
                  name="investmentAmount"
                  value={inputs.investmentAmount}
                  onChange={handleInputChange}
                />
              </div>
              {getErrorMessage(errors, 'investmentAmount') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'investmentAmount')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                {t('duration')}
              </label>
              <div className="flex space-x-2">
                <input
                  type="number"
                  className="input-field"
                  placeholder="12"
                  name="duration"
                  value={inputs.duration}
                  onChange={handleInputChange}
                />
                <select
                  className="input-field"
                  name="durationUnit"
                  value={inputs.durationUnit}
                  onChange={handleInputChange}
                >
                  <option value="months">{t('months')}</option>
                  <option value="years">{t('years')}</option>
                </select>
              </div>
              {getErrorMessage(errors, 'duration') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'duration')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                {t('profitRate')}
              </label>
              <div className="relative">
                <input
                  type="number"
                  step="0.01"
                  className="input-field"
                  placeholder="8.00"
                  name="profitRate"
                  value={inputs.profitRate}
                  onChange={handleInputChange}
                />
              </div>
              {getErrorMessage(errors, 'profitRate') && (
                <p className="text-red-500 text-sm mt-1">{getErrorMessage(errors, 'profitRate')}</p>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                {t('charityDeduction')}
              </label>
              <div className="relative">
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
              </div>
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
                  {formatCurrency(summary?.requiredMonthlyInvestment || 0)}
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
              <h3 className="text-lg font-semibold text-gray-900">{t('monthlyInvestmentProfitCharity')}</h3>
              <button onClick={handleChartExport} className="btn-primary text-sm">
                {t('exportPNG')}
              </button>
            </div>
            <div id="one-time-chart" className="h-80">
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
                          <div className="font-semibold mb-1">{t('month')} {label}</div>
                          <div>{t('profit')}: <span className="font-bold">{formatCurrency(monthlyProfit ?? 0)}</span></div>
                          <div>{t('charity')}: <span className="font-bold">{formatCurrency(monthlyCharity ?? 0)}</span></div>
                          <div>{t('investmentValue')}: <span className="font-bold">{formatCurrency(investmentValue ?? 0)}</span></div>
                        </div>
                      );
                    }}
                  />
                  <Legend />
                  <Bar dataKey="accumulatedValue" fill="#3b82f6" name={t('investmentValue')} />
                  <Bar dataKey="totalProfit" fill="#10b981" name={t('cumulativeProfit')} />
                  <Bar dataKey="totalCharity" fill="#f59e0b" name={t('cumulativeCharity')} />
                </BarChart>
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
            <div id="one-time-results-table" className="overflow-x-auto w-full">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('month')}</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('profit')}</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('charity')}</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">{t('investmentValue')}</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {results.map((result) => (
                    <tr key={result.month}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {result.month}
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

export default OneTimeInvestment; 
import React, { useState } from 'react';
import { MonthlyInvestmentInputs, CalculationResult, SummaryData } from '../models/types';
import { DEFAULT_MONTHLY_INVESTMENT } from '../constants/defaults';
import MonthlyInvestmentView from '../views/MonthlyInvestmentView';

const MonthlyInvestmentController: React.FC = () => {
  const [inputs, setInputs] = useState<MonthlyInvestmentInputs>(DEFAULT_MONTHLY_INVESTMENT);
  const [results, setResults] = useState<CalculationResult[]>([]);
  const [summary, setSummary] = useState<SummaryData | null>(null);
  const [errors, setErrors] = useState<any[]>([]);

  return (
    <MonthlyInvestmentView
      inputs={inputs}
      setInputs={setInputs}
      results={results}
      setResults={setResults}
      summary={summary}
      setSummary={setSummary}
      errors={errors}
      setErrors={setErrors}
    />
  );
};

export default MonthlyInvestmentController; 
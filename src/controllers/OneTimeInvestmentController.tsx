import React, { useState } from 'react';
import { OneTimeInvestmentInputs, CalculationResult, SummaryData } from '../models/types';
import { DEFAULT_ONE_TIME_INVESTMENT } from '../constants/defaults';
import OneTimeInvestmentView from '../views/OneTimeInvestmentView';

const OneTimeInvestmentController: React.FC = () => {
  const [inputs, setInputs] = useState<OneTimeInvestmentInputs>(DEFAULT_ONE_TIME_INVESTMENT);
  const [results, setResults] = useState<CalculationResult[]>([]);
  const [summary, setSummary] = useState<SummaryData | null>(null);
  const [errors, setErrors] = useState<any[]>([]);

  return (
    <OneTimeInvestmentView
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

export default OneTimeInvestmentController; 
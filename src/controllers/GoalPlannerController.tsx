import React, { useState } from 'react';
import { GoalPlannerInputs, CalculationResult, SummaryData } from '../models/types';
import { DEFAULT_GOAL_PLANNER } from '../constants/defaults';
import GoalPlannerView from '../views/GoalPlannerView';

const GoalPlannerController: React.FC = () => {
  const [inputs, setInputs] = useState<GoalPlannerInputs>(DEFAULT_GOAL_PLANNER);
  const [results, setResults] = useState<CalculationResult[]>([]);
  const [summary, setSummary] = useState<SummaryData | null>(null);
  const [errors, setErrors] = useState<any[]>([]);

  return (
    <GoalPlannerView
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

export default GoalPlannerController; 
import React, { useState } from 'react';
import { Calculator, Target, Repeat, HelpCircle } from 'lucide-react';
import OneTimeInvestment from './components/OneTimeInvestment';
import GoalPlanner from './components/GoalPlanner';
import MonthlyInvestment from './components/MonthlyInvestment';
import HelpGuide from './components/HelpGuide';
import './index.css';
import { OneTimeInvestmentInputs, CalculationResult, SummaryData, GoalPlannerInputs, MonthlyInvestmentInputs } from './types';
import { DEFAULT_ONE_TIME_INVESTMENT, DEFAULT_GOAL_PLANNER, DEFAULT_MONTHLY_INVESTMENT } from './constants/defaults';

const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState(0);

  // One-Time Investment state
  const [oneTimeInputs, setOneTimeInputs] = useState<OneTimeInvestmentInputs>(DEFAULT_ONE_TIME_INVESTMENT);
  const [oneTimeResults, setOneTimeResults] = useState<CalculationResult[]>([]);
  const [oneTimeSummary, setOneTimeSummary] = useState<SummaryData | null>(null);
  const [oneTimeErrors, setOneTimeErrors] = useState<any[]>([]);

  // Goal Planner state
  const [goalPlannerInputs, setGoalPlannerInputs] = useState<GoalPlannerInputs>(DEFAULT_GOAL_PLANNER);
  const [goalPlannerResults, setGoalPlannerResults] = useState<CalculationResult[]>([]);
  const [goalPlannerSummary, setGoalPlannerSummary] = useState<SummaryData | null>(null);
  const [goalPlannerErrors, setGoalPlannerErrors] = useState<any[]>([]);

  // Monthly Investment state
  const [monthlyInputs, setMonthlyInputs] = useState<MonthlyInvestmentInputs>(DEFAULT_MONTHLY_INVESTMENT);
  const [monthlyResults, setMonthlyResults] = useState<CalculationResult[]>([]);
  const [monthlySummary, setMonthlySummary] = useState<SummaryData | null>(null);
  const [monthlyErrors, setMonthlyErrors] = useState<any[]>([]);

  const tabs = [
    {
      id: 0,
      name: 'One-Time Investment',
      icon: <Calculator className="w-5 h-5" />
    },
    {
      id: 1,
      name: 'Goal Planner',
      icon: <Target className="w-5 h-5" />
    },
    {
      id: 2,
      name: 'Monthly Investment',
      icon: <Repeat className="w-5 h-5" />
    },
    {
      id: 3,
      name: 'Help & Guidance',
      icon: <HelpCircle className="w-5 h-5" />
    }
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 bg-primary-600 rounded-lg flex items-center justify-center">
                <Calculator className="w-6 h-6 text-white" />
              </div>
              <div>
                <h1 className="text-2xl font-bold text-gray-900">InvestmentApp</h1>
                <p className="text-sm text-gray-600">Financial Planning Made Simple</p>
              </div>
            </div>
            <div className="hidden md:block">
              <div className="flex items-center space-x-4 text-sm text-gray-500">
                <span>Version 1.0.0</span>
                <span>•</span>
                <span>React + TypeScript</span>
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation Tabs */}
      <nav className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex space-x-8 overflow-x-auto">
            {tabs.map((tab) => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`flex items-center space-x-2 py-4 px-1 border-b-2 font-medium text-sm whitespace-nowrap transition-colors duration-200 ${
                  activeTab === tab.id
                    ? 'border-primary-500 text-primary-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                {tab.icon}
                <span>{tab.name}</span>
              </button>
            ))}
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="space-y-8">
          <div style={{ display: activeTab === 0 ? 'block' : 'none' }}>
            <OneTimeInvestment
              inputs={oneTimeInputs}
              setInputs={setOneTimeInputs}
              results={oneTimeResults}
              setResults={setOneTimeResults}
              summary={oneTimeSummary}
              setSummary={setOneTimeSummary}
              errors={oneTimeErrors}
              setErrors={setOneTimeErrors}
            />
          </div>
          <div style={{ display: activeTab === 1 ? 'block' : 'none' }}>
            <GoalPlanner
              inputs={goalPlannerInputs}
              setInputs={setGoalPlannerInputs}
              results={goalPlannerResults}
              setResults={setGoalPlannerResults}
              summary={goalPlannerSummary}
              setSummary={setGoalPlannerSummary}
              errors={goalPlannerErrors}
              setErrors={setGoalPlannerErrors}
            />
          </div>
          <div style={{ display: activeTab === 2 ? 'block' : 'none' }}>
            <MonthlyInvestment
              inputs={monthlyInputs}
              setInputs={setMonthlyInputs}
              results={monthlyResults}
              setResults={setMonthlyResults}
              summary={monthlySummary}
              setSummary={setMonthlySummary}
              errors={monthlyErrors}
              setErrors={setMonthlyErrors}
            />
          </div>
          <div style={{ display: activeTab === 3 ? 'block' : 'none' }}>
            <HelpGuide />
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-white border-t border-gray-200 mt-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="text-center text-sm text-gray-500">
            <p>&copy; 2024 InvestmentApp. All rights reserved.</p>
            <p className="mt-2">
              Built with React, TypeScript, and Tailwind CSS. 
              Financial calculations are estimates and should not be considered as financial advice.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default App; 
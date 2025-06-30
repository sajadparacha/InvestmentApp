import React, { useState } from 'react';
import { Calculator, Target, Repeat, HelpCircle } from 'lucide-react';
import OneTimeInvestmentController from './controllers/OneTimeInvestmentController';
import GoalPlannerController from './controllers/GoalPlannerController';
import MonthlyInvestmentController from './controllers/MonthlyInvestmentController';
import HelpGuide from './components/HelpGuide';
import CreditsView from './views/CreditsView';
import './index.css';
import './i18n';
import { useTranslation } from 'react-i18next';

const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState(0);
  const { t, i18n } = useTranslation();

  const handleLangChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    i18n.changeLanguage(e.target.value);
  };

  const tabs = [
    {
      id: 0,
      name: t('oneTimeInvestment'),
      icon: <Calculator className="w-5 h-5" />
    },
    {
      id: 1,
      name: t('goalPlanner'),
      icon: <Target className="w-5 h-5" />
    },
    {
      id: 2,
      name: t('monthlyInvestment'),
      icon: <Repeat className="w-5 h-5" />
    },
    {
      id: 3,
      name: t('help'),
      icon: <HelpCircle className="w-5 h-5" />
    },
    {
      id: 4,
      name: t('credits'),
      icon: <span className="w-5 h-5">©</span>
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
                <h1 className="text-2xl font-bold text-gray-900">{t('appTitle')}</h1>
                <p className="text-sm text-gray-600">{t('appSubtitle')}</p>
              </div>
            </div>
            <div className="hidden md:block">
              <div className="flex items-center space-x-4 text-sm text-gray-500">
                <span>{t('version', { version: '1.0.0' })}</span>
                <span>•</span>
                <span>{t('techStack')}</span>
              </div>
            </div>
            <select
              className="ml-4 border rounded px-2 py-1 text-sm"
              value={i18n.language}
              onChange={handleLangChange}
              aria-label="Select language"
            >
              <option value="en">{t('langEnglish')}</option>
              <option value="ur">{t('langUrdu')}</option>
            </select>
          </div>
        </div>
      </header>

      {/* Navigation Tabs */}
      <nav className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex space-x-2 mb-8 overflow-x-auto whitespace-nowrap flex-nowrap border-b border-gray-200 scrollbar-thin scrollbar-thumb-gray-300">
            {tabs.map((tab) => (
              <button
                key={tab.id}
                className={`flex items-center px-4 py-2 rounded-t-lg font-medium transition-colors duration-200 focus:outline-none flex-shrink-0 min-w-[120px] md:min-w-[160px] ${activeTab === tab.id ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:bg-gray-100'}`}
                onClick={() => setActiveTab(tab.id)}
              >
                {tab.icon}
                <span className="ml-2">{tab.name}</span>
              </button>
            ))}
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="space-y-8">
          <div style={{ display: activeTab === 0 ? 'block' : 'none' }}>
            <OneTimeInvestmentController />
          </div>
          <div style={{ display: activeTab === 1 ? 'block' : 'none' }}>
            <GoalPlannerController />
          </div>
          <div style={{ display: activeTab === 2 ? 'block' : 'none' }}>
            <MonthlyInvestmentController />
          </div>
          <div style={{ display: activeTab === 3 ? 'block' : 'none' }}>
            <HelpGuide />
          </div>
          <div style={{ display: activeTab === 4 ? 'block' : 'none' }}>
            <CreditsView />
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
import React, { useEffect, useState } from 'react';
import { Calculator, Target, Repeat, HelpCircle } from 'lucide-react';
import OneTimeInvestmentController from './controllers/OneTimeInvestmentController';
import GoalPlannerController from './controllers/GoalPlannerController';
import MonthlyInvestmentController from './controllers/MonthlyInvestmentController';
import HelpGuide from './components/HelpGuide';
import CreditsView from './views/CreditsView';
import './index.css';
import './i18n';
import { useTranslation } from 'react-i18next';
import {
  HashRouter as Router,
  Routes,
  Route,
  useParams,
  useNavigate,
  Navigate
} from 'react-router-dom';

const AppContent: React.FC = () => {
  const [activeTab, setActiveTab] = useState(0);
  const { t, i18n } = useTranslation();
  const { lng } = useParams();
  const navigate = useNavigate();
  const [isLangReady, setIsLangReady] = useState(false);

  // Sync i18n language with URL param and wait for it to be ready
  useEffect(() => {
    if (lng && i18n.language !== lng) {
      setIsLangReady(false);
      i18n.changeLanguage(lng, () => setIsLangReady(true));
    } else {
      setIsLangReady(true);
    }
    // eslint-disable-next-line
  }, [lng]);

  const handleLangChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newLang = e.target.value;
    if (newLang !== lng) {
      navigate(`/${newLang}`);
    }
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

  if (!isLangReady) {
    return <div className="flex justify-center items-center min-h-screen text-lg">Loading...</div>;
  }

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
              <option value="ar">{t('langArabic')}</option>
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
            <h3 className="text-xl font-semibold mb-2">{t('oneTimeInvestment')}</h3>
            <p className="text-gray-600 mb-4">{t('oneTimeInvestmentDesc')}</p>
            <OneTimeInvestmentController />
          </div>
          <div style={{ display: activeTab === 1 ? 'block' : 'none' }}>
            <h3 className="text-xl font-semibold mb-2">{t('goalPlanner')}</h3>
            <p className="text-gray-600 mb-4">{t('goalPlannerDesc')}</p>
            <GoalPlannerController />
          </div>
          <div style={{ display: activeTab === 2 ? 'block' : 'none' }}>
            <h3 className="text-xl font-semibold mb-2">{t('monthlyInvestment')}</h3>
            <p className="text-gray-600 mb-4">{t('monthlyInvestmentDesc')}</p>
            <MonthlyInvestmentController />
          </div>
          <div style={{ display: activeTab === 3 ? 'block' : 'none' }}>
            <h3 className="text-xl font-semibold mb-2">{t('help')}</h3>
            <p className="text-gray-600 mb-4">{t('helpDesc')}</p>
            <HelpGuide />
          </div>
          <div style={{ display: activeTab === 4 ? 'block' : 'none' }}>
            <h3 className="text-xl font-semibold mb-2">{t('credits')}</h3>
            <p className="text-gray-600 mb-4">{t('creditsDesc')}</p>
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

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/en" replace />} />
        <Route path=":lng" element={<AppContent />} />
      </Routes>
    </Router>
  );
};

export default App; 
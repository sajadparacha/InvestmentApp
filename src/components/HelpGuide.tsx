import React, { useState } from 'react';
import { ChevronDown, ChevronRight, HelpCircle, BookOpen, MessageCircle, BarChart3, Download } from 'lucide-react';
import { useTranslation } from 'react-i18next';

interface AccordionItemProps {
  title: string;
  children: React.ReactNode;
  icon: React.ReactNode;
}

const AccordionItem: React.FC<AccordionItemProps> = ({ title, children, icon }) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="border border-gray-200 rounded-lg mb-4">
      <button
        className="w-full px-6 py-4 text-left flex items-center justify-between hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-inset"
        onClick={() => setIsOpen(!isOpen)}
      >
        <div className="flex items-center space-x-3">
          {icon}
          <span className="text-lg font-medium text-gray-900">{title}</span>
        </div>
        {isOpen ? (
          <ChevronDown className="w-5 h-5 text-gray-500" />
        ) : (
          <ChevronRight className="w-5 h-5 text-gray-500" />
        )}
      </button>
      {isOpen && (
        <div className="px-6 pb-4">
          <div className="text-gray-700 space-y-4">
            {children}
          </div>
        </div>
      )}
    </div>
  );
};

const HelpGuide: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center space-x-3">
        <HelpCircle className="w-8 h-8 text-primary-600" />
        <div>
          <h2 className="text-2xl font-bold text-gray-900">{t('help')}</h2>
          <p className="text-gray-600">{t('helpIntro')}</p>
        </div>
      </div>

      {/* Help Content */}
      <div className="space-y-6">
        <AccordionItem title={t('moduleDescriptions')} icon={<BookOpen className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('oneTimeInvestmentCalculator')}</h4>
              <p className="text-gray-600">
                {t('oneTimeInvestmentCalculatorDescription')}
              </p>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('goalPlanner')}</h4>
              <p className="text-gray-600">
                {t('goalPlannerDescription')}
              </p>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('monthlyRecurringInvestmentCalculator')}</h4>
              <p className="text-gray-600">
                {t('monthlyRecurringInvestmentCalculatorDescription')}
              </p>
            </div>
          </div>
        </AccordionItem>

        <AccordionItem title={t('stepByStepGuide')} icon={<MessageCircle className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('gettingStarted')}</h4>
              <ol className="list-decimal list-inside space-y-2 text-gray-600">
                <li>{t('gettingStartedSelectCalculator')}</li>
                <li>{t('gettingStartedEnterParameters')}</li>
                <li>{t('gettingStartedSetCharityDeduction')}</li>
                <li>{t('gettingStartedClickCalculate')}</li>
                <li>{t('gettingStartedReviewSummary')}</li>
                <li>{t('gettingStartedExportResults')}</li>
              </ol>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('inputGuidelines')}</h4>
              <ul className="list-disc list-inside space-y-2 text-gray-600">
                <li><strong>{t('investmentAmount')}:</strong> {t('investmentAmountDescription')}</li>
                <li><strong>{t('duration')}:</strong> {t('durationDescription')}</li>
                <li><strong>{t('profitRate')}:</strong> {t('profitRateDescription')}</li>
                <li><strong>{t('charityDeduction')}:</strong> {t('charityDeductionDescription')}</li>
              </ul>
            </div>
          </div>
        </AccordionItem>

        <AccordionItem title={t('sampleUseCases')} icon={<BarChart3 className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('oneTimeInvestmentExample')}</h4>
              <p className="text-gray-600 mb-2">
                <strong>{t('scenario')}:</strong> {t('oneTimeInvestmentExampleScenario')}
              </p>
              <ul className="list-disc list-inside space-y-1 text-gray-600">
                <li>{t('investmentAmount')}: 10,000</li>
                <li>{t('duration')}: 5 years</li>
                <li>{t('profitRate')}: 8%</li>
                <li>{t('charityDeduction')}: 10%</li>
                <li><strong>{t('result')}:</strong> {t('oneTimeInvestmentExampleResult')}</li>
              </ul>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('goalPlanningExample')}</h4>
              <p className="text-gray-600 mb-2">
                <strong>{t('scenario')}:</strong> {t('goalPlanningExampleScenario')}
              </p>
              <ul className="list-disc list-inside space-y-1 text-gray-600">
                <li>{t('monthlyInvestment')}: 1,000</li>
                <li>{t('targetMonthlyProfit')}: 500</li>
                <li>{t('profitRate')}: 6%</li>
                <li><strong>{t('result')}:</strong> {t('goalPlanningExampleResult')}</li>
              </ul>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('monthlyInvestmentExample')}</h4>
              <p className="text-gray-600 mb-2">
                <strong>{t('scenario')}:</strong> {t('monthlyInvestmentExampleScenario')}
              </p>
              <ul className="list-disc list-inside space-y-1 text-gray-600">
                <li>{t('monthlyAmount')}: 500</li>
                <li>{t('duration')}: 120 months ({t('durationYears')})</li>
                <li>{t('profitRate')}: 7%</li>
                <li>{t('charityDeduction')}: 5%</li>
                <li><strong>{t('result')}:</strong> {t('monthlyInvestmentExampleResult')}</li>
              </ul>
            </div>
          </div>
        </AccordionItem>

        <AccordionItem title={t('faq')} icon={<HelpCircle className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('generalQuestions')}</h4>
              <div className="space-y-3">
                <div>
                  <p className="font-medium text-gray-900">{t('generalQuestionsAccuracy')}</p>
                  <p className="text-gray-600">
                    {t('generalQuestionsAccuracyDescription')}
                  </p>
                </div>
                
                <div>
                  <p className="font-medium text-gray-900">{t('generalQuestionsCharityDeduction')}</p>
                  <p className="text-gray-600">
                    {t('generalQuestionsCharityDeductionDescription')}
                  </p>
                </div>
              </div>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('technicalQuestions')}</h4>
              <div className="space-y-3">
                <div>
                  <p className="font-medium text-gray-900">{t('technicalQuestionsExportFormats')}</p>
                  <p className="text-gray-600">
                    {t('technicalQuestionsExportFormatsDescription')}
                  </p>
                </div>
                
                <div>
                  <p className="font-medium text-gray-900">{t('technicalQuestionsSaveCalculations')}</p>
                  <p className="text-gray-600">
                    {t('technicalQuestionsSaveCalculationsDescription')}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </AccordionItem>

        <AccordionItem title={t('chartInterpretation')} icon={<BarChart3 className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('barCharts')}</h4>
              <p className="text-gray-600">
                {t('barChartsDescription')}
              </p>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('readingTooltips')}</h4>
              <p className="text-gray-600">
                {t('readingTooltipsDescription')}
              </p>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">{t('chartColors')}</h4>
              <p className="text-gray-600">
                {t('chartColorsDescription')}
              </p>
            </div>
          </div>
        </AccordionItem>
      </div>

      {/* Export Help Section */}
      <div className="card">
        <div className="flex items-center space-x-3 mb-4">
          <Download className="w-6 h-6 text-primary-600" />
          <h3 className="text-lg font-semibold text-gray-900">{t('exportOptions')}</h3>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <h4 className="font-medium text-gray-900 mb-2">{t('dataExport')}</h4>
            <ul className="text-sm text-gray-600 space-y-1">
              <li>• <strong>{t('csv')}:</strong> {t('csvDescription')}</li>
              <li>• <strong>{t('excel')}:</strong> {t('excelDescription')}</li>
              <li>• <strong>{t('pdf')}:</strong> {t('pdfDescription')}</li>
            </ul>
          </div>
          <div>
            <h4 className="font-medium text-gray-900 mb-2">{t('chartExport')}</h4>
            <ul className="text-sm text-gray-600 space-y-1">
              <li>• <strong>{t('png')}:</strong> {t('pngDescription')}</li>
              <li>• <strong>{t('pdf')}:</strong> {t('pdfVectorDescription')}</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HelpGuide; 
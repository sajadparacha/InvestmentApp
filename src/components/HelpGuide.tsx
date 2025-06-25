import React, { useState } from 'react';
import { ChevronDown, ChevronRight, HelpCircle, BookOpen, MessageCircle, BarChart3, Download } from 'lucide-react';

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
  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center space-x-3">
        <HelpCircle className="w-8 h-8 text-primary-600" />
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Help & Guidance</h2>
          <p className="text-gray-600">Learn how to use the InvestmentApp effectively</p>
        </div>
      </div>

      {/* Help Content */}
      <div className="space-y-6">
        <AccordionItem title="Module Descriptions" icon={<BookOpen className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">One-Time Investment Calculator</h4>
              <p className="text-gray-600">
                Calculate the projected return from a lump-sum investment over a given duration. 
                This calculator uses compound interest with monthly compounding and allows for optional charity deductions.
              </p>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Goal Planner</h4>
              <p className="text-gray-600">
                Calculate how many months it will take to reach a target monthly profit, given a fixed monthly investment amount, 
                profit rate, and charity deduction. This helps you understand the timeline for achieving your desired monthly returns.
              </p>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Monthly Recurring Investment Calculator</h4>
              <p className="text-gray-600">
                Calculate the growth of regular monthly investments over time. This calculator accounts for 
                compound interest on both the accumulated value and new monthly contributions, with optional charity deductions.
              </p>
            </div>
          </div>
        </AccordionItem>

        <AccordionItem title="Step-by-Step Usage Guide" icon={<MessageCircle className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Getting Started</h4>
              <ol className="list-decimal list-inside space-y-2 text-gray-600">
                <li>Select the appropriate calculator tab based on your investment strategy</li>
                <li>Enter your investment parameters (amount, duration, profit rate)</li>
                <li>Optionally set charity deduction percentage if applicable</li>
                <li>Click the calculate button to generate results</li>
                <li>Review the summary, chart, and detailed breakdown table</li>
                <li>Export your results in your preferred format (CSV, Excel, PDF, PNG)</li>
              </ol>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Input Guidelines</h4>
              <ul className="list-disc list-inside space-y-2 text-gray-600">
                <li><strong>Investment Amount:</strong> Enter the total amount you plan to invest</li>
                <li><strong>Duration:</strong> Specify the investment period in months or years</li>
                <li><strong>Profit Rate:</strong> Enter the expected annual return rate as a percentage</li>
                <li><strong>Charity Deduction:</strong> Set the percentage of profits to be donated (optional)</li>
              </ul>
            </div>
          </div>
        </AccordionItem>

        <AccordionItem title="Sample Use Cases" icon={<BarChart3 className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">One-Time Investment Example</h4>
              <p className="text-gray-600 mb-2">
                <strong>Scenario:</strong> You have 10,000 to invest for 5 years at 8% annual return with 10% charity deduction.
              </p>
              <ul className="list-disc list-inside space-y-1 text-gray-600">
                <li>Investment Amount: 10,000</li>
                <li>Duration: 5 years</li>
                <li>Profit Rate: 8%</li>
                <li>Charity Deduction: 10%</li>
                <li><strong>Result:</strong> Final value of approximately 14,693 with 1,469 donated to charity</li>
              </ul>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Goal Planning Example</h4>
              <p className="text-gray-600 mb-2">
                <strong>Scenario:</strong> You want to achieve a monthly profit of 500 with a monthly investment of 1,000 at 6% annual return.
              </p>
              <ul className="list-disc list-inside space-y-1 text-gray-600">
                <li>Monthly Investment: 1,000</li>
                <li>Target Monthly Profit: 500</li>
                <li>Profit Rate: 6%</li>
                <li><strong>Result:</strong> It will take approximately 18 months to reach the target monthly profit</li>
              </ul>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Monthly Investment Example</h4>
              <p className="text-gray-600 mb-2">
                <strong>Scenario:</strong> You invest 500 monthly for 10 years at 7% annual return with 5% charity deduction.
              </p>
              <ul className="list-disc list-inside space-y-1 text-gray-600">
                <li>Monthly Amount: 500</li>
                <li>Duration: 120 months (10 years)</li>
                <li>Profit Rate: 7%</li>
                <li>Charity Deduction: 5%</li>
                <li><strong>Result:</strong> Final value of approximately 86,439 with 4,322 donated to charity</li>
              </ul>
            </div>
          </div>
        </AccordionItem>

        <AccordionItem title="Frequently Asked Questions" icon={<HelpCircle className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">General Questions</h4>
              <div className="space-y-3">
                <div>
                  <p className="font-medium text-gray-900">Q: How accurate are these calculations?</p>
                  <p className="text-gray-600">
                    A: The calculations use standard compound interest formulas with monthly compounding. 
                    Results are estimates and actual returns may vary based on market conditions and fees.
                  </p>
                </div>
                
                <div>
                  <p className="font-medium text-gray-900">Q: How is charity deduction calculated?</p>
                  <p className="text-gray-600">
                    A: Charity is calculated as a percentage of the profit earned each month, 
                    reducing the amount reinvested but not affecting the principal investment.
                  </p>
                </div>
              </div>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Technical Questions</h4>
              <div className="space-y-3">
                <div>
                  <p className="font-medium text-gray-900">Q: What export formats are available?</p>
                  <p className="text-gray-600">
                    A: You can export data tables as CSV or Excel files, charts as PNG images, 
                    and complete reports as PDF documents.
                  </p>
                </div>
                
                <div>
                  <p className="font-medium text-gray-900">Q: Can I save my calculations?</p>
                  <p className="text-gray-600">
                    A: Currently, the app doesn't save calculations automatically. 
                    Use the export features to save your results locally.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </AccordionItem>

        <AccordionItem title="Chart Interpretation" icon={<BarChart3 className="w-5 h-5 text-primary-600" />}>
          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Bar Charts</h4>
              <p className="text-gray-600">
                All calculators use bar charts to display monthly data. The charts show three key metrics for each month: 
                Profit (monthly earnings), Charity (monthly donations), and Investment Value (total accumulated value).
              </p>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Reading Tooltips</h4>
              <p className="text-gray-600">
                Hover over chart bars to see detailed monthly values. Tooltips display the exact amounts for Profit, 
                Charity, and Investment Value for each month, helping you understand the monthly breakdown of your investment growth.
              </p>
            </div>
            
            <div>
              <h4 className="font-semibold text-gray-900 mb-2">Chart Colors</h4>
              <p className="text-gray-600">
                Each bar represents a month, with the height indicating the Investment Value. The Profit and Charity 
                amounts are calculated as percentages of the monthly earnings and are shown in the tooltip details.
              </p>
            </div>
          </div>
        </AccordionItem>
      </div>

      {/* Export Help Section */}
      <div className="card">
        <div className="flex items-center space-x-3 mb-4">
          <Download className="w-6 h-6 text-primary-600" />
          <h3 className="text-lg font-semibold text-gray-900">Export Options</h3>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <h4 className="font-medium text-gray-900 mb-2">Data Export</h4>
            <ul className="text-sm text-gray-600 space-y-1">
              <li>• <strong>CSV:</strong> Comma-separated values for spreadsheet applications</li>
              <li>• <strong>Excel:</strong> Microsoft Excel format with formatting</li>
              <li>• <strong>PDF:</strong> Printable document with tables and summaries</li>
            </ul>
          </div>
          <div>
            <h4 className="font-medium text-gray-900 mb-2">Chart Export</h4>
            <ul className="text-sm text-gray-600 space-y-1">
              <li>• <strong>PNG:</strong> High-quality image for presentations</li>
              <li>• <strong>PDF:</strong> Vector format for printing</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HelpGuide; 
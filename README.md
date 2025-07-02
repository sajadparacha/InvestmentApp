# InvestmentApp

A comprehensive desktop-based investment planning application designed to help users manage and visualize their financial growth through various investment strategies. Built with React, TypeScript, and modern UI/UX practices.

## Features

### 🧮 Investment Calculators
- **One-Time Investment Calculator**: Calculate projected returns from lump-sum investments with optional charity deductions
- **Goal Planner**: Determine required monthly investments to reach financial goals
- **Monthly Recurring Investment Calculator**: Track growth of regular monthly investments

### 📊 Visualizations
- **Interactive Charts**: Line charts, bar charts, and pie charts for data visualization
- **Real-time Updates**: Charts update automatically as you modify inputs
- **Export Options**: Save charts as PNG images or PDF documents

### 📈 Data Management
- **Detailed Tables**: Monthly breakdown of all calculations
- **Summary Cards**: Key metrics displayed in easy-to-read format
- **Export Functionality**: Export data as CSV, Excel, or PDF

### 🎨 Modern UI/UX
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **Tabbed Interface**: Easy navigation between different calculators
- **Form Validation**: Real-time input validation with helpful error messages
- **Accessibility**: Built with accessibility best practices

## Technology Stack

- **Frontend**: React 18 with TypeScript
- **Styling**: Tailwind CSS with custom design system
- **Charts**: Recharts for data visualization
- **Forms**: React Hook Form for form management
- **Icons**: Lucide React for consistent iconography
- **Export**: jsPDF, html2canvas, and xlsx for data export

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd investmentapp
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

4. **Open your browser**
   Navigate to `http://localhost:3000` to view the application

## Usage

### One-Time Investment Calculator
1. Enter your investment amount
2. Set the duration (months or years)
3. Specify the annual profit rate
4. Optionally set charity deduction percentage
5. Click "Calculate Investment" to see results

### Goal Planner
1. Enter your target goal amount
2. Set the time frame for achieving the goal
3. Specify the expected annual return rate
4. Click "Calculate Required Investment" to see monthly investment needed

### Monthly Investment Calculator
1. Enter your monthly investment amount
2. Set the investment duration in months
3. Specify the annual profit rate
4. Optionally set charity deduction percentage
5. Click "Calculate Monthly Investment" to see growth projections

### Export Options
- **CSV**: Download data tables as comma-separated values
- **Excel**: Export with formatting for spreadsheet applications
- **PDF**: Generate printable reports with tables and summaries
- **PNG**: Save charts as high-quality images

## Project Structure

```
src/
├── components/          # React components
│   ├── OneTimeInvestment.tsx
│   ├── GoalPlanner.tsx
│   ├── MonthlyInvestment.tsx
│   └── HelpGuide.tsx
├── types/              # TypeScript type definitions
│   └── index.ts
├── utils/              # Utility functions
│   ├── calculations.ts
│   ├── validation.ts
│   └── export.ts
├── App.tsx             # Main application component
├── index.tsx           # Application entry point
└── index.css           # Global styles
```

## Key Features

### Financial Calculations
- **Compound Interest**: Monthly compounding with accurate formulas
- **Charity Deductions**: Optional profit sharing with charitable organizations
- **Goal Planning**: Reverse calculations to determine required investments
- **Recurring Investments**: Complex calculations for regular contributions

### Data Validation
- **Input Validation**: Real-time validation with helpful error messages
- **Range Checking**: Ensures values are within reasonable bounds
- **Type Safety**: Full TypeScript support for type-safe development

### Export Capabilities
- **Multiple Formats**: Support for CSV, Excel, PDF, and PNG exports
- **Chart Export**: High-quality image export for presentations
- **Data Export**: Structured data export for further analysis

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Disclaimer

This application is for educational and planning purposes only. Financial calculations are estimates and should not be considered as financial advice. Always consult with a qualified financial advisor before making investment decisions.

## Support

For support, please open an issue in the GitHub repository or contact the development team.

---

**Built with ❤️ using React, TypeScript, and Tailwind CSS**

## Application Overview

### English
This application is a user-friendly investment calculator designed to help you plan and analyze your financial goals. You can calculate returns for one-time investments, monthly recurring investments, and set specific profit goals using the Goal Planner. The app supports multiple languages and provides clear charts, tables, and summaries for easy understanding.

You can also export your results in CSV, Excel, or PDF formats for record-keeping or sharing. The app is accessible on any device, offers guidance and credits, and is ideal for anyone looking to make informed investment decisions with transparency and ease.

### Urdu
یہ ایپلیکیشن ایک آسان سرمایہ کاری کیلکولیٹر ہے جو آپ کو اپنے مالی اہداف کی منصوبہ بندی اور تجزیہ کرنے میں مدد دیتی ہے۔ آپ ایک بار کی سرمایہ کاری، ماہانہ سرمایہ کاری، اور مخصوص منافع کے اہداف کے لیے حساب لگا سکتے ہیں۔ ایپ میں مختلف زبانوں کی سہولت موجود ہے اور یہ چارٹس، ٹیبلز اور خلاصے کی صورت میں نتائج واضح طور پیش کرتی ہے۔

آپ اپنے نتائج کو CSV، ایکسل یا PDF فارمیٹ میں ایکسپورٹ بھی کر سکتے ہیں تاکہ انہیں محفوظ یا شیئر کیا جا سکے۔ یہ ایپ ہر ڈیوائس پر دستیاب ہے، رہنمائی اور کریڈٹس بھی فراہم کرتی ہے، اور شفافیت اور آسانی کے ساتھ سرمایہ کاری کے فیصلے کرنے والوں کے لیے بہترین ہے۔

### Arabic
هذا التطبيق هو آلة حاسبة استثمارية سهلة الاستخدام تساعدك في تخطيط وتحليل أهدافك المالية. يمكنك حساب العوائد للاستثمار لمرة واحدة، والاستثمار الشهري المتكرر، وتحديد أهداف ربح محددة باستخدام مخطط الأهداف. يدعم التطبيق عدة لغات ويعرض النتائج بوضوح من خلال الجداول والرسوم البيانية والملخصات.

يمكنك أيضًا تصدير نتائجك بصيغ CSV أو Excel أو PDF للاحتفاظ بها أو مشاركتها. التطبيق متاح على جميع الأجهزة، ويوفر إرشادات واعتمادات، وهو مثالي لأي شخص يرغب في اتخاذ قرارات استثمارية مدروسة بسهولة وشفافية. 
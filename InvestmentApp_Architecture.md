# Investment App - Architecture Diagram & Documentation

## 🏗️ System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           INVESTMENT APP                                    │
│                        Java Swing Application                               │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           MAIN APPLICATION                                  │
│                    profitcalculation.MainApp                               │
│  • Application entry point                                                  │
│  • Sets up FlatLaf modern UI theme                                         │
│  • Creates tabbed interface                                                 │
│  • Initializes all components                                              │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        TABBED INTERFACE                                     │
│  ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐              │
│  │ Investment      │ │ Goal Planner    │ │ One-Time        │              │
│  │ Calculator      │ │                 │ │ Investment      │              │
│  │                 │ │                 │ │                 │              │
│  └─────────────────┘ └─────────────────┘ └─────────────────┘              │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    ▼               ▼               ▼
┌─────────────────────────┐ ┌─────────────────┐ ┌─────────────────────────┐
│   INVESTMENT CALCULATOR │ │   GOAL PLANNER  │ │  ONE-TIME INVESTMENT    │
│                         │ │                 │ │                         │
│  ┌─────────────────┐    │ │ ┌─────────────┐ │ │ ┌─────────────────┐    │
│  │     VIEW        │    │ │ │    VIEW     │ │ │ │     VIEW        │    │
│  │                 │    │ │ │             │ │ │ │                 │    │
│  │ • Input Fields  │    │ │ │ • Input     │ │ │ │ • Input Fields  │    │
│  │ • Buttons       │    │ │ │   Fields    │ │ │ │ • Buttons       │    │
│  │ • Summary Panel │    │ │ │ • Buttons   │ │ │ │ • Summary Panel │    │
│  │ • Results Table │    │ │ │ • Summary   │ │ │ │ • Results Table │    │
│  │ • Results Table │    │ │ │ • Summary   │ │ │ │ • Results Table │    │
│  └─────────────────┘    │ │ │ • Table     │ │ │ └─────────────────┘    │
│           │              │ │ └─────────────┘ │ │           │              │
│           ▼              │ │         │       │ │           ▼              │
│  ┌─────────────────┐    │ │         ▼       │ │ ┌─────────────────┐    │
│  │   CONTROLLER    │    │ │ ┌─────────────┐ │ │ │   CONTROLLER    │    │
│  │                 │    │ │ │ CONTROLLER  │ │ │ │                 │    │
│  │ • Calculate     │    │ │ │             │ │ │ │ • Calculate     │    │
│  │ • Clear         │    │ │ │ • Calculate │ │ │ │ • Clear         │    │
│  │ • Export CSV    │    │ │ │ • Clear     │ │ │ │ • Export CSV    │    │
│  │ • Export PDF    │    │ │ │ • Export    │ │ │ │ • Export PDF    │    │
│  │ • Show Chart    │    │ │ │ • Chart     │ │ │ │ • Show Chart    │    │
│  │ • Help          │    │ │ │ • Help      │ │ │ │ • Help          │    │
│  └─────────────────┘    │ │ └─────────────┘ │ │ └─────────────────┘    │
│           │              │ │         │       │ │           │              │
│           ▼              │ │         ▼       │ │           ▼              │
│  ┌─────────────────┐    │ │ ┌─────────────┐ │ │ ┌─────────────────┐    │
│  │     MODEL       │    │ │ │   MODEL     │ │ │ │     MODEL       │    │
│  │                 │    │ │ │             │ │ │ │                 │    │
│  │ • Calculations  │    │ │ │ • Goal      │ │ │ │ • Target        │    │
│  │ • Data Storage  │    │ │ │   Planning  │ │ │ │   Profit Calc   │    │
│  │ • Table Model   │    │ │ │ • Months    │ │ │ │ • Months Calc   │    │
│  └─────────────────┘    │ │ │   Required  │ │ │ │ • Table Model   │    │
└─────────────────────────┘ │ └─────────────┘ │ │ └─────────────────┘    │
                            └─────────────────┘ └─────────────────────────┘
```

## 📁 Project Structure

```
InvestmentApp/
├── src/
│   └── main/
│       ├── java/
│       │   └── profitcalculation/
│       │       ├── MainApp.java                    # Application entry point
│       │       ├── controller/
│       │       │   ├── InvestmentCalculatorController.java
│       │       │   ├── GoalPlannerController.java
│       │       │   └── OneTimeInvestmentController.java
│       │       ├── model/
│       │       │   ├── InvestmentCalculatorModel.java
│       │       │   ├── GoalPlannerModel.java
│       │       │   └── OneTimeInvestmentModel.java
│       │       ├── view/
│       │       │   ├── InvestmentCalculatorView.java
│       │       │   ├── GoalPlannerView.java
│       │       │   └── OneTimeInvestmentView.java
│       │       └── util/
│       │           ├── ValidationUtil.java         # Input validation
│       │           └── PropertyLoader.java         # Properties management
│       └── resources/
│           ├── goal_planner.properties             # Default values
│           ├── investment_calculator.properties    # Default values
│           └── one_time_investment.properties      # Default values
├── pom.xml                                         # Maven configuration
└── target/                                         # Compiled output
```

## 🔄 Data Flow Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   USER INPUT    │───▶│   VALIDATION    │───▶│   CALCULATION   │
│                 │    │                 │    │                 │
│ • Investment    │    │ • Number check  │    │ • Model logic   │
│ • Profit Rate   │    │ • Range check   │    │ • Math formulas │
│ • Charity Rate  │    │ • Format check  │    │ • Iterations    │
│ • Months        │    │ • Error display │    │ • Results       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   EXPORT DATA   │◀───│   DISPLAY UI    │◀───│   RESULTS       │
│                 │    │                 │    │                 │
│ • CSV Export    │    │ • Summary Panel │    │ • Total Profit  │
│ • PDF Export    │    │ • Results Table │    │ • Total Charity │
│ • Chart Display │    │ • Chart View    │    │ • Final Amount  │
│ • Help Dialog   │    │ • Buttons       │    │ • Months        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🎯 Component Details

### 📊 Investment Calculator Tab
**Purpose**: Calculate investment growth over a fixed period
- **Inputs**: Investment Amount, Monthly Profit %, Charity %, Number of Months
- **Outputs**: Total Profit, Total Charity, Final Amount, Months Required
- **Features**: Monthly breakdown table, Stacked bar chart, CSV/PDF export

### 🎯 Goal Planner Tab  
**Purpose**: Calculate months needed to reach target profit
- **Inputs**: Target Monthly Profit, Monthly Investment, Monthly Profit Rate %, Charity Rate %
- **Outputs**: Months Required, Total Investment, Final Investment Value, Last Month's Profit
- **Features**: Monthly breakdown table, Multi-line chart, CSV/PDF export

### 🏦 One-Time Investment Tab
**Purpose**: Calculate time to reach target profit with one-time investment
- **Inputs**: Target Monthly Profit, Monthly Profit Rate %, Charity Rate %, Investment Amount
- **Outputs**: Months Required, Total Profit, Total Charity, Final Amount
- **Features**: Monthly breakdown table, Line chart, CSV/PDF export

## 🛠️ Technical Stack

### Core Technologies
- **Java 17** - Programming language
- **Java Swing** - GUI framework
- **Maven** - Build tool and dependency management

### External Libraries
- **FlatLaf** - Modern UI theme
- **JFreeChart** - Chart generation
- **iText PDF** - PDF export functionality
- **Apache Commons CSV** - CSV export functionality

### Design Patterns
- **MVC (Model-View-Controller)** - Architecture pattern
- **Observer Pattern** - Event handling
- **Factory Pattern** - UI component creation

## 🔧 Key Features

### ✅ Input Validation
- Numeric validation for all fields
- Percentage validation for rates
- Integer validation for months
- Error messages with field highlighting

### ✅ Data Export
- **CSV Export**: Spreadsheet format with all data
- **PDF Export**: Professional document with tables and formatting
- **Chart Export**: Visual representations of data

### ✅ Configuration Management
- **Property Files**: Externalized default values
- **Dynamic Loading**: Runtime configuration changes
- **Fallback Values**: Graceful error handling

### ✅ User Experience
- **Modern UI**: FlatLaf theme with consistent styling
- **Responsive Design**: Proper layout and spacing
- **Help System**: Contextual help dialogs
- **Chart Explanations**: Detailed chart interpretation

## 📈 Business Logic

### Investment Calculator
```
Monthly Profit = Current Investment × Monthly Profit Rate
Charity Amount = Monthly Profit × Charity Rate
Net Profit = Monthly Profit - Charity Amount
New Investment = Current Investment + Net Profit
```

### Goal Planner
```
Monthly Profit = Current Investment × Monthly Profit Rate
Charity Amount = Monthly Profit × Charity Rate
Net Profit = Monthly Profit - Charity Amount
New Investment = Current Investment + Monthly Investment + Net Profit
Continue until Monthly Profit ≥ Target Profit
```

### One-Time Investment
```
Monthly Profit = Current Investment × Monthly Profit Rate
Charity Amount = Monthly Profit × Charity Rate
Net Profit = Monthly Profit - Charity Amount
New Investment = Current Investment + Net Profit
Continue until Monthly Profit ≥ Target Profit
```

## 🚀 Deployment & Execution

### Build Process
```bash
mvn clean compile    # Compile the application
mvn package          # Create executable JAR
mvn exec:java        # Run the application
```

### Runtime Requirements
- **Java 17** or higher
- **Maven** for building
- **Sufficient memory** for chart generation

### Distribution
- **Shaded JAR**: All dependencies included
- **Standalone**: No external dependencies required
- **Cross-platform**: Runs on Windows, macOS, Linux

---

*This diagram represents the complete architecture of the Investment App, showing the relationships between components, data flow, and technical implementation details.* 
package profitcalculation.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.*;
import profitcalculation.util.PropertyLoader;

public class InvestmentCalculatorView extends JPanel {
    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Light Blue
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);    // Green
    private static final Color WARNING_COLOR = new Color(241, 196, 15);    // Yellow
    private static final Color DANGER_COLOR = new Color(231, 76, 60);      // Red
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);

    // Load default values from properties file
    private static final String INVESTMENT_AMOUNT_DEFAULT = PropertyLoader.getProperty("investment_calculator.properties", "investment.amount", "3800000");
    private static final String MONTHLY_PROFIT_RATE_DEFAULT = PropertyLoader.getProperty("investment_calculator.properties", "monthly.profit.rate", "2");
    private static final String CHARITY_RATE_DEFAULT = PropertyLoader.getProperty("investment_calculator.properties", "charity.rate", "15");
    private static final String NUMBER_OF_MONTHS_DEFAULT = PropertyLoader.getProperty("investment_calculator.properties", "number.of.months", "48");

    public JTextField investmentField = new JTextField(INVESTMENT_AMOUNT_DEFAULT, 12);
    public JTextField profitField = new JTextField(MONTHLY_PROFIT_RATE_DEFAULT, 12);
    public JTextField charityField = new JTextField(CHARITY_RATE_DEFAULT, 12);
    public JTextField monthsField = new JTextField(NUMBER_OF_MONTHS_DEFAULT, 12);

    public JButton calcBtn = createStyledButton(PropertyLoader.getProperty("investment_calculator.properties", "calculate.button", "🔍 Calculate"), SUCCESS_COLOR);
    public JButton clearBtn = createStyledButton(PropertyLoader.getProperty("investment_calculator.properties", "clear.button", "❌ Clear"), DANGER_COLOR);
    public JButton exportCSVBtn = createStyledButton(PropertyLoader.getProperty("investment_calculator.properties", "export.csv.button", "📃 Export to CSV"), PRIMARY_COLOR);
    public JButton exportPDFBtn = createStyledButton(PropertyLoader.getProperty("investment_calculator.properties", "export.pdf.button", "📄 Export to PDF"), PRIMARY_COLOR);
    public JButton chartBtn = createStyledButton(PropertyLoader.getProperty("investment_calculator.properties", "show.chart.button", "📊 Show Chart"), WARNING_COLOR);
    public JButton explainChartBtn = createStyledButton(PropertyLoader.getProperty("investment_calculator.properties", "explain.chart.button", "📖 Explain Chart"), PRIMARY_COLOR);
    public JButton helpBtn = createStyledButton(PropertyLoader.getProperty("investment_calculator.properties", "help.button", "❓ Help"), PRIMARY_COLOR);
    public JButton fillDefaultsBtn = createStyledButton(PropertyLoader.getProperty("investment_calculator.properties", "fill.defaults.button", "Fill Default Values"), PRIMARY_COLOR);

    public JLabel totalProfitLabel = new JLabel();
    public JLabel totalCharityLabel = new JLabel();
    public JLabel finalAmountLabel = new JLabel();
    public JLabel totalMonthsLabel = new JLabel();
    public JTable resultTable;

    public InvestmentCalculatorView(DefaultTableModel tableModel) {
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Inputs Panel
        JPanel inputPanel = createPanel(PropertyLoader.getProperty("investment_calculator.properties", "panel.title", "Investment Calculator"), true);
        inputPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add input fields with tooltips
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        addRow(inputPanel, gbc, 0, 
            PropertyLoader.getProperty("investment_calculator.properties", "investment.amount.label", "💰 Investment Amount:"), 
            investmentField, 
            PropertyLoader.getProperty("investment_calculator.properties", "investment.amount.tooltip", "Enter the total investment amount"));
        addRow(inputPanel, gbc, 1, 
            PropertyLoader.getProperty("investment_calculator.properties", "monthly.profit.rate.label", "📈 Monthly Profit %:"), 
            profitField, 
            PropertyLoader.getProperty("investment_calculator.properties", "monthly.profit.rate.tooltip", "Enter the expected monthly profit percentage"));
        addRow(inputPanel, gbc, 2, 
            PropertyLoader.getProperty("investment_calculator.properties", "charity.rate.label", "❤️ Charity % of Profit:"), 
            charityField, 
            PropertyLoader.getProperty("investment_calculator.properties", "charity.rate.tooltip", "Enter the percentage of profit to be donated to charity"));
        addRow(inputPanel, gbc, 3, 
            PropertyLoader.getProperty("investment_calculator.properties", "number.of.months.label", "📅 Number of Months:"), 
            monthsField, 
            PropertyLoader.getProperty("investment_calculator.properties", "number.of.months.tooltip", "Enter the investment duration in months"));

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.add(calcBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(helpBtn);
        buttonPanel.add(exportCSVBtn);
        buttonPanel.add(exportPDFBtn);
        buttonPanel.add(chartBtn);
        buttonPanel.add(explainChartBtn);
        buttonPanel.add(fillDefaultsBtn);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        resultTable = new JTable(tableModel);
        resultTable.setFont(MAIN_FONT);
        resultTable.setRowHeight(30);
        resultTable.setGridColor(new Color(189, 195, 199));
        resultTable.getTableHeader().setFont(HEADER_FONT);
        resultTable.getTableHeader().setBackground(PRIMARY_COLOR);
        resultTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setBorder(createTitledBorder(PropertyLoader.getProperty("investment_calculator.properties", "monthly.breakdown.title", "Monthly Breakdown")));
        tableScroll.getViewport().setBackground(PANEL_COLOR);

        JPanel summaryPanel = createPanel(PropertyLoader.getProperty("investment_calculator.properties", "summary.title", "Summary"), false);
        summaryPanel.setLayout(new GridLayout(4, 1, 10, 10));
        
        totalProfitLabel.setFont(HEADER_FONT);
        totalCharityLabel.setFont(HEADER_FONT);
        finalAmountLabel.setFont(HEADER_FONT);
        totalMonthsLabel.setFont(HEADER_FONT);
        
        // Add icons to summary labels
        totalMonthsLabel.setText("<html><span style='font-size:16px'>📅</span> " + 
            PropertyLoader.getProperty("investment_calculator.properties", "months.required.label", "Months Required: ") + 
            (totalMonthsLabel.getText().isEmpty() ? "0" : totalMonthsLabel.getText().replace("Months Required: ", "")));

        totalProfitLabel.setText("<html><span style='font-size:16px'>💰</span> " + 
            PropertyLoader.getProperty("investment_calculator.properties", "total.profit.label", "Total Profit: SAR ") + 
            (totalProfitLabel.getText().isEmpty() ? "0.00" : totalProfitLabel.getText().replace("Total Profit: SAR ", "")));
        totalCharityLabel.setText("<html><span style='font-size:16px'>❤️</span> " + 
            PropertyLoader.getProperty("investment_calculator.properties", "total.charity.label", "Total Charity: SAR ") + 
            (totalCharityLabel.getText().isEmpty() ? "0.00" : totalCharityLabel.getText().replace("Total Charity: SAR ", "")));
        finalAmountLabel.setText("<html><span style='font-size:16px'>🏦</span> " + 
            PropertyLoader.getProperty("investment_calculator.properties", "final.amount.label", "Final Amount: SAR ") + 
            (finalAmountLabel.getText().isEmpty() ? "0.00" : finalAmountLabel.getText().replace("Final Amount: SAR ", "")));
        
        summaryPanel.add(totalMonthsLabel);
        summaryPanel.add(totalProfitLabel);
        summaryPanel.add(totalCharityLabel);
        summaryPanel.add(finalAmountLabel);

        // Create a content panel to hold all components
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(tableScroll, BorderLayout.CENTER);
        contentPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createPanel(String title, boolean hasBorder) {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        if (hasBorder) {
            panel.setBorder(createTitledBorder(title));
        }
        return panel;
    }

    private Border createTitledBorder(String title) {
        Border border = BorderFactory.createLineBorder(PRIMARY_COLOR, 2);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border, title);
        titledBorder.setTitleFont(TITLE_FONT);
        titledBorder.setTitleColor(PRIMARY_COLOR);
        return titledBorder;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(MAIN_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JTextField field, String tooltip) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        
        JLabel l = new JLabel(label);
        l.setFont(HEADER_FONT);
        l.setForeground(PRIMARY_COLOR);
        panel.add(l, gbc);
        
        gbc.gridx = 1;
        field.setFont(MAIN_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setToolTipText(tooltip);
        panel.add(field, gbc);
    }

    public void showHelpDialog() {
        JDialog helpDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            PropertyLoader.getProperty("investment_calculator.properties", "help.dialog.title", "Investment Calculator Help"), true);
        helpDialog.setLayout(new BorderLayout(15, 15));
        helpDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JTextArea description = new JTextArea();
        description.setEditable(false);
        description.setBackground(PANEL_COLOR);
        description.setFont(MAIN_FONT);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setText(PropertyLoader.getProperty("investment_calculator.properties", "help.content", 
            "Welcome to the Investment Calculator! 🎉\n\nThis tool helps you see how your investment will grow over time."));

        JScrollPane scrollPane = new JScrollPane(description);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        helpDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton(PropertyLoader.getProperty("investment_calculator.properties", "help.close.button", "Got it! 👍"));
        closeButton.setFont(MAIN_FONT);
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> helpDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        helpDialog.add(buttonPanel, BorderLayout.SOUTH);

        helpDialog.setSize(500, 500);
        helpDialog.setLocationRelativeTo(this);
        helpDialog.setVisible(true);
    }

    public void showExplainChartDialog() {
        JDialog explainDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            PropertyLoader.getProperty("investment_calculator.properties", "chart.explanation.dialog.title", "Chart Explanation"), false);
        explainDialog.setLayout(new BorderLayout(15, 15));
        explainDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JTextArea explanation = new JTextArea();
        explanation.setEditable(false);
        explanation.setBackground(PANEL_COLOR);
        explanation.setFont(MAIN_FONT);
        explanation.setLineWrap(true);
        explanation.setWrapStyleWord(true);
        explanation.setText(PropertyLoader.getProperty("investment_calculator.properties", "chart.explanation.content", 
            "📊 Investment Calculator Chart Explanation\n\nThis stacked bar chart shows the monthly breakdown of your investment."));

        JScrollPane scrollPane = new JScrollPane(explanation);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        explainDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton(PropertyLoader.getProperty("investment_calculator.properties", "chart.explanation.close.button", "Got it! 👍"));
        closeButton.setFont(MAIN_FONT);
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> explainDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        explainDialog.add(buttonPanel, BorderLayout.SOUTH);

        explainDialog.setSize(500, 500);
        explainDialog.setLocationRelativeTo(this);
        explainDialog.setVisible(true);
    }
}
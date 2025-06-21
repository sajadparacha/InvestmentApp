package profitcalculation.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.*;
import profitcalculation.util.PropertyLoader;

public class OneTimeInvestmentView extends JPanel {
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
    private static final String TARGET_PROFIT_DEFAULT = PropertyLoader.getProperty("one_time_investment.properties", "target.monthly.profit", "5000");
    private static final String MONTHLY_PROFIT_RATE_DEFAULT = PropertyLoader.getProperty("one_time_investment.properties", "monthly.profit.rate", "2.5");
    private static final String CHARITY_RATE_DEFAULT = PropertyLoader.getProperty("one_time_investment.properties", "charity.rate", "10");
    private static final String INVESTMENT_AMOUNT_DEFAULT = PropertyLoader.getProperty("one_time_investment.properties", "investment.amount", "100000");

    public JTextField targetProfitField = new JTextField(TARGET_PROFIT_DEFAULT, 12);
    public JTextField monthlyRateField = new JTextField(MONTHLY_PROFIT_RATE_DEFAULT, 12);
    public JTextField charityField = new JTextField(CHARITY_RATE_DEFAULT, 12);
    public JTextField investmentField = new JTextField(INVESTMENT_AMOUNT_DEFAULT, 12);

    public JButton calculateBtn = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "calculate.button", "🔍 Calculate"), SUCCESS_COLOR);
    public JButton clearBtn = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "clear.button", "❌ Clear"), DANGER_COLOR);
    public JButton exportCSVBtn = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "export.csv.button", "📃 Export to CSV"), PRIMARY_COLOR);
    public JButton exportPDFBtn = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "export.pdf.button", "📄 Export to PDF"), PRIMARY_COLOR);
    public JButton chartBtn = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "show.chart.button", "📊 Show Chart"), WARNING_COLOR);
    public JButton explainChartBtn = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "explain.chart.button", "📖 Explain Chart"), PRIMARY_COLOR);
    public JButton helpBtn = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "help.button", "❓ Help"), PRIMARY_COLOR);
    public JButton fillDefaultsBtn = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "fill.defaults.button", "Fill Default Values"), PRIMARY_COLOR);

    public JLabel monthsLabel = new JLabel();
    public JLabel totalProfitLabel = new JLabel();
    public JLabel totalCharityLabel = new JLabel();
    public JLabel finalAmountLabel = new JLabel();
    public JTable resultTable;

    public OneTimeInvestmentView() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Inputs Panel
        JPanel inputPanel = createPanel(PropertyLoader.getProperty("one_time_investment.properties", "panel.title", "One-Time Investment Calculator"), true);
        inputPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add input fields with tooltips
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        addRow(inputPanel, gbc, 0, 
            PropertyLoader.getProperty("one_time_investment.properties", "target.monthly.profit.label", "📈 Target Monthly Profit:"), 
            targetProfitField, 
            PropertyLoader.getProperty("one_time_investment.properties", "target.monthly.profit.tooltip", "Enter the monthly profit you want to achieve"));
        addRow(inputPanel, gbc, 1, 
            PropertyLoader.getProperty("one_time_investment.properties", "monthly.profit.rate.label", "📈 Monthly Profit Rate %:"), 
            monthlyRateField, 
            PropertyLoader.getProperty("one_time_investment.properties", "monthly.profit.rate.tooltip", "Enter the expected monthly profit percentage"));
        addRow(inputPanel, gbc, 2, 
            PropertyLoader.getProperty("one_time_investment.properties", "charity.rate.label", "❤️ Charity Rate %:"), 
            charityField, 
            PropertyLoader.getProperty("one_time_investment.properties", "charity.rate.tooltip", "Enter the percentage of profit to be donated to charity"));
        addRow(inputPanel, gbc, 3, 
            PropertyLoader.getProperty("one_time_investment.properties", "investment.amount.label", "💰 One-time Investment:"), 
            investmentField, 
            PropertyLoader.getProperty("one_time_investment.properties", "investment.amount.tooltip", "Enter the one-time investment amount (multiples of 500)"));

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.add(calculateBtn);
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

        resultTable = new JTable();
        resultTable.setFont(MAIN_FONT);
        resultTable.setRowHeight(30);
        resultTable.setGridColor(new Color(189, 195, 199));
        resultTable.getTableHeader().setFont(HEADER_FONT);
        resultTable.getTableHeader().setBackground(PRIMARY_COLOR);
        resultTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setBorder(createTitledBorder(PropertyLoader.getProperty("one_time_investment.properties", "monthly.breakdown.title", "Monthly Breakdown")));
        tableScroll.getViewport().setBackground(PANEL_COLOR);

        JPanel summaryPanel = createPanel(PropertyLoader.getProperty("one_time_investment.properties", "summary.title", "Summary"), false);
        summaryPanel.setLayout(new GridLayout(4, 1, 10, 10));
        
        monthsLabel.setFont(HEADER_FONT);
        totalProfitLabel.setFont(HEADER_FONT);
        totalCharityLabel.setFont(HEADER_FONT);
        finalAmountLabel.setFont(HEADER_FONT);
        
        // Add icons to summary labels
        monthsLabel.setText("<html><span style='font-size:16px'>📅</span> " + 
            PropertyLoader.getProperty("one_time_investment.properties", "months.required.label", "Months Required: ") + "0");
        totalProfitLabel.setText("<html><span style='font-size:16px'>💰</span> " + 
            PropertyLoader.getProperty("one_time_investment.properties", "total.profit.label", "Total Profit: SAR ") + "0.00");
        totalCharityLabel.setText("<html><span style='font-size:16px'>❤️</span> " + 
            PropertyLoader.getProperty("one_time_investment.properties", "total.charity.label", "Total Charity: SAR ") + "0.00");
        finalAmountLabel.setText("<html><span style='font-size:16px'>🏦</span> " + 
            PropertyLoader.getProperty("one_time_investment.properties", "final.amount.label", "Final Amount: SAR ") + "0.00");
        
        summaryPanel.add(monthsLabel);
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
            PropertyLoader.getProperty("one_time_investment.properties", "help.dialog.title", "One-Time Investment Calculator Help"), true);
        helpDialog.setLayout(new BorderLayout(15, 15));
        helpDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JTextArea description = new JTextArea();
        description.setEditable(false);
        description.setBackground(PANEL_COLOR);
        description.setFont(MAIN_FONT);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setText(PropertyLoader.getProperty("one_time_investment.properties", "help.content", 
            "Welcome to the One-Time Investment Calculator! 🎉\n\nThis tool helps you determine how long it will take to reach your target monthly profit with a one-time investment."));

        JScrollPane scrollPane = new JScrollPane(description);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton closeButton = createStyledButton(PropertyLoader.getProperty("one_time_investment.properties", "help.close.button", "Close"), SUCCESS_COLOR);
        closeButton.addActionListener(e -> helpDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);

        helpDialog.add(scrollPane, BorderLayout.CENTER);
        helpDialog.add(buttonPanel, BorderLayout.SOUTH);

        helpDialog.setSize(600, 500);
        helpDialog.setLocationRelativeTo(this);
        helpDialog.setVisible(true);
    }
} 
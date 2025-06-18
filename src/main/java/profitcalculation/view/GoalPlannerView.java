package profitcalculation.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.*;

public class GoalPlannerView extends JPanel {
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

    public JTextField goalProfitField = new JTextField("6000", 12);
    public JTextField monthlyInvestmentField = new JTextField("2000", 12);
    public JTextField monthlyRateField = new JTextField("2", 12);

    public JButton calculateBtn = createStyledButton("\uD83D\uDD0D Calculate", SUCCESS_COLOR);
    public JButton clearBtn = createStyledButton("\u274C Clear", DANGER_COLOR);
    public JButton exportCSVBtn = createStyledButton("\uD83D\uDCC3 Export to CSV", PRIMARY_COLOR);
    public JButton exportPDFBtn = createStyledButton("\uD83D\uDCC5 Export to PDF", PRIMARY_COLOR);
    public JButton chartBtn = createStyledButton("\uD83D\uDCCA Show Chart", WARNING_COLOR);
    public JButton helpBtn = createStyledButton("\u2753 Help", PRIMARY_COLOR);
    public JButton fillDefaultsBtn = createStyledButton("Fill Default Values", PRIMARY_COLOR);

    public JLabel monthsLabel = new JLabel();
    public JLabel totalInvestmentLabel = new JLabel();
    public JLabel investmentValueLabel = new JLabel();
    public JLabel lastProfitLabel = new JLabel();
    public JTable resultTable;

    public GoalPlannerView(DefaultTableModel tableModel) {
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Inputs Panel
        JPanel inputPanel = createPanel("Goal Planner", true);
        inputPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add input fields with tooltips
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        addRow(inputPanel, gbc, 0, "\uD83D\uDCB0 Target Monthly Profit (SAR):", goalProfitField,
            "Enter your target monthly profit amount");
        addRow(inputPanel, gbc, 1, "\uD83D\uDCB5 Monthly Investment (SAR):", monthlyInvestmentField,
            "Enter how much you can invest monthly");
        addRow(inputPanel, gbc, 2, "\uD83D\uDCC8 Monthly Profit Rate (%):", monthlyRateField,
            "Enter the expected monthly profit percentage");

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.add(calculateBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(helpBtn);
        buttonPanel.add(exportCSVBtn);
        buttonPanel.add(exportPDFBtn);
        buttonPanel.add(chartBtn);
        buttonPanel.add(fillDefaultsBtn);

        gbc.gridy = 3;
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
        tableScroll.setBorder(createTitledBorder("Monthly Breakdown"));
        tableScroll.getViewport().setBackground(PANEL_COLOR);

        JPanel summaryPanel = createPanel("Summary", false);
        summaryPanel.setLayout(new GridLayout(4, 1, 10, 10));
        
        monthsLabel.setFont(HEADER_FONT);
        totalInvestmentLabel.setFont(HEADER_FONT);
        investmentValueLabel.setFont(HEADER_FONT);
        lastProfitLabel.setFont(HEADER_FONT);
        
        summaryPanel.add(monthsLabel);
        summaryPanel.add(totalInvestmentLabel);
        summaryPanel.add(investmentValueLabel);
        summaryPanel.add(lastProfitLabel);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        // Add help button action
        helpBtn.addActionListener(e -> showHelpDialog());
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
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
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

    private void showHelpDialog() {
        JDialog helpDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Goal Planner Help", true);
        helpDialog.setLayout(new BorderLayout(15, 15));
        helpDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JTextArea description = new JTextArea();
        description.setEditable(false);
        description.setBackground(PANEL_COLOR);
        description.setFont(MAIN_FONT);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setText(
            "Welcome to the Goal Planner! 🎯\n\n" +
            "This tool helps you figure out how long it will take to reach your profit goal.\n\n" +
            "Just tell us:\n\n" +
            "1️⃣ Target Monthly Profit\n" +
            "   How much money you want to earn each month\n\n" +
            "2️⃣ Monthly Investment\n" +
            "   How much you can invest every month\n\n" +
            "3️⃣ Monthly Profit Rate\n" +
            "   How much profit you expect to earn each month\n\n" +
            "After clicking Calculate, you'll see:\n" +
            "✓ How many months until you reach your goal\n" +
            "✓ Total amount you'll need to invest\n" +
            "✓ Your final investment value\n" +
            "✓ Your last month's profit\n\n" +
            "Want to save your plan?\n" +
            "→ Click 'Export to CSV' to save as a spreadsheet\n" +
            "→ Click 'Export to PDF' to save as a document\n" +
            "→ Click 'Show Chart' to see your progress visually"
        );
        description.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(description);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        helpDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = createStyledButton("Got it! 👍", PRIMARY_COLOR);
        closeButton.addActionListener(e -> helpDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        helpDialog.add(buttonPanel, BorderLayout.SOUTH);

        helpDialog.pack();
        helpDialog.setSize(500, 500);
        helpDialog.setLocationRelativeTo(this);
        helpDialog.setVisible(true);
    }
}
package profitcalculation.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GoalPlannerView extends JPanel {
    public JTextField goalProfitField = new JTextField("6000", 12);
    public JTextField monthlyInvestmentField = new JTextField("2000", 12);
    public JTextField monthlyRateField = new JTextField("2", 12);

    public JButton calculateBtn = new JButton("\uD83D\uDD0D Calculate");
    public JButton clearBtn = new JButton("\u274C Clear");
    public JButton exportCSVBtn = new JButton("\uD83D\uDCC3 Export to CSV");
    public JButton exportPDFBtn = new JButton("\uD83D\uDCC5 Export to PDF");
    public JButton chartBtn = new JButton("\uD83D\uDCCA Show Chart");

    public JLabel monthsLabel = new JLabel();
    public JLabel totalInvestmentLabel = new JLabel();
    public JLabel investmentValueLabel = new JLabel();
    public JLabel lastProfitLabel = new JLabel();
    public JTable resultTable;

    public GoalPlannerView(DefaultTableModel tableModel) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Goal Planner Inputs"));
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(inputPanel, gbc, 0, "Target Monthly Profit (SAR):", goalProfitField);
        addRow(inputPanel, gbc, 1, "Monthly Investment (SAR):", monthlyInvestmentField);
        addRow(inputPanel, gbc, 2, "Monthly Profit Rate (%):", monthlyRateField);

        gbc.gridy = 3; gbc.gridx = 0; inputPanel.add(calculateBtn, gbc);
        gbc.gridx = 1; inputPanel.add(clearBtn, gbc);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; inputPanel.add(exportCSVBtn, gbc);
        gbc.gridy = 5; inputPanel.add(exportPDFBtn, gbc);
        gbc.gridy = 6; inputPanel.add(chartBtn, gbc);

        resultTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Monthly Breakdown"));
        resultTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
        resultTable.setRowHeight(24);

        JPanel summaryPanel = new JPanel(new GridLayout(4, 1, 10, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.setBackground(Color.WHITE);
        monthsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalInvestmentLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        investmentValueLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        lastProfitLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        summaryPanel.add(monthsLabel);
        summaryPanel.add(totalInvestmentLabel);
        summaryPanel.add(investmentValueLabel);
        summaryPanel.add(lastProfitLabel);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        JLabel l = new JLabel(label); l.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(l, gbc);
        gbc.gridx = 1;
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(field, gbc);
    }
}
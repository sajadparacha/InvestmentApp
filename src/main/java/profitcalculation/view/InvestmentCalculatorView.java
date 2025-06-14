package profitcalculation.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InvestmentCalculatorView extends JPanel {
    public JTextField investmentField = new JTextField("3800000", 12);
    public JTextField profitField = new JTextField("2", 12);
    public JTextField charityField = new JTextField("15", 12);
    public JTextField monthsField = new JTextField("48", 12);

    public JButton calcBtn = new JButton("\uD83D\uDD0D Calculate");
    public JButton clearBtn = new JButton("\u274C Clear");
    public JButton exportCSVBtn = new JButton("\uD83D\uDCC3 Export to CSV");
    public JButton exportPDFBtn = new JButton("\uD83D\uDCC5 Export to PDF");
    public JButton chartBtn = new JButton("\uD83D\uDCCA Show Chart");

    public JLabel totalProfitLabel = new JLabel();
    public JLabel totalCharityLabel = new JLabel();
    public JLabel finalAmountLabel = new JLabel();
    public JTable resultTable;

    public InvestmentCalculatorView(DefaultTableModel tableModel) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Inputs
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Calculator Inputs"));
        inputPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(inputPanel, gbc, 0, "\uD83D\uDCB0 Investment Amount:", investmentField);
        addRow(inputPanel, gbc, 1, "\uD83D\uDCC8 Monthly Profit %:", profitField);
        addRow(inputPanel, gbc, 2, "\u2764\uFE0F Charity % of Profit:", charityField);
        addRow(inputPanel, gbc, 3, "\uD83D\uDCC6 Number of Months:", monthsField);

        gbc.gridy = 4; gbc.gridx = 0; inputPanel.add(calcBtn, gbc);
        gbc.gridx = 1; inputPanel.add(clearBtn, gbc);
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2; inputPanel.add(exportCSVBtn, gbc);
        gbc.gridy = 6; inputPanel.add(exportPDFBtn, gbc);
        gbc.gridy = 7; inputPanel.add(chartBtn, gbc);

        resultTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Monthly Breakdown"));
        resultTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
        resultTable.setRowHeight(24);

        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 10, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.setBackground(Color.WHITE);
        totalProfitLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalCharityLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        finalAmountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        summaryPanel.add(totalProfitLabel);
        summaryPanel.add(totalCharityLabel);
        summaryPanel.add(finalAmountLabel);

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
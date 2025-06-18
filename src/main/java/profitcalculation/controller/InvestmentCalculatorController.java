package profitcalculation.controller;

import profitcalculation.model.InvestmentCalculatorModel;
import profitcalculation.view.InvestmentCalculatorView;
import profitcalculation.util.ValidationUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

public class InvestmentCalculatorController {
    private final InvestmentCalculatorModel model;
    private final InvestmentCalculatorView view;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public InvestmentCalculatorController(InvestmentCalculatorModel model, InvestmentCalculatorView view) {
        this.model = model;
        this.view = view;

        // Add action listeners
        view.calcBtn.addActionListener(new CalculateListener());
        view.clearBtn.addActionListener(new ClearListener());
        view.exportCSVBtn.addActionListener(new ExportCSVListener());
        view.exportPDFBtn.addActionListener(new ExportPDFListener());
        view.chartBtn.addActionListener(new ChartListener());
        view.explainChartBtn.addActionListener(new ExplainChartListener());
        view.helpBtn.addActionListener(new HelpListener());
        view.fillDefaultsBtn.addActionListener(new FillDefaultsListener());
    }

    private class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder errorMessage = new StringBuilder();
            
            // Validate all fields
            boolean isValid = ValidationUtil.validateNumberField(view.investmentField, "Investment Amount", errorMessage) &&
                            ValidationUtil.validatePercentageField(view.profitField, "Monthly Profit", errorMessage) &&
                            ValidationUtil.validatePercentageField(view.charityField, "Charity Percentage", errorMessage) &&
                            ValidationUtil.validateIntegerField(view.monthsField, "Number of Months", errorMessage);

            if (!isValid) {
                ValidationUtil.showValidationError(errorMessage.toString());
                return;
            }

            try {
                // Parse validated values
                double investment = decimalFormat.parse(view.investmentField.getText().trim()).doubleValue();
                double profitRate = decimalFormat.parse(view.profitField.getText().trim()).doubleValue();
                double charityRate = decimalFormat.parse(view.charityField.getText().trim()).doubleValue();
                int months = Integer.parseInt(view.monthsField.getText().trim());

                // Calculate results
                model.calculate(investment, profitRate, charityRate, months);

                // Update view with results
                view.totalProfitLabel.setText("<html><span style='font-size:16px'>💰</span> Total Profit: SAR " + decimalFormat.format(model.getTotalProfit()));
                view.totalCharityLabel.setText("<html><span style='font-size:16px'>❤️</span> Total Charity: SAR " + decimalFormat.format(model.getTotalCharity()));
                view.finalAmountLabel.setText("<html><span style='font-size:16px'>🏦</span> Final Amount: SAR " + decimalFormat.format(model.getFinalAmount()));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, 
                    "An error occurred during calculation: " + ex.getMessage(),
                    "Calculation Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.investmentField.setText("");
            view.profitField.setText("");
            view.charityField.setText("");
            view.monthsField.setText("");
            view.totalProfitLabel.setText("<html><span style='font-size:16px'>💰</span> Total Profit: SAR 0.00");
            view.totalCharityLabel.setText("<html><span style='font-size:16px'>❤️</span> Total Charity: SAR 0.00");
            view.finalAmountLabel.setText("<html><span style='font-size:16px'>🏦</span> Final Amount: SAR 0.00");
            model.clear();
        }
    }

    private class ExportCSVListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getTableModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(view, "Please calculate results first.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Export to CSV");
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
            fileChooser.setSelectedFile(new File("investment_calculator_results.csv"));

            int result = fileChooser.showSaveDialog(view);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new File(file.getAbsolutePath() + ".csv");
                }

                try (FileWriter writer = new FileWriter(file)) {
                    // Write header
                    writer.write("Investment Calculator Results\n");
                    writer.write("Generated on: " + java.time.LocalDateTime.now() + "\n\n");
                    
                    // Write input parameters
                    writer.write("Input Parameters:\n");
                    writer.write("Investment Amount," + view.investmentField.getText() + "\n");
                    writer.write("Monthly Profit %," + view.profitField.getText() + "\n");
                    writer.write("Charity %," + view.charityField.getText() + "\n");
                    writer.write("Number of Months," + view.monthsField.getText() + "\n\n");
                    
                    // Write summary
                    writer.write("Summary:\n");
                    writer.write("Total Profit," + decimalFormat.format(model.getTotalProfit()) + "\n");
                    writer.write("Total Charity," + decimalFormat.format(model.getTotalCharity()) + "\n");
                    writer.write("Final Amount," + decimalFormat.format(model.getFinalAmount()) + "\n\n");
                    
                    // Write table data
                    writer.write("Monthly Breakdown:\n");
                    writer.write("Month,Profit,Charity,Investment Value\n");
                    
                    for (int i = 0; i < model.getTableModel().getRowCount(); i++) {
                        writer.write(model.getTableModel().getValueAt(i, 0) + "," +
                                   model.getTableModel().getValueAt(i, 1) + "," +
                                   model.getTableModel().getValueAt(i, 2) + "," +
                                   model.getTableModel().getValueAt(i, 3) + "\n");
                    }

                    JOptionPane.showMessageDialog(view, 
                        "CSV file exported successfully to:\n" + file.getAbsolutePath(),
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, 
                        "Error exporting CSV: " + ex.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class ExportPDFListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getTableModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(view, "Please calculate results first.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Export to PDF");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));
            fileChooser.setSelectedFile(new File("investment_calculator_results.pdf"));

            int result = fileChooser.showSaveDialog(view);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".pdf")) {
                    file = new File(file.getAbsolutePath() + ".pdf");
                }

                try {
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();

                    // Add title
                    com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
                    Paragraph title = new Paragraph("Investment Calculator Results", titleFont);
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);
                    document.add(new Paragraph(" ")); // Spacing

                    // Add generation date
                    com.itextpdf.text.Font dateFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.ITALIC);
                    Paragraph date = new Paragraph("Generated on: " + java.time.LocalDateTime.now(), dateFont);
                    date.setAlignment(Element.ALIGN_CENTER);
                    document.add(date);
                    document.add(new Paragraph(" ")); // Spacing

                    // Add input parameters
                    com.itextpdf.text.Font sectionFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
                    document.add(new Paragraph("Input Parameters:", sectionFont));
                    document.add(new Paragraph("Investment Amount: " + view.investmentField.getText()));
                    document.add(new Paragraph("Monthly Profit %: " + view.profitField.getText()));
                    document.add(new Paragraph("Charity %: " + view.charityField.getText()));
                    document.add(new Paragraph("Number of Months: " + view.monthsField.getText()));
                    document.add(new Paragraph(" ")); // Spacing

                    // Add summary
                    document.add(new Paragraph("Summary:", sectionFont));
                    document.add(new Paragraph("Total Profit: SAR " + decimalFormat.format(model.getTotalProfit())));
                    document.add(new Paragraph("Total Charity: SAR " + decimalFormat.format(model.getTotalCharity())));
                    document.add(new Paragraph("Final Amount: SAR " + decimalFormat.format(model.getFinalAmount())));
                    document.add(new Paragraph(" ")); // Spacing

                    // Add table
                    document.add(new Paragraph("Monthly Breakdown:", sectionFont));
                    PdfPTable table = new PdfPTable(4);
                    table.setWidthPercentage(100);

                    // Add headers
                    com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
                    table.addCell(new PdfPCell(new Phrase("Month", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Profit", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Charity", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Investment Value", headerFont)));

                    // Add data
                    com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);
                    for (int i = 0; i < model.getTableModel().getRowCount(); i++) {
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 0).toString(), dataFont)));
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 1).toString(), dataFont)));
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 2).toString(), dataFont)));
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 3).toString(), dataFont)));
                    }

                    document.add(table);
                    document.close();

                    JOptionPane.showMessageDialog(view, 
                        "PDF file exported successfully to:\n" + file.getAbsolutePath(),
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, 
                        "Error exporting PDF: " + ex.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class ChartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getTableModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(view, "Please calculate results first.");
                return;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (int i = 0; i < model.getTableModel().getRowCount(); i++) {
                String month = model.getTableModel().getValueAt(i, 0).toString();
                double profit = Double.parseDouble(model.getTableModel().getValueAt(i, 1).toString());
                double charity = Double.parseDouble(model.getTableModel().getValueAt(i, 2).toString());
                double investmentValue = Double.parseDouble(model.getTableModel().getValueAt(i, 3).toString());

                // Calculate the base investment value (investment value minus profit)
                double baseInvestment = investmentValue - profit;

                dataset.addValue(baseInvestment, "Investment Value", month);
                dataset.addValue(profit - charity, "Net Profit", month);
                dataset.addValue(charity, "Charity", month);
            }

            JFreeChart chart = ChartFactory.createStackedBarChart(
                "Investment Calculator - Monthly Breakdown",
                "Month",
                "Amount (SAR)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
            );

            CategoryPlot plot = chart.getCategoryPlot();
            
            // Configure renderer for better visualization
            org.jfree.chart.renderer.category.StackedBarRenderer renderer = 
                new org.jfree.chart.renderer.category.StackedBarRenderer();
            
            // Set colors for each series
            renderer.setSeriesPaint(0, new Color(52, 152, 219));  // Blue for Investment Value
            renderer.setSeriesPaint(1, new Color(46, 204, 113));  // Green for Net Profit
            renderer.setSeriesPaint(2, new Color(231, 76, 60));   // Red for Charity
            
            // Set minimum bar width and height for better clickability
            renderer.setMinimumBarLength(0.2); // Minimum 20% of available space
            renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
            
            // Add tooltips
            renderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
            
            plot.setRenderer(renderer);
            
            // Configure axis
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setNumberFormatOverride(new java.text.DecimalFormat("#,##0"));
            
            // Set background
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(new Color(200, 200, 200));
            plot.setDomainGridlinePaint(new Color(200, 200, 200));

            ChartPanel chartPanel = new ChartPanel(chart);
            
            // Create button panel for chart
            JPanel chartButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            chartButtonPanel.setBackground(Color.WHITE);
            
            JButton explainButton = new JButton("📖 Explain This Chart");
            explainButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            explainButton.setBackground(new Color(41, 128, 185));
            explainButton.setForeground(Color.WHITE);
            explainButton.setFocusPainted(false);
            explainButton.addActionListener(evt -> showChartExplanationDialog());
            
            chartButtonPanel.add(explainButton);
            
            // Create main panel to hold chart and button
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(chartPanel, BorderLayout.CENTER);
            mainPanel.add(chartButtonPanel, BorderLayout.SOUTH);
            
            JFrame chartFrame = new JFrame("Investment Calculator - Monthly Analysis");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chartFrame.setContentPane(mainPanel);
            chartFrame.setSize(1000, 650);
            chartFrame.setLocationRelativeTo(null);
            chartFrame.setVisible(true);
        }
    }

    private void showChartExplanationDialog() {
        JDialog explainDialog = new JDialog((Frame) null, "Chart Explanation", false);
        explainDialog.setLayout(new BorderLayout(15, 15));
        explainDialog.getContentPane().setBackground(new Color(236, 240, 241));

        JTextArea explanation = new JTextArea();
        explanation.setEditable(false);
        explanation.setBackground(Color.WHITE);
        explanation.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        explanation.setLineWrap(true);
        explanation.setWrapStyleWord(true);
        explanation.setText(
            "📊 Investment Calculator Chart Explanation\n\n" +
            "This stacked bar chart shows the monthly breakdown of your investment:\n\n" +
            "🔵 BLUE (Bottom) - Investment Value:\n" +
            "   • Your base investment amount for each month\n" +
            "   • This is the foundation of your investment\n" +
            "   • Grows as you add profits to your investment\n\n" +
            "🟢 GREEN (Middle) - Net Profit:\n" +
            "   • The profit you keep after charity deductions\n" +
            "   • This is added to your investment value\n" +
            "   • Shows your actual earnings\n\n" +
            "🔴 RED (Top) - Charity:\n" +
            "   • The amount donated to charity each month\n" +
            "   • Deducted from your monthly profit\n" +
            "   • Shows your charitable contributions\n\n" +
            "📈 What to Look For:\n" +
            "• Total bar height = Total investment value\n" +
            "• Growing bars = Your investment is growing\n" +
            "• Green section = Your net earnings\n" +
            "• Red section = Your charitable impact\n\n" +
            "💡 Tips:\n" +
            "• Hover over bars to see exact values\n" +
            "• Compare months to see growth trends\n" +
            "• The chart shows your complete financial picture"
        );

        JScrollPane scrollPane = new JScrollPane(explanation);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        explainDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Got it! 👍");
        closeButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        closeButton.setBackground(new Color(41, 128, 185));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(evt -> explainDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(236, 240, 241));
        buttonPanel.add(closeButton);
        explainDialog.add(buttonPanel, BorderLayout.SOUTH);

        explainDialog.setSize(500, 500);
        explainDialog.setLocationRelativeTo(null);
        explainDialog.setVisible(true);
    }

    private class ExplainChartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.showExplainChartDialog();
        }
    }

    private class HelpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.showHelpDialog();
        }
    }

    private class FillDefaultsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.investmentField.setText("3800000");
            view.profitField.setText("2");
            view.charityField.setText("15");
            view.monthsField.setText("48");
        }
    }
}
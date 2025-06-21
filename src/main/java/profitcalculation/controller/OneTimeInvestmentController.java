package profitcalculation.controller;

import profitcalculation.model.OneTimeInvestmentModel;
import profitcalculation.view.OneTimeInvestmentView;
import profitcalculation.util.ValidationUtil;
import profitcalculation.util.PropertyLoader;

import javax.swing.*;
import java.awt.*;
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

public class OneTimeInvestmentController {
    private final OneTimeInvestmentModel model;
    private final OneTimeInvestmentView view;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public OneTimeInvestmentController(OneTimeInvestmentModel model, OneTimeInvestmentView view) {
        this.model = model;
        this.view = view;
        view.resultTable.setModel(model.getTableModel());

        // Add action listeners
        view.calculateBtn.addActionListener(new CalculateListener());
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
            boolean isValid = ValidationUtil.validateNumberField(view.targetProfitField, "Target Monthly Profit", errorMessage) &&
                            ValidationUtil.validatePercentageField(view.monthlyRateField, "Monthly Profit Rate", errorMessage) &&
                            ValidationUtil.validatePercentageField(view.charityField, "Charity Rate", errorMessage) &&
                            validateInvestmentField(errorMessage);

            if (!isValid) {
                ValidationUtil.showValidationError(errorMessage.toString());
                return;
            }

            try {
                // Parse validated values
                double targetProfit = decimalFormat.parse(view.targetProfitField.getText().trim()).doubleValue();
                double monthlyRate = decimalFormat.parse(view.monthlyRateField.getText().trim()).doubleValue();
                double charityRate = decimalFormat.parse(view.charityField.getText().trim()).doubleValue();
                double investment = decimalFormat.parse(view.investmentField.getText().trim()).doubleValue();

                // Calculate results
                model.calculate(investment, targetProfit, monthlyRate, charityRate);

                // Update view with results
                view.monthsLabel.setText("<html><span style='font-size:16px'>📅</span> " + 
                    PropertyLoader.getProperty("one_time_investment.properties", "months.required.label", "Months Required: ") + 
                    model.getMonthsRequired());
                view.totalProfitLabel.setText("<html><span style='font-size:16px'>💰</span> " + 
                    PropertyLoader.getProperty("one_time_investment.properties", "total.profit.label", "Total Profit: SAR ") + 
                    decimalFormat.format(model.getTotalProfit()));
                view.totalCharityLabel.setText("<html><span style='font-size:16px'>❤️</span> " + 
                    PropertyLoader.getProperty("one_time_investment.properties", "total.charity.label", "Total Charity: SAR ") + 
                    decimalFormat.format(model.getTotalCharity()));
                view.finalAmountLabel.setText("<html><span style='font-size:16px'>🏦</span> " + 
                    PropertyLoader.getProperty("one_time_investment.properties", "final.amount.label", "Final Amount: SAR ") + 
                    decimalFormat.format(model.getFinalAmount()));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, 
                    PropertyLoader.getProperty("one_time_investment.properties", "calculation.error.message", "An error occurred during calculation: {0}").replace("{0}", ex.getMessage()),
                    PropertyLoader.getProperty("one_time_investment.properties", "calculation.error.title", "Calculation Error"),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInvestmentField(StringBuilder errorMessage) {
        String text = view.investmentField.getText().trim();
        if (text.isEmpty()) {
            errorMessage.append("• Investment Amount: Field cannot be empty\n");
            return false;
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (Exception e) {
            errorMessage.append("• Investment Amount: Must be a valid number\n");
            return false;
        }
    }

    private class ClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.targetProfitField.setText("");
            view.monthlyRateField.setText("");
            view.charityField.setText("");
            view.investmentField.setText("");
            view.monthsLabel.setText("<html><span style='font-size:16px'>📅</span> " + 
                PropertyLoader.getProperty("one_time_investment.properties", "months.required.label", "Months Required: ") + "0");
            view.totalProfitLabel.setText("<html><span style='font-size:16px'>💰</span> " + 
                PropertyLoader.getProperty("one_time_investment.properties", "total.profit.label", "Total Profit: SAR ") + "0.00");
            view.totalCharityLabel.setText("<html><span style='font-size:16px'>❤️</span> " + 
                PropertyLoader.getProperty("one_time_investment.properties", "total.charity.label", "Total Charity: SAR ") + "0.00");
            view.finalAmountLabel.setText("<html><span style='font-size:16px'>🏦</span> " + 
                PropertyLoader.getProperty("one_time_investment.properties", "final.amount.label", "Final Amount: SAR ") + "0.00");
            model.clear();
        }
    }

    private class ExportCSVListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getTableModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(view, 
                    PropertyLoader.getProperty("one_time_investment.properties", "please.calculate.first", "Please calculate results first."));
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(PropertyLoader.getProperty("one_time_investment.properties", "export.csv.dialog.title", "Export to CSV"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
            fileChooser.setSelectedFile(new File("one_time_investment_results.csv"));

            int result = fileChooser.showSaveDialog(view);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new File(file.getAbsolutePath() + ".csv");
                }

                try (FileWriter writer = new FileWriter(file)) {
                    // Write header
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.title", "One-Time Investment Results") + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.generated.on", "Generated on: {0}").replace("{0}", java.time.LocalDateTime.now().toString()) + "\n\n");
                    
                    // Write input parameters
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.input.parameters", "Input Parameters:") + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.target.monthly.profit", "Target Monthly Profit") + "," + view.targetProfitField.getText() + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.monthly.profit.rate", "Monthly Profit Rate %") + "," + view.monthlyRateField.getText() + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.charity.rate", "Charity Rate %") + "," + view.charityField.getText() + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.investment.amount", "Investment Amount") + "," + view.investmentField.getText() + "\n\n");
                    
                    // Write summary
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.summary", "Summary:") + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.months.required", "Months Required") + "," + model.getMonthsRequired() + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.total.profit", "Total Profit") + "," + decimalFormat.format(model.getTotalProfit()) + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.total.charity", "Total Charity") + "," + decimalFormat.format(model.getTotalCharity()) + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.final.amount", "Final Amount") + "," + decimalFormat.format(model.getFinalAmount()) + "\n\n");
                    
                    // Write table data
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.monthly.breakdown", "Monthly Breakdown:") + "\n");
                    writer.write(PropertyLoader.getProperty("one_time_investment.properties", "csv.month", "Month") + "," +
                               PropertyLoader.getProperty("one_time_investment.properties", "csv.investment.value", "Investment Value") + "," +
                               PropertyLoader.getProperty("one_time_investment.properties", "csv.profit", "Profit") + "," +
                               PropertyLoader.getProperty("one_time_investment.properties", "csv.charity", "Charity") + "," +
                               "Remaining Profit\n");
                    
                    for (int i = 0; i < model.getTableModel().getRowCount(); i++) {
                        writer.write(model.getTableModel().getValueAt(i, 0) + "," +
                                   model.getTableModel().getValueAt(i, 1) + "," +
                                   model.getTableModel().getValueAt(i, 2) + "," +
                                   model.getTableModel().getValueAt(i, 3) + "," +
                                   model.getTableModel().getValueAt(i, 4) + "\n");
                    }

                    JOptionPane.showMessageDialog(view, 
                        PropertyLoader.getProperty("one_time_investment.properties", "export.success.message", "CSV file exported successfully to:\n{0}").replace("{0}", file.getAbsolutePath()),
                        PropertyLoader.getProperty("one_time_investment.properties", "export.success.title", "Export Successful"),
                        JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, 
                        PropertyLoader.getProperty("one_time_investment.properties", "export.error.message", "Error exporting CSV: {0}").replace("{0}", ex.getMessage()),
                        PropertyLoader.getProperty("one_time_investment.properties", "export.error.title", "Export Error"),
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
            fileChooser.setSelectedFile(new File("one_time_investment_results.pdf"));

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
                    Paragraph title = new Paragraph("One-Time Investment Results", titleFont);
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
                    document.add(new Paragraph("Target Monthly Profit: " + view.targetProfitField.getText()));
                    document.add(new Paragraph("Monthly Profit Rate %: " + view.monthlyRateField.getText()));
                    document.add(new Paragraph("Charity Rate %: " + view.charityField.getText()));
                    document.add(new Paragraph("Investment Amount: " + view.investmentField.getText()));
                    document.add(new Paragraph(" ")); // Spacing

                    // Add summary
                    document.add(new Paragraph("Summary:", sectionFont));
                    document.add(new Paragraph("Months Required: " + model.getMonthsRequired()));
                    document.add(new Paragraph("Total Profit: SAR " + decimalFormat.format(model.getTotalProfit())));
                    document.add(new Paragraph("Total Charity: SAR " + decimalFormat.format(model.getTotalCharity())));
                    document.add(new Paragraph("Final Amount: SAR " + decimalFormat.format(model.getFinalAmount())));
                    document.add(new Paragraph(" ")); // Spacing

                    // Add table
                    document.add(new Paragraph("Monthly Breakdown:", sectionFont));
                    PdfPTable table = new PdfPTable(5);
                    table.setWidthPercentage(100);

                    // Add headers
                    com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
                    table.addCell(new PdfPCell(new Phrase("Month", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Investment Value", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Monthly Profit", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Charity", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Remaining Profit", headerFont)));

                    // Add data
                    com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);
                    for (int i = 0; i < model.getTableModel().getRowCount(); i++) {
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 0).toString(), dataFont)));
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 1).toString(), dataFont)));
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 2).toString(), dataFont)));
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 3).toString(), dataFont)));
                        table.addCell(new PdfPCell(new Phrase(model.getTableModel().getValueAt(i, 4).toString(), dataFont)));
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
                double investmentValue = Double.parseDouble(model.getTableModel().getValueAt(i, 1).toString());
                double monthlyProfit = Double.parseDouble(model.getTableModel().getValueAt(i, 2).toString());
                double charity = Double.parseDouble(model.getTableModel().getValueAt(i, 3).toString());

                dataset.addValue(investmentValue, "Investment Value", month);
                dataset.addValue(monthlyProfit, "Monthly Profit", month);
                dataset.addValue(charity, "Charity", month);
            }

            JFreeChart chart = ChartFactory.createLineChart(
                "One-Time Investment Progress",
                "Month",
                "Amount (SAR)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
            );

            CategoryPlot plot = chart.getCategoryPlot();
            
            // Configure lines
            LineAndShapeRenderer renderer = new LineAndShapeRenderer();
            renderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
            
            // Investment Value line (Blue)
            renderer.setSeriesPaint(0, new Color(52, 152, 219));
            renderer.setSeriesStroke(0, new BasicStroke(3.0f));
            renderer.setSeriesShapesVisible(0, true);
            
            // Monthly Profit line (Green)
            renderer.setSeriesPaint(1, new Color(46, 204, 113));
            renderer.setSeriesStroke(1, new BasicStroke(3.0f));
            renderer.setSeriesShapesVisible(1, true);
            
            // Charity line (Red)
            renderer.setSeriesPaint(2, new Color(231, 76, 60));
            renderer.setSeriesStroke(2, new BasicStroke(3.0f));
            renderer.setSeriesShapesVisible(2, true);
            
            plot.setRenderer(renderer);

            // Create chart window
            JFrame chartFrame = new JFrame("One-Time Investment Progress Chart");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 600));
            
            // Add explain chart button to chart window
            JPanel chartControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton explainButton = new JButton("Explain Chart");
            explainButton.addActionListener(e1 -> showChartExplanationDialog());
            chartControlPanel.add(explainButton);
            
            chartFrame.getContentPane().setLayout(new BorderLayout());
            chartFrame.getContentPane().add(chartPanel, BorderLayout.CENTER);
            chartFrame.getContentPane().add(chartControlPanel, BorderLayout.SOUTH);
            
            chartFrame.pack();
            chartFrame.setLocationRelativeTo(null);
            chartFrame.setVisible(true);
        }
    }

    private void showChartExplanationDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(view), "Chart Explanation", false);
        dialog.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(
            "The One-Time Investment Progress Chart shows three key metrics over time:\n\n" +
            "1. Investment Value (Blue Line):\n" +
            "   - Shows how your initial investment grows over time\n" +
            "   - Includes reinvested profits after charity deduction\n\n" +
            "2. Monthly Profit (Green Line):\n" +
            "   - Shows the profit generated each month\n" +
            "   - Increases as your investment value grows\n\n" +
            "3. Charity (Red Line):\n" +
            "   - Shows the charity amount deducted each month\n" +
            "   - Calculated as a percentage of monthly profit\n\n" +
            "The chart helps you visualize:\n" +
            "- How long it takes to reach your target monthly profit\n" +
            "- The growth rate of your investment\n" +
            "- The relationship between profits and charity\n\n" +
            "Tips:\n" +
            "- Hover over data points to see exact values\n" +
            "- Use the chart to understand the compound effect\n" +
            "- Compare the lines to see the impact of charity on growth"
        );
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setBackground(new Color(250, 250, 250));

        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
    }

    private class ExplainChartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showChartExplanationDialog();
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
            view.targetProfitField.setText(PropertyLoader.getProperty("one_time_investment.properties", "target.monthly.profit", "5000"));
            view.monthlyRateField.setText(PropertyLoader.getProperty("one_time_investment.properties", "monthly.profit.rate", "2.5"));
            view.charityField.setText(PropertyLoader.getProperty("one_time_investment.properties", "charity.rate", "10"));
            view.investmentField.setText(PropertyLoader.getProperty("one_time_investment.properties", "investment.amount", "100000"));
        }
    }
} 
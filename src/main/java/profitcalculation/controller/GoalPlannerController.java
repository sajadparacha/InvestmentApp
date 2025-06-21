package profitcalculation.controller;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import profitcalculation.model.GoalPlannerModel;
import profitcalculation.view.GoalPlannerView;
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

public class GoalPlannerController {
    private final GoalPlannerModel model;
    private final GoalPlannerView view;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public GoalPlannerController(GoalPlannerModel model, GoalPlannerView view) {
        this.model = model;
        this.view = view;

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
            boolean isValid = ValidationUtil.validateNumberField(view.goalProfitField, "Target Monthly Profit", errorMessage) &&
                            ValidationUtil.validateNumberField(view.monthlyInvestmentField, "Monthly Investment", errorMessage) &&
                            ValidationUtil.validatePercentageField(view.monthlyRateField, "Monthly Profit Rate", errorMessage) &&
                            ValidationUtil.validatePercentageField(view.charityField, "Charity Rate", errorMessage);

            if (!isValid) {
                ValidationUtil.showValidationError(errorMessage.toString());
                return;
            }

            try {
                // Parse validated values
                double goalProfit = decimalFormat.parse(view.goalProfitField.getText().trim()).doubleValue();
                double monthlyInvestment = decimalFormat.parse(view.monthlyInvestmentField.getText().trim()).doubleValue();
                double monthlyRate = decimalFormat.parse(view.monthlyRateField.getText().trim()).doubleValue();
                double charityRate = decimalFormat.parse(view.charityField.getText().trim()).doubleValue();

                // Calculate results
                model.calculate(goalProfit, monthlyInvestment, monthlyRate, charityRate);

                // Update view with results
                view.monthsLabel.setText("<html><span style='font-size:16px'>📅</span> " + 
                    PropertyLoader.getProperty("goal_planner.properties", "months.required.label", "Months Required: ") + 
                    model.getMonthsRequired());
                view.totalInvestmentLabel.setText("<html><span style='font-size:16px'>💵</span> " + 
                    PropertyLoader.getProperty("goal_planner.properties", "total.investment.label", "Total Investment: SAR ") + 
                    decimalFormat.format(model.getTotalInvestment()));
                view.investmentValueLabel.setText("<html><span style='font-size:16px'>📈</span> " + 
                    PropertyLoader.getProperty("goal_planner.properties", "final.investment.value.label", "Final Investment Value: SAR ") + 
                    decimalFormat.format(model.getFinalInvestmentValue()));
                view.lastProfitLabel.setText("<html><span style='font-size:16px'>💎</span> " + 
                    PropertyLoader.getProperty("goal_planner.properties", "last.month.profit.label", "Last Month's Profit: SAR ") + 
                    decimalFormat.format(model.getLastMonthProfit()));

        } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, 
                    PropertyLoader.getProperty("goal_planner.properties", "calculation.error.message", "An error occurred during calculation: {0}").replace("{0}", ex.getMessage()),
                    PropertyLoader.getProperty("goal_planner.properties", "calculation.error.title", "Calculation Error"),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        view.goalProfitField.setText("");
        view.monthlyInvestmentField.setText("");
        view.monthlyRateField.setText("");
            view.charityField.setText("");
            view.monthsLabel.setText("<html><span style='font-size:16px'>📅</span> " + 
                PropertyLoader.getProperty("goal_planner.properties", "months.required.label", "Months Required: ") + "0");
            view.totalInvestmentLabel.setText("<html><span style='font-size:16px'>💵</span> " + 
                PropertyLoader.getProperty("goal_planner.properties", "total.investment.label", "Total Investment: SAR ") + "0.00");
            view.investmentValueLabel.setText("<html><span style='font-size:16px'>📈</span> " + 
                PropertyLoader.getProperty("goal_planner.properties", "final.investment.value.label", "Final Investment Value: SAR ") + "0.00");
            view.lastProfitLabel.setText("<html><span style='font-size:16px'>💎</span> " + 
                PropertyLoader.getProperty("goal_planner.properties", "last.month.profit.label", "Last Month's Profit: SAR ") + "0.00");
            model.clear();
        }
    }

    private class ExportCSVListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getTableModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(view, 
                    PropertyLoader.getProperty("goal_planner.properties", "please.calculate.first", "Please calculate results first."));
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(PropertyLoader.getProperty("goal_planner.properties", "export.csv.dialog.title", "Export to CSV"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
            fileChooser.setSelectedFile(new File("goal_planner_results.csv"));

            int result = fileChooser.showSaveDialog(view);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new File(file.getAbsolutePath() + ".csv");
                }

                try (FileWriter writer = new FileWriter(file)) {
                    // Write header
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.title", "Goal Planner Results") + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.generated.on", "Generated on: {0}").replace("{0}", java.time.LocalDateTime.now().toString()) + "\n\n");
                    
                    // Write input parameters
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.input.parameters", "Input Parameters:") + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.target.monthly.profit", "Target Monthly Profit") + "," + view.goalProfitField.getText() + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.monthly.investment", "Monthly Investment") + "," + view.monthlyInvestmentField.getText() + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.monthly.profit.rate", "Monthly Profit Rate %") + "," + view.monthlyRateField.getText() + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.charity.rate", "Charity Rate %") + "," + view.charityField.getText() + "\n\n");
                    
                    // Write summary
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.summary", "Summary:") + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.months.required", "Months Required") + "," + model.getMonthsRequired() + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.total.investment", "Total Investment") + "," + decimalFormat.format(model.getTotalInvestment()) + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.final.investment.value", "Final Investment Value") + "," + decimalFormat.format(model.getFinalInvestmentValue()) + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.last.month.profit", "Last Month's Profit") + "," + decimalFormat.format(model.getLastMonthProfit()) + "\n\n");
                    
                    // Write table data
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.monthly.breakdown", "Monthly Breakdown:") + "\n");
                    writer.write(PropertyLoader.getProperty("goal_planner.properties", "csv.month", "Month") + "," +
                               PropertyLoader.getProperty("goal_planner.properties", "csv.monthly.investment", "Monthly Investment") + "," +
                               PropertyLoader.getProperty("goal_planner.properties", "csv.investment.value", "Investment Value") + "," +
                               PropertyLoader.getProperty("goal_planner.properties", "csv.profit", "Profit") + "," +
                               PropertyLoader.getProperty("goal_planner.properties", "csv.charity", "Charity") + "\n");
                    
                for (int i = 0; i < model.getTableModel().getRowCount(); i++) {
                        writer.write(model.getTableModel().getValueAt(i, 0) + "," +
                                   model.getTableModel().getValueAt(i, 1) + "," +
                                   model.getTableModel().getValueAt(i, 2) + "," +
                                   model.getTableModel().getValueAt(i, 3) + "," +
                                   model.getTableModel().getValueAt(i, 4) + "\n");
                    }

                    JOptionPane.showMessageDialog(view, 
                        PropertyLoader.getProperty("goal_planner.properties", "export.success.message", "CSV file exported successfully to:\n{0}").replace("{0}", file.getAbsolutePath()),
                        PropertyLoader.getProperty("goal_planner.properties", "export.success.title", "Export Successful"),
                        JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, 
                        PropertyLoader.getProperty("goal_planner.properties", "export.error.message", "Error exporting CSV: {0}").replace("{0}", ex.getMessage()),
                        PropertyLoader.getProperty("goal_planner.properties", "export.error.title", "Export Error"),
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
            fileChooser.setSelectedFile(new File("goal_planner_results.pdf"));

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
                    Paragraph title = new Paragraph("Goal Planner Results", titleFont);
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
                    document.add(new Paragraph("Target Monthly Profit: " + view.goalProfitField.getText()));
                    document.add(new Paragraph("Monthly Investment: " + view.monthlyInvestmentField.getText()));
                    document.add(new Paragraph("Monthly Profit Rate %: " + view.monthlyRateField.getText()));
                    document.add(new Paragraph("Charity Rate %: " + view.charityField.getText()));
                    document.add(new Paragraph(" ")); // Spacing

                    // Add summary
                    document.add(new Paragraph("Summary:", sectionFont));
                    document.add(new Paragraph("Months Required: " + model.getMonthsRequired()));
                    document.add(new Paragraph("Total Investment: SAR " + decimalFormat.format(model.getTotalInvestment())));
                    document.add(new Paragraph("Final Investment Value: SAR " + decimalFormat.format(model.getFinalInvestmentValue())));
                    document.add(new Paragraph("Last Month's Profit: SAR " + decimalFormat.format(model.getLastMonthProfit())));
                    document.add(new Paragraph(" ")); // Spacing

                    // Add table
                    document.add(new Paragraph("Monthly Breakdown:", sectionFont));
                    PdfPTable table = new PdfPTable(5);
                    table.setWidthPercentage(100);

                    // Add headers
                    com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
                    table.addCell(new PdfPCell(new Phrase("Month", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Total Investment", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Investment Value", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Monthly Profit", headerFont)));
                    table.addCell(new PdfPCell(new Phrase("Charity", headerFont)));

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

            DefaultCategoryDataset totalInvestmentDataset = new DefaultCategoryDataset();
            DefaultCategoryDataset investmentValueDataset = new DefaultCategoryDataset();
            DefaultCategoryDataset monthlyProfitDataset = new DefaultCategoryDataset();
            DefaultCategoryDataset charityDataset = new DefaultCategoryDataset();

            for (int i = 0; i < model.getTableModel().getRowCount(); i++) {
                String month = model.getTableModel().getValueAt(i, 0).toString();
                double totalInvested = Double.parseDouble(model.getTableModel().getValueAt(i, 1).toString());
                double investmentValue = Double.parseDouble(model.getTableModel().getValueAt(i, 2).toString());
                double monthlyProfit = Double.parseDouble(model.getTableModel().getValueAt(i, 3).toString());
                double charity = Double.parseDouble(model.getTableModel().getValueAt(i, 4).toString());

                totalInvestmentDataset.addValue(totalInvested, "Total Investment", month);
                investmentValueDataset.addValue(investmentValue, "Investment Value", month);
                monthlyProfitDataset.addValue(monthlyProfit, "Monthly Profit", month);
                charityDataset.addValue(charity, "Charity", month);
            }

        JFreeChart chart = ChartFactory.createLineChart(
                "Goal Planner - Investment Progress",
                "Month",
                "Amount (SAR)",
                totalInvestmentDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
            );

        CategoryPlot plot = chart.getCategoryPlot();
            
            // Configure total investment line
            LineAndShapeRenderer totalInvestmentRenderer = new LineAndShapeRenderer();
            totalInvestmentRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
            totalInvestmentRenderer.setSeriesPaint(0, new Color(155, 89, 182)); // Purple
            totalInvestmentRenderer.setSeriesStroke(0, new BasicStroke(3.0f));
            totalInvestmentRenderer.setSeriesShapesVisible(0, true);
            plot.setRenderer(0, totalInvestmentRenderer);

            // Add investment value axis
            NumberAxis investmentValueAxis = new NumberAxis("Investment Value (SAR)");
            investmentValueAxis.setAutoRangeIncludesZero(false);
            plot.setRangeAxis(1, investmentValueAxis);
            plot.setDataset(1, investmentValueDataset);
        plot.mapDatasetToRangeAxis(1, 1);

            // Configure investment value line
            LineAndShapeRenderer investmentValueRenderer = new LineAndShapeRenderer();
            investmentValueRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
            investmentValueRenderer.setSeriesPaint(0, new Color(52, 152, 219)); // Blue
            investmentValueRenderer.setSeriesStroke(0, new BasicStroke(3.0f));
            investmentValueRenderer.setSeriesShapesVisible(0, true);
            plot.setRenderer(1, investmentValueRenderer);

            // Add monthly profit axis with adjusted scale
            NumberAxis monthlyProfitAxis = new NumberAxis("Monthly Profit (SAR)");
            monthlyProfitAxis.setAutoRangeIncludesZero(false);
            double maxProfit = 0;
            for (int i = 0; i < monthlyProfitDataset.getRowCount(); i++) {
                for (int j = 0; j < monthlyProfitDataset.getColumnCount(); j++) {
                    Number value = monthlyProfitDataset.getValue(i, j);
                    if (value != null && value.doubleValue() > maxProfit) {
                        maxProfit = value.doubleValue();
                    }
                }
            }
            monthlyProfitAxis.setRange(0, maxProfit * 1.2); // Add 20% padding
            plot.setRangeAxis(2, monthlyProfitAxis);
            plot.setDataset(2, monthlyProfitDataset);
            plot.mapDatasetToRangeAxis(2, 2);

            // Configure monthly profit line
            LineAndShapeRenderer monthlyProfitRenderer = new LineAndShapeRenderer();
            monthlyProfitRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
            monthlyProfitRenderer.setSeriesPaint(0, new Color(46, 204, 113)); // Green
            monthlyProfitRenderer.setSeriesStroke(0, new BasicStroke(3.0f));
            monthlyProfitRenderer.setSeriesShapesVisible(0, true);
            plot.setRenderer(2, monthlyProfitRenderer);

            // Add charity axis
            NumberAxis charityAxis = new NumberAxis("Charity (SAR)");
            charityAxis.setAutoRangeIncludesZero(false);
            double maxCharity = 0;
            for (int i = 0; i < charityDataset.getRowCount(); i++) {
                for (int j = 0; j < charityDataset.getColumnCount(); j++) {
                    Number value = charityDataset.getValue(i, j);
                    if (value != null && value.doubleValue() > maxCharity) {
                        maxCharity = value.doubleValue();
                    }
                }
            }
            charityAxis.setRange(0, maxCharity * 1.2);
            plot.setRangeAxis(3, charityAxis);
            plot.setDataset(3, charityDataset);
            plot.mapDatasetToRangeAxis(3, 3);

            // Configure charity line
            LineAndShapeRenderer charityRenderer = new LineAndShapeRenderer();
            charityRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
            charityRenderer.setSeriesPaint(0, new Color(231, 76, 60)); // Red
            charityRenderer.setSeriesStroke(0, new BasicStroke(3.0f));
            charityRenderer.setSeriesShapesVisible(0, true);
            plot.setRenderer(3, charityRenderer);

            // Configure axis formatting
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
            
            JFrame chartFrame = new JFrame("Goal Planner - Investment Progress");
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
            "\uD83D\uDCCA Goal Planner Chart Explanation\n\n" +
            "This line chart shows your journey towards your profit goal:\n\n" +
            "\uD83D\uDFE3 PURPLE Line - Total Investment:\n" +
            "   • Shows how much you've invested over time\n" +
            "   • Grows steadily with your monthly investments\n" +
            "   • Represents your actual cash contributions\n\n" +
            "\uD83D\uDD35 BLUE Line - Investment Value:\n" +
            "   • Shows the total value of your investment\n" +
            "   • Includes both your investments and profits\n" +
            "   • Grows faster than total investment due to profits\n\n" +
            "\uD83D\uDFE2 GREEN Line - Monthly Profit:\n" +
            "   • Shows the profit earned each month\n" +
            "   • Based on your current investment value\n" +
            "   • The line you need to reach your target\n\n" +
            "\uD83D\uDD34 RED Line - Charity:\n" +
            "   • Shows the amount donated to charity each month (as a percentage of profit)\n" +
            "   • Follows the same time scale as profit\n" +
            "   • Lets you see your charitable impact over time\n\n" +
            "\uD83D\uDCC8 What to Look For:\n" +
            "• When the green line reaches your target profit\n" +
            "• How the blue line grows faster than purple\n" +
            "• The point where you achieve your goal\n" +
            "• The red line shows your monthly charity contribution\n\n" +
            "\uD83D\uDCA1 Tips:\n" +
            "• Hover over lines to see exact values\n" +
            "• The green line shows your progress towards the goal\n" +
            "• Compare lines to understand your growth pattern\n" +
            "• The red line helps you track your charity impact"
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
            view.goalProfitField.setText(PropertyLoader.getProperty("goal_planner.properties", "target.monthly.profit", "6000"));
            view.monthlyInvestmentField.setText(PropertyLoader.getProperty("goal_planner.properties", "monthly.investment", "2000"));
            view.monthlyRateField.setText(PropertyLoader.getProperty("goal_planner.properties", "monthly.profit.rate", "2"));
            view.charityField.setText(PropertyLoader.getProperty("goal_planner.properties", "charity.rate", "10"));
        }
    }
}
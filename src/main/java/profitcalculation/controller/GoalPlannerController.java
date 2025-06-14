package profitcalculation.controller;

import profitcalculation.model.GoalPlannerModel;
import profitcalculation.view.GoalPlannerView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
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

public class GoalPlannerController {
    private final GoalPlannerModel model;
    private final GoalPlannerView view;

    public GoalPlannerController(GoalPlannerModel model, GoalPlannerView view) {
        this.model = model;
        this.view = view;
        addListeners();
        updateSummary();
    }

    private void addListeners() {
        view.calculateBtn.addActionListener(this::calculate);
        view.clearBtn.addActionListener(e -> clear());
        view.exportCSVBtn.addActionListener(e -> exportCSV());
        view.exportPDFBtn.addActionListener(e -> exportPDF());
        view.chartBtn.addActionListener(e -> showChart());
    }

    private void calculate(ActionEvent e) {
        try {
            double target = Double.parseDouble(view.goalProfitField.getText().trim());
            double invest = Double.parseDouble(view.monthlyInvestmentField.getText().trim());
            double rate = Double.parseDouble(view.monthlyRateField.getText().trim());
            model.calculate(target, invest, rate);
            updateSummary();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Please enter valid numbers in all fields.");
        }
    }

    private void clear() {
        view.goalProfitField.setText("");
        view.monthlyInvestmentField.setText("");
        view.monthlyRateField.setText("");
        model.getTableModel().setRowCount(0);
        updateSummary();
    }

    private void updateSummary() {
        view.monthsLabel.setText("📅 Months Required: " + model.getMonths());
        view.totalInvestmentLabel.setText("\uD83D\uDCB0 Total Invested: " + model.getTotalInvestment());
        view.investmentValueLabel.setText("\uD83D\uDCC8 Investment Value: " + model.getInvestmentValue());
        view.lastProfitLabel.setText("\uD83D\uDCCA Monthly Profit: " + model.getLastProfit());
    }

    private void exportCSV() {
        if (model.getTableModel().getRowCount() == 0) return;
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save CSV File");
        fc.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        if (fc.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".csv")) f = new File(f.getParent(), f.getName() + ".csv");
            try (FileWriter writer = new FileWriter(f)) {
                for (int i = 0; i < model.getTableModel().getColumnCount(); i++)
                    writer.write(model.getTableModel().getColumnName(i) + (i < model.getTableModel().getColumnCount() - 1 ? "," : "\n"));
                for (int i = 0; i < model.getTableModel().getRowCount(); i++) {
                    for (int j = 0; j < model.getTableModel().getColumnCount(); j++)
                        writer.write(model.getTableModel().getValueAt(i, j) + (j < model.getTableModel().getColumnCount() - 1 ? "," : "\n"));
                }
                writer.write("\n" + view.monthsLabel.getText() + "\n" + view.totalInvestmentLabel.getText() +
                        "\n" + view.investmentValueLabel.getText() + "\n" + view.lastProfitLabel.getText());
                JOptionPane.showMessageDialog(view, "CSV exported to: " + f.getAbsolutePath());
            } catch (Exception ex) { JOptionPane.showMessageDialog(view, "Export failed."); }
        }
    }

    private void exportPDF() {
        if (model.getTableModel().getRowCount() == 0) return;
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save PDF File");
        fc.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        if (fc.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".pdf")) f = new File(f.getParent(), f.getName() + ".pdf");
            try {
                Document doc = new Document();
                PdfWriter.getInstance(doc, new java.io.FileOutputStream(f));
                doc.open();
                PdfPTable pdfTable = new PdfPTable(model.getTableModel().getColumnCount());
                for (int i = 0; i < model.getTableModel().getColumnCount(); i++)
                    pdfTable.addCell(new Phrase(model.getTableModel().getColumnName(i)));
                for (int i = 0; i < model.getTableModel().getRowCount(); i++)
                    for (int j = 0; j < model.getTableModel().getColumnCount(); j++)
                        pdfTable.addCell(model.getTableModel().getValueAt(i, j).toString());
                doc.add(pdfTable);
                doc.add(new Paragraph("\n" + view.monthsLabel.getText()));
                doc.add(new Paragraph(view.totalInvestmentLabel.getText()));
                doc.add(new Paragraph(view.investmentValueLabel.getText()));
                doc.add(new Paragraph(view.lastProfitLabel.getText()));
                doc.close();
                JOptionPane.showMessageDialog(view, "PDF exported to: " + f.getAbsolutePath());
            } catch (Exception ex) { JOptionPane.showMessageDialog(view, "Export failed."); }
        }
    }

    private void showChart() {
        DefaultCategoryDataset investmentDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset profitDataset = new DefaultCategoryDataset();
        DefaultTableModel tm = model.getTableModel();
        for (int i = 0; i < tm.getRowCount(); i++) {
            String month = tm.getValueAt(i, 0).toString();
            double profit = Double.parseDouble(tm.getValueAt(i, 3).toString());
            double investment = Double.parseDouble(tm.getValueAt(i, 2).toString());
            investmentDataset.addValue(investment, "Investment Value", month);
            profitDataset.addValue(profit, "Monthly Profit", month);
        }
        JFreeChart chart = ChartFactory.createLineChart(
                "Investment vs Profit Over Time", "Month", "Investment Value (SAR)",
                investmentDataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer investmentRenderer = new LineAndShapeRenderer();
        investmentRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
        investmentRenderer.setSeriesPaint(0, java.awt.Color.BLUE);
        investmentRenderer.setSeriesStroke(0, new java.awt.BasicStroke(2.5f));
        plot.setRenderer(0, investmentRenderer);
        NumberAxis secondaryAxis = new NumberAxis("Monthly Profit (SAR)");
        secondaryAxis.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, secondaryAxis);
        plot.setDataset(1, profitDataset);
        plot.mapDatasetToRangeAxis(1, 1);
        LineAndShapeRenderer profitRenderer = new LineAndShapeRenderer();
        profitRenderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
        profitRenderer.setSeriesPaint(0, java.awt.Color.RED);
        profitRenderer.setSeriesStroke(0, new java.awt.BasicStroke(2.5f));
        plot.setRenderer(1, profitRenderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame chartFrame = new JFrame("Investment & Profit Chart");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setContentPane(chartPanel);
        chartFrame.setSize(850, 600);
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setVisible(true);
    }
}
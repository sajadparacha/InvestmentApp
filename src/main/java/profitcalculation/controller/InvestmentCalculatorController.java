package profitcalculation.controller;

import profitcalculation.model.InvestmentCalculatorModel;
import profitcalculation.view.InvestmentCalculatorView;

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

public class InvestmentCalculatorController {
    private final InvestmentCalculatorModel model;
    private final InvestmentCalculatorView view;

    public InvestmentCalculatorController(InvestmentCalculatorModel model, InvestmentCalculatorView view) {
        this.model = model;
        this.view = view;
        addListeners();
        updateSummary();
    }

    private void addListeners() {
        view.calcBtn.addActionListener(this::calculate);
        view.clearBtn.addActionListener(e -> clear());
        view.exportCSVBtn.addActionListener(e -> exportCSV());
        view.exportPDFBtn.addActionListener(e -> exportPDF());
        view.chartBtn.addActionListener(e -> showChart());
        view.helpBtn.addActionListener(e -> showHelp());
        view.fillDefaultsBtn.addActionListener(e -> fillDefaultValues());
    }

    private void calculate(ActionEvent e) {
        try {
            double inv = Double.parseDouble(view.investmentField.getText().replace(",", "").trim());
            double pct = Double.parseDouble(view.profitField.getText().trim());
            double charPct = Double.parseDouble(view.charityField.getText().trim());
            int months = Integer.parseInt(view.monthsField.getText().trim());
            model.calculate(inv, pct, charPct, months);
            updateSummary();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Please enter valid numbers in all fields.");
        }
    }

    private void clear() {
        view.investmentField.setText("");
        view.profitField.setText("");
        view.charityField.setText("");
        view.monthsField.setText("");
        model.getTableModel().setRowCount(0);
        updateSummary();
    }

    private void updateSummary() {
        view.totalProfitLabel.setText("\u2705 Total Profit: " + model.getTotalProfit());
        view.totalCharityLabel.setText("\u2764\uFE0F Total Charity: " + model.getTotalCharity());
        view.finalAmountLabel.setText("\uD83C\uDFDB\uFE0F Final Amount: " + model.getFinalAmount());
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
                writer.write("\n" + view.totalProfitLabel.getText() + "\n" + view.totalCharityLabel.getText() + "\n" + view.finalAmountLabel.getText());
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
                doc.add(new Paragraph("\n" + view.totalProfitLabel.getText()));
                doc.add(new Paragraph(view.totalCharityLabel.getText()));
                doc.add(new Paragraph(view.finalAmountLabel.getText()));
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
            double profit = Double.parseDouble(tm.getValueAt(i, 1).toString());
            double investment = Double.parseDouble(tm.getValueAt(i, 3).toString());
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

    private void fillDefaultValues() {
        view.investmentField.setText("10000");
        view.profitField.setText("2.5");
        view.charityField.setText("10");
        view.monthsField.setText("12");
    }

    private void showHelp() {
        view.showHelpDialog();
    }
}
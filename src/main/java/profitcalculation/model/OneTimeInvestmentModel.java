package profitcalculation.model;

import javax.swing.table.DefaultTableModel;

public class OneTimeInvestmentModel {
    private DefaultTableModel tableModel;
    private double totalProfit;
    private double totalCharity;
    private double finalAmount;
    private double targetProfit;
    private int monthsRequired;

    public OneTimeInvestmentModel() {
        tableModel = new DefaultTableModel(
            new Object[]{"Month", "Investment Value", "Monthly Profit", "Charity", "Remaining Profit"}, 0
        );
    }

    public void calculate(double oneTimeInvestment, double targetProfit, double monthlyProfitRate, double charityRate) {
        clear();
        this.targetProfit = targetProfit;
        
        double currentInvestment = oneTimeInvestment;
        double monthlyProfit;
        double charity;
        double remainingProfit;
        int month = 1;
        totalProfit = 0;
        totalCharity = 0;

        do {
            monthlyProfit = currentInvestment * (monthlyProfitRate / 100);
            charity = monthlyProfit * (charityRate / 100);
            remainingProfit = monthlyProfit - charity;
            
            totalProfit += monthlyProfit;
            totalCharity += charity;
            
            tableModel.addRow(new Object[]{
                month,
                String.format("%.2f", currentInvestment),
                String.format("%.2f", monthlyProfit),
                String.format("%.2f", charity),
                String.format("%.2f", remainingProfit)
            });
            
            currentInvestment += remainingProfit;
            month++;
        } while (monthlyProfit < targetProfit);

        monthsRequired = month - 1;
        finalAmount = currentInvestment;
    }

    public void clear() {
        tableModel.setRowCount(0);
        totalProfit = 0;
        totalCharity = 0;
        finalAmount = 0;
        targetProfit = 0;
        monthsRequired = 0;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public double getTotalCharity() {
        return totalCharity;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public double getTargetProfit() {
        return targetProfit;
    }

    public int getMonthsRequired() {
        return monthsRequired;
    }
} 
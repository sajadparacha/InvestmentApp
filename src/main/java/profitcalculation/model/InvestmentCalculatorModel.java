package profitcalculation.model;

import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;

public class InvestmentCalculatorModel {
    private double initialInvestment, profitPercent, charityPercent;
    private int months;
    private double totalProfit, totalCharity, finalAmount;
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Month", "Profit", "Charity", "Investment Value"}, 0);

    public boolean calculate(double investment, double profitPct, double charityPct, int numMonths) {
        initialInvestment = investment;
        profitPercent = profitPct;
        charityPercent = charityPct;
        months = numMonths;
        tableModel.setRowCount(0);
        totalProfit = totalCharity = 0;
        double amount = investment;
        for (int i = 1; i <= numMonths; i++) {
            double profit = amount * profitPct / 100;
            double charity = profit * charityPct / 100;
            double netProfit = profit - charity;
            totalCharity += charity;
            totalProfit += netProfit;
            amount += netProfit;
            tableModel.addRow(new Object[]{i, f(profit), f(charity), f(amount)});
        }
        finalAmount = amount;
        return true;
    }

    public DefaultTableModel getTableModel() { return tableModel; }
    public double getTotalProfit() { return totalProfit; }
    public double getTotalCharity() { return totalCharity; }
    public double getFinalAmount() { return finalAmount; }
    private static String f(double n) { return new DecimalFormat("0.00").format(n); }
}
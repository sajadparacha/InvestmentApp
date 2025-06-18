package profitcalculation.model;

import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;

public class GoalPlannerModel {
    private double totalInvestment = 0;
    private double finalInvestmentValue = 0;
    private double lastMonthProfit = 0;
    private int monthsRequired = 0;
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Month", "Total Invested", "Investment Value", "Monthly Profit"}, 0);

    public boolean calculate(double targetProfit, double monthlyInvest, double ratePct) {
        tableModel.setRowCount(0);
        totalInvestment = 0;
        finalInvestmentValue = 0;
        lastMonthProfit = 0;
        monthsRequired = 0;
        
        double rate = ratePct / 100.0;
        double currentValue = 0;
        
        while (true) {
            monthsRequired++;
            currentValue += monthlyInvest;
            double profit = currentValue * rate;
            
            tableModel.addRow(new Object[]{
                monthsRequired,
                f(monthsRequired * monthlyInvest),
                f(currentValue),
                f(profit)
            });
            
            if (profit >= targetProfit) {
                finalInvestmentValue = currentValue;
                lastMonthProfit = profit;
                break;
            }
            
            currentValue += profit;
        }
        
        totalInvestment = monthsRequired * monthlyInvest;
        return true;
    }

    public DefaultTableModel getTableModel() { return tableModel; }
    public int getMonthsRequired() { return monthsRequired; }
    public double getTotalInvestment() { return totalInvestment; }
    public double getFinalInvestmentValue() { return finalInvestmentValue; }
    public double getLastMonthProfit() { return lastMonthProfit; }

    public void clear() {
        tableModel.setRowCount(0);
        monthsRequired = 0;
        totalInvestment = 0;
        finalInvestmentValue = 0;
        lastMonthProfit = 0;
    }

    private static String f(double n) { return new DecimalFormat("0.00").format(n); }
}
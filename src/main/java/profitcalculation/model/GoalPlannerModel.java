package profitcalculation.model;

import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;

public class GoalPlannerModel {
    private double totalInvestment = 0, investmentValue = 0, lastProfit = 0;
    private int months = 0;
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Month", "Total Invested", "Investment Value", "Monthly Profit"}, 0);

    public boolean calculate(double targetProfit, double monthlyInvest, double ratePct) {
        tableModel.setRowCount(0);
        totalInvestment = investmentValue = lastProfit = 0;
        months = 0;
        double rate = ratePct / 100.0, val = 0;
        while (true) {
            val += monthlyInvest;
            months++;
            double profit = val * rate;
            tableModel.addRow(new Object[]{
                    months,
                    f(months * monthlyInvest),
                    f(val),
                    f(profit)
            });
            if (profit >= targetProfit) { investmentValue = val; lastProfit = profit; break; }
            val += profit;
        }
        totalInvestment = months * monthlyInvest;
        return true;
    }

    public DefaultTableModel getTableModel() { return tableModel; }
    public int getMonths() { return months; }
    public double getTotalInvestment() { return totalInvestment; }
    public double getInvestmentValue() { return investmentValue; }
    public double getLastProfit() { return lastProfit; }
    private static String f(double n) { return new DecimalFormat("0.00").format(n); }
}
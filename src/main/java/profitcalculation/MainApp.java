package profitcalculation;

import com.formdev.flatlaf.FlatLightLaf;
import profitcalculation.controller.*;
import profitcalculation.model.*;
import profitcalculation.view.*;

import javax.swing.*;

public class MainApp extends JFrame {
    public MainApp() {
        setTitle("Investment Calculator & Goal Planner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // --- Investment Calculator Tab ---
        InvestmentCalculatorModel calcModel = new InvestmentCalculatorModel();
        InvestmentCalculatorView calcView = new InvestmentCalculatorView(calcModel.getTableModel());
        new InvestmentCalculatorController(calcModel, calcView);

        // --- Goal Planner Tab ---
        GoalPlannerModel goalModel = new GoalPlannerModel();
        GoalPlannerView goalView = new GoalPlannerView(goalModel.getTableModel());
        new GoalPlannerController(goalModel, goalView);

        tabs.addTab("Investment Calculator", calcView);
        tabs.addTab("Goal Planner", goalView);

        add(tabs);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new FlatLightLaf()); }
        catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}
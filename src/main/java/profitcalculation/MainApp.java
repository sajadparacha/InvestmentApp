package profitcalculation;

import com.formdev.flatlaf.FlatLightLaf;
import profitcalculation.controller.InvestmentCalculatorController;
import profitcalculation.controller.GoalPlannerController;
import profitcalculation.model.InvestmentCalculatorModel;
import profitcalculation.model.GoalPlannerModel;
import profitcalculation.view.InvestmentCalculatorView;
import profitcalculation.view.GoalPlannerView;

import javax.swing.*;
import java.awt.*;

public class MainApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Investment Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            // Create models
            InvestmentCalculatorModel calculatorModel = new InvestmentCalculatorModel();
            GoalPlannerModel goalPlannerModel = new GoalPlannerModel();

            // Create views
            InvestmentCalculatorView calculatorView = new InvestmentCalculatorView(calculatorModel.getTableModel());
            GoalPlannerView goalPlannerView = new GoalPlannerView(goalPlannerModel.getTableModel());

            // Create controllers
            new InvestmentCalculatorController(calculatorModel, calculatorView);
            new GoalPlannerController(goalPlannerModel, goalPlannerView);

            // Create tabbed pane
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Investment Calculator", calculatorView);
            tabbedPane.addTab("Goal Planner", goalPlannerView);

            // Set tab icons and tooltips
            tabbedPane.setIconAt(0, new ImageIcon("src/main/resources/calculator.png"));
            tabbedPane.setIconAt(1, new ImageIcon("src/main/resources/goal.png"));
            tabbedPane.setToolTipTextAt(0, "Calculate investment returns and charity amounts");
            tabbedPane.setToolTipTextAt(1, "Plan your investment goals and track progress");

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}
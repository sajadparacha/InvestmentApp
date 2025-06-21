package profitcalculation;

import com.formdev.flatlaf.FlatLightLaf;
import profitcalculation.controller.InvestmentCalculatorController;
import profitcalculation.controller.GoalPlannerController;
import profitcalculation.controller.OneTimeInvestmentController;
import profitcalculation.model.InvestmentCalculatorModel;
import profitcalculation.model.GoalPlannerModel;
import profitcalculation.model.OneTimeInvestmentModel;
import profitcalculation.view.InvestmentCalculatorView;
import profitcalculation.view.GoalPlannerView;
import profitcalculation.view.OneTimeInvestmentView;

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
            OneTimeInvestmentModel oneTimeModel = new OneTimeInvestmentModel();

            // Create views
            InvestmentCalculatorView calculatorView = new InvestmentCalculatorView(calculatorModel.getTableModel());
            GoalPlannerView goalPlannerView = new GoalPlannerView(goalPlannerModel.getTableModel());
            OneTimeInvestmentView oneTimeView = new OneTimeInvestmentView();

            // Create controllers
            new InvestmentCalculatorController(calculatorModel, calculatorView);
            new GoalPlannerController(goalPlannerModel, goalPlannerView);
            new OneTimeInvestmentController(oneTimeModel, oneTimeView);

            // Create tabbed pane
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Investment Calculator", calculatorView);
            tabbedPane.addTab("Goal Planner", goalPlannerView);
            tabbedPane.addTab("One-Time Investment", oneTimeView);

            // Set tab icons and tooltips
            tabbedPane.setIconAt(0, new ImageIcon("src/main/resources/calculator.png"));
            tabbedPane.setIconAt(1, new ImageIcon("src/main/resources/goal.png"));
            tabbedPane.setIconAt(2, new ImageIcon("src/main/resources/onetime.png"));
            tabbedPane.setToolTipTextAt(0, "Calculate investment returns and charity amounts");
            tabbedPane.setToolTipTextAt(1, "Plan your investment goals and track progress");
            tabbedPane.setToolTipTextAt(2, "Calculate returns for one-time investments");

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}
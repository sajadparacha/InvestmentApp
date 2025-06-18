package profitcalculation.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class HomeView extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public HomeView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Investment Management System");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(titleLabel);

        // Features Panel
        JPanel featuresPanel = createPanel("Features", true);
        featuresPanel.setLayout(new GridLayout(2, 1, 20, 20));

        // Investment Calculator Panel
        JPanel calculatorPanel = createFeaturePanel(
            "Investment Calculator",
            "Calculate your investment growth over time with customizable parameters:",
            new String[]{
                "• Set your initial investment amount",
                "• Define monthly profit percentage",
                "• Specify charity contribution",
                "• View detailed monthly breakdown",
                "• Export results to CSV or PDF",
                "• Visualize growth with charts"
            }
        );

        // Goal Planner Panel
        JPanel plannerPanel = createFeaturePanel(
            "Goal Planner",
            "Plan your investment journey to reach specific profit goals:",
            new String[]{
                "• Set target monthly profit",
                "• Define monthly investment amount",
                "• Calculate required time period",
                "• Track progress towards goal",
                "• Export planning data",
                "• Visualize goal achievement"
            }
        );

        featuresPanel.add(calculatorPanel);
        featuresPanel.add(plannerPanel);

        // Navigation Panel
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navigationPanel.setBackground(BACKGROUND_COLOR);
        
        JButton calculatorBtn = new JButton("Go to Investment Calculator");
        JButton plannerBtn = new JButton("Go to Goal Planner");
        
        calculatorBtn.setFont(MAIN_FONT);
        plannerBtn.setFont(MAIN_FONT);
        
        navigationPanel.add(calculatorBtn);
        navigationPanel.add(plannerBtn);

        // Add all panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(featuresPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private JPanel createPanel(String title, boolean hasBorder) {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        if (hasBorder) {
            panel.setBorder(createTitledBorder(title));
        }
        return panel;
    }

    private Border createTitledBorder(String title) {
        Border border = BorderFactory.createLineBorder(PRIMARY_COLOR, 2);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border, title);
        titledBorder.setTitleFont(HEADER_FONT);
        titledBorder.setTitleColor(PRIMARY_COLOR);
        return titledBorder;
    }

    private JPanel createFeaturePanel(String title, String description, String[] features) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(MAIN_FONT);
        descArea.setBackground(PANEL_COLOR);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        panel.add(descArea, BorderLayout.CENTER);

        // Features
        JPanel featuresPanel = new JPanel(new GridLayout(features.length, 1, 5, 5));
        featuresPanel.setBackground(PANEL_COLOR);
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(MAIN_FONT);
            featuresPanel.add(featureLabel);
        }
        panel.add(featuresPanel, BorderLayout.SOUTH);

        return panel;
    }
} 
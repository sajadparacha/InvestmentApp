package profitcalculation.util;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.ParseException;

public class ValidationUtil {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public static boolean validateNumberField(JTextField field, String fieldName, StringBuilder errorMessage) {
        String value = field.getText().trim();
        
        // Check if empty
        if (value.isEmpty()) {
            errorMessage.append(fieldName).append(" is required.\n");
            return false;
        }

        // Try to parse as number
        try {
            double number = decimalFormat.parse(value).doubleValue();
            
            // Check if negative
            if (number < 0) {
                errorMessage.append(fieldName).append(" cannot be negative.\n");
                return false;
            }
            
            return true;
        } catch (ParseException e) {
            errorMessage.append(fieldName).append(" must be a valid number.\n");
            return false;
        }
    }

    public static boolean validatePercentageField(JTextField field, String fieldName, StringBuilder errorMessage) {
        if (!validateNumberField(field, fieldName, errorMessage)) {
            return false;
        }

        try {
            double percentage = decimalFormat.parse(field.getText().trim()).doubleValue();
            if (percentage > 100) {
                errorMessage.append(fieldName).append(" cannot be greater than 100%.\n");
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false; // This should never happen as validateNumberField already checked
        }
    }

    public static boolean validateIntegerField(JTextField field, String fieldName, StringBuilder errorMessage) {
        if (!validateNumberField(field, fieldName, errorMessage)) {
            return false;
        }

        try {
            double number = decimalFormat.parse(field.getText().trim()).doubleValue();
            if (number != Math.floor(number)) {
                errorMessage.append(fieldName).append(" must be a whole number.\n");
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false; // This should never happen as validateNumberField already checked
        }
    }

    public static void showValidationError(String errorMessage) {
        JOptionPane.showMessageDialog(
            null,
            errorMessage,
            "Validation Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
} 
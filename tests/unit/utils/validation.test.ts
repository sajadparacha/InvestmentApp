import { validateOneTimeInvestment, validateMonthlyInvestment, validateGoalPlanner } from '../../../src/utils/validation';

describe('Validation Utils', () => {
  describe('validateOneTimeInvestment', () => {
    it('should return empty errors for valid input', () => {
      const inputs = {
        investmentAmount: '10000',
        duration: '12',
        durationUnit: 'months',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toEqual([]);
    });

    it('should return error for missing investment amount', () => {
      const inputs = {
        investmentAmount: '0',
        duration: '12',
        durationUnit: 'months',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('investmentAmount');
    });

    it('should return error for negative investment amount', () => {
      const inputs = {
        investmentAmount: '-1000',
        duration: '12',
        durationUnit: 'months',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('investmentAmount');
    });

    it('should return error for missing duration', () => {
      const inputs = {
        investmentAmount: '10000',
        duration: '0',
        durationUnit: 'months',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('duration');
    });

    it('should return error for negative duration', () => {
      const inputs = {
        investmentAmount: '10000',
        duration: '-5',
        durationUnit: 'months',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('duration');
    });

    it('should return error for invalid profit rate', () => {
      const inputs = {
        investmentAmount: '10000',
        duration: '12',
        durationUnit: 'months',
        profitRate: '-5',
        charityDeduction: '2.5'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('profitRate');
    });

    it('should return error for profit rate over 100%', () => {
      const inputs = {
        investmentAmount: '10000',
        duration: '12',
        durationUnit: 'months',
        profitRate: '150',
        charityDeduction: '2.5'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('profitRate');
    });

    it('should return error for negative charity deduction', () => {
      const inputs = {
        investmentAmount: '10000',
        duration: '12',
        durationUnit: 'months',
        profitRate: '15',
        charityDeduction: '-1'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('charityDeduction');
    });

    it('should return error for charity deduction over 100%', () => {
      const inputs = {
        investmentAmount: '10000',
        duration: '12',
        durationUnit: 'months',
        profitRate: '15',
        charityDeduction: '150'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('charityDeduction');
    });

    it('should return multiple errors for multiple invalid inputs', () => {
      const inputs = {
        investmentAmount: '-1000',
        duration: '-5',
        durationUnit: 'months',
        profitRate: '150',
        charityDeduction: '-1'
      };

      const errors = validateOneTimeInvestment(inputs);
      expect(errors.length).toBeGreaterThan(1);
    });
  });

  describe('validateMonthlyInvestment', () => {
    it('should return empty errors for valid input', () => {
      const inputs = {
        monthlyAmount: '1000',
        duration: '12',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateMonthlyInvestment(inputs);
      expect(errors).toEqual([]);
    });

    it('should return error for missing monthly amount', () => {
      const inputs = {
        monthlyAmount: '0',
        duration: '12',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateMonthlyInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('monthlyAmount');
    });

    it('should return error for missing duration', () => {
      const inputs = {
        monthlyAmount: '1000',
        duration: '0',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateMonthlyInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('duration');
    });

    it('should return error for negative duration', () => {
      const inputs = {
        monthlyAmount: '1000',
        duration: '-5',
        profitRate: '15',
        charityDeduction: '2.5'
      };

      const errors = validateMonthlyInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('duration');
    });

    it('should return error for invalid profit rate', () => {
      const inputs = {
        monthlyAmount: '1000',
        duration: '12',
        profitRate: '-5',
        charityDeduction: '2.5'
      };

      const errors = validateMonthlyInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('profitRate');
    });

    it('should return error for profit rate over 100%', () => {
      const inputs = {
        monthlyAmount: '1000',
        duration: '12',
        profitRate: '150',
        charityDeduction: '2.5'
      };

      const errors = validateMonthlyInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('profitRate');
    });

    it('should return error for negative charity deduction', () => {
      const inputs = {
        monthlyAmount: '1000',
        duration: '12',
        profitRate: '15',
        charityDeduction: '-1'
      };

      const errors = validateMonthlyInvestment(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('charityDeduction');
    });
  });

  describe('validateGoalPlanner', () => {
    it('should return empty errors for valid input', () => {
      const inputs = {
        targetMonthlyProfit: '1000',
        monthlyInvestment: '5000',
        profitRate: '12',
        charityDeduction: '2.5'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toEqual([]);
    });

    it('should return error for missing target monthly profit', () => {
      const inputs = {
        targetMonthlyProfit: '0',
        monthlyInvestment: '5000',
        profitRate: '12',
        charityDeduction: '2.5'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('targetMonthlyProfit');
    });

    it('should return error for negative target monthly profit', () => {
      const inputs = {
        targetMonthlyProfit: '-500',
        monthlyInvestment: '5000',
        profitRate: '12',
        charityDeduction: '2.5'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('targetMonthlyProfit');
    });

    it('should return error for missing monthly investment', () => {
      const inputs = {
        targetMonthlyProfit: '1000',
        monthlyInvestment: '0',
        profitRate: '12',
        charityDeduction: '2.5'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('monthlyInvestment');
    });

    it('should return error for negative monthly investment', () => {
      const inputs = {
        targetMonthlyProfit: '1000',
        monthlyInvestment: '-2000',
        profitRate: '12',
        charityDeduction: '2.5'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('monthlyInvestment');
    });

    it('should return error for invalid profit rate', () => {
      const inputs = {
        targetMonthlyProfit: '1000',
        monthlyInvestment: '5000',
        profitRate: '-5',
        charityDeduction: '2.5'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('profitRate');
    });

    it('should return error for profit rate over 100%', () => {
      const inputs = {
        targetMonthlyProfit: '1000',
        monthlyInvestment: '5000',
        profitRate: '150',
        charityDeduction: '2.5'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('profitRate');
    });

    it('should return error for negative charity deduction', () => {
      const inputs = {
        targetMonthlyProfit: '1000',
        monthlyInvestment: '5000',
        profitRate: '12',
        charityDeduction: '-1'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('charityDeduction');
    });

    it('should return error for charity deduction over 100%', () => {
      const inputs = {
        targetMonthlyProfit: '1000',
        monthlyInvestment: '5000',
        profitRate: '12',
        charityDeduction: '150'
      };

      const errors = validateGoalPlanner(inputs);
      expect(errors).toHaveLength(1);
      expect(errors[0].field).toBe('charityDeduction');
    });
  });
}); 
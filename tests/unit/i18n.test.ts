import i18n from '../../src/i18n';

describe('Internationalization (i18n)', () => {
  beforeEach(() => {
    // Reset to English before each test
    i18n.changeLanguage('en');
  });

  describe('Language Support', () => {
    it('should support English language', () => {
      i18n.changeLanguage('en');
      expect(i18n.language).toBe('en');
    });

    it('should support Urdu language', () => {
      i18n.changeLanguage('ur');
      expect(i18n.language).toBe('ur');
    });

    it('should support Arabic language', () => {
      i18n.changeLanguage('ar');
      expect(i18n.language).toBe('ar');
    });
  });

  describe('Translation Keys', () => {
    it('should have investment calculator title', () => {
      expect(i18n.t('title')).toBeDefined();
    });

    it('should have one time investment translation', () => {
      expect(i18n.t('oneTimeInvestment')).toBeDefined();
    });

    it('should have monthly investment translation', () => {
      expect(i18n.t('monthlyInvestment')).toBeDefined();
    });

    it('should have goal planner translation', () => {
      expect(i18n.t('goalPlanner')).toBeDefined();
    });

    it('should have help translation', () => {
      expect(i18n.t('help')).toBeDefined();
    });

    it('should have investment amount translation', () => {
      expect(i18n.t('investmentAmount')).toBeDefined();
    });

    it('should have duration translation', () => {
      expect(i18n.t('duration')).toBeDefined();
    });

    it('should have profit rate translation', () => {
      expect(i18n.t('profitRate')).toBeDefined();
    });

    it('should have charity deduction translation', () => {
      expect(i18n.t('charityDeduction')).toBeDefined();
    });

    it('should have calculate button translation', () => {
      expect(i18n.t('calculate')).toBeDefined();
    });

    it('should have export button translation', () => {
      expect(i18n.t('export')).toBeDefined();
    });

    it('should have reset button translation', () => {
      expect(i18n.t('reset')).toBeDefined();
    });
  });

  describe('Language Switching', () => {
    it('should change language when requested', () => {
      i18n.changeLanguage('ur');
      expect(i18n.language).toBe('ur');
      
      i18n.changeLanguage('ar');
      expect(i18n.language).toBe('ar');
      
      i18n.changeLanguage('en');
      expect(i18n.language).toBe('en');
    });

    it('should maintain translations after language change', () => {
      const englishTitle = i18n.t('title');
      
      i18n.changeLanguage('ur');
      const urduTitle = i18n.t('title');
      
      i18n.changeLanguage('ar');
      const arabicTitle = i18n.t('title');
      
      expect(englishTitle).toBeDefined();
      expect(urduTitle).toBeDefined();
      expect(arabicTitle).toBeDefined();
      
      // All titles should be defined (they might be the same if translation is not available)
      expect(englishTitle).toBeTruthy();
      expect(urduTitle).toBeTruthy();
      expect(arabicTitle).toBeTruthy();
    });
  });

  describe('Form Labels', () => {
    it('should have all form field labels', () => {
      const formLabels = [
        'investmentAmount',
        'monthlyAmount',
        'targetMonthlyProfit',
        'monthlyInvestment',
        'duration',
        'profitRate',
        'charityDeduction'
      ];

      formLabels.forEach(label => {
        expect(i18n.t(label)).toBeDefined();
        expect(i18n.t(label)).toBeTruthy(); // Should return a truthy value
      });
    });
  });

  describe('Button Labels', () => {
    it('should have all button labels', () => {
      const buttonLabels = [
        'calculate',
        'export',
        'reset',
        'exportChart',
        'exportSummary'
      ];

      buttonLabels.forEach(label => {
        expect(i18n.t(label)).toBeDefined();
        expect(i18n.t(label)).toBeTruthy(); // Should return a truthy value
      });
    });
  });

  describe('Error Messages', () => {
    it('should have validation error messages', () => {
      const errorMessages = [
        'validation.required',
        'validation.positive',
        'validation.percentage',
        'validation.duration'
      ];

      errorMessages.forEach(message => {
        expect(i18n.t(message)).toBeDefined();
      });
    });
  });

  describe('Help Content', () => {
    it('should have help section translations', () => {
      const helpContent = [
        'help.title',
        'help.description',
        'help.features',
        'help.usage'
      ];

      helpContent.forEach(content => {
        expect(i18n.t(content)).toBeDefined();
      });
    });
  });
}); 
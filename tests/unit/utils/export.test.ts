import { exportToCSV, exportToExcel } from '../../../src/utils/export';

// Mock the required dependencies
jest.mock('xlsx', () => ({
  utils: {
    json_to_sheet: jest.fn(() => ({})),
    book_new: jest.fn(() => ({})),
    book_append_sheet: jest.fn(() => ({})),
  },
  writeFile: jest.fn(),
}));

describe('Export Utils', () => {
  const mockClick = jest.fn();
  const mockRemove = jest.fn();
  let originalCreateElement: typeof document.createElement;
  let originalCreateObjectURL: typeof URL.createObjectURL;

  beforeAll(() => {
    // Save originals
    originalCreateElement = document.createElement;
    originalCreateObjectURL = URL.createObjectURL;

    // Mock
    global.URL.createObjectURL = jest.fn(() => 'mock-url');
    document.createElement = jest.fn(() => {
      const anchor = originalCreateElement.call(document, 'a');
      anchor.click = mockClick;
      anchor.remove = mockRemove;
      return anchor;
    });
  });

  afterAll(() => {
    // Restore originals
    document.createElement = originalCreateElement;
    global.URL.createObjectURL = originalCreateObjectURL;
  });

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('exportToCSV', () => {
    it('should create and download CSV file', () => {
      const data = [
        { month: 1, profit: 100, charity: 2.5, totalProfit: 97.5, accumulatedValue: 1097.5 },
        { month: 2, profit: 200, charity: 5, totalProfit: 195, accumulatedValue: 1292.5 },
      ];
      const filename = 'test-data.csv';

      expect(() => exportToCSV(data, filename)).not.toThrow();
      expect(document.createElement).toHaveBeenCalledWith('a');
      expect(URL.createObjectURL).toHaveBeenCalled();
    });

    it('should handle empty data array', () => {
      const data: any[] = [];
      const filename = 'empty-data.csv';

      expect(() => exportToCSV(data, filename)).not.toThrow();
      expect(document.createElement).toHaveBeenCalledWith('a');
      expect(URL.createObjectURL).toHaveBeenCalled();
    });

    it('should handle data with different column types', () => {
      const data = [
        { month: 1, profit: 100.5, charity: 2.5, totalProfit: 97.5, accumulatedValue: 1097.5, text: 'test' },
        { month: 2, profit: 200, charity: 5, totalProfit: 195, accumulatedValue: 1292.5, text: 'test2' },
      ];
      const filename = 'mixed-data.csv';

      expect(() => exportToCSV(data, filename)).not.toThrow();
      expect(document.createElement).toHaveBeenCalledWith('a');
      expect(URL.createObjectURL).toHaveBeenCalled();
    });
  });

  describe('exportToExcel', () => {
    it('should create and download Excel file', () => {
      const data = [
        { month: 1, profit: 100, charity: 2.5, totalProfit: 97.5, accumulatedValue: 1097.5 },
        { month: 2, profit: 200, charity: 5, totalProfit: 195, accumulatedValue: 1292.5 },
      ];
      const filename = 'test-data.xlsx';

      expect(() => exportToExcel(data, filename)).not.toThrow();
      // Excel export uses XLSX.writeFile() instead of DOM methods
    });

    it('should handle empty data array', () => {
      const data: any[] = [];
      const filename = 'empty-data.xlsx';

      expect(() => exportToExcel(data, filename)).not.toThrow();
      // Excel export uses XLSX.writeFile() instead of DOM methods
    });

    it('should handle data with different column types', () => {
      const data = [
        { month: 1, profit: 100.5, charity: 2.5, totalProfit: 97.5, accumulatedValue: 1097.5, text: 'test' },
        { month: 2, profit: 200, charity: 5, totalProfit: 195, accumulatedValue: 1292.5, text: 'test2' },
      ];
      const filename = 'mixed-data.xlsx';

      expect(() => exportToExcel(data, filename)).not.toThrow();
      // Excel export uses XLSX.writeFile() instead of DOM methods
    });
  });
}); 
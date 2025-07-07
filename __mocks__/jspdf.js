const createMockPdf = () => {
  const mockPdf = {
    setFontSize: jest.fn().mockReturnThis(),
    text: jest.fn().mockReturnThis(),
    addImage: jest.fn().mockReturnThis(),
    save: jest.fn().mockReturnThis(),
    setFont: jest.fn().mockReturnThis()
  };
  return mockPdf;
};

const jsPDF = jest.fn().mockImplementation(() => createMockPdf());

module.exports = jsPDF;
module.exports.default = jsPDF; 
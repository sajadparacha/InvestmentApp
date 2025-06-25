import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { CalculationResult, SummaryData } from '../types';

export const exportToCSV = (data: CalculationResult[], filename: string, inputs?: Record<string, any>, summary?: SummaryData) => {
  let csvContent = '';
  if (inputs) {
    csvContent += 'Inputs\n';
    Object.entries(inputs).forEach(([key, value]) => {
      csvContent += `${key},${value}\n`;
    });
    csvContent += '\n';
  }
  if (summary) {
    csvContent += 'Summary\n';
    Object.entries(summary).forEach(([key, value]) => {
      csvContent += `${key},${value}\n`;
    });
    csvContent += '\n';
  }
  if (data.length > 0) {
    const headers = Object.keys(data[0]).join(',');
    const rows = data.map(row => Object.values(row).join(','));
    csvContent += [headers, ...rows].join('\n');
  }
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = `${filename}.csv`;
  link.click();
};

export const exportToExcel = (data: CalculationResult[], filename: string, inputs?: Record<string, any>, summary?: SummaryData) => {
  const sheets: Record<string, XLSX.WorkSheet> = {};
  if (inputs) {
    sheets['Inputs'] = XLSX.utils.aoa_to_sheet(Object.entries(inputs));
  }
  if (summary) {
    sheets['Summary'] = XLSX.utils.aoa_to_sheet(Object.entries(summary));
  }
  sheets['Monthly Data'] = XLSX.utils.json_to_sheet(data);
  const workbook = XLSX.utils.book_new();
  Object.entries(sheets).forEach(([name, sheet]) => {
    XLSX.utils.book_append_sheet(workbook, sheet, name);
  });
  XLSX.writeFile(workbook, `${filename}.xlsx`);
};

export const exportToPDF = async (elementId: string, filename: string, inputs?: Record<string, any>, summary?: SummaryData) => {
  const element = document.getElementById(elementId);
  if (!element) return;
  const pdf = new jsPDF('p', 'mm', 'a4');
  let y = 10;
  if (inputs) {
    pdf.setFontSize(14);
    pdf.text('Inputs', 10, y);
    y += 8;
    pdf.setFontSize(10);
    Object.entries(inputs).forEach(([key, value]) => {
      pdf.text(`${key}: ${value}`, 10, y);
      y += 6;
    });
    y += 4;
  }
  if (summary) {
    pdf.setFontSize(14);
    pdf.text('Summary', 10, y);
    y += 8;
    pdf.setFontSize(10);
    Object.entries(summary).forEach(([key, value]) => {
      pdf.text(`${key}: ${value}`, 10, y);
      y += 6;
    });
    y += 4;
  }
  // Render the table/chart as image below the text
  const canvas = await html2canvas(element, {
    scale: 2,
    useCORS: true,
    allowTaint: true
  });
  const imgData = canvas.toDataURL('image/png');
  const imgWidth = 190;
  const imgHeight = (canvas.height * imgWidth) / canvas.width;
  pdf.addImage(imgData, 'PNG', 10, y, imgWidth, imgHeight);
  pdf.save(`${filename}.pdf`);
};

export const exportChartToPNG = async (chartElementId: string, filename: string) => {
  const element = document.getElementById(chartElementId);
  if (!element) return;
  const canvas = await html2canvas(element, {
    scale: 2,
    useCORS: true,
    allowTaint: true
  });
  const link = document.createElement('a');
  link.download = `${filename}.png`;
  link.href = canvas.toDataURL();
  link.click();
};

export const exportSummaryToPDF = (summary: SummaryData, filename: string) => {
  const pdf = new jsPDF();
  pdf.setFontSize(20);
  pdf.text('Investment Summary Report', 20, 20);
  pdf.setFontSize(12);
  pdf.text(`Total Profit: ${summary.totalProfit.toFixed(2)}`, 20, 40);
  pdf.text(`Total Charity: ${summary.totalCharity.toFixed(2)}`, 20, 50);
  pdf.text(`Final Value: ${summary.finalValue.toFixed(2)}`, 20, 60);
  if (summary.requiredMonthlyInvestment) {
    pdf.text(`Required Monthly Investment: ${summary.requiredMonthlyInvestment.toFixed(2)}`, 20, 70);
  }
  if (summary.feasibilityMessage) {
    pdf.text(`Note: ${summary.feasibilityMessage}`, 20, 80);
  }
  pdf.save(`${filename}.pdf`);
}; 
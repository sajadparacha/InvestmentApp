import React from 'react';

const CreditsView: React.FC = () => (
  <div className="p-6 md:p-8 lg:p-10 flex flex-col items-center justify-center min-h-[60vh]">
    <div className="card max-w-md w-full text-center p-4 md:p-8">
      <h2 className="text-2xl md:text-3xl font-bold mb-4 text-gray-900">Credits</h2>
      <div className="space-y-2">
        <div>
          <span className="font-semibold text-gray-700">Developer:</span> Sajjad Paracha
        </div>
        <div>
          <span className="font-semibold text-gray-700">Portfolio:</span> <a href="https://www.linkedin.com/in/sajjadparacha/" target="_blank" rel="noopener noreferrer" className="text-primary-600 underline">LinkedIn</a>
        </div>
        <div>
          <span className="font-semibold text-gray-700">GitHub:</span> <a href="https://github.com/sajadparacha" target="_blank" rel="noopener noreferrer" className="text-primary-600 underline">sajadparacha</a>
        </div>
      </div>
    </div>
  </div>
);

export default CreditsView; 
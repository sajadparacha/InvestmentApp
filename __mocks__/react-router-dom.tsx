import React from 'react';

export const HashRouter = ({ children }: { children: React.ReactNode }) => (
  <div data-testid="router">{children}</div>
);

export const Routes = ({ children }: { children: React.ReactNode }) => (
  <div data-testid="routes">{children}</div>
);

export const Route = ({ children }: { children: React.ReactNode }) => (
  <div data-testid="route">{children}</div>
);

export const Navigate = ({ to }: { to: string }) => (
  <div data-testid="navigate" data-to={to}>Navigate to {to}</div>
); 
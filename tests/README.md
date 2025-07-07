# Test Structure and Best Practices

## Directory Structure

```
investmentapp/
├── src/                    # Source code only
│   ├── components/
│   ├── controllers/
│   ├── utils/
│   ├── views/
│   └── ...
├── tests/                  # All test files
│   ├── unit/              # Unit tests
│   │   ├── utils/         # Utility function tests
│   │   ├── controllers/   # Controller component tests
│   │   └── components/    # React component tests
│   ├── integration/       # Integration tests (future)
│   └── e2e/              # End-to-end tests (future)
├── __mocks__/             # Global mocks
│   ├── react-router-dom.tsx
│   ├── jspdf.js
│   └── html2canvas.js
└── jest.config.js         # Jest configuration
```

## Test Organization Principles

### 1. **Separation of Concerns**
- **Source Code**: Only contains business logic, components, and utilities
- **Tests**: All test files are in the `tests/` directory
- **Mocks**: Global mocks are in `__mocks__/` directory

### 2. **Test Categories**
- **Unit Tests**: Test individual functions and components in isolation
- **Integration Tests**: Test interactions between multiple components
- **E2E Tests**: Test complete user workflows

### 3. **Naming Conventions**
- Test files: `*.test.ts` or `*.test.tsx`
- Mock files: `__mocks__/module-name.ts`
- Test directories mirror source structure

## Running Tests

```bash
# Run all tests
npm test

# Run tests with coverage
npm test -- --coverage

# Run specific test file
npm test -- tests/unit/utils/validation.test.ts

# Run tests in watch mode
npm test -- --watch
```

## Writing Tests

### Unit Tests
```typescript
// tests/unit/utils/validation.test.ts
import { validateOneTimeInvestment } from '../../../src/utils/validation';

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
  });
});
```

### Component Tests
```typescript
// tests/unit/components/App.test.tsx
import { render } from '@testing-library/react';
import App from '../../../src/App';

describe('App Component', () => {
  it('should render the main app container', () => {
    const { getByTestId } = render(<App />);
    expect(getByTestId('app-container')).toBeInTheDocument();
  });
});
```

## Mocking Strategy

### 1. **Global Mocks** (`__mocks__/`)
- External libraries that are difficult to mock inline
- Complex dependencies like `jspdf`, `html2canvas`
- React Router components

### 2. **Inline Mocks**
- Simple component mocks
- Utility function mocks
- Test-specific mocks

### 3. **Mock Examples**
```typescript
// __mocks__/react-router-dom.tsx
export const HashRouter = ({ children }) => <div data-testid="router">{children}</div>;
export const Routes = ({ children }) => <div data-testid="routes">{children}</div>;
export const Route = ({ children }) => <div data-testid="route">{children}</div>;
```

## Best Practices

### 1. **Test Structure**
- Use descriptive test names
- Group related tests with `describe` blocks
- Test one thing per test case
- Use `beforeEach` for setup, `afterEach` for cleanup

### 2. **Assertions**
- Use specific assertions (`toBe`, `toEqual`, `toContain`)
- Test both positive and negative cases
- Verify error conditions and edge cases

### 3. **Coverage**
- Aim for high test coverage (80%+)
- Focus on critical business logic
- Test error handling and edge cases

### 4. **Performance**
- Keep tests fast and focused
- Mock expensive operations (API calls, file I/O)
- Use `jest.fn()` for function mocks

## Common Patterns

### Testing Async Functions
```typescript
it('should handle async operations', async () => {
  const result = await someAsyncFunction();
  expect(result).toBeDefined();
});
```

### Testing Event Handlers
```typescript
it('should call handler when button is clicked', () => {
  const handleClick = jest.fn();
  const { getByRole } = render(<Button onClick={handleClick} />);
  
  fireEvent.click(getByRole('button'));
  expect(handleClick).toHaveBeenCalledTimes(1);
});
```

### Testing Form Validation
```typescript
it('should show error for invalid input', () => {
  const { getByText } = render(<Form />);
  
  fireEvent.change(getByLabelText('Amount'), {
    target: { value: '-100' }
  });
  
  expect(getByText('Amount must be positive')).toBeInTheDocument();
});
```

## Troubleshooting

### Common Issues
1. **Import Path Errors**: Ensure relative paths are correct after moving files
2. **Mock Not Working**: Check if mock is in the right location and properly exported
3. **TypeScript Errors**: Make sure test files have proper type annotations

### Debugging Tests
```bash
# Run tests with verbose output
npm test -- --verbose

# Run specific test with debugging
npm test -- --testNamePattern="should validate input"
```

This structure provides a clean separation between source code and tests, making the codebase more maintainable and easier to navigate. 
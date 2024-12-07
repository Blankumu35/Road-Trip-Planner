import { vi } from 'vitest';
import {  render, screen, fireEvent, waitFor  } from '@testing-library/react';
import Home from './Home';


beforeAll(() => {
    global.matchMedia = vi.fn().mockImplementation(query => ({
      matches: false,
      addListener: vi.fn(),
      removeListener: vi.fn(),
    }));
  });

  beforeAll(() => {
    global.google = {
      maps: {
        Map: vi.fn().mockImplementation(() => ({
          setCenter: vi.fn(),
          setZoom: vi.fn(),
        })),
        Geocoder: vi.fn().mockImplementation(() => ({
          geocode: vi.fn().mockImplementation((request, callback) => {
            // Simulate a successful geocode response
            callback([{ formatted_address: '123 Test St' }], 'OK');
          }),
        })),
        places: {
          PlacesService: vi.fn().mockImplementation(() => ({
            textSearch: vi.fn().mockImplementation((request, callback) => {
              // Simulate a successful textSearch response
              callback(
                [
                  {
                    name: '123 Test St',
                    formatted_address: '123 Test St',
                  },
                ],
                'OK'
              );
            }),
          })),
        },
      },
    };
  });


// Correctly mock the default export
vi.mock('react-google-places-autocomplete', () => ({
  __esModule: true, // This ensures that the module is treated as an ES module
  default: () => <div>Mocked Google Places</div>, // Mock the default export
}));


vi.spyOn(global, 'fetch').mockImplementation((url) => {
  if (url === 'http://localhost:8080/all') {
    return Promise.resolve({
      ok: true,
      json: () => Promise.resolve(["DummyTrip"]),
    });
  }
  // For other URLs, perform the default fetch behavior (or return a mock for other cases)
  return fetch(url);
});

describe('Home Component', () => {
  it('should initialize with default states', () => {
    render(<Home />);
    
    // Test initial state
    expect(screen.getByText('Nothing to show here, please search your location.')).toBeInTheDocument();
  });
});

vi.mock('node-fetch', () => vi.fn(() => Promise.resolve({
    json: () => Promise.resolve({
      name: 'Mock Location',
      icon: 'mock-icon',
      description: 'Clear sky',
      temperature: 25,
    }),
    ok: true,
  })));
  
  describe('Home Component', () => {
    it('should fetch weather data and display it after search', async () => {
      render(<Home />);
      
      // Mock search value
      const searchValue = { label: 'Mock Address', value: { structured_formatting: { main_text: 'Mock Location' } } };
  

      // Wait for weather data to load
      await waitFor(() => expect(screen.getByText('Weather')).toBeInTheDocument());

    });
  });

  vi.mock('node-fetch', () => vi.fn(() => Promise.resolve({
    json: () => Promise.resolve({ route: [0, 1, 2] }),
    ok: true,
  })));
  
  describe('Home Component', () => {
    it('should render the optimize button', async () => {
      const { getByText } = render(<Home />);
      fireEvent.click(getByText('Optimize Route!'));
      
    });
  });

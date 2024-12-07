import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'localhost:8080', 
  headers: {
    'Content-Type': 'application/json',  // Default content-type for JSON

  },
});


apiClient.interceptors.request.use(
  (config) => {
    //Check if there's an auth token and add it to the headers
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor: Handle errors globally
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.error('Unauthorized request');
    }
    return Promise.reject(error);
  }
);


export default apiClient;

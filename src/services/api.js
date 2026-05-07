import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');

  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }

  return config;
});

export const getActivities = () => api.get('/activities');
export const addActivity = (activity) => api.post('/activities', activity);

const requireActivityId = (id) => {
  if (id === undefined || id === null || id === '') {
    throw new Error('Activity id is required');
  }

  return id;
};

export const getActivityDetail = (id) =>
  api.get(`/activities/${requireActivityId(id)}`);

export const getActivityRecommendation = (id) =>
  api.get(`/recommendations/activity/${requireActivityId(id)}`);

export const getUserRecommendations = (userId) =>
  api.get(`/recommendations/user/${requireActivityId(userId)}`);

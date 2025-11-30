import axios, { AxiosError, type InternalAxiosRequestConfig } from 'axios';
import i18n from '@/i18n';

export const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

export const api = axios.create({
    baseURL: API_URL,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

let accessToken: string | null = null;

export const setAccessToken = (token: string | null) => {
    accessToken = token;
};

api.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        if (accessToken) {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        // Add Accept-Language header based on current i18n language
        config.headers['Accept-Language'] = i18n.language || 'fr';
        return config;
    },
    (error) => Promise.reject(error)
);
api.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

        if (error.response?.status === 401 && !originalRequest._retry) {
            if (
                originalRequest.url?.includes('/auth/login') ||
                originalRequest.url?.includes('/auth/refresh') ||
                originalRequest.url?.includes('/auth/me')
            ) {
                return Promise.reject(error);
            }

            originalRequest._retry = true;

            try {
                const { data } = await api.post('/auth/refresh');
                setAccessToken(data.accessToken);
                originalRequest.headers['Authorization'] = `Bearer ${data.accessToken}`;
                return api(originalRequest);

            } catch (refreshError) {
                setAccessToken(null);
                window.dispatchEvent(new Event('auth:logout'));
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);
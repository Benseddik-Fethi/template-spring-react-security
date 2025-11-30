import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import './i18n'
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from "@/context/AuthContext.tsx";
import { ThemeProvider } from "@/components/ThemeProvider";

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <BrowserRouter>
            <AuthProvider>
                <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
                    <App />
                </ThemeProvider>
            </AuthProvider>
        </BrowserRouter>
    </React.StrictMode>,
)
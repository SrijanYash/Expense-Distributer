import React, { useEffect, useState } from 'react';
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import HomePage from './HomePage';
import GroupPage from './GroupPage';
import LoginPage from './LoginPage';
import SignupPage from './SignupPage';
import AddExpensePage from './AddExpensePage';
import authService from '../services/authService';

function ProtectedRoute({ children }) {
  const isAuthenticated = authService.isAuthenticated();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
}

function App() {
  const [, setIsAuthenticated] = useState(false);
  const navigate = useNavigate();
  
  useEffect(() => {
    // Check if user is authenticated
    setIsAuthenticated(authService.isAuthenticated());
    
    // Add event listener for storage changes (for logout in other tabs)
    const handleStorageChange = () => {
      setIsAuthenticated(authService.isAuthenticated());
      if (!authService.isAuthenticated()) {
        navigate('/login');
      }
    };
    
    window.addEventListener('storage', handleStorageChange);
    return () => window.removeEventListener('storage', handleStorageChange);
  }, [navigate]);
  
  return (
    <div className="app-container">
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/" element={
          <ProtectedRoute>
            <HomePage />
          </ProtectedRoute>
        } />
        <Route path="/group/:groupId" element={
          <ProtectedRoute>
            <GroupPage />
          </ProtectedRoute>
        } />
        <Route path="/group/:groupId/addExpense" element={
          <ProtectedRoute>
            <AddExpensePage />
          </ProtectedRoute>
        } />
      </Routes>
    </div>
  );
}

export default App;
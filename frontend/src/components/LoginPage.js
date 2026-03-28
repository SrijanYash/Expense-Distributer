import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import apiService from '../services/apiService';
import authService from '../services/authService';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  
  useEffect(() => {
    // Redirect to home if already logged in
    if (authService.isAuthenticated()) {
      navigate('/');
    }
  }, [navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const response = await apiService.login(username);
      const user = response.data;

      if (user && user.password === password) {
        // Store user session data
        authService.setUserSession(user);

        // Clear sensitive data from memory
        setPassword('');

        // Redirect to home page
        navigate('/');
      } else {
        setError('Invalid username or password');
        setPassword('');
      }
    } catch (err) {
      console.error('Login error:', err);
      setError('Invalid username or password');
      setPassword('');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-form-wrapper">
        <h2>Login to Expense Distributer</h2>
        
        {error && <div className="error-message">{error}</div>}
        
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              autoComplete="username"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              autoComplete="current-password"
            />
          </div>
          
          <button 
            type="submit" 
            className="login-button"
            disabled={isLoading}
          >
            {isLoading ? 'Logging in...' : 'Login'}
          </button>
          <div className="signup-cta">
            <span>New here?</span>
            <button type="button" className="link-button" onClick={() => navigate('/signup')}>Create an account</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
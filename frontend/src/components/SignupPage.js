import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import apiService from '../services/apiService';

function SignupPage() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === 'phone' ? value.replace(/\D/g, '') : value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      await apiService.registerUser(formData);
      navigate('/login');
    } catch (err) {
      setError('Signup failed. Please try again.');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-form-wrapper">
        <h2>Create an Account</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="name">Name</label>
            <input
              type="text" id="name" name="name"
              value={formData.name} onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email" id="email" name="email"
              value={formData.email} onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="phone">Phone</label>
            <input
              type="tel" id="phone" name="phone"
              value={formData.phone} onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password" id="password" name="password"
              value={formData.password} onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="login-button" disabled={isLoading}>
            {isLoading ? 'Signing up...' : 'Sign Up'}
          </button>

          <div className="signup-cta">
            <button type="button" className="btn btn-secondary" onClick={() => navigate('/login')}>
              Back to Login
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SignupPage;
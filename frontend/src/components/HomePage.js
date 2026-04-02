import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Sidebar from './Sidebar';
import apiService from '../services/apiService';
import authService from '../services/authService';

function HomePage() {
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [groups, setGroups] = useState([]);
  const navigate = useNavigate();
  
  // Get user ID from auth service
  const currentUserId = authService.getUserId();

  useEffect(() => {
    // Fetch user's expenses
    const fetchUserExpenses = async () => {
      try {
        // This would be replaced with actual API call
        const response = await apiService.getUserExpenses(currentUserId);
        setExpenses(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to load expenses');
        setLoading(false);
        console.error(err);
      }
    };

    fetchUserExpenses();
  }, [currentUserId]);

  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const res = await apiService.getUserById(currentUserId);
        setCurrentUser(res.data);
      } catch (err) {
        console.error('Failed to load user details', err);
      }
    };
    if (currentUserId) {
      fetchUserDetails();
    }
  }, [currentUserId]);

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const res = await apiService.getUserGroups(currentUserId);
        setGroups(res.data);
      } catch (err) {
        console.error('Failed to load groups', err);
      }
    };
    if (currentUserId) {
      fetchGroups();
    }
  }, [currentUserId]);


  
  const handleLogout = async () => {
    try {
      // Call logout API
      await apiService.logout();
      // Clear user session
      authService.clearUserSession();
      // Redirect to login page
      navigate('/login');
    } catch (err) {
      console.error('Logout error:', err);
      // Even if API fails, clear local session
      authService.clearUserSession();
      navigate('/login');
    }
  };

  return (
    <div className="home-container">
      <Sidebar />
      <div className="main-content">
        <div className="header">
          <h1 className="page-title">My Expenses</h1>
          <div className="user-info">
            {currentUser ? (
              <span>Welcome, {currentUser.name}</span>
            ) : (
              <span>Welcome</span>
            )}
          </div>
          <button className="logout-button" onClick={handleLogout}>Logout</button>
        </div>

        <div className="groups-section">
          <h2>My Groups</h2>
          {groups.length === 0 ? (
            <p className="no-groups">No groups found</p>
          ) : (
            <ul className="group-list">
              {groups.map((group) => (
                <li key={group.groupId}>
                  <Link to={`/group/${group.groupId}`}>{group.name}</Link>
                </li>
              ))}
            </ul>
          )}
        </div>

        {loading ? (
          <p>Loading expenses...</p>
        ) : error ? (
          <p className="error-message">{error}</p>
        ) : (
          <div className="expense-log">
            {expenses.length === 0 ? (
              <p className="no-expenses">No expenses found</p>
            ) : (
              expenses.map((expense) => (
                <div key={expense.id} className="expense-item">
                  <div className="expense-details">
                    <span className="expense-description">{expense.description}</span>
                    <div className="expense-meta">
                      <span className="expense-user">Paid by: {expense.userName}</span>
                      <span className="expense-group">
                        Group: <Link to={`/group/${expense.groupId}`}>{expense.groupName || 'Unknown Group'}</Link>
                      </span>
                    </div>
                  </div>
                  <span className="expense-amount">${expense.amount.toFixed(2)}</span>
                </div>
              ))
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default HomePage;
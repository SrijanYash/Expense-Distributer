import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Sidebar from './Sidebar';
import apiService from '../services/apiService';

function HomePage() {
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Mock user ID for demonstration - would come from auth in real app
  const currentUserId = 1;

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

  return (
    <>
      <Sidebar />
      <div className="main-content">
        <div className="page-header">
          <h1 className="page-title">My Expenses</h1>
          <button className="btn btn-primary">Add New Expense</button>
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
    </>
  );
}

export default HomePage;
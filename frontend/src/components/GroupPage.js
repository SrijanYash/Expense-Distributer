import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Sidebar from './Sidebar';
import apiService from '../services/apiService';
import authService from '../services/authService';

function GroupPage() {
  const { groupId } = useParams();
  const [group, setGroup] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [userExpenses, setUserExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userGroupView, setUserGroupView] = useState(null);
  const currentUserId = authService.getUserId();

  useEffect(() => {
    const fetchGroupData = async () => {
      try {
        const res = await apiService.getUserGroupView(currentUserId, groupId);
        setUserGroupView(res.data);
        setGroup({ name: res.data.groupName, groupId: res.data.groupId });
        setExpenses(res.data.groupLogs || []);
        setUserExpenses(res.data.userExpenses || []);
        setLoading(false);
      } catch (err) {
        setError('Failed to load group data');
        setLoading(false);
        console.error(err);
      }
    };

    if (currentUserId) {
      fetchGroupData();
    }
  }, [currentUserId, groupId]);

  return (
    <>
      <Sidebar />
      <div className="main-content">
        {loading ? (
          <p>Loading group data...</p>
        ) : error ? (
          <p className="error-message">{error}</p>
        ) : (
          <>
            <div className="page-header">
              <h1 className="page-title">{group?.name || 'Group Details'}</h1>
              <Link className="btn btn-primary" to={`/group/${groupId}/addExpense`}>Add Expense</Link>
            </div>
            
            <div className="group-header">
              <h2 className="group-name">{group?.name} Expenses</h2>
              
              <div className="user-expenses">
                {userExpenses.map((userExpense) => (
                  <div key={userExpense.userId} className="user-expense-card">
                    <div className="user-expense-name">{userExpense.userName}</div>
                    <div className="user-expense-amount">${userExpense.amount.toFixed(2)}</div>
                  </div>
                ))}
              </div>
            </div>
            
            <div className="expense-log">
              {expenses.length === 0 ? (
                <p className="no-expenses">No expenses in this group</p>
              ) : (
                expenses.map((expense) => (
                  <div key={expense.id} className="expense-item">
                    <div className="expense-details">
                      <span className="expense-description">{expense.description}</span>
                      <div className="expense-meta">
                        <span className="expense-user">Paid by: {expense.userName}</span>
                        <span className="expense-split-type">Split: {expense.splitType}</span>
                      </div>
                    </div>
                    <span className="expense-amount">${expense.amount.toFixed(2)}</span>
                  </div>
                ))
              )}
            </div>
          </>
        )}
      </div>
    </>
  );
}

export default GroupPage;
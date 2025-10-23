import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Sidebar from './Sidebar';
import apiService from '../services/apiService';

function GroupPage() {
  const { groupId } = useParams();
  const [group, setGroup] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [userExpenses, setUserExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchGroupData = async () => {
      try {
        // Fetch group details
        const groupResponse = await apiService.getGroupById(groupId);
        setGroup(groupResponse.data);
        
        // Fetch group expenses
        const expensesResponse = await apiService.getExpensesByGroupId(groupId);
        setExpenses(expensesResponse.data);
        
        // Fetch user expenses in the group
        const userExpensesResponse = await apiService.getUserExpensesInGroup(groupId);
        setUserExpenses(userExpensesResponse.data);
        
        setLoading(false);
      } catch (err) {
        setError('Failed to load group data');
        setLoading(false);
        console.error(err);
      }
    };

    fetchGroupData();
  }, [groupId]);

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
              <button className="btn btn-primary">Add Group Expense</button>
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
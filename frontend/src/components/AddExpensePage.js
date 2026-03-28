import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiService from '../services/apiService';
import authService from '../services/authService';

function AddExpensePage() {
  const { groupId } = useParams();
  const navigate = useNavigate();

  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState('');
  const [splitType, setSplitType] = useState('EQUAL'); // EQUAL | PERCENTAGE | CUSTOM
  const [members, setMembers] = useState([]); // {id, name}
  const [userIds, setUserIds] = useState([]);
  const [paidBy, setPaidBy] = useState(null);
  const [percentages, setPercentages] = useState([]);
  const [amounts, setAmounts] = useState([]);
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    const loadMembers = async () => {
      try {
        const res = await apiService.getGroupUserViewList(groupId);
        const views = res.data || [];
        const m = views.map(v => ({ id: v.userId, name: v.userName }));
        setMembers(m);
        const ids = m.map(x => x.id);
        setUserIds(ids);
        setPercentages(Array(ids.length).fill(0));
        setAmounts(Array(ids.length).fill(0));
        const meId = Number(authService.getUserId());
        setPaidBy(ids.includes(meId) ? meId : (ids[0] || null));
      } catch (err) {
        console.error('Failed to load group users', err);
      }
    };
    loadMembers();
  }, [groupId]);

  const splitTypeCode = () => {
    if (splitType === 'EQUAL') return 1;       // uses DTO with userIds
    if (splitType === 'PERCENTAGE') return 2;  // percentages required
    return 3;                                  // CUSTOM amounts required
  };

  const validate = () => {
    const total = parseFloat(amount || '0');
    if (!description || !total || total <= 0) {
      setError('Enter a valid description and amount.');
      return false;
    }
    if (splitType === 'PERCENTAGE') {
      const sum = percentages.reduce((a, b) => a + Number(b || 0), 0);
      if (Math.round(sum) !== 100) {
        setError('Percentages must sum to 100.');
        return false;
      }
    }
    if (splitType === 'CUSTOM') {
      const sum = amounts.reduce((a, b) => a + Number(b || 0), 0);
      if (Math.round(sum * 100) !== Math.round(total * 100)) {
        setError('Custom amounts must sum to the total.');
        return false;
      }
    }
    setError('');
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    const payer = members.find(m => m.id === Number(paidBy));
    const expence = {
      groupId: Number(groupId),
      userId: Number(paidBy),
      userName: payer ? payer.name : authService.getUsername(),
      amount: Number(amount),
      description
    };

    const dto = { userIds: userIds.map(Number) };
    if (splitType === 'PERCENTAGE') dto.percentage = percentages.map(p => Number(p));
    if (splitType === 'CUSTOM') dto.amount = amounts.map(a => Number(a));

    try {
      setIsSubmitting(true);
      await apiService.addExpense({ expence, splitDetails: dto }, splitTypeCode());
      navigate(`/group/${groupId}`);
    } catch (err) {
      console.error('Add expense failed', err);
      setError('Failed to add expense. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const updatePercentage = (idx, val) => {
    const arr = [...percentages];
    arr[idx] = val;
    setPercentages(arr);
  };

  const updateAmount = (idx, val) => {
    const arr = [...amounts];
    arr[idx] = val;
    setAmounts(arr);
  };

  return (
    <div className="login-container">
      <div className="login-form-wrapper">
        <h2>Add Expense</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="description">Description</label>
            <input id="description" value={description} onChange={e => setDescription(e.target.value)} required />
          </div>

          <div className="form-group">
            <label htmlFor="amount">Amount</label>
            <input id="amount" type="number" step="0.01" value={amount} onChange={e => setAmount(e.target.value)} required />
          </div>

          <div className="form-group">
            <label>Paid By</label>
            <select value={paidBy ?? ''} onChange={e => setPaidBy(Number(e.target.value))} required>
              <option value='' disabled>Select payer</option>
              {members.map(m => (
                <option key={m.id} value={m.id}>{m.name}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Split Type</label>
            <select value={splitType} onChange={e => setSplitType(e.target.value)}>
              <option value="EQUAL">Equal</option>
              <option value="PERCENTAGE">Percentage</option>
              <option value="CUSTOM">Custom Amounts</option>
            </select>
          </div>

          {splitType === 'PERCENTAGE' && (
            <div className="form-group">
              <label>Percentages (sum to 100)</label>
              {members.map((m, idx) => (
                <div key={m.id} className="form-inline">
                  <span>{m.name}</span>
                  <input
                    type="number"
                    step="0.01"
                    value={percentages[idx]}
                    onChange={e => updatePercentage(idx, e.target.value)}
                  />
                </div>
              ))}
            </div>
          )}

          {splitType === 'CUSTOM' && (
            <div className="form-group">
              <label>Custom Amounts (sum to total)</label>
              {members.map((m, idx) => (
                <div key={m.id} className="form-inline">
                  <span>{m.name}</span>
                  <input
                    type="number"
                    step="0.01"
                    value={amounts[idx]}
                    onChange={e => updateAmount(idx, e.target.value)}
                  />
                </div>
              ))}
            </div>
          )}

          <div className="signup-cta">
            <button type="button" className="btn btn-secondary" onClick={() => navigate(`/group/${groupId}`)}>
              Cancel
            </button>
            <button type="submit" className="login-button" disabled={isSubmitting}>
              {isSubmitting ? 'Adding...' : 'Add Expense'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddExpensePage;
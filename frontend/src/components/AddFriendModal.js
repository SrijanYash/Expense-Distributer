import React, { useState } from 'react';
import apiService from '../services/apiService';

function AddFriendModal({ onClose, onAddFriend, currentUserId }) {
  const [formData, setFormData] = useState({
    name: '',
    email: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [canInvite, setCanInvite] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const name = (formData.name || '').trim();
    const email = (formData.email || '').trim();
    if (!name || !email) {
      setError('Please fill in name and email');
      return;
    }

    try {
      setLoading(true);
      await apiService.addFriend(currentUserId, { name, email });
      onAddFriend();
    } catch (err) {
      const data = err?.response?.data;
      let msg = 'Failed to add friend. Please check the name matches the email and try again.';
      if (typeof data === 'string') {
        msg = data;
      } else if (data && typeof data === 'object') {
        msg = data.message || data.error || JSON.stringify(data);
      } else if (err?.message) {
        msg = err.message;
      }
      setError(msg);
      setCanInvite(String(msg).toLowerCase().includes('user not found'));
      console.error('Error adding friend:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h2>Add New Friend</h2>
          <button className="modal-close" onClick={onClose}>×</button>
        </div>
        
        <form onSubmit={handleSubmit}>
          {error && <div className="error-message">{error}</div>}
          
          <div className="form-group">
            <label htmlFor="name">Name</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Enter friend's name"
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Enter friend's email"
              required
            />
          </div>
          
          
          <div className="modal-actions">
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
            {canInvite && (
              <button type="button" className="btn btn-primary" disabled={loading}
                onClick={async () => {
                  const name = (formData.name || '').trim();
                  const email = (formData.email || '').trim();
                  if (!name || !email) {
                    setError('Please fill in name and email');
                    return;
                  }
                  try {
                    setLoading(true);
                    await apiService.inviteFriend(currentUserId, { name, email });
                    setError('Invitation sent');
                    setCanInvite(false);
                    onAddFriend();
                  } catch (err) {
                    const data = err?.response?.data;
                    let msg = 'Failed to invite friend.';
                    if (typeof data === 'string') { msg = data; }
                    else if (data && typeof data === 'object') { msg = data.message || data.error || JSON.stringify(data); }
                    else if (err?.message) { msg = err.message; }
                    setError(msg);
                  } finally {
                    setLoading(false);
                  }
                }}
              >
                {loading ? 'Inviting...' : 'Create & Invite'}
              </button>
            )}
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Adding...' : 'Add Friend'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddFriendModal;
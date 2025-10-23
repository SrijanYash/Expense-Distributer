import React, { useState } from 'react';
import apiService from '../services/apiService';

function CreateGroupModal({ onClose, onCreateGroup, friends }) {
  const [groupName, setGroupName] = useState('');
  const [selectedFriends, setSelectedFriends] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleFriendSelection = (friendId) => {
    if (selectedFriends.includes(friendId)) {
      setSelectedFriends(selectedFriends.filter(id => id !== friendId));
    } else {
      setSelectedFriends([...selectedFriends, friendId]);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!groupName) {
      setError('Please enter a group name');
      return;
    }
    
    if (selectedFriends.length === 0) {
      setError('Please select at least one friend');
      return;
    }
    
    try {
      setLoading(true);
      // Current user ID would come from auth in a real app
      const currentUserId = 1;
      const groupData = {
        name: groupName,
        userIds: [currentUserId, ...selectedFriends]
      };
      
      const response = await apiService.addGroup(groupData);
      onCreateGroup(response.data);
      setLoading(false);
    } catch (err) {
      setError('Failed to create group. Please try again.');
      setLoading(false);
      console.error('Error creating group:', err);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h2>Create New Group</h2>
          <button className="modal-close" onClick={onClose}>×</button>
        </div>
        
        <form onSubmit={handleSubmit}>
          {error && <div className="error-message">{error}</div>}
          
          <div className="form-group">
            <label htmlFor="groupName">Group Name</label>
            <input
              type="text"
              id="groupName"
              value={groupName}
              onChange={(e) => setGroupName(e.target.value)}
              placeholder="Enter group name"
              required
            />
          </div>
          
          <div className="form-group">
            <label>Select Friends</label>
            <div className="friends-selection">
              {friends.length === 0 ? (
                <p>No friends available. Add friends first.</p>
              ) : (
                friends.map(friend => (
                  <div key={friend.id} className="friend-checkbox">
                    <input
                      type="checkbox"
                      id={`friend-${friend.id}`}
                      checked={selectedFriends.includes(friend.id)}
                      onChange={() => handleFriendSelection(friend.id)}
                    />
                    <label htmlFor={`friend-${friend.id}`}>{friend.name}</label>
                  </div>
                ))
              )}
            </div>
          </div>
          
          <div className="modal-actions">
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Creating...' : 'Create Group'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreateGroupModal;
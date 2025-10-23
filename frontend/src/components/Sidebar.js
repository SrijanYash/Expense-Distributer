import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import apiService from '../services/apiService';
import AddFriendModal from './AddFriendModal';
import CreateGroupModal from './CreateGroupModal';

function Sidebar() {
  const [groups, setGroups] = useState([]);
  const [friends, setFriends] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddFriendModal, setShowAddFriendModal] = useState(false);
  const [showCreateGroupModal, setShowCreateGroupModal] = useState(false);
  const location = useLocation();
  
  // Mock user ID for demonstration - would come from auth in real app
  const currentUserId = 1;

  useEffect(() => {
    const fetchSidebarData = async () => {
      try {
        // Fetch user's groups
        const groupsResponse = await apiService.getUserGroups(currentUserId);
        setGroups(groupsResponse.data);
        
        // Fetch user's friends
        const friendsResponse = await apiService.getUsers();
        // Filter out current user from friends list
        const filteredFriends = friendsResponse.data.filter(user => user.id !== currentUserId);
        setFriends(filteredFriends);
        
        setLoading(false);
      } catch (err) {
        console.error('Failed to load sidebar data:', err);
        setLoading(false);
      }
    };

    fetchSidebarData();
  }, [currentUserId]);

  return (
    <>
      <div className="sidebar">
        <h2 className="sidebar-header">Expense Distributer</h2>
        
        <div className="sidebar-section">
          <div className="sidebar-header-with-button">
            <h3>My Groups</h3>
            <button className="sidebar-add-button" onClick={() => setShowCreateGroupModal(true)}>
              + Create Group
            </button>
          </div>
          {loading ? (
            <p>Loading groups...</p>
          ) : (
            <ul className="sidebar-list">
              {groups.length === 0 ? (
                <li>No groups found</li>
              ) : (
                groups.map((group) => (
                  <li 
                    key={group.groupId} 
                    className={`sidebar-list-item ${location.pathname === `/group/${group.groupId}` ? 'active' : ''}`}
                  >
                    <Link to={`/group/${group.groupId}`}>{group.name}</Link>
                  </li>
                ))
              )}
            </ul>
          )}
        </div>
        
        <div className="sidebar-section">
          <div className="sidebar-header-with-button">
            <h3>My Friends</h3>
            <button className="sidebar-add-button" onClick={() => setShowAddFriendModal(true)}>
              + Add Friend
            </button>
          </div>
          {loading ? (
            <p>Loading friends...</p>
          ) : (
            <ul className="sidebar-list">
              {friends.length === 0 ? (
                <li>No friends found</li>
              ) : (
                friends.map((friend) => (
                  <li key={friend.id} className="sidebar-list-item">
                    {friend.name}
                  </li>
                ))
              )}
            </ul>
          )}
        </div>
      </div>
      
      {showAddFriendModal && (
        <AddFriendModal 
          onClose={() => setShowAddFriendModal(false)} 
          onAddFriend={(newFriend) => {
            setFriends([...friends, newFriend]);
            setShowAddFriendModal(false);
          }}
        />
      )}
      
      {showCreateGroupModal && (
        <CreateGroupModal 
          onClose={() => setShowCreateGroupModal(false)} 
          onCreateGroup={(newGroup) => {
            setGroups([...groups, newGroup]);
            setShowCreateGroupModal(false);
          }}
          friends={friends}
        />
      )}
    </>
  );
}

export default Sidebar;
/**
 * Authentication service for managing user sessions
 */

// Session storage keys
const USER_ID_KEY = 'userId';
const USERNAME_KEY = 'username';

const authService = {
  setUserSession: (user) => {
    localStorage.setItem('isLoggedIn', 'true');
    localStorage.setItem('userId', user.id);
    localStorage.setItem('username', user.name);
    window.dispatchEvent(new Event('storage'));
  },
  
  /**
   * Clear user session data
   */
  clearUserSession: () => {
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem(USER_ID_KEY);
    localStorage.removeItem(USERNAME_KEY);
    
    // Dispatch storage event for other tabs
    window.dispatchEvent(new Event('storage'));
  },
  
  /**
   * Check if user is authenticated
   * @returns {boolean} Authentication status
   */
  isAuthenticated: () => {
    return localStorage.getItem('isLoggedIn') === 'true';
  },
  
  /**
   * Get current user ID
   * @returns {string|null} User ID or null if not authenticated
   */
  getUserId: () => {
    return localStorage.getItem(USER_ID_KEY);
  },
  
  /**
   * Get current username
   * @returns {string|null} Username or null if not authenticated
   */
  getUsername: () => {
    return localStorage.getItem(USERNAME_KEY);
  }
};

export default authService;
/**
 * @module apiService
 * @description This file contains Axios-based API service functions for interacting with backend services.
 * Axios is a promise-based HTTP client for the browser and Node.js, providing a simple and flexible API for making HTTP requests.
 * 
 * Key Features:
 * - Base URL configuration: All Axios instances are configured with a base URL for consistent API calls.
 * - Default headers setup: Headers like Content-Type can be set globally for all requests.
 * - Interceptors: Allows handling of requests and responses, enabling features like authentication token management and error handling.
 * - Error handling mechanisms: Centralized error handling ensures consistent behavior across all API calls.
 * 
 * Usage Examples:
 * - GET: `apiService.getUsers()`
 * - POST: `apiService.addUser(userData)`
 * - PUT: `apiService.updateExpense(id, expenseData)`
 * - DELETE: Not implemented in this file but can be added similarly.
 * 
 * Benefits of Centralized API Service Pattern:
 * - Consistent error handling across all API calls.
 * - Request/response transformation for uniform data handling.
 * - Authentication token management for secure API access.
 * - Single point of configuration for easier maintenance and updates.
 */

import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API || 'https://api-service-pt6y.onrender.com';
const ALT_API_BASE_URL = process.env.REACT_APP_API_ALT || 'http://localhost:8085';

// Create axios instances for different services
const userService = axios.create({
  baseURL: `${API_BASE_URL}/user`
});

const groupService = axios.create({
  baseURL: `${API_BASE_URL}/group`
});

const expenseService = axios.create({
  baseURL: `${API_BASE_URL}/expence`
});

const userGroupService = axios.create({
  baseURL: `${API_BASE_URL}/user-group`
});

// API service methods
const apiService = {
  // Authentication endpoints
  login: (username) => userService.get(`getUserByName/${username}`),
  logout: () => userService.post('logout'),
  
  // User endpoints
  getUsers: () => userService.get('allUsers'),
  getUserById: (id) => userService.get(`getUserById/${id}`),
  addUser: (userData) => userService.post('register', userData),
  registerUser: (userData) => userService.post('register', userData),

  // Friends endpoints
  getFriends: (userId) => userService.get(`${userId}/friends`),
  addFriend: (userId, data) => userService.post(`${userId}/friends`, data),
  inviteFriend: (userId, data) => userService.post(`${userId}/friends/invite`, data),
  
  // Group endpoints
  getGroups: () => groupService.get('getGroups'),
  getGroupById: (id) => groupService.get(`${id}/GetGroup`),
  addGroup: (groupData) => groupService.post('createGroup', groupData),
  createGroup: (groupData) => groupService.post('createGroup', groupData),
  getUserIdsInGroup: (groupId) => groupService.get(`${groupId}/ListOfUsers`),
  
  // Expense endpoints
  getExpenseById: (id) => expenseService.get(`getExpence/${id}`),
  getExpensesByGroupId: (groupId) => expenseService.get(`getExpenceByGroupId/${groupId}`),
  getExpensesByDescription: (description) => expenseService.get(`getExpenceByDescription/${description}`),
  addExpense: (expenseData, splitType) => expenseService.post(`addExpence/${splitType}`, expenseData),
  addExpenses: (expensesData) => expenseService.post('addListOfExpence', expensesData),
  updateExpense: (id, expenseData) => expenseService.put(`updateExpence/${id}`, expenseData),
  
  // User-Group endpoints
  getUserGroupView: (userId, groupId) => userGroupService.get(`userId-${userId}/groupId-${groupId}`),
  getGroupUserViewList: (groupId) => userGroupService.get(`groupId-${groupId}/getGroupUserView`),
  createUserGroupViews: (groupId, userIds) => userGroupService.post(`${groupId}/NewUserGroupViews`, userIds),
  getGroupMemberDetails: async (groupId) => {
    try {
      return await userGroupService.get(`${groupId}/MemberDetails`);
    } catch (err) {
      try {
        return await axios.get(`${ALT_API_BASE_URL}/user-group/${groupId}/MemberDetails`);
      } catch (e2) {
        throw err;
      }
    }
  },
  getUserGroups: (userId) => groupService.get(`User/${userId}/Groups`),
  
  // Mock endpoints for demonstration (would be replaced with actual endpoints)
  getUserExpenses: (userId) => {
    // Mock implementation - would be replaced with actual API call
    return Promise.resolve({
      data: [
        {
          id: 1,
          groupId: 1,
          groupName: 'Roommates',
          userId: userId,
          userName: 'John Doe',
          amount: 45.50,
          description: 'Groceries',
          splitType: 'EQUAL'
        },
        {
          id: 2,
          groupId: 2,
          groupName: 'Trip to NYC',
          userId: userId,
          userName: 'John Doe',
          amount: 120.75,
          description: 'Hotel',
          splitType: 'PERCENTAGE'
        }
      ]
    });
  },
  
  getUserExpensesInGroup: (groupId) => {
    // Mock implementation - would be replaced with actual API call
    return Promise.resolve({
      data: [
        {
          userId: 1,
          userName: 'John Doe',
          amount: 120.50
        },
        {
          userId: 2,
          userName: 'Jane Smith',
          amount: -45.25
        },
        {
          userId: 3,
          userName: 'Bob Johnson',
          amount: -75.25
        }
      ]
    });
  }
};

export default apiService;

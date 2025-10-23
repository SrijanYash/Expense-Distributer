import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

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
  baseURL: `${API_BASE_URL}/userGroup`
});

// API service methods
const apiService = {
  // User endpoints
  getUsers: () => userService.get('/getUsers'),
  getUserById: (id) => userService.get(`/getUser/${id}`),
  addUser: (userData) => userService.post('/addUser', userData),
  addFriend: (friendData) => userService.post('/addUser', friendData),
  
  // Group endpoints
  getGroups: () => groupService.get('/getGroups'),
  getGroupById: (id) => groupService.get(`/getGroup/${id}`),
  addGroup: (groupData) => groupService.post('/addGroup', groupData),
  createGroup: (groupData) => groupService.post('/addGroup', groupData),
  
  // Expense endpoints
  getExpenseById: (id) => expenseService.get(`/getExpence/${id}`),
  getExpensesByGroupId: (groupId) => expenseService.get(`/getExpenceByGroupId/${groupId}`),
  getExpensesByDescription: (description) => expenseService.get(`/getExpenceByDescription/${description}`),
  addExpense: (expenseData, splitType) => expenseService.post(`/addExpence/${splitType}`, expenseData),
  addExpenses: (expensesData) => expenseService.post('/addListOfExpence', expensesData),
  updateExpense: (id, expenseData) => expenseService.put(`/updateExpence/${id}`, expenseData),
  
  // User-Group endpoints
  getUserGroups: (userId) => userGroupService.get(`/getUserGroups/${userId}`),
  
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
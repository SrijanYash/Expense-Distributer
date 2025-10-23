import React from 'react';
import { Routes, Route } from 'react-router-dom';
import HomePage from './HomePage';
import GroupPage from './GroupPage';

function App() {
  return (
    <div className="app-container">
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/group/:groupId" element={<GroupPage />} />
      </Routes>
    </div>
  );
}

export default App;
import './App.css'
import { Routes, Route } from 'react-router-dom'
import Login from './Pages/Login'
import Register from './Pages/Register'
import Home from './Pages/Home'
import DashBoardLayout from './Pages/DashBoardLayout'
function App() {
  return (
      <Routes>
        <Route path='/' element={<Login />} />
        <Route path='/register' element={<Register />} />
        <Route path='/dashboard' element={<DashBoardLayout />}>
          <Route index element={<Home />} />
          <Route path="profile" element={<div>Profile Page</div>} />
        </Route>  
        <Route path="/agency/:agencyId" element={<AgencyPage />} />
      </Routes>
  );  
}

export default App

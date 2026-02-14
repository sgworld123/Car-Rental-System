import './App.css'
import { Routes, Route } from 'react-router-dom'
import Login from './Pages/Login'
import Register from './Pages/Register'
import Home from './Pages/Home'
import DashBoardLayout from './Pages/DashBoardLayout'
import AgencyPage from './Pages/AgencyPage'
import { ConfirmBooking } from './Pages/ConfirmBooking'
function App() {
  return (
      <Routes>
        <Route path='/' element={<Login />} />
        <Route path='/register' element={<Register />} />
        <Route path='/dashboard' element={<DashBoardLayout />}>
          <Route index element={<Home />} />
          <Route path="profile" element={<div>Profile Page</div>} />
        </Route>  
        <Route path="/agency/:id" element={<AgencyPage />} />
        <Route path="/confirm-booking" element={<ConfirmBooking />} />
      </Routes>
  );  
}

export default App

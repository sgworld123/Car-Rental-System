import './App.css'
import { Routes, Route } from 'react-router-dom'
import Login from './Pages/Login'
import Register from './Pages/Register'
import Home from './Pages/Home'
import DashBoardLayout from './Pages/DashBoardLayout'
import AgencyPage from './Pages/AgencyPage'
import { ConfirmBooking } from './Pages/ConfirmBooking'
import VehicleDetails from './Pages/VehicleDetails'
import MyBookings from './Pages/MyBookings'
import Profile from './Pages/Profile'
import Settings from './Pages/Settings'
function App() {
  return (
      <Routes>
        <Route path='/' element={<Login />} />
        <Route path='/register' element={<Register />} />
        <Route path='/dashboard' element={<DashBoardLayout />}>
          <Route index element={<Home />} />
          <Route path="bookings" element={<MyBookings />} />
          <Route path="settings" element={<Settings />} />
          <Route path="profile" element={<Profile />} />
        </Route>  
        <Route path="/agency/:id" element={<AgencyPage />} />
        <Route path="/vehicle-details/:vehicleId" element={<VehicleDetails />} />
        <Route path="/confirm-booking" element={<ConfirmBooking />} />
        
      </Routes>
  );  
}

export default App

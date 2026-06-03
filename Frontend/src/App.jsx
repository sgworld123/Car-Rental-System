import './App.css'
import { Routes, Route } from 'react-router-dom'

import Login from './Pages/LoginPage/Login'
import Register from './Pages/RegisterPage/Register'
import Home from './Pages/HomePage/Home'
import DashBoardLayout from './Pages/DashBoardLayout'
import AgencyPage from './Pages/AgencyPage/AgencyPage'
import VehicleDetails from './Pages/VehicleDetails/VehicleDetails'
import MyBookings from './Pages/MyBookings/MyBookings'
import Profile from './Pages/Profile/Profile'
import AboutMe from './Pages/AboutMePage/AboutMe'

import ProtectedRoute from './Components/ProtectedRoute'

function App() {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path='/' element={<Login />} />
      <Route path='/register' element={<Register />} />
      {/* Protected Dashboard Routes */}
      <Route path='/dashboard' element={<ProtectedRoute><DashBoardLayout /></ProtectedRoute>}>
        <Route index element={<Home />} />
        <Route path="bookings" element={<MyBookings />} />
        <Route path="aboutme" element={<AboutMe />} />
        <Route path="profile" element={<Profile />} />
        <Route path="agency/:id" element={<AgencyPage />} />                              
        <Route path="agency/:id/vehicle-details/:vehicleId" element={<VehicleDetails />} /> 
      </Route>

    </Routes>
  )
}

export default App
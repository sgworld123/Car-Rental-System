import './App.css'
import { Routes, Route } from 'react-router-dom'

import Login from './Pages/LoginPage/Login'
import Register from './Pages/RegisterPage/Register'
import Home from './Pages/HomePage/Home'
import DashBoardLayout from './Pages/DashBoardLayout'
import AgencyPage from './Pages/AgencyPage/AgencyPage'
import { ConfirmBooking } from './Pages/ConfirmBooking'
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
      <Route
        path='/dashboard'
        element={
          <ProtectedRoute>
            <DashBoardLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<Home />} />
        <Route path="bookings" element={<MyBookings />} />
        <Route path="settings" element={<AboutMe />} />
        <Route path="profile" element={<Profile />} />
      </Route>

      <Route
        path="/agency/"
        element={
          <ProtectedRoute>
            <AgencyPage />
          </ProtectedRoute>
        }
      />
      {/* Other Protected Pages */}

      <Route
        path="/vehicle-details/"
        element={
          <ProtectedRoute>
            <VehicleDetails />
          </ProtectedRoute>
        }
      />

      <Route
        path="/confirm-booking"
        element={
          <ProtectedRoute>
            <ConfirmBooking />
          </ProtectedRoute>
        }
      />

    </Routes>
  )
}

export default App
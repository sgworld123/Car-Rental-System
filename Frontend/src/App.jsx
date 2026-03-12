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
import AboutMe from './Pages/AboutMe'

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

      {/* Other Protected Pages */}
      <Route
        path="/agency/:id"
        element={
          <ProtectedRoute>
            <AgencyPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/vehicle-details/:vehicleId"
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
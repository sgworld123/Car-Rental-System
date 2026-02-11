import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRegister } from '../Hooks/useRegister';

const SignUpPage = () => {
  const { register, loading, error } = useRegister();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    username: '',
    password: '',
    confirmPassword: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSignUp = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      alert("Passwords do not match!");
      return;
    }
    console.log('Registering user:', formData);
    const success = await register(formData);
    if(success){
      navigate('/');
    }
    else{
      alert(error || "Registration failed. Please try again.");
    }
  };

  const handleSignInRedirect = (e) => {
    e.preventDefault();
    console.log('Navigate to Sign In');
    navigate('/');
  };

  const styles = {
    container: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      minHeight: '100vh', // Changed to minHeight to handle scrolling on small screens
      width: '100vw',
      backgroundColor: '#121212',
      color: '#e0e0e0',
      fontFamily: 'Arial, sans-serif',
      padding: '20px 0', // Add padding for small screens
    },
    card: {
      backgroundColor: '#1e1e1e',
      padding: '40px',
      borderRadius: '8px',
      boxShadow: '0 4px 15px rgba(0, 0, 0, 0.5)',
      width: '400px', // Slightly wider for more fields
      textAlign: 'center',
      border: '1px solid #333',
    },
    title: {
      marginBottom: '25px',
      fontSize: '24px',
      fontWeight: '600',
      color: '#ffffff',
    },
    inputGroup: {
      marginBottom: '15px', // Slightly tighter spacing
      textAlign: 'left',
    },
    label: {
      display: 'block',
      marginBottom: '5px',
      fontSize: '14px',
      color: '#b0b0b0',
    },
    input: {
      width: '100%',
      padding: '10px', // Slightly smaller padding to fit more fields
      borderRadius: '4px',
      border: '1px solid #333',
      backgroundColor: '#2c2c2c',
      color: '#fff',
      fontSize: '14px',
      boxSizing: 'border-box',
      outline: 'none',
    },
    buttonContainer: {
      marginTop: '20px',
      display: 'flex',
      flexDirection: 'column',
      gap: '15px',
    },
    signUpButton: {
      width: '100%',
      padding: '12px',
      borderRadius: '4px',
      border: 'none',
      backgroundColor: '#6200ea',
      color: '#fff',
      fontSize: '16px',
      fontWeight: 'bold',
      cursor: 'pointer',
      transition: 'background-color 0.2s',
    },
    signInLink: {
      background: 'none',
      border: 'none',
      color: '#bb86fc', // Light purple for text links
      textDecoration: 'underline',
      cursor: 'pointer',
      fontSize: '14px',
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Create Account</h2>
        
        <form onSubmit={handleSignUp}>
          {/* Full Name */}
          <div style={styles.inputGroup}>
            <label style={styles.label} htmlFor="fullName">Full Name</label>
            <input
              style={styles.input}
              type="text"
              name="fullName"
              value={formData.fullName}
              onChange={handleChange}
              placeholder="John Doe"
              required
            />
          </div>

          {/* Email */}
          <div style={styles.inputGroup}>
            <label style={styles.label} htmlFor="email">Email Address</label>
            <input
              style={styles.input}
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="john@example.com"
              required
            />
          </div>

          {/* Username */}
          <div style={styles.inputGroup}>
            <label style={styles.label} htmlFor="username">Username</label>
            <input
              style={styles.input}
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Choose a username"
              required
            />
          </div>

          {/* Password */}
          <div style={styles.inputGroup}>
            <label style={styles.label} htmlFor="password">Password</label>
            <input
              style={styles.input}
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Create a password"
              required
            />
          </div>

          {/* Confirm Password */}
          <div style={styles.inputGroup}>
            <label style={styles.label} htmlFor="confirmPassword">Confirm Password</label>
            <input
              style={styles.input}
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="Confirm your password"
              required
            />
          </div>

          <div style={styles.buttonContainer}>
            <button 
              style={styles.signUpButton} 
              type="submit"
              onMouseOver={(e) => e.target.style.backgroundColor = '#3700b3'}
              onMouseOut={(e) => e.target.style.backgroundColor = '#6200ea'}
            >
              Sign Up
            </button>
            
            <button 
              style={styles.signInLink} 
              onClick={handleSignInRedirect}
            >
              Already have an account? Sign In
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SignUpPage;
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLogin } from '../Hooks/useLogin';

const Login = () => {
  const { login,loading,error } = useLogin();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });

  
  const handleSignIn = async (e) => {
    e.preventDefault();
    console.log('Sign In clicked', formData);
    const success = await login(formData.username, formData.password);
    if (success) {
      navigate('/dashboard'); // Redirect to dashboard on successful login
    } else {
      alert('Invalid username or password');
    }
  };
  
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };
  const handleSignUp = (e) => {
    e.preventDefault();
    console.log('Sign Up clicked');
    navigate('/register');
  };

  // --- Styles Object ---
  const styles = {
    container: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
      width: '100vw',
      backgroundColor: '#121212', // Very dark grey background
      color: '#e0e0e0',
      fontFamily: 'Arial, sans-serif',
    },
    card: {
      backgroundColor: '#1e1e1e', // Slightly lighter card background
      padding: '40px',
      borderRadius: '8px',
      boxShadow: '0 4px 15px rgba(0, 0, 0, 0.5)',
      width: '350px',
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
      marginBottom: '20px',
      textAlign: 'left',
    },
    label: {
      display: 'block',
      marginBottom: '8px',
      fontSize: '14px',
      color: '#b0b0b0',
    },
    input: {
      width: '100%',
      padding: '12px',
      borderRadius: '4px',
      border: '1px solid #333',
      backgroundColor: '#2c2c2c', // Dark input background
      color: '#fff',
      fontSize: '16px',
      boxSizing: 'border-box', // Ensures padding doesn't affect width
      outline: 'none',
    },
    buttonContainer: {
      display: 'flex',
      justifyContent: 'space-between',
      gap: '10px',
      marginTop: '10px',
    },
    signInButton: {
      flex: 1,
      padding: '12px',
      borderRadius: '4px',
      border: 'none',
      backgroundColor: '#6200ea', // Primary accent color (Purple)
      color: '#fff',
      fontSize: '16px',
      fontWeight: 'bold',
      cursor: 'pointer',
      transition: 'background-color 0.2s',
    },
    signUpButton: {
      flex: 1,
      padding: '12px',
      borderRadius: '4px',
      border: '1px solid #6200ea',
      backgroundColor: 'transparent',
      color: '#6200ea',
      fontSize: '16px',
      fontWeight: 'bold',
      cursor: 'pointer',
      transition: 'all 0.2s',
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Welcome Back</h2>
        
        <form>
          {/* Username Input */}
          <div style={styles.inputGroup}>
            <label style={styles.label} htmlFor="username">Username</label>
            <input
              style={styles.input}
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Enter your username"
            />
          </div>

          {/* Password Input */}
          <div style={styles.inputGroup}>
            <label style={styles.label} htmlFor="password">Password</label>
            <input
              style={styles.input}
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Enter your password"
            />
          </div>

          {/* Action Buttons */}
          <div style={styles.buttonContainer}>
            <button 
              style={styles.signInButton} 
              onClick={handleSignIn}
              onMouseOver={(e) => e.target.style.backgroundColor = '#3700b3'}
              onMouseOut={(e) => e.target.style.backgroundColor = '#6200ea'}
            >
              Sign In
            </button>
            
            <button 
              style={styles.signUpButton} 
              onClick={handleSignUp}
              onMouseOver={(e) => {
                e.target.style.backgroundColor = '#6200ea'; 
                e.target.style.color = '#fff';
              }}
              onMouseOut={(e) => {
                e.target.style.backgroundColor = 'transparent'; 
                e.target.style.color = '#6200ea';
              }}
            >
              Sign Up
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
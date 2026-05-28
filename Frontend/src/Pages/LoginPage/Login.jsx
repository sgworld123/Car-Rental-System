import styles from "./Login.module.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useLogin } from "../../Hooks/useLogin";
import {
  FaApple,
  FaGoogle,
  FaCube,
} from "react-icons/fa";

export default function Login() {
  const { login, loading, error } = useLogin();
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
  return (
    <div className={styles.page}>
      <div className={styles.left}>
        <div className={styles.overlay}></div>

        <div className={styles.brand}>RENTENGINE</div>

        <div className={styles.heroContent}>
          <span>DRIVE WITHOUT LIMITS</span>

          <h1>
            Premium Cars.
            <br />
            <h1>Seamless Rentals.</h1>
          </h1>

          <p>
            Book everyday vehicles with ease.
            From weekend escapes to business travel, 
            RentEngine delivers a smooth rental experience.
          </p>
        </div>
      </div>

      <div className={styles.right}>
        <div className={styles.card}>
          <div className={styles.tabs}>
            <button className={styles.active}>
              LOG IN
            </button>
          </div>

          <div className={styles.content}>
            <h2>Welcome Back</h2>

            <p>
              Continue your journey with RENTENGINE.
            </p>

            <form className={styles.form}>
              <div className={styles.field}>
                <label>EMAIL ADDRESS</label>

                <input
                  type="email"
                  placeholder="alexander@vance.com"
                  value={formData.username}
                  onChange={handleChange}
                  name="username"
                />
              </div>

              <div className={styles.field}>
                <label>PASSWORD</label>

                <input
                  type="password"
                  placeholder="••••••••"
                  value={formData.password}
                  onChange={handleChange}
                  name="password"
                />
              </div>

              <div className={styles.options}>
                <label className={styles.checkbox}>
                  <input type="checkbox" />
                  <span>Remember Me</span>
                </label>

                <button type="button">
                  Forgot Credentials?
                </button>
              </div>

              <button className={styles.loginBtn} onClick={handleSignIn}>
                AUTHENTICATE
              </button>
              <button
                style={styles.signUpButton}
                onClick={handleSignUp}
                className={styles.loginBtn}
              >
                Sign Up
              </button>
            </form>
          </div>
        </div>

        <p className={styles.footer}>
          © 2026 RENTENGINE. All rights reserved.
        </p>
      </div>
    </div>
  );
}
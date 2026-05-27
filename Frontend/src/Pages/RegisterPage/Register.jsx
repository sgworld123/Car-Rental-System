import styles from "./Register.module.css";
import {
    FaCarSide,
    FaArrowRight,
    FaEye,
} from "react-icons/fa";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRegister } from "../../Hooks/useRegister";

export default function Register() {
    const { register, loading, error } = useRegister();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        fullName: '',
        email: '',
        phone: '',
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
        if (success) {
            navigate('/');
        }
        else {
            alert(error || "Registration failed. Please try again.");
        }
    };

    const handleSignInRedirect = (e) => {
        e.preventDefault();
        console.log('Navigate to Sign In');
        navigate('/');
    };

    return (
        <div className={styles.page}>
            <div className={styles.grid}></div>

            <header className={styles.navbar}>
                <div className={styles.brand}>
                    <FaCarSide />
                    <span>DriveEasy</span>
                </div>
            </header>

            <div className={styles.card}>
                <div className={styles.heading}>
                    <h1>Join DriveEasy</h1>

                    <p>
                        Experience luxury and convenience on your own terms.
                        Start your journey today.
                    </p>
                </div>

                <form className={styles.form}>
                    <div className={styles.formGrid}>
                        <div className={styles.field}>
                            <label>Full Name</label>
                            <input
                                type="text"
                                placeholder="John Doe"
                                name="fullName"
                                value={formData.fullName}
                                onChange={handleChange}
                            />
                        </div>

                        <div className={styles.field}>
                            <label>Username</label>
                            <input
                                type="text"
                                placeholder="johndoe88"
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                            />
                        </div>

                        <div className={styles.field}>
                            <label>Email Address</label>
                            <input
                                type="email"
                                placeholder="john@example.com"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                            />
                        </div>

                        <div className={styles.field}>
                            <label>Phone Number</label>
                            <input
                                type="text"
                                placeholder="1234567890"
                                name="phone"
                                value={formData.phone}
                                onChange={handleChange}
                            />
                        </div>

                        <div className={styles.field}>
                            <label>Password</label>

                            <div className={styles.passwordInput}>
                                <input
                                    type="password"
                                    placeholder="Password"
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                />
                                <FaEye />
                            </div>
                        </div>

                        <div className={styles.field}>
                            <label>Confirm Password</label>

                            <div className={styles.passwordInput}>
                                <input
                                    type="password"
                                    placeholder="Password"
                                    name="confirmPassword"
                                    value={formData.confirmPassword}
                                    onChange={handleChange}
                                />
                                <FaEye />
                            </div>
                        </div>
                    </div>

                    <div className={styles.checkboxRow}>
                        <input type="checkbox" id="terms" />

                        <label htmlFor="terms">
                            I agree to the <span>Terms of Service</span> and{" "}
                            <span>Privacy Policy</span>.
                        </label>
                    </div>

                    <button className={styles.submitBtn} onClick={handleSignUp}>
                        Create Account
                        <FaArrowRight />
                    </button>

                    <p className={styles.bottomText} onClick={handleSignInRedirect}>
                        Already have an account? <span>Sign in</span>
                    </p>
                </form>
            </div>

            <footer className={styles.footer}>
                <p>© 2024 DriveEasy Global Mobility. All rights reserved.</p>

                <div className={styles.footerLinks}>
                    <span>Security</span>
                    <span>Cookies</span>
                </div>
            </footer>

            <div className={styles.floatingCard}>
                <FaCarSide />
            </div>
        </div>
    );
}
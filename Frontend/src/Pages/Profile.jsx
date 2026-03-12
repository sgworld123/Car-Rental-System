import React, { useEffect, useState } from "react";
import { useGetUserInfo } from "../Hooks/useGetUserInfo";

const Profile = () => {
  const { loading, error, getInfo } = useGetUserInfo();
  const [user, setUser] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      const response = await getInfo();
      console.log(response);

      if (response?.data) {
        setUser(response.data);
        console.log(user)
      }
    };
    fetchProfile();
  }, []);

  if (loading)
    return (
      <div style={styles.center}>
        <p style={styles.muted}>Loading profile...</p>
      </div>
    );

  if (error)
    return (
      <div style={styles.center}>
        <p style={{ color: "#ff6b6b" }}>Failed to load profile.</p>
      </div>
    );

  return (
    <div style={styles.page}>
      <div style={styles.card}>
        <div style={styles.avatar}>
          {user?.name?.charAt(0)}
        </div>

        <h2 style={styles.name}>{user?.name}</h2>
        <p style={styles.email}>{user?.email}</p>

        <div style={styles.divider} />

        <div style={styles.row}>
          <span style={styles.value}>Phone : {user?.phone}</span>
        </div>

        <button style={styles.button}>Edit Profile</button>
      </div>
    </div>
  );
};

const styles = {
  page: {
    minHeight: "100vh",
    background: "#0f0f0f",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    padding: "24px",
  },

  card: {
    background: "#1a1a1a",
    padding: "32px",
    borderRadius: "18px",
    width: "100%",
    maxWidth: "380px",
    boxShadow: "0 0 40px rgba(0,0,0,.6)",
    textAlign: "center",
  },

  avatar: {
    width: "72px",
    height: "72px",
    borderRadius: "50%",
    background: "#2a2a2a",
    margin: "0 auto 16px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "28px",
    fontWeight: "600",
  },

  name: {
    margin: 0,
    color: "White",
    fontSize: "20px",
  },

  email: {
    color: "#999",
    fontSize: "14px",
    marginTop: "4px",
  },

  divider: {
    height: "1px",
    background: "#2a2a2a",
    margin: "20px 0",
  },

  row: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "20px",
  },

  label: {
    color: "#888",
  },

  value: {
    color: "White",
    fontWeight: "500",
  },

  button: {
    width: "100%",
    padding: "12px",
    background: "#fff",
    color: "#000",
    border: "none",
    borderRadius: "10px",
    cursor: "pointer",
    fontWeight: "600",
  },

  center: {
    minHeight: "100vh",
    background: "#0f0f0f",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },

  muted: {
    color: "#999",
  },
};

export default Profile;

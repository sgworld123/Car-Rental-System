import React, { useState, useEffect } from "react";

const styles = {
  page: {
    background: "#0f0f0f",
    minHeight: "100vh",
    padding: "50px 20px",
    color: "#fff",
    display: "flex",
    justifyContent: "center"
  },

  container: {
    maxWidth: "900px",
    width: "100%"
  },

  title: {
    fontSize: "34px",
    fontWeight: "600",
    marginBottom: "6px"
  },

  subtitle: {
    color: "#aaa",
    marginBottom: "30px",
    fontSize: "15px"
  },

  card: {
    background: "#1a1a1a",
    borderRadius: "16px",
    padding: "25px",
    marginBottom: "24px",
    border: "1px solid #2a2a2a"
  },

  sectionTitle: {
    fontSize: "20px",
    marginBottom: "12px",
    color: "#b57cff"
  },

  text: {
    color: "#ccc",
    lineHeight: "1.7",
    fontSize: "15px"
  },

  link: {
    color: "#b57cff",
    textDecoration: "none",
    fontSize: "14px"
  },

  likeButton: {
    marginTop: "10px",
    padding: "10px 18px",
    borderRadius: "10px",
    border: "none",
    background: "linear-gradient(135deg,#6d28d9,#9333ea)",
    color: "#fff",
    cursor: "pointer"
  }
};

const AboutMe = () => {

  const [likes, setLikes] = useState(0);

  useEffect(() => {
    const storedLikes = localStorage.getItem("projectLikes");
    if (storedLikes) {
      setLikes(Number(storedLikes));
    }
  }, []);

  const handleLike = () => {
    const newLikes = likes + 1;
    setLikes(newLikes);
    localStorage.setItem("projectLikes", newLikes);
  };

  return (
    <div style={styles.page}>
      <div style={styles.container}>

        {/* Header */}
        <h1 style={styles.title}>Thanks for Visiting 👋</h1>
        <p style={styles.subtitle}>
          I appreciate you taking the time to explore this project.
        </p>

        {/* About */}
        <div style={styles.card}>
          <h2 style={styles.sectionTitle}>Who Built This?</h2>
          <p style={styles.text}>
            Hi, I'm <b>Shreyansh Gupta</b>. I'm a backend-focused developer who
            enjoys building scalable systems and exploring distributed
            architectures.
            <br /><br />
            My main stack revolves around <b>Java, Spring Boot, microservices,
            RabbitMq, and distributed systems</b>. Alongside backend engineering,
            I actively practice <b>data structures and algorithms</b> through
            competitive programming.
            <br /><br />
            This project is one of the systems I built while experimenting with
            real-world backend architecture and system design concepts.
          </p>
        </div>

        {/* Architecture */}
        <div style={styles.card}>
          <h2 style={styles.sectionTitle}>System Architecture</h2>
          <p style={styles.text}>
            If you're interested in exploring how this system is built,
            you can check the full architecture and source code on GitHub.
          </p>

          <a
            style={styles.link}
            href="https://github.com/sgworld123/Car-Rental-System"
            target="_blank"
            rel="noreferrer"
          >
            View Architecture on GitHub →
          </a>
        </div>

        {/* Like Section */}
        <div style={styles.card}>
          <h2 style={styles.sectionTitle}>Enjoyed the Project?</h2>

          <p style={styles.text}>
            If you liked exploring the system, feel free to drop a like.
          </p>

          <button onClick={handleLike} style={styles.likeButton}>
            👍 Like ({likes})
          </button>
        </div>

        {/* Profiles */}
        <div style={styles.card}>
          <h2 style={styles.sectionTitle}>More About Me</h2>

          <p style={styles.text}>
            If you'd like to explore more of my coding work or connect:
          </p>

          <p style={{ marginTop: "10px" }}>
            LeetCode →{" "}
            <a
              style={styles.link}
              href="https://leetcode.com/u/sgworld123/"
              target="_blank"
              rel="noreferrer"
            >
              leetcode.com/u/sgworld123
            </a>
          </p>

          <p>
            CodeChef →{" "}
            <a
              style={styles.link}
              href="https://www.codechef.com/users/sgworld123"
              target="_blank"
              rel="noreferrer"
            >
              codechef.com/users/sgworld123
            </a>
          </p>

          <p>
            LinkedIn →{" "}
            <a
              style={styles.link}
              href="https://www.linkedin.com/in/dsa-dev-shreyansh/"
              target="_blank"
              rel="noreferrer"
            >
              linkedin.com/in/dsa-dev-shreyansh
            </a>
          </p>

          <p>
            Portfolio →{" "}
            <a
              style={styles.link}
              href="https://my-portfolio-snowy-eight-14.vercel.app/"
              target="_blank"
              rel="noreferrer"
            >
              View Portfolio
            </a>
          </p>
        </div>

        {/* Footer */}
        <p style={{ color: "#777", textAlign: "center", marginTop: "20px" }}>
          Thanks again for checking out the project 🙌
        </p>

      </div>
    </div>
  );
};

export default AboutMe;
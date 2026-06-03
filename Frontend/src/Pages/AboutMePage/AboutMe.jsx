import styles from "./AboutMe.module.css";
import React from "react";

import {
  FaInfoCircle,
  FaMoon,
  FaCodeBranch,
  FaTrashAlt,
  FaGithub,
  FaEnvelope,
  FaArrowRight,
  FaCircle,
} from "react-icons/fa";

export default function About() {
  const[likes, setLikes] = React.useState(0);
  const handleLike = () => {
    alert("Thanks for liking the project! 🙌");
  };

  return (
    <div className={styles.page}>

    <div className={styles.grid}></div>

    <div className={styles.container}>

      {/* HERO */}

      <div className={styles.hero}>

        <div className={styles.badge}>
          Crafted with Java & System Design
        </div>

        <h1 className={styles.title}>
          Thanks for Visiting 👋
        </h1>

        <p className={styles.subtitle}>
          I appreciate you taking the time to explore this project and its architecture.
        </p>

      </div>

      {/* ABOUT */}

      <div className={styles.card}>

        <h2 className={styles.sectionTitle}>
          Who Built This?
        </h2>

        <p className={styles.text}>
          Hi, I'm <b>Shreyansh Gupta</b>. I'm a backend-focused developer who
          enjoys building scalable systems and exploring distributed
          architectures.
          <br /><br />

          My main stack revolves around
          <b> Java, Spring Boot, Microservices,
          RabbitMQ, MongoDB, and distributed systems</b>.

          <br /><br />

          Alongside backend engineering,
          I actively practice
          <b> Data Structures & Algorithms</b>
          through competitive programming and system-level problem solving.

          <br /><br />

          This project was built while experimenting with
          real-world backend architecture, asynchronous communication,
          and scalable service design.
        </p>

      </div>

      {/* ARCHITECTURE */}

      <div className={styles.card}>

        <h2 className={styles.sectionTitle}>
          System Architecture
        </h2>

        <p className={styles.text}>
          If you're interested in understanding how the system is structured,
          you can explore the full architecture and implementation on GitHub.
        </p>

        <a
          className={styles.primaryLink}
          href="https://github.com/sgworld123/Car-Rental-System"
          target="_blank"
          rel="noreferrer"
        >
          View Architecture on GitHub →
        </a>

      </div>

      {/* LIKE SECTION */}

      <div className={styles.card}>

        <h2 className={styles.sectionTitle}>
          Enjoyed the Project?
        </h2>

        <p className={styles.text}>
          If you liked exploring the system, feel free to leave a like.
        </p>

        <button
          onClick={handleLike}
          className={styles.likeButton}
        >
          👍 Like
        </button>

      </div>

      {/* LINKS */}

      <div className={styles.card}>

        <h2 className={styles.sectionTitle}>
          More About Me
        </h2>

        <p className={styles.text}>
          If you'd like to connect or explore more of my work:
        </p>

        <div className={styles.linksGrid}>

          <a
            className={styles.profileLink}
            href="https://leetcode.com/u/sgworld123/"
            target="_blank"
            rel="noreferrer"
          >
            <span>LeetCode</span>
            <small>leetcode.com/u/sgworld123</small>
          </a>

          <a
            className={styles.profileLink}
            href="https://www.codechef.com/users/sgworld123"
            target="_blank"
            rel="noreferrer"
          >
            <span>CodeChef</span>
            <small>codechef.com/users/sgworld123</small>
          </a>

          <a
            className={styles.profileLink}
            href="https://www.linkedin.com/in/dsa-dev-shreyansh/"
            target="_blank"
            rel="noreferrer"
          >
            <span>LinkedIn</span>
            <small>linkedin.com/in/dsa-dev-shreyansh</small>
          </a>

          <a
            className={styles.profileLink}
            href="https://portfolio-new-ten-ruby.vercel.app/"
            target="_blank"
            rel="noreferrer"
          >
            <span>Portfolio</span>
            <small>View Portfolio</small>
          </a>

        </div>

      </div>

      {/* FOOTER */}

      <p className={styles.footer}>
        Thanks again for checking out the project 🙌
      </p>

    </div>

  </div>
);

}
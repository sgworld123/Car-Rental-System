import styles from "./AboutMe.module.css";

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
  return (
    <div className={styles.page}>
      <div className={styles.grid}></div>

      <div className={styles.container}>
        <div className={styles.heading}>
          <h1>About DriveEasy</h1>

          <p>
            Refining the car rental experience through
            thoughtful design and seamless technology.
          </p>
        </div>

        <div className={styles.layout}>
          <div className={styles.left}>
            <div className={styles.visionCard}>
              <div className={styles.cardTitle}>
                <FaInfoCircle />
                <h2>The Vision</h2>
              </div>

              <p>
                DriveEasy was born from the idea that renting
                a vehicle should be as enjoyable as the journey
                itself. We’ve combined a warm minimalist
                aesthetic with a performance-first architecture
                to create a platform that feels intuitive,
                reliable, and premium.
              </p>

              <div className={styles.techGrid}>
                <div>
                  <label>TECH STACK</label>
                  <h4>Tailwind CSS v3</h4>
                </div>

                <div>
                  <label>ARCHITECTURE</label>
                  <h4>Atomic Design</h4>
                </div>

                <div>
                  <label>AESTHETIC</label>
                  <h4>Warm Minimal</h4>
                </div>
              </div>
            </div>

            <div className={styles.architectCard}>
              <img
                src="https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=1200&auto=format&fit=crop"
                alt="architect"
              />

              <div className={styles.architectContent}>
                <h2>Meet the Architect</h2>

                <span>Lead UI/UX Developer</span>

                <p>
                  Dedicated to creating functional beauty in
                  the digital space. Specializing in
                  component-driven design systems and
                  performance optimization.
                </p>

                <div className={styles.actions}>
                  <button className={styles.githubBtn}>
                    <FaGithub />
                    GitHub
                  </button>

                  <button className={styles.contactBtn}>
                    <FaEnvelope />
                    Contact
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className={styles.right}>
            <div className={styles.settingsCard}>
              <h3>APPLICATION SETTINGS</h3>

              <div className={styles.setting}>
                <div className={styles.settingLeft}>
                  <span className={styles.iconBox}>
                    <FaMoon />
                  </span>

                  <p>Dark Mode</p>
                </div>

                <div className={styles.toggle}></div>
              </div>

              <div className={styles.setting}>
                <div className={styles.settingLeft}>
                  <span className={styles.iconBox}>
                    <FaCodeBranch />
                  </span>

                  <p>App Version</p>
                </div>

                <span className={styles.version}>
                  v2.4.0-stable
                </span>
              </div>

              <div className={styles.setting}>
                <div className={styles.settingLeft}>
                  <span
                    className={`${styles.iconBox} ${styles.red}`}
                  >
                    <FaTrashAlt />
                  </span>

                  <p>Clear Cache</p>
                </div>

                <button className={styles.resetBtn}>
                  Reset
                </button>
              </div>
            </div>

            <div className={styles.opensourceCard}>
              <h2>Open Source</h2>

              <p>
                DriveEasy is proudly open source. Join our
                community and help shape the future of
                mobility.
              </p>

              <button>
                Visit Repository
                <FaArrowRight />
              </button>

              <div className={styles.bgArrow}>‹</div>
            </div>

            <div className={styles.statusCard}>
              <FaCircle />
              <span>All systems operational</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
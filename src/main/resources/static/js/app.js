/**
 * DevPath — Main Application Logic & View Router
 */

const App = {
  allMistakes: [],

  async init() {
    console.log("Initializing DevPath Portal...");
    this.setupEventListeners();

    // Check if authenticated
    const token = API.getToken();
    if (token) {
      try {
        const user = await API.getCurrentUser();
        State.setUser(user);
        this.navigate('dashboard');
      } catch (e) {
        console.warn("Token expired or invalid, clearing session.");
        API.setToken(null);
        State.setUser(null);
        this.navigate('landing');
      }
    } else {
      this.navigate('landing');
    }
  },

  setupEventListeners() {
    window.addEventListener('hashchange', () => {
      const hash = window.location.hash.replace('#', '') || 'landing';
      this.navigate(hash, false);
    });
  },

  // View Navigation Router
  navigate(viewName, updateHash = true) {
    if (updateHash) {
      window.location.hash = viewName;
    }

    // Toggle navigation active class
    document.querySelectorAll('.nav-link').forEach(el => el.classList.remove('active'));
    const activeNav = document.getElementById(`nav-${viewName}`);
    if (activeNav) activeNav.classList.add('active');

    // Toggle view section
    document.querySelectorAll('.view-section').forEach(el => el.classList.remove('active-view'));
    const targetSection = document.getElementById(`${viewName}-view`);
    if (targetSection) {
      targetSection.classList.add('active-view');
      window.scrollTo(0, 0);
    }

    State.activeView = viewName;

    // Load view data
    switch (viewName) {
      case 'dashboard':
        this.loadDashboardView();
        break;
      case 'radar':
        this.loadRadarView();
        break;
      case 'mistakes':
        this.loadMistakesView();
        break;
      case 'paths':
        this.loadLearningPathsView();
        break;
      case 'coding':
        this.loadCodingArenaView();
        break;
      case 'assessments':
        this.loadAssessmentsView();
        break;
      case 'missions':
        this.loadMissionsView();
        break;
      case 'progress':
        this.loadProofOfProgressView();
        break;
      case 'admin':
        this.loadAdminView();
        break;
      case 'health':
        this.loadHealthView();
        break;
    }
  },

  // Auth Modals & Demo Shortcuts
  openLoginModal() {
    this.switchAuthTab('login');
    document.getElementById('auth-modal').classList.add('open');
  },

  openRegisterModal() {
    this.switchAuthTab('register');
    document.getElementById('auth-modal').classList.add('open');
  },

  closeAuthModal() {
    document.getElementById('auth-modal').classList.remove('open');
  },

  switchAuthTab(tab) {
    const isLogin = tab === 'login';
    document.getElementById('login-form').style.display = isLogin ? 'block' : 'none';
    document.getElementById('register-form').style.display = isLogin ? 'none' : 'block';
    document.getElementById('auth-tab-login').classList.toggle('active', isLogin);
    document.getElementById('auth-tab-register').classList.toggle('active', !isLogin);
    document.getElementById('auth-modal-title').textContent = isLogin ? 'Sign In to DevPath' : 'Create Student Account';
  },

  prefillDemoStudent() {
    document.getElementById('login-email').value = 'student@devpath.com';
    document.getElementById('login-password').value = 'Student@123';
  },

  prefillDemoAdmin() {
    document.getElementById('login-email').value = 'admin@devpath.com';
    document.getElementById('login-password').value = 'Admin@123';
  },

  async quickDemoStudent() {
    try {
      const res = await API.login('student@devpath.com', 'Student@123');
      const user = await API.getCurrentUser();
      State.setUser(user);
      this.showToast(`Logged in as Demo Student (${user.name})`, 'success');
      this.navigate('dashboard');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async quickDemoAdmin() {
    try {
      const res = await API.login('admin@devpath.com', 'Admin@123');
      const user = await API.getCurrentUser();
      State.setUser(user);
      this.showToast(`Logged in as Administrator (${user.name})`, 'success');
      this.navigate('admin');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    try {
      await API.login(email, password);
      const user = await API.getCurrentUser();
      State.setUser(user);
      this.closeAuthModal();
      this.showToast('Login successful!', 'success');
      this.navigate('dashboard');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async handleRegister(e) {
    e.preventDefault();
    const name = document.getElementById('reg-name').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    const targetRole = document.getElementById('reg-role').value;

    try {
      await API.register(name, email, password, targetRole, 'Computer Science Department');
      const user = await API.getCurrentUser();
      State.setUser(user);
      this.closeAuthModal();
      this.showToast('Account registered successfully!', 'success');
      this.navigate('dashboard');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  logout() {
    API.setToken(null);
    State.setUser(null);
    this.showToast('Logged out successfully', 'success');
    this.navigate('landing');
  },

  // 1. DASHBOARD VIEW
  async loadDashboardView() {
    try {
      const dash = await API.getStudentDashboard();

      document.getElementById('dash-greeting').textContent = `Welcome back, ${dash.studentName || 'Student'}!`;
      document.getElementById('dash-role').textContent = dash.targetRole || 'Software Engineer';
      document.getElementById('dash-college').textContent = dash.college || 'Engineering';

      document.getElementById('dash-readiness-val').textContent = `${dash.radar.overallReadinessIndex || 0}%`;
      document.getElementById('dash-lessons-val').textContent = dash.completedLessonsCount || 0;
      document.getElementById('dash-challenges-val').textContent = dash.solvedChallengesCount || 0;
      
      const delta = dash.progress.percentagePointsDelta || 0;
      document.getElementById('dash-progress-delta').textContent = delta >= 0 ? `+${delta}%` : `${delta}%`;

      document.getElementById('dash-strongest').textContent = dash.radar.strongestSkill || 'N/A';
      document.getElementById('dash-weakest').textContent = dash.radar.weakestSkill || 'N/A';
      document.getElementById('dash-radar-badge').textContent = dash.radar.isDemoData ? 'Baseline Sample' : 'Real Assessment Data';

      // Render Dashboard Radar Chart
      this.renderRadarChart('dashboardRadarCanvas', dash.radar);

      // Render Adaptive Recommendations
      const recList = document.getElementById('dash-recommendations-list');
      if (dash.recommendations && dash.recommendations.length > 0) {
        recList.innerHTML = dash.recommendations.map(r => `
          <div style="background:#111827;border:1px solid #243049;border-radius:var(--radius-sm);padding:1rem;border-left:4px solid ${r.priority === 'HIGH' ? '#f43f5e' : '#6366f1'};">
            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:0.35rem;">
              <span class="badge ${r.priority === 'HIGH' ? 'badge-hard' : 'badge-easy'}">${r.priority} Priority</span>
              <span style="font-size:0.75rem;color:var(--text-muted);">${r.type}</span>
            </div>
            <h4 style="font-size:1rem;margin-bottom:0.35rem;">${r.title}</h4>
            <p style="font-size:0.85rem;color:var(--text-secondary);line-height:1.5;margin-bottom:0.75rem;">
              <strong>Diagnostic Reason:</strong> ${r.reason}
            </p>
            <button class="btn btn-secondary btn-sm" onclick="App.handleRecommendationAction('${r.type}', ${r.referenceId}, '${r.referenceSlug || ''}')">
              Start Activity &rarr;
            </button>
          </div>
        `).join('');
      } else {
        recList.innerHTML = `<p style="color:var(--text-muted);font-size:0.9rem;">No diagnostic gaps identified. Keep taking placement assessments!</p>`;
      }

      // Render Recent Attempts
      const tbody = document.getElementById('dash-attempts-tbody');
      if (dash.recentAssessments && dash.recentAssessments.length > 0) {
        tbody.innerHTML = dash.recentAssessments.map(a => `
          <tr style="border-bottom:1px solid #1e293b;">
            <td style="padding:0.75rem 1rem;font-weight:600;">${a.assessmentTitle}</td>
            <td style="padding:0.75rem 1rem;">${a.score} / ${a.totalMarks}</td>
            <td style="padding:0.75rem 1rem;">${a.percentage}%</td>
            <td style="padding:0.75rem 1rem;">
              <span class="badge ${a.passed ? 'badge-easy' : 'badge-hard'}">${a.passed ? 'PASSED' : 'RETRY'}</span>
            </td>
            <td style="padding:0.75rem 1rem;color:var(--text-muted);font-size:0.85rem;">
              ${a.completedAt ? new Date(a.completedAt).toLocaleDateString() : 'Recent'}
            </td>
          </tr>
        `).join('');
      } else {
        tbody.innerHTML = `<tr><td colspan="5" style="padding:1.5rem;text-align:center;color:var(--text-muted);">No attempts recorded yet. Take your first placement assessment!</td></tr>`;
      }
    } catch (err) {
      console.error("Dashboard error:", err);
      this.showToast("Please login to view dashboard", "error");
      this.openLoginModal();
    }
  },

  handleRecommendationAction(type, id, slug) {
    if (type === 'LESSON') {
      this.viewLessonById(id);
    } else if (type === 'CODING_CHALLENGE') {
      this.viewChallengeById(id);
    } else {
      this.navigate('assessments');
    }
  },

  // 2. RADAR VIEW
  async loadRadarView() {
    try {
      const radar = await API.getRadar();
      this.renderRadarChart('fullRadarCanvas', radar);

      document.getElementById('radar-full-status').textContent = radar.isDemoData ? 'Baseline Sample' : 'Active Account Data';
      document.getElementById('radar-next-activity').textContent = radar.recommendedNextActivity || 'Take assessments to refresh skill readiness scores.';

      const categories = [
        { name: 'Programming Fundamentals', score: radar.programmingFundamentals },
        { name: 'Data Structures & Algorithms', score: radar.dataStructuresAndAlgorithms },
        { name: 'Aptitude & Logical Reasoning', score: radar.aptitudeAndLogical },
        { name: 'Computer Science Fundamentals', score: radar.csFundamentals },
        { name: 'Technical Interview Preparation', score: radar.techInterviewPrep },
      ];

      const barsContainer = document.getElementById('radar-categories-bars');
      barsContainer.innerHTML = categories.map(cat => `
        <div>
          <div style="display:flex;justify-content:space-between;font-size:0.9rem;margin-bottom:0.35rem;">
            <span>${cat.name}</span>
            <strong style="color:${cat.score >= 70 ? '#34d399' : cat.score >= 50 ? '#fbbf24' : '#fb7185'}">${cat.score}%</strong>
          </div>
          <div class="progress-bar-container">
            <div class="progress-bar-fill" style="width:${cat.score}%;background:${cat.score >= 70 ? '#10b981' : cat.score >= 50 ? '#f59e0b' : '#f43f5e'};"></div>
          </div>
        </div>
      `).join('');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  renderRadarChart(canvasId, radarData) {
    const canvas = document.getElementById(canvasId);
    if (!canvas || typeof Chart === 'undefined') return;

    if (State.charts[canvasId]) {
      State.charts[canvasId].destroy();
    }

    const data = [
      radarData.programmingFundamentals || 0,
      radarData.dataStructuresAndAlgorithms || 0,
      radarData.aptitudeAndLogical || 0,
      radarData.csFundamentals || 0,
      radarData.techInterviewPrep || 0,
    ];

    const ctx = canvas.getContext('2d');
    State.charts[canvasId] = new Chart(ctx, {
      type: 'radar',
      data: {
        labels: [
          'Programming Fundamentals',
          'DSA & Algorithms',
          'Aptitude & Logic',
          'CS Fundamentals',
          'Interview Prep',
        ],
        datasets: [{
          label: 'Placement Readiness Score (%)',
          data: data,
          backgroundColor: 'rgba(99, 102, 241, 0.25)',
          borderColor: '#6366f1',
          borderWidth: 2,
          pointBackgroundColor: '#8b5cf6',
          pointBorderColor: '#ffffff',
          pointHoverBackgroundColor: '#ffffff',
          pointHoverBorderColor: '#8b5cf6',
          pointRadius: 4,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          r: {
            angleLines: { color: 'rgba(255, 255, 255, 0.1)' },
            grid: { color: 'rgba(255, 255, 255, 0.1)' },
            pointLabels: {
              color: '#94a3b8',
              font: { size: 11, family: 'Outfit' },
            },
            ticks: {
              backdropColor: 'transparent',
              color: '#64748b',
              stepSize: 20,
            },
            min: 0,
            max: 100,
          },
        },
        plugins: {
          legend: { display: false },
        },
      },
    });
  },

  // 3. MISTAKE-TO-MASTERY VIEW
  async loadMistakesView() {
    try {
      const mistakes = await API.getMistakes();
      this.allMistakes = mistakes;
      this.renderMistakesList(mistakes);
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  filterMistakes(category) {
    document.querySelectorAll('#mistake-filter-nav .tab-btn').forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');

    if (category === 'ALL') {
      this.renderMistakesList(this.allMistakes);
    } else {
      const filtered = this.allMistakes.filter(m => m.mistakeCategory === category);
      this.renderMistakesList(filtered);
    }
  },

  renderMistakesList(mistakes) {
    const container = document.getElementById('mistakes-list-container');
    if (!mistakes || mistakes.length === 0) {
      container.innerHTML = `
        <div class="card" style="text-align:center;padding:3rem;">
          <span style="font-size:2.5rem;display:block;margin-bottom:1rem;">&#127881;</span>
          <h3>No Mistakes Logged in this Category</h3>
          <p style="color:var(--text-secondary);margin-top:0.5rem;">Take a diagnostic assessment to analyze your logic traps and concept gaps.</p>
          <button class="btn btn-primary" style="margin-top:1.5rem;" onclick="App.navigate('assessments')">Take Diagnostic Assessment</button>
        </div>
      `;
      return;
    }

    container.innerHTML = mistakes.map((m, idx) => `
      <div class="card" style="border-left:4px solid var(--accent-rose);">
        <div style="display:flex;justify-content:space-between;align-items:flex-start;flex-wrap:wrap;gap:0.75rem;margin-bottom:0.75rem;">
          <div style="display:flex;gap:0.5rem;align-items:center;">
            <span class="badge badge-hard">Mistake #${idx + 1}</span>
            <span class="badge badge-concept">${m.mistakeCategoryLabel || m.mistakeCategory}</span>
            <span style="font-size:0.85rem;color:var(--text-muted);">${m.category} &bull; ${m.topic}</span>
          </div>
        </div>

        <h3 style="font-size:1.15rem;margin-bottom:0.75rem;line-height:1.5;">${m.questionText}</h3>

        ${m.codeSnippet ? `<pre class="code-box"><code>${this.escapeHtml(m.codeSnippet)}</code></pre>` : ''}

        <div class="grid-2" style="background:#111827;border-radius:var(--radius-sm);padding:1rem;margin:1rem 0;font-size:0.9rem;">
          <div>
            <div style="color:#fb7185;font-weight:600;margin-bottom:0.25rem;">&#10006; Your Submitted Answer:</div>
            <div style="color:var(--text-secondary);">${m.selectedOptionText || 'No answer submitted'}</div>
          </div>
          <div>
            <div style="color:#34d399;font-weight:600;margin-bottom:0.25rem;">&#10004; Correct Answer:</div>
            <div style="color:var(--text-primary);">${m.correctOptionText}</div>
          </div>
        </div>

        <div style="background:#1e293b;border-radius:var(--radius-sm);padding:1rem;margin-top:1rem;">
          <h4 style="color:#fbbf24;font-size:0.95rem;margin-bottom:0.35rem;">Diagnostic Explanation:</h4>
          <p style="color:var(--text-secondary);font-size:0.9rem;line-height:1.6;margin-bottom:0.75rem;">${m.explanation}</p>
          
          <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:0.5rem;padding-top:0.75rem;border-top:1px solid #334155;font-size:0.85rem;">
            <span>Recommended Revision: <strong style="color:var(--primary);">${m.revisionTopic || m.topic}</strong></span>
            <span style="color:#34d399;">Tip: ${m.followUpTip || 'Review core edge cases.'}</span>
          </div>
        </div>
      </div>
    `).join('');
  },

  // 4. LEARNING PATHS (LEARNING HUB)
  async loadLearningPathsView() {
    try {
      const paths = await API.getLearningPaths();
      const completedSet = await API.getCompletedLessonIds().catch(() => new Set());
      const grid = document.getElementById('learning-paths-grid');

      grid.innerHTML = paths.map(path => `
        <div class="card card-interactive" onclick="App.viewPathLessons(${path.id}, '${path.slug}')">
          <div class="card-header">
            <span class="badge badge-concept">${path.category}</span>
            <span class="badge badge-medium">${path.difficulty}</span>
          </div>
          <h3 style="font-size:1.3rem;margin-bottom:0.5rem;">${path.title}</h3>
          <p style="color:var(--text-secondary);font-size:0.9rem;line-height:1.5;margin-bottom:1.5rem;">${path.description}</p>
          <div style="display:flex;justify-content:space-between;align-items:center;font-size:0.85rem;color:var(--text-muted);border-top:1px solid var(--border-subtle);padding-top:1rem;">
            <span>&#9201; ~${path.estimatedHours} Hours</span>
            <span style="color:var(--primary);font-weight:600;">View Lessons &rarr;</span>
          </div>
        </div>
      `).join('');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async viewPathLessons(pathId, slug) {
    try {
      const lessons = await API.getLessonsByPath(pathId);
      if (lessons.length > 0) {
        this.renderLessonDetail(lessons[0]);
      } else {
        this.showToast('No published lessons found in this roadmap yet.', 'error');
      }
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async viewLessonById(id) {
    try {
      const lesson = await API.getLessonById(id);
      this.renderLessonDetail(lesson);
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  renderLessonDetail(lesson) {
    State.lesson.data = lesson;
    document.getElementById('lesson-title').textContent = lesson.title;
    document.getElementById('lesson-topic').textContent = `Topic: ${lesson.topic}`;
    document.getElementById('lesson-category-badge').textContent = lesson.category;
    document.getElementById('lesson-difficulty-badge').textContent = lesson.difficulty;
    document.getElementById('lesson-time-badge').textContent = `${lesson.readTimeMinutes || 10} min read`;
    document.getElementById('lesson-summary').textContent = lesson.summary;
    document.getElementById('lesson-content-body').innerHTML = this.formatMarkdown(lesson.content);
    document.getElementById('lesson-prereqs').textContent = lesson.prerequisites || 'Basic programming fundamentals';
    document.getElementById('lesson-takeaways').textContent = lesson.keyTakeaways || 'Mastered core algorithmic concept';

    this.navigate('lesson-detail');
  },

  async toggleCurrentLesson() {
    if (!State.lesson.data) return;
    try {
      const progress = await API.toggleLessonComplete(State.lesson.data.id);
      const isCompleted = progress.completed;
      const btn = document.getElementById('lesson-complete-btn');
      btn.textContent = isCompleted ? '✓ Completed' : 'Mark as Completed';
      btn.className = isCompleted ? 'btn btn-success' : 'btn btn-outline';
      this.showToast(isCompleted ? 'Lesson completed!' : 'Marked as uncompleted', 'success');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  // 5. CODING ARENA
  async loadCodingArenaView() {
    try {
      const challenges = await API.getChallenges();
      const grid = document.getElementById('coding-challenges-grid');

      grid.innerHTML = challenges.map(c => `
        <div class="card card-interactive" onclick="App.viewChallengeById(${c.id})">
          <div class="card-header">
            <span class="badge ${c.difficulty === 'EASY' ? 'badge-easy' : c.difficulty === 'HARD' ? 'badge-hard' : 'badge-medium'}">${c.difficulty}</span>
            <span class="badge badge-syntax">${c.topic}</span>
          </div>
          <h3 style="font-size:1.3rem;margin-bottom:0.5rem;">${c.title}</h3>
          <p style="color:var(--text-secondary);font-size:0.9rem;line-height:1.5;margin-bottom:1.25rem;">
            ${c.description.substring(0, 150)}...
          </p>
          <div style="display:flex;justify-content:space-between;align-items:center;font-size:0.85rem;color:var(--text-muted);border-top:1px solid var(--border-subtle);padding-top:0.75rem;">
            <span>Target: <strong>${c.timeComplexity}</strong> Time</span>
            <span style="color:var(--primary);font-weight:600;">Solve Challenge &rarr;</span>
          </div>
        </div>
      `).join('');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async viewChallengeById(id) {
    try {
      const c = await API.getChallengeById(id);
      State.coding.challenge = c;

      document.getElementById('challenge-title').textContent = c.title;
      document.getElementById('challenge-diff-badge').textContent = c.difficulty;
      document.getElementById('challenge-topic-badge').textContent = c.topic;
      document.getElementById('challenge-description').textContent = c.description;
      document.getElementById('challenge-example-in').textContent = c.exampleInput || 'nums = [2, 7, 11, 15], target = 9';
      document.getElementById('challenge-example-out').textContent = c.exampleOutput || '[0, 1]';
      document.getElementById('challenge-constraints').textContent = c.constraints || 'Standard time and space limits apply.';
      document.getElementById('challenge-approach').textContent = c.expectedApproach || 'Optimal single-pass algorithm.';
      document.getElementById('challenge-time-comp').textContent = c.timeComplexity || 'O(n)';
      document.getElementById('challenge-space-comp').textContent = c.spaceComplexity || 'O(1)';

      const langSelect = document.getElementById('challenge-lang-select');
      langSelect.value = 'java';
      document.getElementById('challenge-code-input').value = c.starterCodeJava || '// Write your solution here...';

      document.getElementById('challenge-result-box').style.display = 'none';

      this.navigate('coding-detail');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  handleLanguageChange() {
    const lang = document.getElementById('challenge-lang-select').value;
    const c = State.coding.challenge;
    if (!c) return;

    if (lang === 'java') {
      document.getElementById('challenge-code-input').value = c.starterCodeJava || '// Write your Java solution here...';
    } else {
      document.getElementById('challenge-code-input').value = c.starterCodeJs || '// Write your JavaScript solution here...';
    }
  },

  runLocalCodeCheck() {
    const code = document.getElementById('challenge-code-input').value;
    const resBox = document.getElementById('challenge-result-box');

    if (!code || code.trim().length < 15) {
      this.showToast('Please provide your solution implementation before running test cases.', 'error');
      return;
    }

    resBox.style.display = 'block';
    resBox.style.background = '#111827';
    resBox.style.border = '1px solid #10b981';
    resBox.innerHTML = `
      <div style="color:#34d399;font-weight:700;margin-bottom:0.25rem;">✓ Static Checks Passed</div>
      <p style="font-size:0.85rem;color:var(--text-secondary);">Algorithm contains proper loop structures and return definitions. Ready to submit to verification engine.</p>
    `;
  },

  async submitChallengeSolution() {
    const c = State.coding.challenge;
    if (!c) return;

    const code = document.getElementById('challenge-code-input').value;
    const lang = document.getElementById('challenge-lang-select').value;

    try {
      const res = await API.submitCode(c.id, lang, code);
      const resBox = document.getElementById('challenge-result-box');
      resBox.style.display = 'block';

      if (res.status === 'ACCEPTED') {
        resBox.style.background = 'rgba(16, 185, 129, 0.15)';
        resBox.style.border = '1px solid #10b981';
        resBox.innerHTML = `
          <div style="color:#34d399;font-weight:700;font-size:1.1rem;margin-bottom:0.25rem;">✓ Status: ACCEPTED</div>
          <p style="font-size:0.9rem;color:var(--text-primary);margin-bottom:0.5rem;">${res.message}</p>
          <div style="font-size:0.85rem;color:var(--text-secondary);">
            Test cases: <strong>${res.testCasesPassed} / ${res.totalTestCases}</strong> &bull; Complexity: <strong>${res.timeComplexity}</strong>
          </div>
        `;
        this.showToast('Solution accepted! Score recorded.', 'success');
      } else {
        resBox.style.background = 'rgba(244, 63, 94, 0.15)';
        resBox.style.border = '1px solid #f43f5e';
        resBox.innerHTML = `
          <div style="color:#fb7185;font-weight:700;font-size:1.1rem;margin-bottom:0.25rem;">✕ Status: ${res.status}</div>
          <p style="font-size:0.9rem;color:var(--text-primary);margin-bottom:0.5rem;">${res.message}</p>
          <div style="font-size:0.85rem;color:var(--text-secondary);">Passed: ${res.testCasesPassed} / ${res.totalTestCases}</div>
        `;
        this.showToast('Test cases failed. Check edge conditions.', 'error');
      }
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  // 6. ASSESSMENTS CATALOG & TAKING ARENA
  async loadAssessmentsView() {
    try {
      const assessments = await API.getAssessments();
      const grid = document.getElementById('assessments-catalog-grid');

      grid.innerHTML = assessments.map(a => `
        <div class="card card-interactive" onclick="App.startAssessment(${a.id})">
          <div class="card-header">
            <span class="badge ${a.isPlacementMock ? 'badge-hard' : 'badge-concept'}">
              ${a.isPlacementMock ? 'Comprehensive Mock' : 'Category Diagnostic'}
            </span>
            <span class="badge badge-medium">${a.difficulty}</span>
          </div>
          <h3 style="font-size:1.3rem;margin-bottom:0.5rem;">${a.title}</h3>
          <p style="color:var(--text-secondary);font-size:0.9rem;line-height:1.5;margin-bottom:1.5rem;">${a.description}</p>
          <div style="display:flex;justify-content:space-between;align-items:center;font-size:0.85rem;color:var(--text-muted);border-top:1px solid var(--border-subtle);padding-top:1rem;">
            <span>&#9201; ${a.timeLimitMinutes} Mins &bull; ${a.totalQuestions} Questions</span>
            <button class="btn btn-primary btn-sm">Start Test &rarr;</button>
          </div>
        </div>
      `).join('');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async startAssessment(id) {
    try {
      const assessment = await API.getAssessmentById(id);
      State.assessment.data = assessment;
      State.assessment.answers = {};
      State.assessment.flagged = new Set();
      State.assessment.currentQuestionIndex = 0;
      State.assessment.remainingSeconds = (assessment.timeLimitMinutes || 15) * 60;
      State.assessment.startTime = Date.now();

      document.getElementById('arena-title').textContent = assessment.title;

      // Start timer
      if (State.assessment.timerInterval) clearInterval(State.assessment.timerInterval);
      State.assessment.timerInterval = setInterval(() => {
        State.assessment.remainingSeconds--;
        this.updateTimerDisplay();
        if (State.assessment.remainingSeconds <= 0) {
          clearInterval(State.assessment.timerInterval);
          this.submitAssessment(true);
        }
      }, 1000);

      this.renderArenaQuestion();
      this.renderArenaPalette();
      this.navigate('assessment-take');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  updateTimerDisplay() {
    const totalSec = State.assessment.remainingSeconds;
    const mins = Math.floor(totalSec / 60);
    const secs = totalSec % 60;
    const display = `${mins < 10 ? '0' : ''}${mins}:${secs < 10 ? '0' : ''}${secs}`;
    const el = document.getElementById('arena-timer-display');
    if (el) el.textContent = display;
  },

  renderArenaQuestion() {
    const a = State.assessment.data;
    if (!a || !a.questions || a.questions.length === 0) return;

    const idx = State.assessment.currentQuestionIndex;
    const q = a.questions[idx];

    document.getElementById('arena-question-tracker').textContent = `Question ${idx + 1} of ${a.questions.length}`;
    document.getElementById('arena-q-category').textContent = `${q.category} &bull; ${q.topic}`;
    document.getElementById('arena-q-text').textContent = q.questionText;

    // Code snippet
    const codeBox = document.getElementById('arena-q-code-container');
    if (q.codeSnippet && q.codeSnippet.trim()) {
      codeBox.style.display = 'block';
      document.getElementById('arena-q-code').textContent = q.codeSnippet;
    } else {
      codeBox.style.display = 'none';
    }

    // Flag button
    const isFlagged = State.assessment.flagged.has(q.id);
    const flagBtn = document.getElementById('arena-flag-btn');
    flagBtn.textContent = isFlagged ? '⚑ Flagged' : '⚐ Flag for Review';
    flagBtn.style.color = isFlagged ? '#fbbf24' : 'var(--text-primary)';

    // Options
    const currentSelected = State.assessment.answers[q.id];
    const optionsContainer = document.getElementById('arena-options-container');
    const options = [
      { letter: 'A', text: q.optionA },
      { letter: 'B', text: q.optionB },
      { letter: 'C', text: q.optionC },
      { letter: 'D', text: q.optionD },
    ];

    optionsContainer.innerHTML = options.map(opt => `
      <div class="option-card ${currentSelected === opt.letter ? 'selected' : ''}" onclick="App.selectOption(${q.id}, '${opt.letter}')">
        <div class="option-letter">${opt.letter}</div>
        <div style="font-size:0.95rem;color:var(--text-primary);">${opt.text}</div>
      </div>
    `).join('');

    // Previous / Next buttons
    document.getElementById('arena-prev-btn').disabled = (idx === 0);
    document.getElementById('arena-next-btn').textContent = (idx === a.questions.length - 1) ? 'Review & Submit' : 'Next Question';
  },

  renderArenaPalette() {
    const a = State.assessment.data;
    if (!a || !a.questions) return;

    const grid = document.getElementById('arena-palette-grid');
    grid.innerHTML = a.questions.map((q, i) => {
      const isAnswered = !!State.assessment.answers[q.id];
      const isCurrent = (i === State.assessment.currentQuestionIndex);
      const isFlagged = State.assessment.flagged.has(q.id);

      let bg = '#1e293b';
      let border = '#334155';
      if (isCurrent) {
        border = '#6366f1';
      }
      if (isAnswered) {
        bg = 'rgba(16, 185, 129, 0.25)';
        border = '#10b981';
      }
      if (isFlagged) {
        border = '#f59e0b';
      }

      return `
        <button style="padding:0.5rem;background:${bg};border:2px solid ${border};color:white;border-radius:var(--radius-sm);cursor:pointer;font-weight:700;" onclick="App.goToQuestion(${i})">
          ${i + 1}
        </button>
      `;
    }).join('');
  },

  selectOption(qId, letter) {
    State.assessment.answers[qId] = letter;
    this.renderArenaQuestion();
    this.renderArenaPalette();
  },

  toggleFlagCurrentQuestion() {
    const a = State.assessment.data;
    const q = a.questions[State.assessment.currentQuestionIndex];
    if (State.assessment.flagged.has(q.id)) {
      State.assessment.flagged.delete(q.id);
    } else {
      State.assessment.flagged.add(q.id);
    }
    this.renderArenaQuestion();
    this.renderArenaPalette();
  },

  goToQuestion(idx) {
    State.assessment.currentQuestionIndex = idx;
    this.renderArenaQuestion();
    this.renderArenaPalette();
  },

  prevQuestion() {
    if (State.assessment.currentQuestionIndex > 0) {
      State.assessment.currentQuestionIndex--;
      this.renderArenaQuestion();
      this.renderArenaPalette();
    }
  },

  nextQuestion() {
    const a = State.assessment.data;
    if (State.assessment.currentQuestionIndex < a.questions.length - 1) {
      State.assessment.currentQuestionIndex++;
      this.renderArenaQuestion();
      this.renderArenaPalette();
    } else {
      this.confirmSubmitAssessment();
    }
  },

  confirmSubmitAssessment() {
    const answeredCount = Object.keys(State.assessment.answers).length;
    const totalCount = State.assessment.data.questions.length;
    const confirmed = confirm(`Are you sure you want to submit? You have answered ${answeredCount} of ${totalCount} questions.`);
    if (confirmed) {
      this.submitAssessment(false);
    }
  },

  async submitAssessment(isTimeout = false) {
    if (State.assessment.timerInterval) clearInterval(State.assessment.timerInterval);

    const a = State.assessment.data;
    const durationSeconds = Math.round((Date.now() - State.assessment.startTime) / 1000);

    try {
      const result = await API.submitAssessment(a.id, durationSeconds, State.assessment.answers);
      this.renderAssessmentResult(result);
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  renderAssessmentResult(result) {
    document.getElementById('res-score-title').textContent = `${result.score} / ${result.totalMarks}`;
    document.getElementById('res-score-pct').textContent = `${result.percentage}% Accuracy`;

    const passBadge = document.getElementById('res-pass-badge');
    passBadge.textContent = result.passed ? 'PASSED — PLACEMENT READY' : 'RETRY — GAPS DETECTED';
    passBadge.className = result.passed ? 'badge badge-easy' : 'badge badge-hard';

    // Category breakdown
    const breakdownContainer = document.getElementById('res-category-breakdown');
    breakdownContainer.innerHTML = Object.entries(result.categoryBreakdown || {}).map(([cat, pct]) => `
      <div>
        <div style="display:flex;justify-content:space-between;font-size:0.9rem;margin-bottom:0.35rem;">
          <span>${cat}</span>
          <strong style="color:${pct >= 70 ? '#34d399' : '#fb7185'};">${pct}%</strong>
        </div>
        <div class="progress-bar-container">
          <div class="progress-bar-fill" style="width:${pct}%;background:${pct >= 70 ? '#10b981' : '#f43f5e'};"></div>
        </div>
      </div>
    `).join('');

    // Mistakes review
    const mistakesContainer = document.getElementById('res-questions-review-list');
    if (!result.mistakes || result.mistakes.length === 0) {
      mistakesContainer.innerHTML = `
        <div style="background:#111827;border-radius:var(--radius-sm);padding:2rem;text-align:center;">
          <h4 style="color:#34d399;margin-bottom:0.5rem;">Flawless Assessment! No Mistakes Logged.</h4>
          <p style="color:var(--text-secondary);font-size:0.9rem;">You correctly handled all algorithmic boundary cases and theoretical concepts.</p>
        </div>
      `;
    } else {
      mistakesContainer.innerHTML = result.mistakes.map((m, idx) => `
        <div style="background:#111827;border:1px solid #243049;border-left:4px solid #f43f5e;border-radius:var(--radius-sm);padding:1.25rem;">
          <div style="display:flex;gap:0.5rem;align-items:center;margin-bottom:0.5rem;">
            <span class="badge badge-hard">Missed Question</span>
            <span class="badge badge-concept">${m.mistakeCategoryLabel}</span>
            <span style="font-size:0.8rem;color:var(--text-muted);">${m.topic}</span>
          </div>
          <h4 style="font-size:1.05rem;line-height:1.5;margin-bottom:0.75rem;">${m.questionText}</h4>
          
          <div class="grid-2" style="font-size:0.9rem;background:#0b0f19;padding:0.75rem;border-radius:var(--radius-sm);margin-bottom:0.75rem;">
            <div style="color:#fb7185;"><strong>Your Choice:</strong> ${m.selectedOptionText || 'None'}</div>
            <div style="color:#34d399;"><strong>Correct Answer:</strong> ${m.correctOptionText}</div>
          </div>

          <p style="font-size:0.85rem;color:var(--text-secondary);line-height:1.6;">
            <strong>Explanation:</strong> ${m.explanation}
          </p>
          <div style="margin-top:0.5rem;font-size:0.8rem;color:#fbbf24;">
            Tip: ${m.followUpTip}
          </div>
        </div>
      `).join('');
    }

    this.navigate('assessment-result');
  },

  // 7. PLACEMENT MISSIONS
  async loadMissionsView() {
    try {
      const missions = await API.getMissions();
      const progressList = await API.getStudentMissions().catch(() => []);
      const progressMap = {};
      progressList.forEach(p => { progressMap[p.mission.id] = p; });

      const container = document.getElementById('missions-container');
      container.innerHTML = missions.map(m => {
        const prog = progressMap[m.id];
        const status = prog ? prog.status : 'NOT_STARTED';
        const completedCount = prog ? prog.completedTasksCount : 0;
        const totalCount = prog ? prog.totalTasksCount : (m.tasks ? m.tasks.length : 4);
        const pct = totalCount > 0 ? Math.round((completedCount / totalCount) * 100) : 0;

        return `
          <div class="card">
            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem;flex-wrap:wrap;gap:0.5rem;">
              <div>
                <span class="badge badge-concept" style="margin-bottom:0.5rem;">${m.durationDays}-Day Preparation Mission</span>
                <h2 style="font-size:1.5rem;">${m.title}</h2>
              </div>
              <div>
                <span class="badge ${status === 'COMPLETED' ? 'badge-easy' : status === 'IN_PROGRESS' ? 'badge-medium' : 'badge-syntax'}">
                  ${status.replace('_', ' ')}
                </span>
              </div>
            </div>

            <p style="color:var(--text-secondary);font-size:0.95rem;margin-bottom:1.5rem;">${m.description}</p>

            <div style="margin-bottom:1.5rem;">
              <div style="display:flex;justify-content:space-between;font-size:0.85rem;margin-bottom:0.35rem;">
                <span>Sprint Progress (${completedCount}/${totalCount} Tasks)</span>
                <strong>${pct}%</strong>
              </div>
              <div class="progress-bar-container">
                <div class="progress-bar-fill" style="width:${pct}%;"></div>
              </div>
            </div>

            <div style="display:flex;flex-direction:column;gap:0.75rem;margin-bottom:1.5rem;">
              ${(m.tasks || []).map(t => `
                <div style="display:flex;align-items:center;justify-content:space-between;background:#111827;padding:0.75rem 1rem;border-radius:var(--radius-sm);border:1px solid #243049;">
                  <div>
                    <span style="font-size:0.8rem;color:var(--primary);font-weight:700;">Day ${t.dayNumber} &bull; ${t.taskType}:</span>
                    <strong style="margin-left:0.5rem;font-size:0.95rem;">${t.taskTitle}</strong>
                    <div style="font-size:0.85rem;color:var(--text-secondary);margin-top:0.2rem;">${t.taskDescription}</div>
                  </div>
                  <button class="btn btn-secondary btn-sm" onclick="App.completeMissionTask(${m.id}, ${t.id})">
                    Check Off
                  </button>
                </div>
              `).join('')}
            </div>

            <div style="display:flex;gap:0.75rem;">
              <button class="btn btn-primary btn-sm" onclick="App.startMission(${m.id})">
                ${status === 'NOT_STARTED' ? 'Begin Mission Sprint' : 'Resume Mission'}
              </button>
            </div>
          </div>
        `;
      }).join('');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async startMission(id) {
    try {
      await API.startMission(id);
      this.showToast('Mission activated!', 'success');
      this.loadMissionsView();
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async completeMissionTask(missionId, taskId) {
    try {
      await API.completeMissionTask(missionId, taskId);
      this.showToast('Task marked as completed!', 'success');
      this.loadMissionsView();
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  // 8. PROOF OF PROGRESS
  async loadProofOfProgressView() {
    try {
      const proof = await API.getProofOfProgress();
      document.getElementById('proof-initial-pct').textContent = `${proof.initialScorePercentage}%`;
      document.getElementById('proof-current-pct').textContent = `${proof.currentScorePercentage}%`;

      const delta = proof.percentagePointsDelta || 0;
      const deltaEl = document.getElementById('proof-delta-badge');
      deltaEl.textContent = delta >= 0 ? `+${delta}% Improvement` : `${delta}%`;

      const weakList = document.getElementById('proof-initial-weak-list');
      weakList.innerHTML = (proof.initialWeakAreas || []).map(w => `<li>${w}</li>`).join('');

      const strongList = document.getElementById('proof-current-strong-list');
      strongList.innerHTML = (proof.currentStrengths || []).map(s => `<li>${s}</li>`).join('');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  // 9. ADMIN VIEW
  async loadAdminView() {
    try {
      const stats = await API.getAdminStats();
      document.getElementById('adm-students-val').textContent = stats.totalStudents || 0;
      document.getElementById('adm-lessons-val').textContent = stats.totalLessons || 0;
      document.getElementById('adm-questions-val').textContent = stats.totalQuestions || 0;
      document.getElementById('adm-attempts-val').textContent = stats.totalAssessmentsTaken || 0;

      // Load lessons list
      const paths = await API.getLearningPaths();
      let allLessons = [];
      for (const p of paths) {
        const lessons = await API.getLessonsByPath(p.id);
        allLessons = allLessons.concat(lessons);
      }

      const lTbody = document.getElementById('adm-lessons-tbody');
      lTbody.innerHTML = allLessons.map(l => `
        <tr style="border-bottom:1px solid #1e293b;">
          <td style="padding:0.75rem 1rem;font-family:var(--font-code);">${l.id}</td>
          <td style="padding:0.75rem 1rem;font-weight:600;">${l.title}</td>
          <td style="padding:0.75rem 1rem;">${l.topic}</td>
          <td style="padding:0.75rem 1rem;"><span class="badge badge-concept">${l.category}</span></td>
          <td style="padding:0.75rem 1rem;"><span class="badge badge-medium">${l.difficulty}</span></td>
          <td style="padding:0.75rem 1rem;">
            <button class="btn btn-danger btn-sm" onclick="App.deleteLesson(${l.id})">Delete</button>
          </td>
        </tr>
      `).join('');

      // Load attempts
      const attempts = await API.getAllAttempts();
      const sTbody = document.getElementById('adm-students-tbody');
      sTbody.innerHTML = attempts.map(a => `
        <tr style="border-bottom:1px solid #1e293b;">
          <td style="padding:0.75rem 1rem;font-family:var(--font-code);">${a.id}</td>
          <td style="padding:0.75rem 1rem;font-weight:600;">${a.user ? a.user.name : 'Student'}</td>
          <td style="padding:0.75rem 1rem;">${a.score} / ${a.totalMarks}</td>
          <td style="padding:0.75rem 1rem;">${a.percentage}%</td>
          <td style="padding:0.75rem 1rem;">
            <span class="badge ${a.passed ? 'badge-easy' : 'badge-hard'}">${a.passed ? 'PASSED' : 'RETRY'}</span>
          </td>
          <td style="padding:0.75rem 1rem;">${a.durationSeconds}s</td>
        </tr>
      `).join('');
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  switchAdminTab(tab) {
    document.getElementById('adm-tab-lessons').style.display = (tab === 'lessons') ? 'block' : 'none';
    document.getElementById('adm-tab-questions').style.display = (tab === 'questions') ? 'block' : 'none';
    document.getElementById('adm-tab-students').style.display = (tab === 'students') ? 'block' : 'none';
  },

  openCreateLessonModal() {
    document.getElementById('create-lesson-modal').classList.add('open');
  },

  closeCreateLessonModal() {
    document.getElementById('create-lesson-modal').classList.remove('open');
  },

  async handleCreateLesson(e) {
    e.preventDefault();
    const title = document.getElementById('adm-l-title').value;
    const category = document.getElementById('adm-l-category').value;
    const topic = document.getElementById('adm-l-topic').value;
    const summary = document.getElementById('adm-l-summary').value;
    const content = document.getElementById('adm-l-content').value;

    const slug = title.toLowerCase().replace(/[^a-z0-9]+/g, '-');

    try {
      await API.createLesson({
        title,
        slug,
        category,
        topic,
        difficulty: 'INTERMEDIATE',
        summary,
        content,
        learningPath: { id: 1 },
        published: true,
      });
      this.closeCreateLessonModal();
      this.showToast('Lesson published successfully!', 'success');
      this.loadAdminView();
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async deleteLesson(id) {
    if (!confirm('Are you sure you want to delete this lesson?')) return;
    try {
      await API.deleteLesson(id);
      this.showToast('Lesson deleted', 'success');
      this.loadAdminView();
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  // 10. HEALTH & DEVOPS DEMO VIEW
  async loadHealthView() {
    try {
      const data = await API.getHealth();
      document.getElementById('health-status-badge').textContent = data.status;
      document.getElementById('health-status-badge').style.color = (data.status === 'UP') ? '#34d399' : '#fb7185';
      document.getElementById('health-version-val').textContent = data.version;
      document.getElementById('health-build-val').textContent = data.buildNumber;
      document.getElementById('health-mem-val').textContent = `${data.memory.totalMemoryMb} MB`;
      document.getElementById('health-raw-json').textContent = JSON.stringify(data, null, 2);
    } catch (err) {
      document.getElementById('health-status-badge').textContent = 'UNHEALTHY (503)';
      document.getElementById('health-status-badge').style.color = '#fb7185';
      document.getElementById('health-raw-json').textContent = `Error: ${err.message}`;
    }
  },

  async refreshHealth() {
    await this.loadHealthView();
    this.showToast('Health probe polled', 'success');
  },

  async simulateFailure() {
    try {
      await API.simulateFailure();
      this.showToast('Unhealthy state activated (HTTP 503)! Observe Kubernetes probe failure.', 'error');
      await this.loadHealthView();
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  async simulateRecovery() {
    try {
      await API.simulateRecovery();
      this.showToast('Service recovered to UP status (HTTP 200)!', 'success');
      await this.loadHealthView();
    } catch (err) {
      this.showToast(err.message, 'error');
    }
  },

  // Helpers
  showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
      <span>${type === 'success' ? '✓' : '⚠'}</span>
      <span>${message}</span>
    `;
    container.appendChild(toast);
    setTimeout(() => {
      toast.style.opacity = '0';
      setTimeout(() => toast.remove(), 300);
    }, 3500);
  },

  formatMarkdown(content) {
    if (!content) return '';
    return content
      .replace(/### (.*?)\n/g, '<h3 style="font-size:1.3rem;margin:1.5rem 0 0.5rem 0;color:var(--text-primary);">$1</h3>')
      .replace(/#### (.*?)\n/g, '<h4 style="font-size:1.1rem;margin:1.25rem 0 0.4rem 0;color:#fbbf24;">$1</h4>')
      .replace(/```(java|sql|javascript)?\n([\s\S]*?)```/g, '<pre class="code-box"><code>$2</code></pre>')
      .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
      .replace(/`([^`]+)`/g, '<code style="background:#0b0f19;color:#e2e8f0;padding:2px 6px;border-radius:4px;font-family:var(--font-code);font-size:0.9em;">$1</code>')
      .replace(/\n\n/g, '<br><br>');
  },

  escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  },
};

// Initialize app when DOM is ready
document.addEventListener('DOMContentLoaded', () => App.init());

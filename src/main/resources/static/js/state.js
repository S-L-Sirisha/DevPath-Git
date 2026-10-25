/**
 * DevPath — State Management Store
 */

const State = {
  user: null,
  activeView: 'landing',
  
  // Current active assessment state
  assessment: {
    data: null,
    answers: {},
    flagged: new Set(),
    currentQuestionIndex: 0,
    timerInterval: null,
    remainingSeconds: 0,
    startTime: null,
  },

  // Active coding challenge state
  coding: {
    challenge: null,
    submissions: [],
  },

  // Current lesson detail state
  lesson: {
    data: null,
    isCompleted: false,
  },

  // Active mission state
  mission: {
    data: null,
    progress: null,
  },

  // Chart instances
  charts: {
    radar: null,
    progress: null,
  },

  setUser(user) {
    this.user = user;
    const userRoleEl = document.getElementById('user-role-badge');
    const userNameEl = document.getElementById('user-name-display');
    const authNavEl = document.getElementById('nav-auth-section');

    if (user) {
      if (userRoleEl) userRoleEl.textContent = user.role === 'ROLE_ADMIN' ? 'ADMIN' : 'STUDENT';
      if (userNameEl) userNameEl.textContent = user.name.split(' ')[0];
      if (authNavEl) {
        authNavEl.innerHTML = `
          <div class="user-pill" style="display:flex;align-items:center;gap:0.5rem;background:#1e293b;padding:0.35rem 0.75rem;border-radius:9999px;font-size:0.85rem;border:1px solid #334155;">
            <span style="width:8px;height:8px;border-radius:50%;background:#10b981;"></span>
            <span>${user.name}</span>
            <span style="font-size:0.7rem;background:${user.role === 'ROLE_ADMIN' ? '#8b5cf6' : '#6366f1'};color:white;padding:1px 6px;border-radius:4px;">${user.role === 'ROLE_ADMIN' ? 'Admin' : 'Student'}</span>
          </div>
          <button class="btn btn-secondary btn-sm" onclick="App.logout()">Logout</button>
        `;
      }
    } else {
      if (authNavEl) {
        authNavEl.innerHTML = `
          <button class="btn btn-outline btn-sm" onclick="App.openLoginModal()">Login</button>
          <button class="btn btn-primary btn-sm" onclick="App.openRegisterModal()">Get Started</button>
        `;
      }
    }
  },
};

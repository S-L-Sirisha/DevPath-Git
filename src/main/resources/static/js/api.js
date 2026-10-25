/**
 * DevPath — REST API Client
 */

const API = {
  baseUrl: '/api',

  getToken() {
    return localStorage.getItem('devpath_token');
  },

  setToken(token) {
    if (token) {
      localStorage.setItem('devpath_token', token);
    } else {
      localStorage.removeItem('devpath_token');
    }
  },

  async request(endpoint, options = {}) {
    const url = `${this.baseUrl}${endpoint}`;
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers,
    };

    const token = this.getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    try {
      const response = await fetch(url, {
        ...options,
        headers,
      });

      if (response.status === 204) {
        return null;
      }

      const data = await response.json();

      if (!response.ok) {
        const errorMsg = data.message || data.error || `HTTP error ${response.status}`;
        throw new Error(errorMsg);
      }

      return data;
    } catch (err) {
      console.error(`API Error [${endpoint}]:`, err);
      throw err;
    }
  },

  // Auth Endpoints
  async register(name, email, password, targetRole, college) {
    const res = await this.request('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ name, email, password, targetRole, college }),
    });
    this.setToken(res.token);
    return res;
  },

  async login(email, password) {
    const res = await this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
    this.setToken(res.token);
    return res;
  },

  async getCurrentUser() {
    return await this.request('/auth/me');
  },

  async updateProfile(profileData) {
    return await this.request('/auth/profile', {
      method: 'PUT',
      body: JSON.stringify(profileData),
    });
  },

  // Learning Hub
  async getLearningPaths() {
    return await this.request('/learning-paths');
  },

  async getPathBySlug(slug) {
    return await this.request(`/learning-paths/${slug}`);
  },

  async getLessonsByPath(pathId) {
    return await this.request(`/learning-paths/${pathId}/lessons`);
  },

  async getLessonById(id) {
    return await this.request(`/lessons/${id}`);
  },

  async toggleLessonComplete(id) {
    return await this.request(`/student/lessons/${id}/toggle-complete`, { method: 'POST' });
  },

  async getCompletedLessonIds() {
    return await this.request('/student/completed-lessons');
  },

  // Assessments
  async getAssessments() {
    return await this.request('/assessments');
  },

  async getAssessmentById(id) {
    return await this.request(`/assessments/${id}`);
  },

  async submitAssessment(assessmentId, durationSeconds, answers) {
    return await this.request('/student/assessments/submit', {
      method: 'POST',
      body: JSON.stringify({ assessmentId, durationSeconds, answers }),
    });
  },

  async getAssessmentHistory() {
    return await this.request('/student/assessments/history');
  },

  // Diagnostics & Unique Features
  async getRadar() {
    return await this.request('/student/radar');
  },

  async getRecommendations() {
    return await this.request('/student/recommendations');
  },

  async getMistakes() {
    return await this.request('/student/mistakes');
  },

  async getProofOfProgress() {
    return await this.request('/student/progress');
  },

  async getStudentDashboard() {
    return await this.request('/student/dashboard');
  },

  // Coding Arena
  async getChallenges(category = '') {
    const query = category ? `?category=${category}` : '';
    return await this.request(`/challenges${query}`);
  },

  async getChallengeById(id) {
    return await this.request(`/challenges/${id}`);
  },

  async submitCode(challengeId, language, codeContent) {
    return await this.request('/student/challenges/submit', {
      method: 'POST',
      body: JSON.stringify({ challengeId, language, codeContent }),
    });
  },

  async getSubmissions(challengeId = null) {
    const query = challengeId ? `?challengeId=${challengeId}` : '';
    return await this.request(`/student/challenges/submissions${query}`);
  },

  // Placement Missions
  async getMissions() {
    return await this.request('/missions');
  },

  async getMissionById(id) {
    return await this.request(`/missions/${id}`);
  },

  async getStudentMissions() {
    return await this.request('/student/missions');
  },

  async startMission(missionId) {
    return await this.request(`/student/missions/${missionId}/start`, { method: 'POST' });
  },

  async completeMissionTask(missionId, taskId) {
    return await this.request(`/student/missions/${missionId}/tasks/${taskId}/complete`, { method: 'POST' });
  },

  // Health & Recovery Demo
  async getHealth() {
    return await this.request('/health');
  },

  async simulateFailure() {
    return await this.request('/health/simulate-failure', { method: 'POST' });
  },

  async simulateRecovery() {
    return await this.request('/health/simulate-recovery', { method: 'POST' });
  },

  // Admin Endpoints
  async getAdminStats() {
    return await this.request('/admin/stats');
  },

  async getAdminUsers() {
    return await this.request('/admin/users');
  },

  async createLesson(lesson) {
    return await this.request('/admin/lessons', {
      method: 'POST',
      body: JSON.stringify(lesson),
    });
  },

  async deleteLesson(id) {
    return await this.request(`/admin/lessons/${id}`, { method: 'DELETE' });
  },

  async createQuestion(question) {
    return await this.request('/admin/questions', {
      method: 'POST',
      body: JSON.stringify(question),
    });
  },

  async deleteQuestion(id) {
    return await this.request(`/admin/questions/${id}`, { method: 'DELETE' });
  },

  async getAllAttempts() {
    return await this.request('/admin/attempts');
  },
};

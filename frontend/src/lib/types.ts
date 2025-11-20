/**
 * Zustand Store 타입 정의
 * 단일 Store에서 관리할 모든 상태의 타입을 정의합니다.
 */

// 사용자 관련 타입
export interface User {
  id: string;
  name: string;
  email?: string;
  [key: string]: unknown;
}

// 축구 관련 타입
export interface Player {
  id: string;
  name: string;
  team?: string;
  position?: string;
  [key: string]: unknown;
}

export interface Schedule {
  id: string;
  date: string;
  homeTeam: string;
  awayTeam: string;
  stadium?: string;
  [key: string]: unknown;
}

export interface Team {
  id: string;
  name: string;
  [key: string]: unknown;
}

export interface Stadium {
  id: string;
  name: string;
  location?: string;
  [key: string]: unknown;
}

// 사용자 상태
export interface UserState {
  isAuthenticated: boolean;
  user: User | null;
  setUser: (user: User | null) => void;
  logout: () => void;
}

// 축구 데이터 상태
export interface SoccerState {
  players: Player[];
  schedules: Schedule[];
  teams: Team[];
  stadiums: Stadium[];
  loading: boolean;
  error: string | null;
  setPlayers: (players: Player[]) => void;
  setSchedules: (schedules: Schedule[]) => void;
  setTeams: (teams: Team[]) => void;
  setStadiums: (stadiums: Stadium[]) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
}

// UI 상태
export interface UIState {
  searchInput: string;
  selectedTab: string;
  sidebarOpen: boolean;
  setSearchInput: (input: string) => void;
  setSelectedTab: (tab: string) => void;
  toggleSidebar: () => void;
}

// 전체 Store 타입 (단일 Store)
export interface AppStore extends UserState, SoccerState, UIState {
  resetStore: () => void;
}


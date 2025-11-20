/**
 * Soccer Slice
 * 축구 데이터 관련 상태 관리 (players, schedules, teams, stadiums)
 */

import { StateCreator } from 'zustand';
import type { AppStore, SoccerState } from '../types';

export const createSoccerSlice: StateCreator<
  AppStore,
  [['zustand/devtools', never], ['zustand/persist', unknown]],
  [],
  SoccerState
> = (set) => ({
  players: [],
  schedules: [],
  teams: [],
  stadiums: [],
  loading: false,
  error: null,
  setPlayers: (players) => set({ players }, false, 'setPlayers'),
  setSchedules: (schedules) => set({ schedules }, false, 'setSchedules'),
  setTeams: (teams) => set({ teams }, false, 'setTeams'),
  setStadiums: (stadiums) => set({ stadiums }, false, 'setStadiums'),
  setLoading: (loading) => set({ loading }, false, 'setLoading'),
  setError: (error) => set({ error }, false, 'setError'),
});


/**
 * User Slice
 * 사용자 인증 및 사용자 정보 관련 상태 관리
 */

import { StateCreator } from 'zustand';
import type { AppStore, UserState } from '../types';

export const createUserSlice: StateCreator<
  AppStore,
  [['zustand/devtools', never], ['zustand/persist', unknown]],
  [],
  UserState
> = (set) => ({
  isAuthenticated: false,
  user: null,
  setUser: (user) =>
    set({ user, isAuthenticated: !!user }, false, 'setUser'),
  logout: () =>
    set({ user: null, isAuthenticated: false }, false, 'logout'),
});


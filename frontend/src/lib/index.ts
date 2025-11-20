/**
 * Zustand 단일 Store
 * 모든 슬라이스를 combine하여 하나의 Store로 관리합니다.
 */

import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { createUserSlice } from './slices/userSlice';
import { createSoccerSlice } from './slices/soccerSlice';
import { createUISlice } from './slices/uiSlice';
import type { AppStore } from './types';

export const useStore = create<AppStore>()(
  devtools(
    persist(
      (...a) => ({
        ...createUserSlice(...a),
        ...createSoccerSlice(...a),
        ...createUISlice(...a),
        // === Common Actions ===
        resetStore: () => {
          const set = a[0];
          set(
            {
              isAuthenticated: false,
              user: null,
              players: [],
              schedules: [],
              teams: [],
              stadiums: [],
              loading: false,
              error: null,
              searchInput: '',
              selectedTab: 'home',
              sidebarOpen: false,
            },
            false,
            'resetStore'
          );
        },
      }),
      {
        name: 'app-storage', // localStorage key
        partialize: (state) => ({
          // persist할 상태만 선택 (민감한 정보 제외)
          user: state.user,
          isAuthenticated: state.isAuthenticated,
          searchInput: state.searchInput,
          selectedTab: state.selectedTab,
        }),
      }
    ),
    { name: 'AppStore' } // Redux DevTools 이름
  )
);


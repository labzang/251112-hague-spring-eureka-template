/**
 * UI Slice
 * UI 상태 관리 (searchInput, selectedTab, sidebarOpen 등)
 */

import { StateCreator } from 'zustand';
import type { AppStore, UIState } from '../types';

export const createUISlice: StateCreator<
  AppStore,
  [['zustand/devtools', never], ['zustand/persist', unknown]],
  [],
  UIState
> = (set) => ({
  searchInput: '',
  selectedTab: 'home',
  sidebarOpen: false,
  setSearchInput: (input) => set({ searchInput: input }, false, 'setSearchInput'),
  setSelectedTab: (tab) => set({ selectedTab: tab }, false, 'setSelectedTab'),
  toggleSidebar: () =>
    set(
      (state) => ({ sidebarOpen: !state.sidebarOpen }),
      false,
      'toggleSidebar'
    ),
});


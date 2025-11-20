# Zustand 단일 Store 관리 전략

## 1. 개요

이 문서는 Next.js 16 App Router 환경에서 Zustand를 사용하여 **단일 전역 Store**를 구성하고 관리하는 전략을 설명합니다.

## 2. 현재 프로젝트 상태

- **프레임워크**: Next.js 16.0.1 (App Router)
- **React 버전**: 19.2.0
- **TypeScript**: 사용 중
- **현재 상태 관리**: useState로 로컬 상태만 관리
- **API 클라이언트**: `app/lib/api-client.ts`에 이미 구현됨

## 3. 설계 원칙

### 3.1 단일 Store 원칙
- **하나의 중앙화된 Store만 생성**
- 모든 전역 상태를 단일 Store 내에서 슬라이스(Slice) 패턴으로 관리
- Store는 기능별로 논리적으로 분리하되, 물리적으로는 하나의 파일에 통합

### 3.2 Store 구조 설계
```
store/
  ├── index.ts          # 단일 Store 정의 및 export
  ├── types.ts          # Store 관련 TypeScript 타입 정의
  └── slices/           # 기능별 슬라이스 (선택사항, 단일 파일로도 가능)
      ├── userSlice.ts
      ├── soccerSlice.ts
      └── uiSlice.ts
```

## 4. 구현 전략

### 4.1 의존성 설치

```bash
npm install zustand
```

### 4.2 Store 디렉토리 구조

**옵션 A: 단일 파일 방식 (권장 - 단순성)**
```
app/
  └── store/
      ├── index.ts      # 모든 상태와 액션을 하나의 파일에 정의
      └── types.ts      # 타입 정의
```

**옵션 B: 슬라이스 패턴 (확장성)**
```
app/
  └── store/
      ├── index.ts      # 슬라이스들을 combine하여 단일 store 생성
      ├── types.ts
      └── slices/
          ├── userSlice.ts
          ├── soccerSlice.ts
          └── uiSlice.ts
```

### 4.3 Store 타입 정의 전략

**types.ts 예시 구조:**
```typescript
// app/store/types.ts

// 사용자 관련 상태
export interface UserState {
  isAuthenticated: boolean;
  user: User | null;
}

// 축구 데이터 관련 상태
export interface SoccerState {
  players: Player[];
  schedules: Schedule[];
  teams: Team[];
  stadiums: Stadium[];
  loading: boolean;
  error: string | null;
}

// UI 상태
export interface UIState {
  searchInput: string;
  selectedTab: string;
  sidebarOpen: boolean;
}

// 전체 Store 타입 (단일 Store)
export interface AppStore extends UserState, SoccerState, UIState {
  // 공통 액션들
  resetStore: () => void;
}
```

### 4.4 단일 Store 구현 전략

**index.ts 구현 방식:**

```typescript
// app/store/index.ts

import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import type { AppStore } from './types';

export const useStore = create<AppStore>()(
  devtools(
    persist(
      (set, get) => ({
        // === User State ===
        isAuthenticated: false,
        user: null,
        
        // === Soccer State ===
        players: [],
        schedules: [],
        teams: [],
        stadiums: [],
        loading: false,
        error: null,
        
        // === UI State ===
        searchInput: '',
        selectedTab: 'home',
        sidebarOpen: false,
        
        // === Actions ===
        // User Actions
        setUser: (user) => set({ user, isAuthenticated: !!user }),
        logout: () => set({ user: null, isAuthenticated: false }),
        
        // Soccer Actions
        setPlayers: (players) => set({ players }),
        setSchedules: (schedules) => set({ schedules }),
        setTeams: (teams) => set({ teams }),
        setStadiums: (stadiums) => set({ stadiums }),
        setLoading: (loading) => set({ loading }),
        setError: (error) => set({ error }),
        
        // UI Actions
        setSearchInput: (input) => set({ searchInput: input }),
        setSelectedTab: (tab) => set({ selectedTab: tab }),
        toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),
        
        // Common Actions
        resetStore: () => set({
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
        }),
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
```

### 4.5 선택적 슬라이스 패턴 (확장 시)

만약 Store가 커져서 관리가 어려워지면, 슬라이스 패턴을 사용하되 **여전히 단일 Store로 export**:

```typescript
// app/store/slices/userSlice.ts
import { StateCreator } from 'zustand';
import type { AppStore, UserState } from '../types';

export const createUserSlice: StateCreator<AppStore, [], [], UserState> = (set) => ({
  isAuthenticated: false,
  user: null,
  setUser: (user) => set({ user, isAuthenticated: !!user }),
  logout: () => set({ user: null, isAuthenticated: false }),
});

// app/store/index.ts
import { create } from 'zustand';
import { createUserSlice } from './slices/userSlice';
import { createSoccerSlice } from './slices/soccerSlice';
import { createUISlice } from './slices/uiSlice';

export const useStore = create<AppStore>()(
  devtools(
    persist(
      (...a) => ({
        ...createUserSlice(...a),
        ...createSoccerSlice(...a),
        ...createUISlice(...a),
      }),
      { name: 'app-storage' }
    ),
    { name: 'AppStore' }
  )
);
```

## 5. 사용 전략

### 5.1 컴포넌트에서 Store 사용

```typescript
// Client Component에서 사용
'use client';

import { useStore } from '@/app/store';

export default function MyComponent() {
  // 전체 상태 접근
  const { players, loading, setPlayers } = useStore();
  
  // 선택적 구독 (성능 최적화)
  const searchInput = useStore((state) => state.searchInput);
  const setSearchInput = useStore((state) => state.setSearchInput);
  
  // 또는 여러 값 한번에
  const { searchInput, selectedTab } = useStore((state) => ({
    searchInput: state.searchInput,
    selectedTab: state.selectedTab,
  }));
}
```

### 5.2 API 호출과 Store 통합

```typescript
// app/lib/api-client.ts 확장 또는 새로운 파일
import { apiClient } from './api-client';
import { useStore } from '@/app/store';

export const soccerService = {
  async fetchPlayers() {
    const { setLoading, setError, setPlayers } = useStore.getState();
    
    try {
      setLoading(true);
      setError(null);
      const players = await apiClient.get('/soccer/players');
      setPlayers(players);
      return players;
    } catch (error) {
      setError(error instanceof Error ? error.message : '알 수 없는 오류');
      throw error;
    } finally {
      setLoading(false);
    }
  },
};
```

### 5.3 Server Component에서의 주의사항

- Zustand Store는 **Client Component에서만 사용 가능**
- Server Component에서는 props나 서버 데이터를 Client Component로 전달
- 초기 데이터는 Server Component에서 fetch 후 Client Component의 초기 상태로 설정

## 6. 마이그레이션 전략

### 6.1 기존 useState 마이그레이션

1. **전역으로 공유되어야 하는 상태 식별**
   - 여러 컴포넌트에서 사용되는 상태
   - 페이지 간 유지되어야 하는 상태
   - API 응답 데이터

2. **로컬 상태 유지**
   - 단일 컴포넌트에서만 사용되는 상태는 useState 유지
   - 폼 입력 등 임시 상태는 useState 유지

3. **점진적 마이그레이션**
   - 한 번에 하나씩 기능별로 마이그레이션
   - 기존 코드와 병행하여 테스트

### 6.2 기존 코드 예시 (page.tsx)

**Before:**
```typescript
const [input, setInput] = useState("");
```

**After:**
```typescript
const searchInput = useStore((state) => state.searchInput);
const setSearchInput = useStore((state) => state.setSearchInput);
```

## 7. 최적화 전략

### 7.1 선택적 구독
- 필요한 상태만 구독하여 불필요한 리렌더링 방지
- `useStore((state) => state.specificValue)` 사용

### 7.2 Shallow 비교
- 객체나 배열을 구독할 때는 shallow 비교 사용
```typescript
import { shallow } from 'zustand/shallow';

const { players, schedules } = useStore(
  (state) => ({ players: state.players, schedules: state.schedules }),
  shallow
);
```

### 7.3 Middleware 활용
- `devtools`: Redux DevTools 연동
- `persist`: localStorage/sessionStorage 동기화
- `immer`: 불변성 관리 (선택사항)

## 8. 파일 구조 최종 권장안

```
frontend/
  app/
    store/
      ├── index.ts           # 단일 Store 정의 (필수)
      ├── types.ts           # 타입 정의 (필수)
      └── hooks.ts           # 커스텀 훅 (선택사항)
    lib/
      ├── api-client.ts      # 기존 API 클라이언트
      └── services.ts        # Store와 연동된 서비스 레이어 (선택사항)
```

## 9. 체크리스트

### 설치 및 기본 설정
- [ ] `npm install zustand` 실행
- [ ] `app/store/` 디렉토리 생성
- [ ] `app/store/types.ts` 생성 및 타입 정의
- [ ] `app/store/index.ts` 생성 및 단일 Store 구현

### 기능 구현
- [ ] User 상태 및 액션 정의
- [ ] Soccer 데이터 상태 및 액션 정의
- [ ] UI 상태 및 액션 정의
- [ ] 공통 액션 (resetStore 등) 정의

### 통합
- [ ] 기존 `page.tsx`의 useState를 Store로 마이그레이션
- [ ] API 호출과 Store 연동
- [ ] 필요한 컴포넌트에서 Store 사용

### 최적화
- [ ] devtools middleware 추가
- [ ] persist middleware 추가 (필요시)
- [ ] 선택적 구독 적용

### 테스트
- [ ] Store 동작 테스트
- [ ] 상태 초기화 테스트
- [ ] 페이지 새로고침 시 상태 유지 테스트

## 10. 주의사항

1. **단일 Store 원칙 준수**: 여러 Store를 만들지 않고 하나만 유지
2. **Server Component**: Zustand는 Client Component에서만 사용
3. **초기 렌더링**: SSR 환경에서 초기 상태 관리 주의
4. **타입 안정성**: TypeScript 타입을 철저히 정의
5. **성능**: 불필요한 리렌더링 방지를 위한 선택적 구독 필수

## 11. 참고 자료

- [Zustand 공식 문서](https://docs.pmnd.rs/zustand)
- [Zustand GitHub](https://github.com/pmndrs/zustand)
- [Next.js App Router와 Zustand 통합 가이드](https://docs.pmnd.rs/zustand/integrations/nextjs)


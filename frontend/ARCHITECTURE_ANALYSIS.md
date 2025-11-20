# 프로젝트 구조 분석 및 권장 사항

## 질문: components와 guidelines를 lib 안에 두는 것이 좋을까?

### 결론: **아니요, 권장하지 않습니다.**

## 1. Next.js App Router의 디렉토리 구조 원칙

### app 디렉토리의 역할
- **Next.js App Router의 핵심**: `app/` 디렉토리는 라우팅과 페이지 렌더링을 담당합니다
- **파일 기반 라우팅**: `app/` 내의 폴더 구조가 URL 경로를 결정합니다
- **특수 파일들**: `layout.tsx`, `page.tsx`, `route.ts` 등은 Next.js가 특별히 인식합니다

### app 디렉토리에 두면 안 되는 것들
- ❌ 재사용 가능한 UI 컴포넌트 (`components/`)
- ❌ 비즈니스 로직/유틸리티 (`lib/`)
- ❌ 문서 파일 (`guidelines/`)
- ❌ 상태 관리 코드 (`store/`)

## 2. 각 디렉토리의 올바른 역할

### ✅ components/ (독립 디렉토리 권장)
**역할**: 재사용 가능한 UI 컴포넌트
- **위치**: `src/components/` (루트 레벨)
- **이유**:
  - 여러 페이지/라우트에서 공통으로 사용
  - `app/` 내부에 두면 라우팅과 혼동 가능
  - 컴포넌트는 라우팅과 무관한 독립적인 단위
- **예시**: `Sidebar`, `Chatbot`, `MapView`, `PlacePopup` 등

### ✅ lib/ (독립 디렉토리 권장)
**역할**: 유틸리티, 헬퍼 함수, 비즈니스 로직
- **위치**: `src/lib/` (루트 레벨)
- **포함 내용**:
  - API 클라이언트
  - 유틸리티 함수
  - 헬퍼 함수
  - 타입 정의 (공통)
- **포함하지 않아야 할 것**:
  - ❌ UI 컴포넌트 (components에 있어야 함)
  - ❌ 문서 파일 (guidelines에 있어야 함)
  - ❌ 상태 관리 (store에 있어야 함)

### ✅ store/ (독립 디렉토리 권장)
**역할**: 전역 상태 관리 (Zustand)
- **위치**: `src/store/` (루트 레벨)
- **포함 내용**:
  - Zustand store 정의
  - 슬라이스 파일들
  - 타입 정의

### ✅ guidelines/ (독립 디렉토리 또는 루트)
**역할**: 프로젝트 문서 및 가이드라인
- **위치**: `src/guidelines/` 또는 프로젝트 루트
- **포함 내용**:
  - 개발 가이드라인
  - 코딩 컨벤션
  - 문서 파일들
- **lib에 두지 않는 이유**:
  - 문서는 실행 코드가 아님
  - lib은 코드/로직을 위한 디렉토리
  - 문서는 별도 관리가 적절

## 3. 현재 프로젝트 구조 분석

### 현재 구조
```
src/
├── app/              # ✅ Next.js App Router (올바름)
│   ├── api/          # ✅ API Routes (올바름)
│   ├── config/       # ⚠️  app 내부보다는 src/config 권장
│   └── lib/          # ❌ app 내부에 lib은 부적절
├── components/       # ✅ 독립 디렉토리 (올바름)
├── lib/              # ✅ 독립 디렉토리 (올바름)
├── store/            # ✅ 독립 디렉토리 (올바름)
└── guidelines/       # ✅ 독립 디렉토리 (올바름)
```

### 문제점
1. **`app/lib/` 존재**: app 내부에 lib이 있는 것은 부적절
   - `app/`은 라우팅 전용이어야 함
   - `src/lib/`으로 통합 필요

2. **`app/config/` 위치**: 
   - 설정 파일은 `src/config/` 또는 `src/lib/config/`이 더 적절
   - app 내부는 라우팅 관련 파일만

## 4. 권장 구조

### ✅ 올바른 구조
```
src/
├── app/                    # Next.js App Router (라우팅만)
│   ├── api/               # API Routes
│   │   ├── search/
│   │   └── soccer/
│   ├── layout.tsx         # 루트 레이아웃
│   ├── page.tsx           # 홈 페이지
│   └── globals.css        # 전역 스타일
│
├── components/            # ✅ 재사용 가능한 UI 컴포넌트
│   ├── Chatbot.tsx
│   ├── MapView.tsx
│   ├── PlacePopup.tsx
│   ├── Sidebar.tsx
│   └── ui/                # UI 라이브러리 컴포넌트
│
├── lib/                   # ✅ 유틸리티, 헬퍼, 비즈니스 로직
│   ├── api-client.ts      # API 클라이언트
│   └── utils.ts           # 유틸리티 함수
│
├── store/                 # ✅ 전역 상태 관리
│   ├── index.ts
│   ├── types.ts
│   └── slices/
│
├── config/                # ✅ 설정 파일 (선택사항)
│   └── services.ts
│
└── guidelines/            # ✅ 문서 (선택사항)
    └── Guidelines.md
```

## 5. 왜 components를 lib에 두지 않는가?

### 관심사의 분리 (Separation of Concerns)
- **components**: UI 렌더링 담당
- **lib**: 비즈니스 로직, 유틸리티 담당
- **store**: 상태 관리 담당

### 명확성과 가독성
- 각 디렉토리의 역할이 명확해야 함
- `lib/components/`는 혼란스러움
- `components/`는 직관적이고 표준적

### 확장성
- 컴포넌트가 많아지면 `components/` 내부에 하위 구조 생성 가능
- `lib/`과 `components/`를 분리하면 각각 독립적으로 확장 가능

## 6. 왜 guidelines를 lib에 두지 않는가?

### 문서 vs 코드
- **guidelines**: 문서 파일 (Markdown 등)
- **lib**: 실행 코드 (TypeScript/JavaScript)
- 문서는 코드와 분리되어야 함

### 빌드 프로세스
- 문서는 빌드에 포함되지 않음
- 코드는 빌드에 포함됨
- 분리하면 빌드 최적화 가능

## 7. 최종 권장 사항

### ✅ 유지해야 할 구조
1. **`src/components/`** - 독립 디렉토리로 유지
2. **`src/lib/`** - 독립 디렉토리로 유지 (유틸리티, API 클라이언트)
3. **`src/store/`** - 독립 디렉토리로 유지 (상태 관리)
4. **`src/guidelines/`** - 독립 디렉토리로 유지 또는 프로젝트 루트

### 🔧 수정이 필요한 부분
1. **`app/lib/`** → `src/lib/`으로 통합
2. **`app/config/`** → `src/config/` 또는 `src/lib/config/`으로 이동

### 📋 디렉토리 역할 요약
| 디렉토리 | 역할 | 위치 |
|---------|------|------|
| `app/` | Next.js 라우팅 및 페이지 | `src/app/` |
| `components/` | 재사용 가능한 UI 컴포넌트 | `src/components/` |
| `lib/` | 유틸리티, 헬퍼, 비즈니스 로직 | `src/lib/` |
| `store/` | 전역 상태 관리 | `src/store/` |
| `config/` | 설정 파일 | `src/config/` 또는 `src/lib/config/` |
| `guidelines/` | 문서 및 가이드라인 | `src/guidelines/` 또는 루트 |

## 결론

**components와 guidelines를 lib 안에 두지 마세요.**

이유:
1. **관심사의 분리**: 각 디렉토리는 명확한 역할을 가져야 함
2. **표준 관례**: Next.js/React 커뮤니티의 표준 구조를 따르는 것이 좋음
3. **유지보수성**: 명확한 구조는 유지보수를 쉽게 만듦
4. **확장성**: 각 디렉토리를 독립적으로 확장 가능

**현재 구조가 올바릅니다. 각 디렉토리를 독립적으로 유지하세요.**


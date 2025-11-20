/**
 * 백엔드 서비스 URL 설정
 * Docker 네트워크에서는 컨테이너 이름으로 통신
 * 
 * 서버 사이드 (API Routes): Docker 네트워크 내부에서 discoveryserver:8080 사용
 * 클라이언트 사이드 (브라우저): localhost:8080 사용 (포트 포워딩)
 */
export const SERVICES = {
  API_GATEWAY: typeof window === 'undefined'
    ? process.env.API_GATEWAY_URL || 'http://discoveryserver:8080'  // 서버 사이드 (Docker 네트워크)
    : process.env.NEXT_PUBLIC_API_GATEWAY_URL || 'http://localhost:8080',  // 클라이언트 사이드 (브라우저)
} as const;

/**
 * Soccer Service API 엔드포인트
 */
export const SOCCER_ENDPOINTS = {
  SCHEDULES: '/api/soccer/schedules',
  PLAYERS: '/api/soccer/players',
  TEAM: '/api/soccer/team',
  STADIUMS: '/api/soccer/stadiums',
} as const;


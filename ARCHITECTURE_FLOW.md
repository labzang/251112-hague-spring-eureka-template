# API Gateway 패턴 - 데이터 흐름 전략

## 개요
`page.tsx`의 검색 요청이 Eureka와 Discovery(API Gateway)를 거쳐 PlayerController로 데이터가 이동하는 마이크로서비스 아키텍처 구현

---

## 📊 전체 데이터 흐름도

```
┌─────────────┐
│  Browser    │
│ (page.tsx)  │
└──────┬──────┘
       │ POST /api/search
       │ {domain: "default", keyword: "손흥민"}
       ▼
┌──────────────────┐
│   Next.js        │
│ (Frontend:3000)  │
│ /api/search      │
│ route.ts         │
└──────┬───────────┘
       │ POST http://discoveryserver:8080/api/search
       │ (Docker 네트워크 내부 통신)
       ▼
┌──────────────────┐
│  API Gateway     │
│ (Discovery:8080) │
│                  │
│ 라우팅 규칙:     │
│ /api/search      │
│   → lb://soccer  │
└──────┬───────────┘
       │
       │ Service Discovery
       ▼
┌──────────────────┐
│ Eureka Server    │
│ (:8761)          │
│                  │
│ 등록된 서비스:   │
│ - soccer        │
│ - user          │
│ - common        │
└──────┬───────────┘
       │ Load Balancing
       │ soccer 인스턴스 선택
       ▼
┌──────────────────┐
│ Soccer Service   │
│ (:8083)          │
│                  │
│ SearchController │
│   /search        │
└──────┬───────────┘
       │ searchByKeyword()
       ▼
┌──────────────────┐
│ PlayerService    │
│ Impl             │
└──────┬───────────┘
       │ findAll() + filter
       ▼
┌──────────────────┐
│ PlayerRepository │
│ (JPA)            │
└──────┬───────────┘
       │ SQL Query
       ▼
┌──────────────────┐
│   PostgreSQL     │
│   Database       │
│   players 테이블 │
└──────────────────┘
```

---

## 🔧 구현된 컴포넌트

### 1. **Frontend (Next.js)**

#### `page.tsx`
```typescript
// 브라우저에서 실행
const response = await axios.post("/api/search", {
  domain: "default",
  keyword: message,
});
```

#### `frontend/app/api/search/route.ts`
```typescript
// Next.js API Routes (서버 사이드)
const backendResponse = await fetch(
  `${SERVICES.API_GATEWAY}/api/search`,  // http://discoveryserver:8080
  { method: "POST", body: JSON.stringify(body) }
);
```

### 2. **API Gateway (Discovery Client)**

#### `server/discovery/src/main/resources/application.yaml`
```yaml
spring:
  cloud:
    gateway:
      routes:
        # Search 라우팅
        - id: search-service
          uri: lb://soccer          # Eureka에서 'soccer' 서비스 찾기
          predicates:
            - Path=/api/search
          filters:
            - StripPrefix=1         # /api 제거 → /search
```

**라우팅 로직:**
- `/api/search` 요청 수신
- `StripPrefix=1`: `/api` 제거
- `lb://soccer`: Eureka를 통해 soccer 서비스 검색
- 최종 전달: `http://soccerservice:8080/search`

### 3. **Eureka Server**

#### 역할
- 서비스 등록 및 관리
- Health Check
- Load Balancing 정보 제공

#### 등록된 서비스들
```
eureka-server:8761
├── soccer (8083)
├── user (8082)
├── common (8081)
└── discovery (8080)
```

### 4. **Soccer Service**

#### `SearchController.java`
```java
@RestController
@RequestMapping("/search")
public class SearchController {
    
    private final PlayerService playerService;
    
    @PostMapping
    public Messenger search(@RequestBody SearchDTO searchDTO) {
        String keyword = searchDTO.getKeyword();
        
        // domain에 따라 분기
        switch (searchDTO.getDomain()) {
            case "player":
            case "default":
                return searchPlayers(keyword);
            default:
                return Messenger.error("지원하지 않는 도메인");
        }
    }
    
    private Messenger searchPlayers(String keyword) {
        var players = playerService.searchByKeyword(keyword);
        return Messenger.success("검색 완료", players);
    }
}
```

#### `PlayerServiceImpl.java`
```java
@Service
public class PlayerServiceImpl implements PlayerService {
    
    private final PlayerRepository playerRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<PlayerModel> searchByKeyword(String keyword) {
        List<Player> allPlayers = playerRepository.findAll();
        
        return allPlayers.stream()
            .filter(player -> 
                player.getPlayer_name().contains(keyword) ||
                player.getE_player_name().contains(keyword) ||
                player.getNickname().contains(keyword)
            )
            .map(this::convertToModel)
            .collect(Collectors.toList());
    }
}
```

#### `PlayerRepository.java`
```java
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    // JPA가 자동으로 findAll(), findById() 등 제공
}
```

---

## 🔐 보안 및 네트워크

### Docker 네트워크
```yaml
# docker-compose.yaml
networks:
  spring-network:
    driver: bridge
```

모든 서비스가 `spring-network`에 연결되어 컨테이너 이름으로 통신:
- `discoveryserver:8080`
- `eurekaserver:8761`
- `soccerservice:8082`

### CORS 설정
```java
// CorsConfig.java (Discovery)
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        corsConfig.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://my-next-app:3000"
        ));
    }
}
```

---

## 🚀 요청/응답 예시

### 요청 (Request)

#### 1. 브라우저 → Next.js
```http
POST http://localhost:3000/api/search
Content-Type: application/json

{
  "domain": "default",
  "keyword": "손흥민"
}
```

#### 2. Next.js API Routes → API Gateway
```http
POST http://discoveryserver:8080/api/search
Content-Type: application/json

{
  "domain": "default",
  "keyword": "손흥민"
}
```

#### 3. API Gateway → Soccer Service (via Eureka)
```http
POST http://soccerservice:8080/search
Content-Type: application/json

{
  "domain": "default",
  "keyword": "손흥민"
}
```

### 응답 (Response)

```json
{
  "message": "'손흥민' 검색 결과 1건이 발견되었습니다.",
  "status": 200,
  "data": [
    {
      "id": 123,
      "player_name": "손흥민",
      "e_player_name": "Son Heung-Min",
      "nickname": "소니",
      "position": "FW",
      "back_no": "7",
      "team_uk": "K01"
    }
  ]
}
```

---

## ⚙️ 환경 변수

### `docker-compose.yaml`
```yaml
nextjs:
  environment:
    - NODE_ENV=production
    - API_GATEWAY_URL=http://discoveryserver:8080        # 서버사이드
    - NEXT_PUBLIC_API_GATEWAY_URL=http://localhost:8080   # 클라이언트사이드

discovery:
  environment:
    - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka:8761/eureka/

soccer:
  environment:
    - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka:8761/eureka/
    - SERVER_PORT=8083
```

---

## 📈 확장 가능성

### 현재 구조의 장점

1. **서비스 독립성**: 각 마이크로서비스가 독립적으로 배포/확장 가능
2. **로드 밸런싱**: Eureka를 통한 자동 로드 밸런싱
3. **장애 격리**: 한 서비스 장애가 전체 시스템에 영향 없음
4. **확장 용이**: 새로운 서비스 추가 시 Eureka에 등록만 하면 됨

### 향후 개선 방향

1. **검색 최적화**: DB 레벨에서 검색 (QueryDSL, Full-Text Search)
   ```java
   @Query("SELECT p FROM Player p WHERE " +
          "p.player_name LIKE %:keyword% OR " +
          "p.e_player_name LIKE %:keyword%")
   List<Player> searchByKeyword(@Param("keyword") String keyword);
   ```

2. **캐싱 추가**: Redis를 이용한 검색 결과 캐싱
   ```java
   @Cacheable(value = "playerSearch", key = "#keyword")
   public List<PlayerModel> searchByKeyword(String keyword) { ... }
   ```

3. **Rate Limiting**: API Gateway에서 요청 제한
   ```yaml
   spring:
     cloud:
       gateway:
         routes:
           - id: search-service
             filters:
               - name: RequestRateLimiter
                 args:
                   redis-rate-limiter.replenishRate: 10
   ```

4. **모니터링**: Spring Boot Actuator + Prometheus + Grafana
5. **인증/인가**: JWT 토큰 기반 인증

---

## 🧪 테스트 방법

### 1. 로컬 환경에서 테스트
```bash
# 컨테이너 시작
docker-compose up -d

# Eureka 대시보드 확인
http://localhost:8761

# Next.js 프론트엔드 접속
http://localhost:3000

# 검색 테스트
- 입력창에 "손흥민" 입력
- 마이크 아이콘 클릭
```

### 2. 직접 API 호출 테스트
```bash
# API Gateway를 통한 검색
curl -X POST http://localhost:8080/api/search \
  -H "Content-Type: application/json" \
  -d '{"domain": "default", "keyword": "손흥민"}'

# Next.js API Routes를 통한 검색
curl -X POST http://localhost:3000/api/search \
  -H "Content-Type: application/json" \
  -d '{"domain": "default", "keyword": "손흥민"}'
```

### 3. 로그 확인
```bash
# soccerservice 로그
docker logs soccerservice -f

# discoveryserver 로그
docker logs discoveryserver -f

# eurekaserver 로그
docker logs eurekaserver -f
```

---

## 📝 요약

이 아키텍처는 **API Gateway 패턴**을 사용하여:

1. ✅ 프론트엔드와 백엔드 분리
2. ✅ CORS 문제 해결
3. ✅ 서비스 디스커버리를 통한 동적 라우팅
4. ✅ 로드 밸런싱 및 장애 격리
5. ✅ 확장 가능한 마이크로서비스 구조

를 구현했습니다.

**핵심 흐름:**
```
page.tsx → Next.js API Routes → API Gateway → Eureka → Soccer Service → PlayerService → PlayerRepository → Database
```





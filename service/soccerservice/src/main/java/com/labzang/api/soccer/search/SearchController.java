package com.labzang.api.soccer.search;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labzang.api.soccer.common.Messenger;
import com.labzang.api.soccer.player.PlayerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final PlayerService playerService;

    /**
     * 통합 검색 API
     * - domain에 따라 다른 서비스를 호출
     * - keyword로 플레이어 검색
     */
    @PostMapping
    public Messenger search(@RequestBody SearchDTO searchDTO) {
        log.info("검색 요청 - domain: {}, keyword: {}", searchDTO.getDomain(), searchDTO.getKeyword());
        
        try {
            String domain = searchDTO.getDomain();
            String keyword = searchDTO.getKeyword();
            
            if (keyword == null || keyword.trim().isEmpty()) {
                return Messenger.error("검색어를 입력해주세요.");
            }
            
            // domain에 따라 다른 서비스 호출
            switch (domain) {
                case "player":
                    return searchPlayers(keyword);
                case "default":
                    // 기본값은 player 검색
                    return searchPlayers(keyword);
                default:
                    return Messenger.error("지원하지 않는 검색 도메인입니다: " + domain);
            }
        } catch (Exception e) {
            log.error("검색 중 오류 발생", e);
            return Messenger.error("검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 선수 검색
     */
    private Messenger searchPlayers(String keyword) {
        log.info("선수 검색 - keyword: {}", keyword);
        
        // PlayerService를 통해 선수 검색
        var players = playerService.searchByKeyword(keyword);
        
        if (players.isEmpty()) {
            return Messenger.success("검색 결과가 없습니다.", players);
        }
        
        return Messenger.success(
            String.format("'%s' 검색 결과 %d건이 발견되었습니다.", keyword, players.size()),
            players
        );
    }
}

package com.labzang.api.soccer.player;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public void findById(PlayerModel playerDTO) {
        // TODO: implement if needed
    }

    @Override
    public void findAll() {
        // TODO: implement if needed
    }

    @Override
    public void save(PlayerModel playerDTO) {
        // TODO: implement if needed
    }

    @Override
    public void saveAll(List<PlayerModel> playerDTOList) {
        // TODO: implement if needed
    }

    @Override
    public void update(PlayerModel playerDTO) {
        // TODO: implement if needed
    }

    @Override
    public void delete(PlayerModel playerDTO) {
        // TODO: implement if needed
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerModel> searchByKeyword(String keyword) {
        log.info("Searching players - keyword: {}", keyword);

        // Simple in-memory filtering; replace with DB-level search in production.
        List<Player> allPlayers = playerRepository.findAll();

        String searchKeyword = keyword.toLowerCase();

        return allPlayers.stream()
                .filter(player ->
                        (player.getPlayer_name() != null && player.getPlayer_name().toLowerCase().contains(searchKeyword)) ||
                        (player.getE_player_name() != null && player.getE_player_name().toLowerCase().contains(searchKeyword)) ||
                        (player.getNickname() != null && player.getNickname().toLowerCase().contains(searchKeyword))
                )
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to DTO model.
     */
    private PlayerModel convertToModel(Player player) {
        return PlayerModel.builder()
                .id(player.getId())
                .player_uk(player.getPlayer_uk())
                .player_name(player.getPlayer_name())
                .e_player_name(player.getE_player_name())
                .nickname(player.getNickname())
                .join_yyyy(player.getJoin_yyyy())
                .position(player.getPosition())
                .back_no(player.getBack_no())
                .nation(player.getNation())
                .birth_date(player.getBirth_date())
                .solar(player.getSolar())
                .height(player.getHeight())
                .weight(player.getWeight())
                .team_uk(player.getTeam_uk())
                .build();
    }
}



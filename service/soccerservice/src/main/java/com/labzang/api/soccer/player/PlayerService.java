package com.labzang.api.soccer.player;

import java.util.List;

public interface PlayerService {
    public void findById(PlayerModel playerDTO);

    public void findAll();

    public void save(PlayerModel playerDTO);

    public void saveAll(List<PlayerModel> playerDTOList);

    public void update(PlayerModel playerDTO);

    public void delete(PlayerModel playerDTO);
    
    /**
     * Search players by keyword (name, position, nickname).
     */
    public List<PlayerModel> searchByKeyword(String keyword);
}


package com.labzang.api.soccer.team;

import java.util.List;
import site.aiion.api.soccer.team.domain.TeamDTO;
import site.aiion.api.common.domain.Messenger;

public interface TeamService {
    public Messenger findById(TeamModel teamDTO);
    public Messenger findAll();
    public Messenger save(TeamModel teamDTO);
    public Messenger saveAll(List<TeamModel> teamDTOList);
    public Messenger update(TeamModel teamDTO);
    public Messenger delete(TeamModel teamDTO);
}

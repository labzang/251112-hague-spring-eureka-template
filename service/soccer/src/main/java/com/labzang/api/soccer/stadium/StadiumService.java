package com.labzang.api.soccer.stadium;

import java.util.List;
import site.aiion.api.soccer.stadium.domain.StadiumDTO;
import site.aiion.api.common.domain.Messenger;

public interface StadiumService {
    public Messenger findById(StadiumModel stadiumDTO);
    public Messenger findAll();
    public Messenger save(StadiumModel stadiumDTO);
    public Messenger saveAll(List<StadiumModel> stadiumDTOList);
    public Messenger update(StadiumModel stadiumDTO);
    public Messenger delete(StadiumModel stadiumDTO);
}

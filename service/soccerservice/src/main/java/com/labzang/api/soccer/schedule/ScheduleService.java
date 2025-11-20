package com.labzang.api.soccer.schedule;

import java.util.List;
import com.labzang.api.soccer.common.Messenger;

public interface ScheduleService {
    public Messenger findById(ScheduleModel scheduleDTO);
    public Messenger findAll();
    public Messenger save(ScheduleModel scheduleDTO);
    public Messenger saveAll(List<ScheduleModel> scheduleDTOList);
    public Messenger update(ScheduleModel scheduleDTO);
    public Messenger delete(ScheduleModel scheduleDTO);
}



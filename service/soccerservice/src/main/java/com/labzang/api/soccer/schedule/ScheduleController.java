package com.labzang.api.soccer.schedule;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import com.labzang.api.soccer.common.Messenger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/findById")
    public Messenger findById(@RequestBody ScheduleModel scheduleDTO) {
        return scheduleService.findById(scheduleDTO);
    }

    @GetMapping
    public Messenger findAll() {
        return scheduleService.findAll();
    }

    @PostMapping
    public Messenger save(@RequestBody ScheduleModel scheduleDTO) {
        return scheduleService.save(scheduleDTO);
    }

    @PostMapping("/saveAll")
    public Messenger saveAll(@RequestBody List<ScheduleModel> scheduleDTOList) {
        return scheduleService.saveAll(scheduleDTOList);
    }

    @PutMapping
    public Messenger update(@RequestBody ScheduleModel scheduleDTO) {
        return scheduleService.update(scheduleDTO);
    }

    @DeleteMapping
    public Messenger delete(@RequestBody ScheduleModel scheduleDTO) {
        return scheduleService.delete(scheduleDTO);
    }

}


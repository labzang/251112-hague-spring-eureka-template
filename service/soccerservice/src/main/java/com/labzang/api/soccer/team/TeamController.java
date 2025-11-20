package com.labzang.api.soccer.team;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import com.labzang.api.common.domain.Messenger;
import com.labzang.api.soccer.team.domain.TeamDTO;
import com.labzang.api.soccer.team.service.TeamService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/findById")
    public Messenger findById(@RequestBody TeamModel teamDTO) {
        return teamService.findById(teamDTO);
    }

    @GetMapping
    public Messenger findAll() {
        return teamService.findAll();
    }

    @PostMapping
    public Messenger save(@RequestBody TeamModel teamDTO) {
        return teamService.save(teamDTO);
    }

    @PostMapping("/saveAll")
    public Messenger saveAll(@RequestBody List<TeamModel> teamDTOList) {
        return teamService.saveAll(teamDTOList);
    }

    @PutMapping
    public Messenger update(@RequestBody TeamModel teamDTO) {
        return teamService.update(teamDTO);
    }

    @DeleteMapping
    public Messenger delete(@RequestBody TeamModel teamDTO) {
        return teamService.delete(teamDTO);
    }

}


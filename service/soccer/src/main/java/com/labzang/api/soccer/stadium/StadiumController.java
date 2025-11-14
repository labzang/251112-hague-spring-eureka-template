package com.labzang.api.soccer.stadium;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.aiion.api.common.domain.Messenger;
import site.aiion.api.soccer.stadium.domain.StadiumDTO;
import site.aiion.api.soccer.stadium.service.StadiumService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stadiums")
public class StadiumController {

    private final StadiumService stadiumService;

    @PostMapping("/findById")
    public Messenger findById(@RequestBody StadiumModel stadiumDTO) {
        return stadiumService.findById(stadiumDTO);
    }

    @GetMapping
    public Messenger findAll() {
        return stadiumService.findAll();
    }

    @PostMapping
    public Messenger save(@RequestBody StadiumModel stadiumDTO) {
        return stadiumService.save(stadiumDTO);
    }

    @PostMapping("/all")
    public Messenger saveAll(@RequestBody List<StadiumModel> stadiumDTOList) {
        return stadiumService.saveAll(stadiumDTOList);
    }

    @PutMapping
    public Messenger update(@RequestBody StadiumModel stadiumDTO) {
        return stadiumService.update(stadiumDTO);
    }

    @DeleteMapping
    public Messenger delete(@RequestBody StadiumModel stadiumDTO) {
        return stadiumService.delete(stadiumDTO);
    }

}

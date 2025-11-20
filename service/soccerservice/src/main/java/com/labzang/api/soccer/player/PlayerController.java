package com.labzang.api.soccer.player;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.labzang.api.soccer.common.Messenger;
import lombok.RequiredArgsConstructor;
import com.labzang.api.soccer.player.PlayerModel;
import com.labzang.api.soccer.player.PlayerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/findById")
    public Messenger findById(@RequestBody PlayerModel playerDTO) {
        playerService.findById(playerDTO);
        return null;
    }

    @GetMapping
    public Messenger findAll() {
        return null;
    }

    @PostMapping
    public Messenger save(@RequestBody PlayerModel playerDTO) {
        return null;
    }

    @PostMapping("/saveAll")
    public Messenger saveAll(@RequestBody List<PlayerModel> playerDTOList) {
        return null;
    }

    @PutMapping
    public Messenger update(@RequestBody PlayerModel playerDTO) {
        return null;
    }

    @DeleteMapping
    public Messenger delete(@RequestBody PlayerModel playerDTO) {
        return null;
    }

}


package com.labzang.api.soccer.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDTO {
    private String domain;   // 검???�메??(player, team, schedule ??
    private String keyword;  // 검???�워??
}






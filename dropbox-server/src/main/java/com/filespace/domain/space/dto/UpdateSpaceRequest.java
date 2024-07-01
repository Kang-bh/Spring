package com.filespace.domain.space.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.filespace.domain.space.domain.Space;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateSpaceRequest {
    private String name;
    private String label;
    private Long maxPeople;
}

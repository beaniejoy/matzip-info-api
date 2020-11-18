package com.matzip.matziplist.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class GradeApiResponse {

    private Long id;

    private Double total;

    private Double taste;

    private Double price;

    private Double service;

    private Integer number;
}

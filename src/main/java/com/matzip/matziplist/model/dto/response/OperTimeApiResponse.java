package com.matzip.matziplist.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperTimeApiResponse {

    private Long id;

    private String day;

    private String time;
}

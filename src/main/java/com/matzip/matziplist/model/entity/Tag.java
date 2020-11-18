package com.matzip.matziplist.model.entity;

import com.matzip.matziplist.model.dto.response.TagApiResponse;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"restaurant"})
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Restaurant restaurant;

    public TagApiResponse toResponse() {
        return TagApiResponse.builder()
                .id(id)
                .name(name)
                .build();
    }
}

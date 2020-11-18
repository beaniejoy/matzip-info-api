package com.matzip.matziplist.application;

import com.matzip.matziplist.model.dto.request.RestaurantApiRequest;
import com.matzip.matziplist.model.dto.response.RestaurantApiResponse;
import com.matzip.matziplist.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final MenuItemRepository menuItemRepository;

    private final TagRepository tagRepository;

    private final OperTimeRepository operTimeRepository;

    private final GradeRepository gradeRepository;

    public List<RestaurantApiResponse> getRestaurantList() {

        return restaurantRepository.findAll().stream()
                .map(Restaurant::toResponseForList)
                .collect(Collectors.toList());

    }

    // restaurant 상세정보 조회
    public RestaurantApiResponse getRestaurant(Long id) {

        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id))
                .toResponse();
    }

    public void bulkSave(List<RestaurantApiRequest> requestList) {
        gradeRepository.deleteAll();
        operTimeRepository.deleteAll();
        tagRepository.deleteAll();
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();

        requestList.forEach(request -> {
            Restaurant saved = restaurantRepository.save(request.toEntity());

            // save menuItem table data
            if (!request.getMenuItems().isEmpty()) {
                request.getMenuItems()
                        .forEach(menuItemApiRequest ->
                                menuItemRepository.save(menuItemApiRequest.toEntity(saved)));
            }

            // save tag table data
            if (!request.getTag().isEmpty()) {
                request.getTag()
                        .forEach(tagApiRequest ->
                                tagRepository.save(tagApiRequest.toEntity(saved)));
            }

            // save operTime table data
            if (!request.getOperTime().isEmpty()) {
                request.getOperTime()
                        .forEach(operTimeApiRequest ->
                                operTimeRepository.save(operTimeApiRequest.toEntity(saved)));
            }

            // save grade table data
            if (request.getGrade() != null) {
                gradeRepository.save(request.getGrade().toEntity(saved));
            }

        });

    }
}

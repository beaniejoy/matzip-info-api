package com.matzip.matziplist.application;

import com.matzip.matziplist.model.dto.request.RestaurantApiRequest;
import com.matzip.matziplist.model.dto.response.RestaurantApiResponse;
import com.matzip.matziplist.model.entity.MenuItemRepository;
import com.matzip.matziplist.model.entity.Restaurant;
import com.matzip.matziplist.model.entity.RestaurantRepository;
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

    public List<RestaurantApiResponse> getRestaurantList() {

        return restaurantRepository.findAll().stream()
                .map(Restaurant::toResponse)
                .collect(Collectors.toList());

    }

    public RestaurantApiResponse getRestaurant(Long id) {

        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id))
                .toResponse();
    }

    public void bulkSave(List<RestaurantApiRequest> requestList) {
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();

        requestList.forEach(request -> {
            Restaurant saved = restaurantRepository.save(request.toEntity());

            if(!request.getMenuItems().isEmpty()) {
                request.getMenuItems()
                        .forEach(menuItemApiRequest -> menuItemRepository.save(menuItemApiRequest.toEntity(saved)));
            }

        });

    }
}

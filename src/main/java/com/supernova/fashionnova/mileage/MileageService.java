package com.supernova.fashionnova.mileage;

import com.supernova.fashionnova.mileage.dto.MileageResponseDto;
import com.supernova.fashionnova.order.Order;
import com.supernova.fashionnova.order.OrdersRepository;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final OrdersRepository ordersRepository;

    /**
     * 마일리지 내역 조회
     *
     * @param user
     * @param page
     * @return List<MileageResponseDto>
     */
    public List<MileageResponseDto> getMileageHistoryList(User user, int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Order> mileagePage = ordersRepository.findByUser(user, pageable);

        return mileagePage.stream()
            .map(MileageResponseDto::new)
            .collect(Collectors.toList());

    }

}

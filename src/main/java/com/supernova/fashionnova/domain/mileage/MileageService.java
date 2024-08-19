package com.supernova.fashionnova.domain.mileage;

import com.supernova.fashionnova.domain.mileage.dto.MileageResponseDto;
import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.payment.PayAction;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final MileageRepository mileageRepository;

    /**
     * 마일리지 내역 조회
     *
     * @param user
     * @param page
     * @return List<MileageResponseDto>
     */
    public List<MileageResponseDto> getMileageHistoryList(User user, int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Mileage> mileagePage = mileageRepository.findByUser(user, pageable);

        return mileagePage.stream()
            .map(MileageResponseDto::new)
            .collect(Collectors.toList());

    }

    @Transactional
    public void calculateMileage(PayAction action, Order order, User user) {

//      Mileage mileage = mileageRepository.findByUserId(user.getId()).stream().map(Mileage::getMileage)
        Mileage usedMileage = new Mileage(user, 0L);
        Mileage plusMileage = new Mileage(user, 0L);

        if (PayAction.BUY.equals(action)) {
            // 사용 마일리지 차감
            user.updateMileage(user.getMileage() - order.getUsedMileage());
            usedMileage.updateMileage(-order.getUsedMileage());
            mileageRepository.save(usedMileage);
            // 등급 혜택 별로 마일리지 추가하기
            plusMileage.updateMileage(getMileageByGrade(order, user));
            mileageRepository.save(plusMileage);
        } else {
            // 환불했을 때
            user.updateMileage(user.getMileage() + order.getUsedMileage());
            usedMileage.updateMileage(order.getUsedMileage());
            mileageRepository.save(usedMileage);
            // 등급 혜택 별로 마일리지 추가하기
            plusMileage.updateMileage(getMileageByGrade(order, user));
            mileageRepository.save(plusMileage);
        }

    }

    private Long getMileageByGrade(Order order, User user) {

        switch (user.getUserGrade()) {
            case BRONZE -> {
                return (long) (0.01 * order.getTotalPrice()) / 10 * 10;
            }
            case SILVER -> {
                return (long) (0.03 * order.getTotalPrice()) / 10 * 10;
            }
            case GOLD -> {
                return (long) (0.05 * order.getTotalPrice()) / 10 * 10;
            }
        }

        return null;
    }

}

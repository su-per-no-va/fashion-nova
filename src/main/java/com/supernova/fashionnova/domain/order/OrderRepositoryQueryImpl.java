package com.supernova.fashionnova.domain.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {

    private final JPAQueryFactory queryFactory;
    QOrder order = QOrder.order;

    @Override
    public Optional<Long> findTodayOrderTotalPriceSum() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);

        return calculateTotalPriceSum(startOfDay, endOfDay);
    }

    @Override
    public Optional<Long> findWeekOrderTotalPriceSum() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setStartOfDay(calendar);
        LocalDateTime startDate = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        setEndOfDay(calendar);
        LocalDateTime endDate = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());

        return calculateTotalPriceSum(startDate, endDate);
    }

    @Override
    public Optional<Long> findMonthOrderTotalPriceSum(int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setStartOfDay(calendar);
        LocalDateTime startDate = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());

        //현재 설정된 최대값(월의 마지막 날짜를 반환)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setEndOfDay(calendar);
        LocalDateTime endDate = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());

        if (month > startDate.getMonth().getValue()) {
            throw new CustomException(ErrorType.NOT_YET);
        }

        return calculateTotalPriceSum(startDate, endDate);
    }

    private Optional<Long> calculateTotalPriceSum(LocalDateTime startDate, LocalDateTime endDate) {
        // 날짜 범위 조건
        BooleanExpression date = order.modifiedAt.between(startDate, endDate);
        // 상태가 'SUCCESS'인 조건
        BooleanExpression status = order.orderStatus.eq(OrderStatus.SUCCESS);
        // 날짜와 상태 조건을 조합
        BooleanExpression dateAndStatus = date.and(status);
        // 가격의 총합 계산
        NumberExpression<Long> priceSum = order.totalPrice.sum();

        Long totalSum = queryFactory.select(priceSum)
            .from(order)
            .where(dateAndStatus)
            .fetchOne();

        return Optional.ofNullable(totalSum);
    }

    private void setStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private void setEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

}

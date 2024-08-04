package com.supernova.fashionnova.global.common;

import static org.hibernate.query.sqm.tree.SqmNode.log;

import com.supernova.fashionnova.admin.AdminService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MileageScheduler {

    private final AdminService adminService;

    public MileageScheduler(AdminService adminService) {
        this.adminService = adminService;
    }

    // 매년 1월, 4월, 7월, 10월 1일 자정에 실행
    @Scheduled(cron = "0 0 0 1 1,4,7,10 ?")
    public void resetMileage() {
        adminService.deleteMileage();
        log.info("Mileage reset");
    }

}

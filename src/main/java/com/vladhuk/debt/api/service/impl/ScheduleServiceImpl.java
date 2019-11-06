package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    @Override
    public void deleteOldRequests() {

    }
}

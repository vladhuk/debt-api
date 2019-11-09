package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.AppException;
import com.vladhuk.debt.api.model.Status;
import com.vladhuk.debt.api.repository.StatusRepository;
import com.vladhuk.debt.api.service.StatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status getStatus(Status.StatusName statusName) {
        return statusRepository.findByName(statusName)
                .orElseThrow(() -> new AppException("Request status not set."));
    }

}

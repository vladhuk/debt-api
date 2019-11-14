package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.Status;
import com.vladhuk.debt.api.repository.StatusRepository;
import com.vladhuk.debt.api.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    private static final Logger logger = LoggerFactory.getLogger(StatusServiceImpl.class);

    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status getStatus(Status.StatusName statusName) {
        final Optional<Status> status = statusRepository.findByName(statusName);

        if (status.isEmpty()) {
            logger.error("Status with name {} not founded", statusName);
            throw new ResourceNotFoundException("Status", "name", statusName);
        }

        return status.get();
    }

}

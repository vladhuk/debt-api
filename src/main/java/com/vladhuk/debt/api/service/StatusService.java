package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.Status;

public interface StatusService {

    Status getStatus(Status.StatusName statusName);

}

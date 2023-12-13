package com.example.example100.user.service;

import com.example.example100.common.model.ServiceResult;
import com.example.example100.user.model.UserPointInput;

public interface PointService {
    ServiceResult addPoint(String email, UserPointInput userPointInput);
}

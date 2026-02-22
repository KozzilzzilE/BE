package com.pocketco.domain.learning.application;

import com.pocketco.domain.admin.dto.AddAppliedRequest;
import com.pocketco.domain.admin.dto.AddAppliedResponse;

import java.util.List;

public interface AppliedService {
    List<AddAppliedResponse> addApplied(List<AddAppliedRequest> reqs);
}
package ru.test.conveyor.service;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.ScoringDataDTO;

public interface ScoringService {
    CreditDTO getCreditCalculation(ScoringDataDTO scoringDataDTO);

}

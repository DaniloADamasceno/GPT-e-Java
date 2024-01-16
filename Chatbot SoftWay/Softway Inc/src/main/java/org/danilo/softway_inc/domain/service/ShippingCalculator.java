package org.danilo.softway_inc.domain.service;

import org.danilo.softway_inc.domain.DataShippingCalculations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

// --> Calculador de Frete
@Service
public class ShippingCalculator {
    // --> Logica do cálculo de frete

    public BigDecimal computeShippingCost(DataShippingCalculations dataShippingCalculations) {
        return new BigDecimal("3,45").multiply(new BigDecimal(dataShippingCalculations.quantityProducts()));
    }
}

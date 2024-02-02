package br.com.danilo.softway_inc.domain.service;

import br.com.danilo.softway_inc.domain.DataShippingCalculations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

// --> Calculador de Frete
@Service
public class ShippingCalculator {

    public static BigDecimal computeShippingCost(DataShippingCalculations dataShippingCalculations) {
        //lógica para cálculo de frete aquí...

        return new BigDecimal("3.45").multiply(new BigDecimal(dataShippingCalculations.quantityProducts()));
    }

}

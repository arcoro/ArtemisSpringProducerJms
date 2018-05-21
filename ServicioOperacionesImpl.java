package com.spring.jms.servicios;

import com.spring.jms.datos.Operaciones;
import org.springframework.stereotype.Component;

@Component
public class ServicioOperacionesImpl implements ServicioOperaciones {
    @Override
    public int sumar(Operaciones operaciones) {
        return operaciones.getN1()+operaciones.getN2();
    }
}

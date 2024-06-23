package com.upc.FinanazasGrupo4.shared.helpers.Calculadora;

import com.upc.FinanazasGrupo4.resource.dto.CreateEntryDataCronograma;
import com.upc.FinanazasGrupo4.shared.helpers.Clases.ColumnasCronogramaPago;
import com.upc.FinanazasGrupo4.shared.helpers.Clases.VariablesIntermediasCalculoCronograma;
import com.upc.FinanazasGrupo4.shared.helpers.Utilidades.Utilidades;
import com.upc.FinanazasGrupo4.domain.model.Cuota;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraCuota {

    public static String SIN_PLAZO_GRACIA="S";
    public static String PLAZO_GRACIA_TOTAL="T";
    public static String PLAZO_GRACIA_PARCIAL="P";

    public static int CUOTA_CERO=0;

    public static double realizarCalculoCuotaSegunFrecuenciaPago(double saldo, double tasaInteres, double tasaDesgravamen, int tiempoDeFinanciamiento,int tiempoCuotaActual,String estadoPlazoGracia) {

        //Hay 4 casos
        //Caso de calculo sin plazo de gracia
        //Caso de calculo de la cuota con plazo de gracia parcial
        //Caso de calculo de la cuota con plazo de gracia total
        //Caso de que se tiene que calcular la cuota cuando ya se ha sido afectado por algun plazo de gracia (total o parcial)

        if(estadoPlazoGracia.equalsIgnoreCase(SIN_PLAZO_GRACIA)){
            //SI ES SIN PLAZO DE GRACIA
            //=+(montoPrestamos*(tasaInteres+tasaDesgravamen))/(1-(1+(tasaInteres+tasaDesgravamen))^-tiempoDeFinanciamiento)
            double tasaTotalSegunFrecuenciaPago = (tasaInteres / 100) + (tasaDesgravamen / 100);
            double denominador = 1 - Math.pow((1 + tasaTotalSegunFrecuenciaPago), -(tiempoDeFinanciamiento-(tiempoCuotaActual-1)));
            return (saldo * tasaTotalSegunFrecuenciaPago) / denominador;

        } else if (estadoPlazoGracia.equalsIgnoreCase(PLAZO_GRACIA_PARCIAL)) {
            return calculoInteres(saldo,tasaInteres)+CalculadoraSeguroDesgravamen.calcularSeguroDesgravamenConPrestamo(saldo,tasaDesgravamen);
        } else{
            //SI ES CON PLAZO DE GRACIA TOTAL
            return 0.0;
        }


    }

    public static double calcularNumeroCuotasTotales(int tiempoAnios, String frecuenciaPago) {
        int pagosPorAnio = 0;
        String frecuenciaPagoMayuscula= frecuenciaPago.toUpperCase();

        switch (frecuenciaPagoMayuscula) {
            case "DIARIA":
                pagosPorAnio = 365;
                break;
            case "SEMANAL":
                pagosPorAnio = 52;
                break;
            case "QUINCENAL":
                pagosPorAnio = 26;
                break;
            case "MENSUAL":
                pagosPorAnio = 12;
                break;
            case "BIMESTRAL":
                pagosPorAnio = 6;
                break;
            case "TRIMESTRAL":
                pagosPorAnio = 4;
                break;
            case "CUATRIMESTRAL":
                pagosPorAnio = 3;
                break;
            case "SEMESTRAL":
                pagosPorAnio = 2;
                break;
            case "ANUAL":
                pagosPorAnio = 1;
                break;
            default:
                // Manejar un caso por defecto o lanzar una excepción si la frecuencia no es válida
                break;
        }

        return tiempoAnios * pagosPorAnio;
    }

    public static double calculoInteres(double saldoInicial, double tasaEfectivaSegunFrecuenciaPago) {
        return saldoInicial * (tasaEfectivaSegunFrecuenciaPago / 100);
    }

    public static String determinarEstadoPlazoGracia( CreateEntryDataCronograma createEntryDataCronograma){
        String ESTADO_PLAZO_GRACIA;

        if(createEntryDataCronograma.getPlazoDeGracia()==null){
            ESTADO_PLAZO_GRACIA=SIN_PLAZO_GRACIA;
        }else{
            if(createEntryDataCronograma.getPlazoDeGracia().equalsIgnoreCase("TOTAL")){
                ESTADO_PLAZO_GRACIA=PLAZO_GRACIA_TOTAL;
            }else{
                ESTADO_PLAZO_GRACIA=PLAZO_GRACIA_PARCIAL;
            }
        }

        return ESTADO_PLAZO_GRACIA;
    }

    public static double calcularSaldoFinal(ColumnasCronogramaPago columnasCronogramaPago, String ESTADO_PLAZO_GRACIA){

        if(ESTADO_PLAZO_GRACIA.equalsIgnoreCase(PLAZO_GRACIA_TOTAL)){
            return columnasCronogramaPago.saldoInicial+columnasCronogramaPago.interes;
        }else if(ESTADO_PLAZO_GRACIA.equalsIgnoreCase(PLAZO_GRACIA_PARCIAL)){
            return columnasCronogramaPago.saldoInicial;
        }else{
            //SIN PLAZO DE GRACIA
            return columnasCronogramaPago.saldoInicial-columnasCronogramaPago.amortizacion;
        }
    }

    public static List<Cuota> obtenerListaCuotas(CreateEntryDataCronograma createEntryDataCronograma) {

        //Instanciar variables que vamos a tener si o si
        VariablesIntermediasCalculoCronograma variablesIntermediasCalculoCronograma = instanciarVariablesIntermedias(createEntryDataCronograma);
        ColumnasCronogramaPago columnasCronogramaPago = instanciarColumnasCronogramaPago(createEntryDataCronograma, variablesIntermediasCalculoCronograma);

        String ESTADO_PLAZO_GRACIA = determinarEstadoPlazoGracia(createEntryDataCronograma);

        double numeroCuotasParciales= variablesIntermediasCalculoCronograma.numeroCuotasPlazoGraciaParcial;


        //Instanciamos lista de cuotas
        List<Cuota> listaCuotas = new ArrayList<>();

        //Cuotas del prestamo
        for(int cuotaActual=0;cuotaActual<=variablesIntermediasCalculoCronograma.numeroCuotas;cuotaActual++){

            Cuota cuotaNueva = new Cuota();

            if(cuotaActual==CUOTA_CERO){
                cuotaNueva.setSaldoInicial(columnasCronogramaPago.saldoInicial);
                cuotaNueva.setNumeroDeCuota(0);
                cuotaNueva.setAmortizacion(0);
                cuotaNueva.setInteres(0);
                cuotaNueva.setSeguroDesgravamen(0);
                cuotaNueva.setSeguroVehicular(0);
                cuotaNueva.setPortes(0);
                cuotaNueva.setCostosRegistrales(0);
                cuotaNueva.setCostosNotariales(0);
                cuotaNueva.setCuota(0);
                cuotaNueva.setFechaDePago(createEntryDataCronograma.getFechaInicio());
                cuotaNueva.setSaldoFinal(columnasCronogramaPago.saldoInicial);
                cuotaNueva.setFlujo(columnasCronogramaPago.saldoInicial);
            }else{

                if(cuotaActual>numeroCuotasParciales) {
                    ESTADO_PLAZO_GRACIA = SIN_PLAZO_GRACIA;
                }


                columnasCronogramaPago.numeroCuota= cuotaActual;
                columnasCronogramaPago.interes= Utilidades.redondear(columnasCronogramaPago.saldoInicial*(variablesIntermediasCalculoCronograma.tasaEfectiva/100),2);
                columnasCronogramaPago.seguroDesgravamen=Utilidades.redondear(CalculadoraSeguroDesgravamen.calcularSeguroDesgravamenConPrestamo(columnasCronogramaPago.saldoInicial,variablesIntermediasCalculoCronograma.tasaDesgravamen),2);
                columnasCronogramaPago.cuota= Utilidades.redondear(
                        realizarCalculoCuotaSegunFrecuenciaPago(
                                columnasCronogramaPago.saldoInicial,
                                variablesIntermediasCalculoCronograma.tasaEfectiva,
                                variablesIntermediasCalculoCronograma.tasaDesgravamen,
                                variablesIntermediasCalculoCronograma.numeroCuotas,
                                cuotaActual,
                                ESTADO_PLAZO_GRACIA)
                        ,2);

                if(ESTADO_PLAZO_GRACIA.equalsIgnoreCase(PLAZO_GRACIA_TOTAL)){
                    columnasCronogramaPago.amortizacion=0;
                }else{
                    columnasCronogramaPago.amortizacion= Utilidades.redondear(columnasCronogramaPago.cuota-columnasCronogramaPago.interes-columnasCronogramaPago.seguroDesgravamen,2);
                }

                //en el saldo Final hay distintos casos, si es con plazo de gracia o sin plazo de gracia
                columnasCronogramaPago.saldoFinal= Utilidades.redondear(
                        calcularSaldoFinal(
                                columnasCronogramaPago,
                                ESTADO_PLAZO_GRACIA),
                        2);


                columnasCronogramaPago.flujo= Utilidades.redondear(columnasCronogramaPago.cuota+columnasCronogramaPago.seguroVehicular+columnasCronogramaPago.portes+columnasCronogramaPago.costosNotariales+columnasCronogramaPago.costosRegistrales,2);
                columnasCronogramaPago.fechaVencimiento= CalculadoraFechas.calcularFechaDePago(createEntryDataCronograma.fechaInicio,cuotaActual, createEntryDataCronograma.frecuenciaPago);


                //Guardamos el saldo inicial y Actualizamos el valor del saldo inicial para la siguiente cuota
                cuotaNueva.setSaldoInicial(Utilidades.redondear(columnasCronogramaPago.saldoInicial,2));
                columnasCronogramaPago.saldoInicial=columnasCronogramaPago.saldoFinal;
                cuotaNueva.setNumeroDeCuota(cuotaActual);
                cuotaNueva.setAmortizacion(columnasCronogramaPago.amortizacion);
                cuotaNueva.setInteres(columnasCronogramaPago.interes);
                cuotaNueva.setSeguroDesgravamen(columnasCronogramaPago.seguroDesgravamen);
                cuotaNueva.setSeguroVehicular(columnasCronogramaPago.seguroVehicular);
                cuotaNueva.setPortes(createEntryDataCronograma.getPortes());
                cuotaNueva.setCostosRegistrales(createEntryDataCronograma.getCostosRegistrales());
                cuotaNueva.setCostosNotariales(createEntryDataCronograma.getCostosNotariales());
                cuotaNueva.setCuota(columnasCronogramaPago.cuota);
                cuotaNueva.setFechaDePago(columnasCronogramaPago.fechaVencimiento);
                cuotaNueva.setSaldoFinal(columnasCronogramaPago.saldoFinal);
                cuotaNueva.setFlujo(columnasCronogramaPago.flujo);
            }

            listaCuotas.add(cuotaNueva);
        }


        //Añadimos la ultima cuota
        listaCuotas.add(calculoUltimaCuota(createEntryDataCronograma,variablesIntermediasCalculoCronograma, columnasCronogramaPago));

        return listaCuotas;
    }

    public static Cuota calculoUltimaCuota(CreateEntryDataCronograma createEntryDataCronograma, VariablesIntermediasCalculoCronograma variablesIntermediasCalculoCronograma, ColumnasCronogramaPago columnasCronogramaPago) {
        double amortizacion = 0;
        double interes = variablesIntermediasCalculoCronograma.cuotaFinal * (variablesIntermediasCalculoCronograma.tasaEfectiva / 100);
        double valorSeguroDesgravamen = variablesIntermediasCalculoCronograma.cuotaFinal * (variablesIntermediasCalculoCronograma.tasaDesgravamen / 100);

        Cuota ultimaCuota = new Cuota();
        ultimaCuota.setNumeroDeCuota((int) variablesIntermediasCalculoCronograma.numeroCuotas + 1);
        ultimaCuota.setSaldoInicial(Utilidades.redondear(variablesIntermediasCalculoCronograma.cuotaFinal,2));
        ultimaCuota.setAmortizacion(Utilidades.redondear(amortizacion,2));
        ultimaCuota.setInteres(Utilidades.redondear(interes,2));
        ultimaCuota.setSeguroDesgravamen(Utilidades.redondear(valorSeguroDesgravamen,2));
        ultimaCuota.setSeguroVehicular(Utilidades.redondear(columnasCronogramaPago.seguroVehicular,2));
        ultimaCuota.setPortes(columnasCronogramaPago.portes);
        ultimaCuota.setCostosRegistrales(columnasCronogramaPago.costosRegistrales);
        ultimaCuota.setCostosNotariales(columnasCronogramaPago.costosNotariales);
        ultimaCuota.setCuota(Utilidades.redondear(variablesIntermediasCalculoCronograma.cuotaFinal + amortizacion + interes + valorSeguroDesgravamen,2));
        ultimaCuota.setFechaDePago(CalculadoraFechas.calcularFechaDePago(variablesIntermediasCalculoCronograma.fechaInicio, (int) variablesIntermediasCalculoCronograma.numeroCuotas + 1, createEntryDataCronograma.frecuenciaPago));
        ultimaCuota.setSaldoFinal(0);
        ultimaCuota.setFlujo(
                Utilidades.redondear(
                        variablesIntermediasCalculoCronograma.cuotaFinal +
                        columnasCronogramaPago.seguroVehicular +
                        columnasCronogramaPago.portes +
                        columnasCronogramaPago.costosRegistrales +
                        columnasCronogramaPago.costosNotariales,2)

        );

        return ultimaCuota;
    }

    public static ColumnasCronogramaPago instanciarColumnasCronogramaPago(CreateEntryDataCronograma createEntryDataCronograma, VariablesIntermediasCalculoCronograma variablesIntermediasCalculoCronograma){

        double saldoInicial= variablesIntermediasCalculoCronograma.montoAFinanciar;
        double amortizacion=0;
        double interes=0;
        double valorSeguroDesgravamen=0;
        double valorSeguroVehicular= Utilidades.redondear(CalculadoraSeguroVehicular.calculoSeguroVehicularDelVehiculo(createEntryDataCronograma.getPrecioVehiculo(), variablesIntermediasCalculoCronograma.tasaSeguroVehicular), 2);
        double saldoFinal= Utilidades.redondear(saldoInicial-amortizacion,2);

        ColumnasCronogramaPago columnasCronogramaPago = ColumnasCronogramaPago.builder()
                .numeroCuota(0)
                .fechaVencimiento(createEntryDataCronograma.getFechaInicio())
                .saldoInicial(Utilidades.redondear(saldoInicial,2))
                .interes(interes)
                .cuota(0)
                .amortizacion(amortizacion)
                .seguroDesgravamen(valorSeguroDesgravamen)
                .seguroVehicular(valorSeguroVehicular)
                .portes(createEntryDataCronograma.getPortes())
                .costosNotariales(createEntryDataCronograma.getCostosNotariales())
                .costosRegistrales(createEntryDataCronograma.getCostosRegistrales())
                .saldoFinal(Utilidades.redondear(saldoFinal,2))
                .flujo(Utilidades.redondear(saldoFinal,2))
                .build();

        return columnasCronogramaPago;
    }

    public static VariablesIntermediasCalculoCronograma instanciarVariablesIntermedias(CreateEntryDataCronograma createEntryDataCronograma){

        double tasaEfectiva;

        if(createEntryDataCronograma.getTipoTasaInteres().equalsIgnoreCase("EFECTIVA")) {
            tasaEfectiva= CalculadoraTasaInteresEfectiva.convertirEfectivaAEfectivaDeAcuerdoALaFrecuenciaPago(createEntryDataCronograma.getPorcentajeTasaInteres(), createEntryDataCronograma.getPlazoTasaInteres(), createEntryDataCronograma.getFrecuenciaPago());
        }else{
            //Si no es efectiva (osea es nominal), se convierte a efectiva
            //La tasa efectiva nominal debe ser pasada a una tasa efectiva de acuerdo a la frecuencia de pago
            tasaEfectiva= CalculadoraTasaInteresNominal.convertirATasaEfectivaDeAcuerdoALaFrecuenciaPago(createEntryDataCronograma.getPlazoTasaInteres(), createEntryDataCronograma.getPorcentajeTasaInteres(), createEntryDataCronograma.getCapitalizacion(), createEntryDataCronograma.getFrecuenciaPago());
        }

        VariablesIntermediasCalculoCronograma variablesIntermediasCalculoCronograma =
                VariablesIntermediasCalculoCronograma.builder()
                        .porcentajePrestamoAFinanciar(Utilidades.calcularPorcentajePrestamoAFinanciar(createEntryDataCronograma.getPorcentajeCuotaInicial(), createEntryDataCronograma.getPorcentajeCuotaFinal()))
                        .fechaInicio(createEntryDataCronograma.getFechaInicio())
                        .tasaEfectiva(tasaEfectiva)
                        .tasaDesgravamen(CalculadoraSeguroDesgravamen.calcularTasaSeguroConFrecuenciaPago(createEntryDataCronograma.getFrecuenciaPago(), createEntryDataCronograma.getTiempoSeguroDesgravamen(), createEntryDataCronograma.getPorcentajeSeguroDesgravamen()))
                        .tasaSeguroVehicular(CalculadoraSeguroVehicular.calcularTasaSeguroVehicularDadoFrecuenciaPago(createEntryDataCronograma.getFrecuenciaPago(), createEntryDataCronograma.getTiempoSeguroVehicular(), createEntryDataCronograma.getPorcentajeSeguroVehicular()))
                        .montoAFinanciar(Utilidades.calcularMontoAplicandoPorcentaje(createEntryDataCronograma.getPrecioVehiculo(),Utilidades.calcularPorcentajePrestamoAFinanciar(createEntryDataCronograma.getPorcentajeCuotaInicial(), createEntryDataCronograma.getPorcentajeCuotaFinal())))
                        .cuotaInicial(Utilidades.calcularMontoAplicandoPorcentaje(createEntryDataCronograma.getPrecioVehiculo(), createEntryDataCronograma.getPorcentajeCuotaInicial()))
                        .cuotaFinal(Utilidades.calcularMontoAplicandoPorcentaje(createEntryDataCronograma.getPrecioVehiculo(), createEntryDataCronograma.getPorcentajeCuotaFinal()))
                        .numeroCuotas((int) CalculadoraCuota.calcularNumeroCuotasTotales(createEntryDataCronograma.getNumeroAnios(), createEntryDataCronograma.getFrecuenciaPago()))
                        .numeroCuotasPlazoGraciaParcial(createEntryDataCronograma.getTiempoPlazoDeGracia())
                        .numeroCuotasPlazoGraciaTotal(createEntryDataCronograma.getTiempoPlazoDeGracia())
                        .build();

        return variablesIntermediasCalculoCronograma;
    }

}

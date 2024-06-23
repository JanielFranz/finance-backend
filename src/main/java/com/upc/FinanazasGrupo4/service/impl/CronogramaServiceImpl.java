package com.upc.FinanazasGrupo4.service.impl;

import com.upc.FinanazasGrupo4.domain.model.*;
import com.upc.FinanazasGrupo4.domain.persistence.repository.*;
import com.upc.FinanazasGrupo4.resource.dto.CreateEntryDataCronograma;
import com.upc.FinanazasGrupo4.shared.exception.ValidationException;
import com.upc.FinanazasGrupo4.shared.helpers.Calculadora.CalculadoraCuota;
import com.upc.FinanazasGrupo4.shared.helpers.Utilidades.Utilidades;
import com.upc.FinanazasGrupo4.domain.service.inter.CronogramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CronogramaServiceImpl implements CronogramaService {


    @Autowired
    private CronogramaRepository cronogramaRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TasaInteresRepository tasaInteresRepository;

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private MonedaRepository monedaRepository;

    @Override
    public List<Cronograma> getAllCronogramas() {
        return null;
    }

    @Override
    public List<Cuota> getAllCuotasByCronograma(Long id) {
        return null;
    }
    @Override
    public Informacion getInformacionByCronograma(Long id) {
        return null;
    }
    @Override
    public void deleteCronograma(Long id) {
    }
    @Override
    public Cronograma saveCronograma(Long customerId, CreateEntryDataCronograma createEntryDataCronograma) {

        validacionCustomer(customerId);
        validacionMoneda(createEntryDataCronograma);

        validarDatosEntrada(createEntryDataCronograma);

        convertirTodosLosCamposAMayuscula(createEntryDataCronograma);

        Cronograma nuevoCronograma= Cronograma.builder().build();


        rellenarCuotas(calcularListaCuotasSegunPlazo(createEntryDataCronograma),nuevoCronograma);
        rellenarInformacion(createEntryDataCronograma,nuevoCronograma);
        rellenarVehiculo(createEntryDataCronograma,nuevoCronograma);
        rellenarCustomer(customerId,nuevoCronograma);

        //Primero se guarda el cronograma y luego se guardan las cuotas del cronograma
        cronogramaRepository.save(nuevoCronograma)
                .getCuotas()
                .forEach(cuota -> cuotaRepository.save(cuota));

        return nuevoCronograma;
    }

    @Override
    public List<Cronograma> getAllCronogramasByCustomerId(Long customerId) {
        if(!customerRepository.existsById(customerId)){
            throw new ValidationException("No existe el customer con el id: "+customerId);
        }
        return cronogramaRepository.findAllByCustomerId(customerId);
    }

    @Override
    public List<Cronograma> getAllCronogramaByCustomerIdAndId(Long customerId, Long id) {
        return cronogramaRepository.findAllByCustomerIdAndId(customerId, id);
    }


    private String determinarAbreviaturaTasaInteres(CreateEntryDataCronograma createEntryDataCronograma){

        //obtengo los datos del tipo de tasa y su plazo
        String tipoTasa= createEntryDataCronograma.getTipoTasaInteres();
        String plazoTasa= createEntryDataCronograma.getPlazoTasaInteres();

        //llamo al repositorio de tasa de interes para obtener la abreviatura
        TasaInteres tasaInteres=tasaInteresRepository.findByTipoAndPlazo(tipoTasa,plazoTasa);

        return tasaInteres.abreviatura;
    }
    private List<Cuota> calcularListaCuotasSegunPlazo(CreateEntryDataCronograma createEntryDataCronograma){

        return CalculadoraCuota.obtenerListaCuotas(createEntryDataCronograma);
/*        if(createEntryDataCronograma.getPlazoDeGracia()==null)return CalculadoraCuota.obtenerListaCuotasMetodoSinPlazoGracia(createEntryDataCronograma);

        switch (createEntryDataCronograma.getPlazoDeGracia().toUpperCase()){
            case "PARCIAL":
                return CalculadoraCuota.obtenerListaCuotasMetodoConPlazoGraciaParcial(createEntryDataCronograma);
            case "TOTAL":
                return CalculadoraCuota.obtenerListaCuotasMetodoConPlazoGraciaTotal(createEntryDataCronograma);
            default:
                return CalculadoraCuota.obtenerListaCuotasMetodoSinPlazoGracia(createEntryDataCronograma);
        }*/
    }
    private void convertirTodosLosCamposAMayuscula(CreateEntryDataCronograma createEntryDataCronograma){

        if(createEntryDataCronograma.getTipoTasaInteres()!=null){
            createEntryDataCronograma.setTipoTasaInteres(createEntryDataCronograma.getTipoTasaInteres().toUpperCase());
        }else {
            throw new ValidationException("No se ingreso el tipo de tasa de interes");
        }

        if(createEntryDataCronograma.getPlazoTasaInteres()!=null){
            createEntryDataCronograma.setPlazoTasaInteres(createEntryDataCronograma.getPlazoTasaInteres().toUpperCase());
        }else {
            throw new ValidationException("No se ingreso el plazo de la tasa de interes");
        }

        if(createEntryDataCronograma.getTiempoSeguroDesgravamen()!=null){
            createEntryDataCronograma.setTiempoSeguroDesgravamen(createEntryDataCronograma.getTiempoSeguroDesgravamen().toUpperCase());
        }else {
            throw new ValidationException("No se ingreso el tiempo del seguro de desgravamen");
        }

        if(createEntryDataCronograma.getTiempoSeguroVehicular()!=null){
            createEntryDataCronograma.setTiempoSeguroVehicular(createEntryDataCronograma.getTiempoSeguroVehicular().toUpperCase());
        }else{
            throw  new ValidationException("No se ingreso el tiempo del seguro vehicular");
        }

        //la capitalizacion si puede ser nula
        if(createEntryDataCronograma.getCapitalizacion()!=null){
            createEntryDataCronograma.setCapitalizacion(createEntryDataCronograma.getCapitalizacion().toUpperCase());
        }else {
            createEntryDataCronograma.setCapitalizacion(null);
        }

        //el plazo de gracia tambien puede ser nulo
        if(createEntryDataCronograma.getPlazoDeGracia()!=null) {
            createEntryDataCronograma.setPlazoDeGracia(createEntryDataCronograma.getPlazoDeGracia().toUpperCase());
        }else {
            createEntryDataCronograma.setPlazoDeGracia(null);
        }

        //la frecuencia de pago debe estar si o si
        if(createEntryDataCronograma.getFrecuenciaPago()!=null) {
            createEntryDataCronograma.setFrecuenciaPago(createEntryDataCronograma.getFrecuenciaPago().toUpperCase());
        }else {
            throw new ValidationException("No se ingreso la frecuencia de pago");
        }

        if(createEntryDataCronograma.getTipoMoneda()!=null){
            createEntryDataCronograma.setTipoMoneda(createEntryDataCronograma.getTipoMoneda().toUpperCase());
        }else{
            throw new ValidationException("No se ingreso el tipo de moneda");
        }

    }
    private void validarDatosEntrada(CreateEntryDataCronograma createEntryDataCronograma){

        if(createEntryDataCronograma.getPlazoDeGracia()!=null){
            validacionesConPlazoGracia(createEntryDataCronograma);
        }
        validacionesCasoTasaNominal(createEntryDataCronograma);
        validacionesCuotaInicial(createEntryDataCronograma);
        validacionesCuotaFinal(createEntryDataCronograma);
    }
    private void rellenarCuotas(List<Cuota> listaCuotas, Cronograma cronograma){
        //Lista externa de cuotas
        List<Cuota> cuotas= new ArrayList<>();
        //Asociar cada cuota al cronograma
        for(Cuota cuota:listaCuotas){
            cuota.setCronograma(cronograma);
            cuotas.add(cuota);
        }
        cronograma.setCuotas(cuotas);
    }
    private void rellenarInformacion(CreateEntryDataCronograma createEntryDataCronograma, Cronograma cronograma){


        double porcentajePrestamoAFinanciar= Utilidades.calcularPorcentajePrestamoAFinanciar(createEntryDataCronograma.getPorcentajeCuotaInicial(), createEntryDataCronograma.getPorcentajeCuotaFinal());
        //sacar monto a financiar
        double montoAFinanciar= Utilidades.calcularMontoAplicandoPorcentaje(createEntryDataCronograma.getPrecioVehiculo(),porcentajePrestamoAFinanciar);

        Moneda moneda = monedaRepository.findByNombre(createEntryDataCronograma.getTipoMoneda());
        List<Moneda> monedas = new ArrayList<>();
        monedas.add(moneda);

        //Creamos un objeto Informacion y lo rellenamos con lo que obtenemos mediante el builder
        Informacion information = Informacion.builder()
                .numeroAnios(createEntryDataCronograma.numeroAnios)
                .porcentajeCuotaInicial(createEntryDataCronograma.porcentajeCuotaInicial)
                .plazoTasaInteres(createEntryDataCronograma.plazoTasaInteres)
                .porcentajeTasaInteres(createEntryDataCronograma.porcentajeTasaInteres)
                .capitalizacion(createEntryDataCronograma.capitalizacion)
                .tiempoPlazoDeGracia(createEntryDataCronograma.tiempoPlazoDeGracia)
                .porcentajeSeguroDesgravamen(createEntryDataCronograma.porcentajeSeguroDesgravamen)
                .tiempoSeguroDesgravamen(createEntryDataCronograma.tiempoSeguroDesgravamen)
                .porcentajeSeguroVehicular(createEntryDataCronograma.porcentajeSeguroVehicular)
                .tiempoSeguroVehicular(createEntryDataCronograma.tiempoSeguroVehicular)
                .portes(createEntryDataCronograma.portes)
                .costosRegistrales(createEntryDataCronograma.costosRegistrales)
                .costosNotariales(createEntryDataCronograma.costosNotariales)
                .fechaInicio(createEntryDataCronograma.fechaInicio)
                .porcentajePrestamoFinanciar(porcentajePrestamoAFinanciar)
                .porcentajeCuotaFinal(createEntryDataCronograma.porcentajeCuotaFinal)
                .montoCuotaFinal(Utilidades.calcularMontoAplicandoPorcentaje(createEntryDataCronograma.getPrecioVehiculo(), createEntryDataCronograma.getPorcentajeCuotaFinal()))
                .frecuenciaPago(createEntryDataCronograma.frecuenciaPago)
                .montoPrestamoFinanciar(montoAFinanciar)
                .tipoTasaInteres(createEntryDataCronograma.tipoTasaInteres)
                .plazoDeGracia(createEntryDataCronograma.plazoDeGracia)
                .abreviaturaTasaInteres(determinarAbreviaturaTasaInteres(createEntryDataCronograma))
                .monedas(monedas)
                .build();
        //Se actualiza el cronograma
        cronograma.setInformacion(information);

    }
    private void rellenarVehiculo(CreateEntryDataCronograma createEntryDataCronograma, Cronograma cronograma){


        Vehiculo vehiculo = Vehiculo.builder()
                        .precio(createEntryDataCronograma.getPrecioVehiculo())
                        .marca(createEntryDataCronograma.getMarcaVehiculo())
                        .modelo(createEntryDataCronograma.getModeloVehiculo())
                        .build();
        //Se actualiza el cronograma
        cronograma.setVehiculo(vehiculo);
    }
    public void rellenarCustomer(Long idCustomer, Cronograma cronograma){
        //Se actualiza el customer
        //Encontramos al customer por su id
        customerRepository.findById(idCustomer).ifPresent(customer -> {
            //Se agrega el cronograma al customer
            cronograma.setCustomer(customer);
            //customer.getCronograma().add(cronograma);
            //Se actualiza el customer
            //customerRepository.save(customer);
        });
    }
    private void validacionesConPlazoGracia(CreateEntryDataCronograma createEntryDataCronograma){

        double cuotasTotales=CalculadoraCuota.calcularNumeroCuotasTotales(createEntryDataCronograma.getNumeroAnios(), createEntryDataCronograma.getFrecuenciaPago());

        double mitadCuotasTotales= cuotasTotales/2;

        if(createEntryDataCronograma.getPlazoDeGracia().isEmpty()){
            //Si no se ingresa el plazo de gracia, se devuelve un error
            throw new ValidationException("No se ingreso el plazo de gracia");
        }
        if(createEntryDataCronograma.getTiempoPlazoDeGracia()==null || createEntryDataCronograma.tiempoPlazoDeGracia==0){
            //Si no se ingresa el tiempo del plazo de gracia, se devuelve un error
            throw new ValidationException("No se ingreso el tiempo del plazo de gracia o es 0");
        }
        if(createEntryDataCronograma.getTiempoPlazoDeGracia()>mitadCuotasTotales){
            //Si el plazo de gracia es mayor a la mitad de las cuotas totales, se devuelve un error
            throw new ValidationException("" +
                    "El plazo de gracia no puede ser mayor a la mitad de las cuotas totales dado la frecuencia de pago" +
                    ". Cuotas totales: "+(int)cuotasTotales+" , mitad de cuotas totales: "+ (int)mitadCuotasTotales+", frecuencia de pago elegida: "+ createEntryDataCronograma.getFrecuenciaPago().toUpperCase());
        }

    }
    private void validacionesCasoTasaNominal(CreateEntryDataCronograma createEntryDataCronograma){

        if(createEntryDataCronograma.getTipoTasaInteres().equalsIgnoreCase("NOMINAL")){
            if(createEntryDataCronograma.getPlazoTasaInteres()==null || createEntryDataCronograma.getPlazoTasaInteres().isEmpty()){
                //Si no se ingresa el plazo de la tasa nominal, se devuelve un error
                throw new ValidationException("No se ingreso el plazo de la tasa nominal");
            }

            if(createEntryDataCronograma.getCapitalizacion()==null || createEntryDataCronograma.getCapitalizacion().isEmpty()) {
                //Si no se ingresa la capitalizacion de la tasa nominal, se devuelve un error
                throw new ValidationException("No se ingreso la capitalizacion de la tasa nominal");
            }

        }
    }

    private void validacionesCuotaInicial(CreateEntryDataCronograma createEntryDataCronograma){

        int porcentajeMinimoCuotaInicial = 20;
        int porcentajeMaximoCuotaInicial = 30;

        if(createEntryDataCronograma.getPorcentajeCuotaInicial()<porcentajeMinimoCuotaInicial || createEntryDataCronograma.getPorcentajeCuotaInicial()>porcentajeMaximoCuotaInicial){
            //Si no se ingresa la cuota inicial, se devuelve un error
            throw new ValidationException("El porcentaje de la cuota inicial debe ser como mínimo "+porcentajeMinimoCuotaInicial+"% y como máximo "+porcentajeMaximoCuotaInicial+"%");
        }
    }

    private void validacionesCuotaFinal(CreateEntryDataCronograma createEntryDataCronograma){

        int porcentajeMinimoCuotaFinal = 40;
        int porcentajeMaximoCuotaFinal = 50;

        if(createEntryDataCronograma.getPorcentajeCuotaFinal()<porcentajeMinimoCuotaFinal || createEntryDataCronograma.getPorcentajeCuotaFinal()>porcentajeMaximoCuotaFinal){
            //Si no se ingresa la cuota inicial, se devuelve un error
            throw new ValidationException("El porcentaje de la cuota final debe ser como mínimo "+porcentajeMinimoCuotaFinal+"% y como máximo "+porcentajeMaximoCuotaFinal+"%");
        }

    }

    private void validacionCustomer(Long idCustomer){
        if(!customerRepository.existsById(idCustomer)){
            throw new ValidationException("No existe el customer con el id: "+idCustomer);
        }
    }

    private void validacionMoneda(CreateEntryDataCronograma createEntryDataCronograma){

        String tipoMoneda= createEntryDataCronograma.getTipoMoneda();
        if(!tipoMoneda.equalsIgnoreCase("SOLES") && !tipoMoneda.equalsIgnoreCase("DOLARES")){
            throw new ValidationException("No se permite otro tipo que no sean SOLES O DOLARES, No existe la moneda: "+tipoMoneda);
        }
    }


}

package com.ejemplo.sistemaventas;

import db.VentaDAO;
import model.Venta;
import parser.CSVDataReader;
import parser.DataReader;
import parser.JSONDataReader;
import parser.XMLDataReader;
import service.ReporteService;
import service.VentaService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SistemaVentas {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA DE INTEGRACION Y ANALISIS DE VENTAS ===");

        
        VentaDAO ventaDAO = new VentaDAO();
        VentaService ventaService = new VentaService();
        ReporteService reporteService = new ReporteService();

        List<String> erroresGlobales = new ArrayList<>();
        List<Venta> todasLasVentasValidas = new ArrayList<>();

        int totalRegistrosRecibidos = 0;

        
        File archivoCSV = new File("ventas.csv");
        File archivoJSON = new File("ventas.json");
        File archivoXML = new File("ventas.xml");

        
        Object[][] fuentes = {
            {archivoCSV, new CSVDataReader()},
            {archivoJSON, new JSONDataReader()},
            {archivoXML, new XMLDataReader()}
        };

       
        for (Object[] fuente : fuentes) {
            File archivo = (File) fuente[0];
            DataReader reader = (DataReader) fuente[1];

            if (archivo.exists()) {
                System.out.println("Procesando archivo: " + archivo.getName());
                List<Venta> ventasLeidas = reader.leerArchivo(archivo, erroresGlobales);
                totalRegistrosRecibidos += ventasLeidas.size();

                for (Venta venta : ventasLeidas) {
                    
                    if (ventaService.validarYNormalizar(venta, archivo.getName(), erroresGlobales)) {
                        
                        if (ventaDAO.guardarVenta(venta)) {
                            todasLasVentasValidas.add(venta);
                        } else {
                           
                            erroresGlobales.add(archivo.getName() + " | ID: " + venta.getIdVenta() + " | Fallo insercion en BD.");
                        }
                    }
                }
            } else {
                System.out.println("Aviso: El archivo " + archivo.getName() + " no fue encontrado en la raiz del proyecto.");
            }
        }

        int totalAceptados = todasLasVentasValidas.size();
        int totalRechazados = erroresGlobales.size();

        
        for (String err : erroresGlobales) {
            ventaDAO.registrarError("PROCESO_BATCH", "N/A", err);
        }

        
        reporteService.generarReporteErrores(erroresGlobales, "errores_procesamiento.txt");
        reporteService.generarReporteAnalisis(todasLasVentasValidas, totalRegistrosRecibidos, 
                                               totalAceptados, totalRechazados, "reporte_analisis.txt");

        System.out.println("\n=== PROCESAMIENTO FINALIZADO CON EXITO ===");
        System.out.println("Registros recibidos: " + totalRegistrosRecibidos);
        System.out.println("Registros aceptados: " + totalAceptados);
        System.out.println("Registros rechazados: " + totalRechazados);
        System.out.println("Se han generado los archivos 'reporte_analisis.txt' y 'errores_procesamiento.txt'.");
    }
}

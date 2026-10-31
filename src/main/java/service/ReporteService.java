package service;

import model.Venta;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteService {

    public void generarReporteErrores(List<String> errores, String rutaArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write("====================================================\n");
            writer.write("          REGISTRO DE ERRORES DE PROCESAMIENTO      \n");
            writer.write("====================================================\n\n");

            if (errores.isEmpty()) {
                writer.write("No se registraron errores durante la ejecucion.\n");
            } else {
                for (String err : errores) {
                    writer.write(err + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de errores: " + e.getMessage());
        }
    }

    public void generarReporteAnalisis(List<Venta> ventas, int totalRecibidos, int totalAceptados, 
                                       int totalRechazados, String rutaArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write("====================================================\n");
            writer.write("         REPORTE DE ANALISIS DE VENTAS             \n");
            writer.write("====================================================\n\n");

            
            double pctRechazados = totalRecibidos > 0 ? ((double) totalRechazados / totalRecibidos) * 100 : 0.0;
            writer.write("1. RESUMEN DE PROCESAMIENTO:\n");
            writer.write("   - Total registros recibidos: " + totalRecibidos + "\n");
            writer.write("   - Total registros aceptados: " + totalAceptados + "\n");
            writer.write("   - Total registros rechazados: " + totalRechazados + "\n");
            writer.write(String.format("   - Porcentaje de rechazo: %.2f%%\n\n", pctRechazados));

            if (ventas.isEmpty()) {
                writer.write("No hay ventas aceptadas para calcular indicadores analiticos.\n");
                return;
            }

            
            BigDecimal montoTotalVendido = ventas.stream()
                    .map(Venta::getMontoTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal promedioPorTransaccion = montoTotalVendido.divide(
                    BigDecimal.valueOf(ventas.size()), 2, RoundingMode.HALF_UP);

            writer.write("2. INDICADORES GENERALES:\n");
            writer.write("   - Monto Total Vendido: Q " + montoTotalVendido + "\n");
            writer.write("   - Promedio Monetario por Transaccion: Q " + promedioPorTransaccion + "\n\n");

           
            writer.write("3. VENTAS POR SUCURSAL:\n");
            Map<String, BigDecimal> ventasPorSucursal = ventas.stream()
                    .collect(Collectors.groupingBy(Venta::getSucursal,
                            Collectors.reducing(BigDecimal.ZERO, Venta::getMontoTotal, BigDecimal::add)));
            ventasPorSucursal.forEach((sucursal, monto) -> 
                tryWrite(writer, "   - " + sucursal + ": Q " + monto + "\n")
            );
            writer.write("\n");

            
            writer.write("4. VENTAS POR MES:\n");
            Map<String, BigDecimal> ventasPorMes = ventas.stream()
                    .collect(Collectors.groupingBy(v -> v.getFecha().getYear() + "-" + String.format("%02d", v.getFecha().getMonthValue()),
                            Collectors.reducing(BigDecimal.ZERO, Venta::getMontoTotal, BigDecimal::add)));
            ventasPorMes.forEach((mes, monto) -> 
                tryWrite(writer, "   - Mes " + mes + ": Q " + monto + "\n")
            );
            writer.write("\n");

            
            Map<String, BigDecimal> ventasPorProducto = ventas.stream()
                    .collect(Collectors.groupingBy(Venta::getProducto,
                            Collectors.reducing(BigDecimal.ZERO, Venta::getMontoTotal, BigDecimal::add)));

            Optional<Map.Entry<String, BigDecimal>> productoMayor = ventasPorProducto.entrySet().stream()
                    .max(Map.Entry.comparingByValue());
            Optional<Map.Entry<String, BigDecimal>> productoMenor = ventasPorProducto.entrySet().stream()
                    .min(Map.Entry.comparingByValue());

            writer.write("5. PRODUCTOS DESTACADOS:\n");
            productoMayor.ifPresent(p -> tryWrite(writer, "   - Producto de mayor venta: " + p.getKey() + " (Q " + p.getValue() + ")\n"));
            productoMenor.ifPresent(p -> tryWrite(writer, "   - Producto de menor venta: " + p.getKey() + " (Q " + p.getValue() + ")\n\n"));

            
            writer.write("6. TOP 3 VENDEDORES:\n");
            Map<String, BigDecimal> ventasPorVendedor = ventas.stream()
                    .collect(Collectors.groupingBy(Venta::getVendedor,
                            Collectors.reducing(BigDecimal.ZERO, Venta::getMontoTotal, BigDecimal::add)));

            ventasPorVendedor.entrySet().stream()
                    .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                    .limit(3)
                    .forEach(v -> tryWrite(writer, "   - " + v.getKey() + ": Q " + v.getValue() + "\n"));

            writer.write("\n");

            
            writer.write("7. CONCLUSIONES ANALÍTICAS:\n");
            writer.write("   a) El desempeño general refleja una concentracion de ingresos importante en las sucursales principales.\n");
            writer.write("   b) El porcentaje de inconsistencias en los archivos de origen sugiere implementar validaciones previas en la captura de datos.\n");
            writer.write("   c) La estrategia comercial debería incentivar a los productos con menor facturación para diversificar el portafolio activo.\n");

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de reporte de analisis: " + e.getMessage());
        }
    }

    private void tryWrite(BufferedWriter writer, String texto) {
        try {
            writer.write(texto);
        } catch (IOException e) {
            System.err.println("Error de escritura: " + e.getMessage());
        }
    }
}
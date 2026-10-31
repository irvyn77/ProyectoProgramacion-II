package parser;

import model.Venta;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CSVDataReader implements DataReader {

    @Override
    public List<Venta> leerArchivo(File archivo, List<String> errores) {
        List<Venta> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    if (linea.toLowerCase().contains("id")) continue;
                }

                String[] campos = linea.split(",");
                if (campos.length < 8) {
                    errores.add(archivo.getName() + " | Linea incompleta: " + linea);
                    continue;
                }

                try {
                    Venta v = new Venta();
                    v.setIdVenta(campos[0].trim());
                    v.setFecha(LocalDate.parse(campos[1].trim()));
                    v.setSucursal(campos[2].trim());
                    v.setCodigoProducto(campos[3].trim());
                    v.setProducto(campos[4].trim());
                    v.setCantidad(Integer.parseInt(campos[5].trim()));
                    v.setPrecioUnitario(new BigDecimal(campos[6].trim()));
                    v.setVendedor(campos[7].trim());

                    lista.add(v);
                } catch (Exception e) {
                    errores.add(archivo.getName() + " | Error de formato en registro: " + linea + " | " + e.getMessage());
                }
            }
        } catch (Exception e) {
            errores.add(archivo.getName() + " | Error al leer archivo CSV: " + e.getMessage());
        }

        return lista;
    }
}
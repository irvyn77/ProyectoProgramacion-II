package service;

import model.Venta;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VentaService {

    private final Set<String> idsProcesados = new HashSet<>();

 
    public boolean validarYNormalizar(Venta venta, String archivoOrigen, List<String> errores) {
        if (venta == null) {
            errores.add(archivoOrigen + " | Objeto de venta nulo.");
            return false;
        }

      
        if (venta.getIdVenta() != null) venta.setIdVenta(venta.getIdVenta().trim().toUpperCase());
        if (venta.getSucursal() != null) venta.setSucursal(limpiarTexto(venta.getSucursal()));
        if (venta.getCodigoProducto() != null) venta.setCodigoProducto(venta.getCodigoProducto().trim().toUpperCase());
        if (venta.getProducto() != null) venta.setProducto(limpiarTexto(venta.getProducto()));
        if (venta.getVendedor() != null) venta.setVendedor(limpiarTexto(venta.getVendedor()));

        
        if (esVacio(venta.getIdVenta()) || esVacio(venta.getSucursal()) || 
            esVacio(venta.getCodigoProducto()) || esVacio(venta.getProducto()) || 
            esVacio(venta.getVendedor())) {
            errores.add(archivoOrigen + " | ID: " + venta.getIdVenta() + " | Campos obligatorios incompletos o vacios.");
            return false;
        }

        
        if (venta.getFecha() == null) {
            errores.add(archivoOrigen + " | ID: " + venta.getIdVenta() + " | Fecha invalida o ausente.");
            return false;
        }

        if (venta.getCantidad() <= 0) {
            errores.add(archivoOrigen + " | ID: " + venta.getIdVenta() + " | Cantidad no valida: " + venta.getCantidad());
            return false;
        }

        if (venta.getPrecioUnitario() == null || venta.getPrecioUnitario().doubleValue() <= 0) {
            errores.add(archivoOrigen + " | ID: " + venta.getIdVenta() + " | Precio unitario invalido.");
            return false;
        }

        
        if (idsProcesados.contains(venta.getIdVenta())) {
            errores.add(archivoOrigen + " | ID: " + venta.getIdVenta() + " | Registro duplicado detectado en proceso.");
            return false;
        }

        idsProcesados.add(venta.getIdVenta());
        return true;
    }

    private String limpiarTexto(String texto) {
        if (texto == null) return "";
    
        return texto.trim().replaceAll("\\s+", " ");
    }

    private boolean esVacio(String str) {
        return str == null || str.trim().isEmpty();
    }
}

package db;

import model.Venta;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

   
    public boolean guardarVenta(Venta venta) {
        String sql = "INSERT INTO ventas (id_venta, fecha, sucursal, codigo_producto, producto, cantidad, precio_unitario, monto_total, vendedor) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, venta.getIdVenta());
            stmt.setDate(2, Date.valueOf(venta.getFecha()));
            stmt.setString(3, venta.getSucursal());
            stmt.setString(4, venta.getCodigoProducto());
            stmt.setString(5, venta.getProducto());
            stmt.setInt(6, venta.getCantidad());
            stmt.setBigDecimal(7, venta.getPrecioUnitario());
            stmt.setBigDecimal(8, venta.getMontoTotal());
            stmt.setString(9, venta.getVendedor());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            
            if (e.getErrorCode() == 1062) {
                registrarError("SISTEMA", venta.getIdVenta(), "Registro duplicado en la base de datos (id_venta)");
            } else {
                registrarError("SISTEMA", venta.getIdVenta(), "Error SQL: " + e.getMessage());
            }
            return false;
        }
    }

  
    public void registrarError(String archivoOrigen, String lineaOParte, String motivo) {
        String sql = "INSERT INTO errores_procesamiento (archivo_origen, linea_o_registro, motivo_error) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, archivoOrigen);
            stmt.setString(2, lineaOParte);
            stmt.setString(3, motivo);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error crítico al registrar error en BD: " + e.getMessage());
        }
    }

    
    public boolean existeIdVenta(String idVenta) {
        String sql = "SELECT COUNT(*) FROM ventas WHERE id_venta = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idVenta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar existencia de ID: " + e.getMessage());
        }
        return false;
    }

    
    public List<Venta> obtenerTodas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venta v = new Venta();
                v.setIdVenta(rs.getString("id_venta"));
                v.setFecha(rs.getDate("fecha").toLocalDate());
                v.setSucursal(rs.getString("sucursal"));
                v.setCodigoProducto(rs.getString("codigo_producto"));
                v.setProducto(rs.getString("producto"));
                v.setCantidad(rs.getInt("cantidad"));
                v.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                v.setVendedor(rs.getString("vendedor"));

                lista.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar ventas: " + e.getMessage());
        }
        return lista;
    }
}

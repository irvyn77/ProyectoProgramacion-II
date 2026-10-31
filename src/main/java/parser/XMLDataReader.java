package parser;

import model.Venta;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XMLDataReader implements DataReader {

    @Override
    public List<Venta> leerArchivo(File archivo, List<String> errores) {
        List<Venta> lista = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("venta");

            for (int i = 0; i < nList.getLength(); i++) {
                Element elem = (Element) nList.item(i);
                try {
                    Venta v = new Venta();
                    v.setIdVenta(getTagValue("idVenta", elem));
                    v.setFecha(LocalDate.parse(getTagValue("fecha", elem)));
                    v.setSucursal(getTagValue("sucursal", elem));
                    v.setCodigoProducto(getTagValue("codigoProducto", elem));
                    v.setProducto(getTagValue("producto", elem));
                    v.setCantidad(Integer.parseInt(getTagValue("cantidad", elem)));
                    v.setPrecioUnitario(new BigDecimal(getTagValue("precioUnitario", elem)));
                    v.setVendedor(getTagValue("vendedor", elem));

                    lista.add(v);
                } catch (Exception e) {
                    errores.add(archivo.getName() + " | Error de formato en nodo XML posicion " + i + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            errores.add(archivo.getName() + " | Error al leer archivo XML: " + e.getMessage());
        }

        return lista;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        return nodeList.item(0).getNodeValue().trim();
    }
}
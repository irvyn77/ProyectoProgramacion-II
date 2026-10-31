package parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Venta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JSONDataReader implements DataReader {

    @Override
    public List<Venta> leerArchivo(File archivo, List<String> errores) {
        List<Venta> lista = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); 

        try {
            lista = mapper.readValue(archivo, new TypeReference<List<Venta>>() {});
        } catch (Exception e) {
            errores.add(archivo.getName() + " | Error al deserializar JSON: " + e.getMessage());
        }

        return lista;
    }
}
package parser;

import model.Venta;
import java.io.File;
import java.util.List;

public interface DataReader {
    List<Venta> leerArchivo(File archivo, List<String> errores);
}

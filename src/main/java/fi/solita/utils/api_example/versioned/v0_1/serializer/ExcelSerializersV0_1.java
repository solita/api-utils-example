package fi.solita.utils.api_example.versioned.v0_1.serializer;

import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newMap;
import static fi.solita.utils.functional.Functional.concat;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.vividsolutions.jts.geom.Coordinate;

import fi.solita.utils.api.base.ExcelModule;
import fi.solita.utils.api.base.ExcelSerializer;
import fi.solita.utils.api.base.ExcelSerializers;
import fi.solita.utils.functional.Pair;


public class ExcelSerializersV0_1 extends ExcelSerializers {

    private final SerializersV0_1 s;

    public ExcelSerializersV0_1(SerializersV0_1 s) {
        super(s);
        this.s = s;
    }
    
    public final Map.Entry<? extends Class<?>, ? extends ExcelSerializer<?>> coordinate = Pair.of(Coordinate.class, new ExcelSerializer<Coordinate>() {
        @Override
        public Cells render(ExcelModule module, Row row, int columnIndex, Coordinate value) {
            double[] cs = s.ser(value);
            
            CellStyle style = row.getSheet().getWorkbook().createCellStyle();
            style.setDataFormat(row.getSheet().getWorkbook().createDataFormat().getFormat("#.000000"));
            
            Cell a = newCell(row, columnIndex, Double.toString(cs[0]));
            Cell b = newCell(row, columnIndex+1, Double.toString(cs[1]));
            a.setCellStyle(style);
            b.setCellStyle(style);
            return new Cells(newList(a, b), cs[0] + "," + cs[1]);
        }
        
        @Override
        public List<String> columns(ExcelModule module, Class<Coordinate> type) {
            return newList("coord1", "coord2");
        }
    });

    @Override
    public Map<Class<?>,ExcelSerializer<?>> serializers() { return newMap(
        concat(super.serializers().entrySet(), newList(
            coordinate
        )));
    }
}

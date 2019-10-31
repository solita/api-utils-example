package fi.solita.utils.api_example.versioned.v0_1;

import fi.solita.utils.api.base.VersionBase;
import fi.solita.utils.api_example.versioned.v0_1.serializer.CsvSerializersV0_1;
import fi.solita.utils.api_example.versioned.v0_1.serializer.ExcelSerializersV0_1;
import fi.solita.utils.api_example.versioned.v0_1.serializer.HtmlSerializersV0_1;
import fi.solita.utils.api_example.versioned.v0_1.serializer.HttpSerializersV0_1;
import fi.solita.utils.api_example.versioned.v0_1.serializer.JsonSerializersV0_1;
import fi.solita.utils.api_example.versioned.v0_1.serializer.SerializersV0_1;

public class Version0_1 extends VersionBase {

    public static final String VERSION = "0.1";
    
    @Override
    public SerializersV0_1 serializers() {
        return new SerializersV0_1();
    }
    
    @Override
    public HttpSerializersV0_1 httpSerializers() { return new HttpSerializersV0_1(serializers()); }
    
    @Override
    public JsonSerializersV0_1 jsonSerializers() { return new JsonSerializersV0_1(serializers()); }
    
    @Override
    public HtmlSerializersV0_1 htmlSerializers() { return new HtmlSerializersV0_1(serializers()); }
    
    @Override
    public CsvSerializersV0_1 csvSerializers() { return new CsvSerializersV0_1(serializers()); }
    
    @Override
    public ExcelSerializersV0_1 excelSerializers() { return new ExcelSerializersV0_1(serializers()); }
}
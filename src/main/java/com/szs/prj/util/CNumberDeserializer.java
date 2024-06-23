package com.szs.prj.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class CNumberDeserializer extends JsonDeserializer<String> {

    //산출세액의 형변환
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = p.getText();

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setGroupingSeparator(',');
            Number number = new DecimalFormat().parse(text.replaceAll(",", ""));
            return decimalFormat.format(number);
        } catch (ParseException e) {
            BigDecimal bigDecimal = new BigDecimal(text);
            return decimalFormat.format(bigDecimal);
        }
    }
}
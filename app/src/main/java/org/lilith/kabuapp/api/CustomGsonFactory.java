package org.lilith.kabuapp.api;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;

public class CustomGsonFactory extends JsonFactory {

    private final Gson gson = new Gson();

    @Override
    public JsonGenerator createJsonGenerator(OutputStream out, Charset charset) throws IOException {
        final Writer writer = new OutputStreamWriter(out, charset);
        return new JsonGenerator() {
            private boolean firstField = true;
            private boolean closed = false;

            @Override
            public void writeStartArray() throws IOException {
                writer.write('[');
                firstField = true;
            }

            @Override
            public void writeEndArray() throws IOException {
                writer.write(']');
                firstField = true;
            }

            @Override
            public void writeStartObject() throws IOException {
                writer.write('{');
                firstField = true;
            }

            @Override
            public void writeEndObject() throws IOException {
                writer.write('}');
                firstField = true;
            }

            @Override
            public void writeFieldName(String name) throws IOException {
                if (!firstField) {
                    writer.write(',');
                }
                gson.toJson(name, writer);
                writer.write(':');
                firstField = false;
            }

            @Override
            public void writeString(String value) throws IOException {
                if (!firstField && writer.toString().endsWith("}")) {
                    writer.write(",");
                }
                gson.toJson(value, writer);
                firstField = false;
            }

            @Override
            public void writeBoolean(boolean value) throws IOException {
                if (!firstField) {
                    writer.write(',');
                }
                writer.write(String.valueOf(value));
                firstField = false;
            }

            @Override
            public void writeNumber(int v) throws IOException {

            }

            @Override
            public void writeNumber(long value) throws IOException {
                if (!firstField) {
                    writer.write(',');
                }
                writer.write(String.valueOf(value));
                firstField = false;
            }

            @Override
            public void writeNumber(BigInteger v) throws IOException {

            }

            @Override
            public void writeNumber(float v) throws IOException {

            }

            @Override
            public void writeNumber(double value) throws IOException {
                if (!firstField) {
                    writer.write(',');
                }
                writer.write(String.valueOf(value));
                firstField = false;
            }

            @Override
            public void writeNumber(BigDecimal v) throws IOException {

            }

            @Override
            public void writeNumber(String encodedValue) throws IOException {

            }

            @Override
            public void writeNull() throws IOException {
                if (!firstField) {
                    writer.write(',');
                }
                writer.write("null");
                firstField = false;
            }

            @Override
            public JsonFactory getFactory() {
                return null;
            }

            @Override
            public void flush() throws IOException {
                writer.flush();
            }

            @Override
            public void close() throws IOException {
                if (!closed) {
                    writer.close();
                    closed = true;
                }
            }
        };
    }

    @Override
    public JsonGenerator createJsonGenerator(Writer writer) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public com.google.api.client.json.JsonParser createJsonParser(InputStream in) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public com.google.api.client.json.JsonParser createJsonParser(InputStream in, Charset charset) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public JsonParser createJsonParser(String value) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public JsonParser createJsonParser(Reader reader) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

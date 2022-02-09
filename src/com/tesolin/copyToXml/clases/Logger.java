package com.tesolin.copyToXml.clases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Logger {

    private static final String ARCHIVO_LOG           = "CopyToXML.log";
    private static final String SALTO_LINEA           = "\n";
    private static final String ARCHIVO_PROPERTIES    = "/recursos/Strings.properties";
    // private static final String ARCHIVO_PROPERTIES = "/recursos/Logger.properties";


    public static void log(String mensaje) {

        String timeStamp = loggerTimestamp("yyyy-MM-dd HH.mm.ss.sssss");

        System.out.println(timeStamp + "  >>>  " + mensaje);

        loggerFile(timeStamp + "  >>>  " + mensaje, true);

    }

    public static void logProperty(String clave, String texto) {

        String mensaje;

        if (texto == null || texto.trim().contentEquals(""))
            mensaje   = consultaPropiedad(clave);
        else
            mensaje   = consultaPropiedad(clave).replace("{{TEXTO}}", texto);

        String timeStamp = loggerTimestamp("yyyy-MM-dd HH.mm.ss.sssss");

        System.out.println(timeStamp + "  >>>  " + mensaje);

        loggerFile(timeStamp + "  >>>  " + mensaje, true);

    }

    private static void loggerFile(String cadena, Boolean concatena) {

        try {

            File file = new File(ARCHIVO_LOG);

            FileWriter writer = new FileWriter(file, concatena);

            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(cadena);

            bufferedWriter.write(SALTO_LINEA);

            bufferedWriter.close();

            writer.close();

        } catch (Exception e) {

            // Impresión del error.
            e.printStackTrace();

        }

    }

    private static String loggerTimestamp(String mascara) {

        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat(mascara);

        return formatter.format(date);

    }

    private static String consultaPropiedad(String clave) {

        try {

            InputStream inputStream = Utils.class.getResourceAsStream(ARCHIVO_PROPERTIES);

            if (inputStream == null)
                return "";

            Properties prop = new Properties();

            prop.load(inputStream);

            String valor = prop.getProperty(clave);

            inputStream.close();

            return valor;

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }

    }

}

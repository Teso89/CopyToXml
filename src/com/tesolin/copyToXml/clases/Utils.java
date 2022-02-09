package com.tesolin.copyToXml.clases;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Utils {

    private static final String ARCHIVO_PROPIEDADES = "/recursos/Strings.properties";

    public static String obtieneTransaccion(String texto) {

        int pos = texto.lastIndexOf('.');

        String tx = texto.substring(pos -4, pos);

        return tx;
    }

    public static Integer obtieneLongitudInteger(String texto) {

        int pos1 = texto.lastIndexOf('(');
        int pos2 = texto.indexOf(')', pos1);

        String longitud;

        if (pos1 == -1 || pos2  == -1) {

            pos1 = texto.lastIndexOf("PIC ");
            pos2 = texto.lastIndexOf('.');

            int cantDecimales = pos2 - (pos1 +4);

            longitud = cantDecimales + "";

        } else {
            longitud = texto.substring(pos1 + 1, pos2);
        }

        try {

            return Integer.parseInt(longitud);

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }

    }

    public static String obtieneTipoDato(String texto) {

        /*
            0 = Alfanumérico.
            1 = Numérico.
            2 = ???
            3 = ???
            4 = Decimal.
         */

        int pos1 = texto.lastIndexOf("PIC ");
        int pos2 = texto.lastIndexOf('(');

        if (pos2 == -1)
            pos2 = texto.lastIndexOf('.');

        String tipoDato = texto.substring(pos1 +4, pos2).trim();

        if (tipoDato.contains("X"))
            return "0";

        if (tipoDato.contains("9"))
            if (texto.contains(")V9"))
                return "4";
            else
                return "1";

        return "?";
    }

    public static String obtieneDecimales(String texto) {

        int pos1 = texto.lastIndexOf(")V9");
        int pos2 = texto.lastIndexOf('.');

        if (pos1 == -1)
            return "0";

        int cantDecimales = pos2 - (pos1 +2);

        return cantDecimales + "";

    }

    public static String obtieneNombreVariable(String texto, String prefijo) {

        int pos1 = texto.indexOf(prefijo);

        if (pos1 == -1)
            pos1 = texto.indexOf("FILLER");

        int pos2 = texto.indexOf(' ', pos1);

        if (pos2 == -1)
            pos2 = texto.length();

        String nombreVariable = texto.substring(pos1, pos2);

        return nombreVariable;

    }

    public static String consultaPropiedades(String clave) {

        try {

            InputStream inputStream = Utils.class.getResourceAsStream(ARCHIVO_PROPIEDADES);

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

    public static String normalizaLinea(String linea) {

        String auxiliar = linea.trim().toUpperCase();

        auxiliar = auxiliar.replace("   ", " ")
                           .replace("   ", " ")
                           .replace("   ", " ")
                           .replace("   ", " ")
                           .replace("  ", " ");

        return auxiliar;

    }

    public static String completarEspacios(String texto, int tope) {

        int longitud = texto.length() + 2;

        StringBuilder sb = new StringBuilder();

        for (int i = longitud; i < tope; i++)
            sb.append(" ");

        return sb.toString();

    }

    public static String ingresoTeclado() {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {

            return bufferedReader.readLine();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }

    }

}

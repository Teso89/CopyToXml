package com.tesolin.copyToXml.clases;

public class Validador {

    private static final String MARCA_ENTRADA  = "{{ENTRADA}}";
    private static final String MARCA_SALIDA   = "{{SALIDA}}";
    private static final String MARCA_HEADER   = "{{HEADER}}";
    private static final String MARCA_RESET    = "{{RESET}}";

    public static boolean valida_01(String fila) {

        if (fila.length() <= 0)
            return false;

        if (!fila.endsWith("."))
            return false;

        if (!fila.startsWith("01"))
            return false;

        return true;

    }

    public static boolean valida_pic(String fila) {

        if (fila.length() <= 0)
            return false;

        if (!fila.endsWith("."))
            return false;

        if (!fila.toUpperCase().contains("PIC "))
            return false;

        return true;

    }

    public static boolean valida_comentario(String fila) {

        if (fila.length() <= 0)
            return false;

        if (!fila.startsWith("*"))
            return false;

        return true;

    }

    public static boolean valida_varGrupo(String fila) {

        if (fila.length() <= 0)
            return false;

        if (fila.startsWith("01"))
            return false;

        if (fila.toUpperCase().contains("PIC "))
            return false;

        if (!fila.endsWith("."))
            return false;

        return true;

    }

    public static Integer valida_marca(String fila, Integer actual) {

        Integer resultado = actual;

        if (fila.contains(MARCA_RESET))
            resultado = 0;

        if (fila.contains(MARCA_ENTRADA))
            resultado = 1;

        if (fila.contains(MARCA_SALIDA))
            resultado = 2;

        if (fila.contains(MARCA_HEADER))
            resultado = 3;

        return resultado;

    }

    public static boolean valida_txt(String nombreArchivo) {

        int longit = nombreArchivo.length();

        if (longit < 5)
            return false;

        String aux = nombreArchivo.substring(longit -4, longit).toUpperCase();

        return aux.contains(".TXT");

    }

    public static boolean valida_variableCortada(String fila) {

        if (fila.length() <= 0)
            return false;

        if (fila.endsWith("."))
            return false;

        return true;

    }

}
package com.tesolin.copyToXml.main;

import com.tesolin.copyToXml.clases.Logger;
import com.tesolin.copyToXml.clases.Procesador;
import com.tesolin.copyToXml.clases.Utils;
import com.tesolin.copyToXml.clases.Validador;
import java.io.*;

public class Main {

    // Atributos de la clase.
    private static boolean muestraAyuda;
    private static String entrada;
    private static String salida;

    // Método principal.
    public static void main (String[] args0) {

        muestraAyuda = false;
        entrada      = System.getProperty("user.home");
        salida       = "";

        // Se analizan y cargan los parámetros ingresados.
        recorreParametros(args0);

        if (muestraAyuda == true) {
            muestraAyuda();
        } else {
            buscaArchivos();
        }

    }

    // Método para mostrar la explicación del programa.
    public static void muestraAyuda() {

        /*
        String chamullo = "\n" +
                        "Instrucciones." + "\n" +
                        "--------------" + "\n" +
                        "\n" +
                        "1) Poner uno o mas copys Cobol junto a este .JAR." + "\n" +
                        "2) Abrir el copy y poner las marcas {{ENTRADA}} y {{SALIDA}} al principio de las variable de grupo." + "\n" +
                        "   Para excluir una linea del procesamiento solo se debe agregar un * (asterisco) al principio." + "\n" +
                        "Por ejemplo:" + "\n" +
                        "            {{ENTRADA}}  05 HS27-DATOS-ENTRADA." + "\n" +
                        "            {{SALIDA}}  05 HS27-RESPUESTA." + "\n" +
                        "            * 15 HS27-TAR-CLA              PIC  X(19).  " + "\n" +
                        "3) Correr el .JAR y esperar a obtener los .XML" + "\n"
                ;
        */

        String chamullo =
                "Intrucciones:\n" +
                "\n" +
                "    1)  Poner uno o mas copys en el directorio del usuario, ej. \"C\\\\Users\\\\Daniel\" o \"/home/daniel\".\n" +
                "    2)  A cada copy agregarle las marcas {{ENTRADA}} y {{SALIDA}} al principio de las variables de grupo para indicar la REQUEST y BODYRESPONSE.\n" +
                "        Si se quiere excluir una o mas lineas del procesamiento, se debe agregar un asterisco al principio de la fila.\n" +
                "    3)  Correr el servicio para generar los XML, por defecto se guardan en el mismo directio del .JAR.\n" +
                "\n" +
                "Opciones:\n" +
                "\n" +
                "    --help          Muestra esta ayuda.\n" +
                "    --in            Permite cambiar el directorio de entrada.\n" +
                "    --out           Permite cambiar el directorio de salida.\n" +
                "\n" +
                "Ejemplo:\n" +
                "\n" +
                "    {{ENTRADA}}  05 HS27-DATOS-ENTRADA.\"\n" +
                "    {{SALIDA}}  05 HS27-RESPUESTA.\"\n" +
                "    * 15 HS27-TAR-CLA              PIC  X(19).\"\n\n";

        System.out.println(chamullo);
    }

    // Método para buscar todos los archivos en el directorio.
    public static void buscaArchivos() {

        Logger.logProperty("msg.evento.leyendo.directorio.r", entrada);

        int aux                = 0;
        File directorio        = new File(entrada);
        File[] listadoArchivos = directorio.listFiles();

        if (listadoArchivos == null) {
            Logger.logProperty("msg.error.directorio.vacio", null);
            return;
        }

        for (int i = 0; i < listadoArchivos.length; i++) {

            if (Validador.valida_txt(listadoArchivos[i].getName())) {

                Logger.logProperty("msg.evento.leyendo.archivo.r", listadoArchivos[i].getName());

                aux++;

                Procesador procesador = new Procesador(salida);
                procesador.analizaArchivo(listadoArchivos[i].getAbsolutePath());
                procesador.generaXML();

            }

        }

        if (aux <= 0)
            Logger.logProperty("msg.error.directorio.sinTXT", null);

    }

    // Método para recorrer el array de parámetros.
    private static void recorreParametros(String[] array) {

        if (array.length > 0) {

            for (int i = 0; i < array.length; i++) {

                switch (array[i]) {
                    case "--help" :
                        muestraAyuda = true;
                        break;
                    case "--in"   :
                        entrada      = evaluaValores(array,i+1);
                        break;
                    case "--out"  :
                        salida       = evaluaValores(array,i+1);
                        break;
                    default       :
                        break;
                }

            }

        }

    }

    // Método para evaluar los valores de cada parámetro.
    private static String evaluaValores(String[] array, int i) {

        if (i >= array.length)
            return null;

        if (array[i].contains("--"))
            return null;
        else
            return array[i];

    }

}

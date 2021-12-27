package com.tesolin.copyToXml.main;

import com.tesolin.copyToXml.clases.Logger;
import com.tesolin.copyToXml.clases.Procesador;
import com.tesolin.copyToXml.clases.Utils;
import com.tesolin.copyToXml.clases.Validador;
import java.io.*;
import java.util.Properties;

public class Main {

    // Método principal.
    public static void main (String[] args0) {


        if (args0.length > 0) {

            switch (args0[0]) {
                case "--help" : muestraAyuda();
                case "--file" : buscaArchivos(args0[1]);
                default: break;
            }

        } else {
            buscaArchivos(System.getProperty("user.home"));
        }

    }

    // Método para mostrar la explicación del programa.
    public static void muestraAyuda() {

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

        System.out.println(chamullo);

    }

    // Método para buscar todos los archivos en el directorio.
    public static void buscaArchivos(String ruta) {

        Logger.logProperty("msg.evento.leyendo.directorio.r", ruta);

        int aux                = 0;
        File directorio        = new File(ruta);
        File[] listadoArchivos = directorio.listFiles();

        if (listadoArchivos == null) {
            Logger.logProperty("msg.error.directorio.vacio", null);
            return;
        }

        for (int i = 0; i < listadoArchivos.length; i++) {

            if (Validador.valida_txt(listadoArchivos[i].getName())) {

                Logger.logProperty("msg.evento.leyendo.archivo.r", listadoArchivos[i].getName());

                aux++;

                Procesador procesador = new Procesador();
                procesador.analizaArchivo(listadoArchivos[i].getAbsolutePath());
                procesador.generaXML();

            }

        }

        if (aux <= 0)
            Logger.logProperty("msg.error.directorio.sinTXT", null);

    }

}

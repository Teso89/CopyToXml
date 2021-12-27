package com.tesolin.copyToXml.clases;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class Procesador {

    private final ArrayList <String[]> request;
    private final ArrayList <String[]> responseBody;
    private String tx;
    private int tag;
    private int posicion;
    private static final Integer POSICION_INICIAL = 32;
    private String aux_nombre;

    public Procesador() {
        this.request        = new ArrayList<>();
        this.responseBody   = new ArrayList<>();
        this.tx             = "UNDEFINED_TX";
        this.tag            = 0;
        this.posicion       = POSICION_INICIAL;
        this.aux_nombre     = null;
    }

    public void analizaArchivo(String archivo) {

        try {

            File file = new File(archivo);

            FileReader reader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(reader);

            String linea = "";

            while ((linea = bufferedReader.readLine()) != null) {
                linea = Utils.normalizaLinea(linea);
                procesaLinea(linea);
            }

            bufferedReader.close();
            reader.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void procesaLinea(String linea) {

        if (!Validador.valida_comentario(linea)) {


            // Valida si la linea es 01 para obtener el nobmre de la TX.
            if (Validador.valida_01(linea))
                this.tx = Utils.obtieneTransaccion(linea);


            // Valida si es una variable de grupo, y si lo es si contiene {{ENTRADA}} y {{SALIDA}}.
            if (Validador.valida_varGrupo(linea))
                this.tag = Validador.valida_marca(linea, this.tag);


            // Valida si la liena es una definición de variable.
            if (Validador.valida_pic(linea)) {


                // Obtiene TDD, Nombre, Posición, Longitud y Decimales.
                String[] campo = new String[] {
                        Utils.obtieneTipoDato(linea),
                        //Utils.obtieneNombreVariable(linea, this.tx),
                        (this.aux_nombre == null) ? Utils.obtieneNombreVariable(linea, this.tx) : this.aux_nombre,
                        this.posicion + "",
                        Utils.obtieneLongitudInteger(linea).toString(),
                        Utils.obtieneDecimales(linea)
                };

                this.aux_nombre = null;

                if (this.tag == 1) {

                    campo[2] = this.posicion + "";
                    this.posicion += Utils.obtieneLongitudInteger(linea);
                    request.add(campo);

                } else if (this.tag == 2) {
                    responseBody.add(campo);
                }


            } else if (Validador.valida_variableCortada(linea)) {
                this.aux_nombre = Utils.obtieneNombreVariable(linea, this.tx);
            }

        }

    }

    public void generaXML() {

        Logger.logProperty("msg.evento.escribiendo.XML.r", this.tx + ".xml");

        if (this.request      == null || this.request.size()      == 0) {
            Logger.logProperty("msg.error.validacion.campos.entrada", null);
            return;
        }

        if (this.responseBody == null || this.responseBody.size() == 0) {
            Logger.logProperty("msg.error.validacion.campos.salida", null);
            return;
        }

        String nombreXML = this.tx + ".xml";
        String[] fila;
        String cadena;

        try {

            File file = new File(nombreXML);

            FileWriter writer = new FileWriter(file);

            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(Utils.consultaPropiedades("tag.metadata.inicio").replace("{{nombreTX}}", this.tx));

            // Request.
            Logger.logProperty("msg.evento.escribiendo.request", null);
            bufferedWriter.write(Utils.consultaPropiedades("tag.request.inicio"));
            bufferedWriter.write(Utils.consultaPropiedades("tag.request.fijos").replace("{{nombreTX}}", this.tx));


            for (int i = 0; i < this.request.size(); i++) {
                fila = this.request.get(i);
                cadena = Utils.consultaPropiedades("tag.field");
                cadena = cadena.replace("{{tdd}}", fila[0])
                        .replace("{{nombreVariable}}", fila[1])
                        .replace("{{espacios}}", Utils.completarEspacios(fila[1],35))
                        .replace("{{posicion}}", fila[2])
                        .replace("{{espacios2}}", Utils.completarEspacios(fila[2],8))
                        .replace("{{longitud}}", fila[3])
                        .replace("{{espacios3}}", Utils.completarEspacios(fila[3],8))
                        .replace("{{cantDecimales}}", fila[4]);
                bufferedWriter.write(cadena);
            }

            bufferedWriter.write(Utils.consultaPropiedades("tag.request.fin"));

            // Response Header.
            Logger.logProperty("msg.evento.escribiendo.responseHeader", null);
            bufferedWriter.write(Utils.consultaPropiedades("tag.responseHeader.inicio"));
            bufferedWriter.write(Utils.consultaPropiedades("tag.responseHeader.fijos"));
            bufferedWriter.write(Utils.consultaPropiedades("tag.responseHeader.fin"));

            // Response Body.
            Logger.logProperty("msg.evento.escribiendo.responseBody", null);
            bufferedWriter.write(Utils.consultaPropiedades("tag.responseBody.inicio"));

            for (int i = 0; i < this.responseBody.size(); i++) {
                fila = this.responseBody.get(i);
                cadena = Utils.consultaPropiedades("tag.field");
                cadena = cadena.replace("{{tdd}}", fila[0])
                        .replace("{{nombreVariable}}", fila[1])
                        .replace("{{espacios}}", Utils.completarEspacios(fila[1],35))
                        .replace("{{posicion}}", "-1")
                        .replace("{{espacios2}}", "  ")
                        .replace("{{longitud}}", fila[3])
                        .replace("{{espacios3}}", Utils.completarEspacios(fila[3],8))
                        .replace("{{cantDecimales}}", fila[4]);
                bufferedWriter.write(cadena);
            }

            bufferedWriter.write(Utils.consultaPropiedades("tag.responseBody.fin"));
            bufferedWriter.write(Utils.consultaPropiedades("tag.metadata.fin"));

            bufferedWriter.close();
            writer.close();

            Logger.logProperty("msg.estatus.ok.r", this.tx + ".xml");

        } catch (Exception e) {
            e.printStackTrace();

            Logger.logProperty("msg.estatus.error.r", this.tx + ".xml");
        }



    }

}
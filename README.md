# CopyToXml

Mini aplicación Java para la exportación de archivos de XML de transacciones para Prisma Plataforma Internacional.

<br/>

## Instrucciones.

1) Copiar el ejecutable CopyToXml.jar en cualquier directorio.
2) Guardar uno o mas archivos TXT con el copy Cobol en el directorio del Usuario.
3) Poner a cada TXT una marca para indicar cuales son los campos de entrada y de salida (ver mas adelante).
4) Ejecutar el JAR y esperar que se exporten los XML, estos archivos se generan en el mismo directorio del JAR junto con el Log.

<br/> <hr/> <br/>

## Marcadores.

Los marcadores son textos que se agregan al Copy para indicar en que momento las variables son de entrada o de salida.
Cada marca debe ir al principio de las líneas, deben usarse una sola vez y solo se aplican a las variables de grupo (aquellas lineas que no contengan PIC, VALUE, etc.).

<br/>

<b>{{ENTRADA}}</b> - Este marcador se usa para indicar el conjunto de variables de entrada, esto compone la <b>request</b>.

<b>{{SALIDA}}</b> - Este marcador se usa para indicar el conjunto de variables de salida, esto compone la <b>response body</b>.

<b>"*" Asterisco</b> - En cobol los asteriscos son comentarios, se puede poner asteriscos para ignorar lineas del copy.

<br/> <hr/> <br/>

## Comandos.

De momento, la cantidad de comandos disponibles es muy limitada, los que se encuentran disponibles son:

<b>--help</b> - Muestra la ayuda del programa. <br/>
<b>--file</b> - Indica la ruta donde debe ir a buscar los archivos TXT, por                     ejemplo: --file C:\Users\nombre_usuario\Desktop\Copys
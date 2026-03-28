using System;
using System.IO;
using System.Messaging;

namespace Adaptador_XML
{
    internal class Program
    {
        static void Main(string[] args)
        {
            string carpeta = @"Z:\Iplacex\Cursos\INSI\insi-ejercicios\U3\S5\AccesosXML\2025-01-08";
            if (args.Length > 0)
            {                
                carpeta = args[0];
            }

            try
            {   
                string[] archivosXML = Directory.GetFiles(carpeta, "*.xml");

                foreach (string archivo in archivosXML)
                {
                    var contenidoXML = File.ReadAllText(archivo);

                    Console.WriteLine(new string('\n', 2));
                    Console.WriteLine(new string('#', 10));
                    Console.WriteLine(archivo);
                    Console.WriteLine(new string('-', 8));
                    Console.WriteLine(contenidoXML);
                    Console.WriteLine(new string('#', 10));

                    enviarMensaje(contenidoXML);
                }
            }
            catch (DirectoryNotFoundException dnfe)
            {
                Console.Error.WriteLine("El directorio indicado " + carpeta + " NO fue encontrado.");
            }
            catch (Exception e)
            {
                Console.Error.WriteLine("Ocurrió un error inesperado: " + e.Message);
            }
        }

        private static void enviarMensaje(string contenidoXML)
        {
            using(var tx = new MessageQueueTransaction())
            {                
                try
                {
                    tx.Begin();
                    var cola = new MessageQueue(@".\private$\accesos_raw");
                    var formatter = new XmlMessageFormatter(new[] { typeof(string) });

                    var mensaje = new Message(contenidoXML, formatter)
                    {
                        Recoverable = true
                    };

                    cola.Send(mensaje, tx);
                    tx.Commit();
                }
                catch(Exception e)
                {
                    tx.Abort();
                    Console.Error.WriteLine("Error inesperado, no se pudo enviar el mensaje a la cola: ", e.Message);
                }
            }
        }
    }
}

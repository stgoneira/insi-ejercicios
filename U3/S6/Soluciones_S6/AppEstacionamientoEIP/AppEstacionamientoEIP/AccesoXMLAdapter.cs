using System;
using System.Diagnostics;
using System.IO;
using System.Messaging;
using System.Threading;

namespace EstacionamientoEIP
{
    internal class AccesoXMLAdapter
    {
        public string XmlBasePath { get; }
        public string ColaAccesosRaw { get; }

        // Constructor obliga a inicializar
        public AccesoXMLAdapter(string xmlBasePath, string colaAccesosRaw)
        {
            XmlBasePath = xmlBasePath ?? throw new ArgumentNullException(nameof(xmlBasePath));
            ColaAccesosRaw = colaAccesosRaw ?? throw new ArgumentNullException(nameof(colaAccesosRaw));
        }

        public void Ejecutar()
        {
            try
            {
                Trace.WriteLine("AccesoXMLAdapter.Ejecutar()", "INFO");
                var watcher = new FileSystemWatcher(XmlBasePath, "*.xml");
                watcher.Created += watcher_Created;
                watcher.EnableRaisingEvents = true;
                watcher.IncludeSubdirectories = true;
            } catch(IOException ioe)
            {
                Trace.WriteLine("AccesoXMLAdapter.Ejecutar error al leer el archivo XML: "+ioe.Message, "ERROR");
            }
            
        }

        private void watcher_Created(object sender, FileSystemEventArgs e)
        {
            // Esperar 1 segundo para evitar problemas de bloqueo, etc
            Thread.Sleep(2000);
            Trace.WriteLine($"AccesoXMLAdapter: Archivo nuevo: {e.FullPath}", "INFO");
            var contenidoXML = File.ReadAllText(e.FullPath);
            enviarMensaje(contenidoXML);
        }

        private void enviarMensaje(string contenidoXML)
        {
            using (var tx = new MessageQueueTransaction())
            {
                try
                {
                    tx.Begin();
                    var cola = new MessageQueue($@".\private$\{ColaAccesosRaw}");
                    var formatter = new XmlMessageFormatter(new[] { typeof(string) });

                    var mensaje = new Message(contenidoXML, formatter)
                    {
                        Recoverable = true
                    };

                    cola.Send(mensaje, tx);
                    tx.Commit();
                }
                catch (Exception e)
                {
                    tx.Abort();
                    Console.Error.WriteLine($"Error inesperado, no se pudo enviar el mensaje a la cola {ColaAccesosRaw}: ", e.Message);
                }
            }
        }
    }
}
